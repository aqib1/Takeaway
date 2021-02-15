package org.got.takeaway.repositories.impl;

import org.got.takeaway.domain.player.Player;
import org.got.takeaway.domain.player.PlayerStatus;
import org.got.takeaway.repositories.PlayerRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

/**
 * * ===== Database information ==============
 * <p>
 * This AccountDaoImpl class is used to provide account details and IO operation
 * against data. For the simplicity of task as it mentioned in requirement II i
 * am not using any database instead of that i am using map, in future we can
 * replace that structure with actual database
 * </p>
 * ========= Thread safety information =======
 * <p>
 * To make it thread-safe we will use synchronization as we are using Map, and
 * that will be updated, with requests. we have to make our data synchronize for
 * the sake of multiple requests (threads) safety, in the case of database we
 * have isolation to prevent our database
 * </p>
 * <p>
 * Synchronization can be acquired on method level (Coarse grain locking
 * mechanism) but this will make our method slow as no thread else can enter to
 * other methods (in database term we can say highest level of isolation) we
 * will use fine grain locking mechanism separately for read mechanism and
 * writing mechanism
 * </p>
 * <p>
 * I am using java 1.8 StampedLock @see {@link StampedLock} <br>
 * <strong> Reason of using StampedLock is one of its feature optimistic locking
 * in this lock as per documentation said, we do not need to apply full-fledged
 * read lock every time, some time lock is not held by any write operation, we
 * use tryOptimisticRead to check if the lock is hold by write operation and
 * then check result with validate method. </strong> <br>
 * Java 1.8 StampedLock is much more efficient and fast as compared to
 * ReentrantLock specially optimistic locking which make synchronization
 * overhead very slow. You can also use ReentrantLock but it very slow as
 * compared to new java 1.8 stamped lock
 * </p>
 *
 * @author AQIB JAVED
 * @version 1.0
 * @since 12/13/2019
 */
@Repository
public class PlayerRepositoryImpl implements PlayerRepository {

    private final StampedLock lock = new StampedLock();

    private final Map<String, Player> availablePlayer = new HashMap<>();

    @Override
    public void save(Player player) {
        long stamp = lock.writeLock();
        try {
            availablePlayer.put(player.getName(), player);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public Player findByName(String name) {
        // return zero if it acquire by a write lock (exclusive locked)
        long stamp = lock.tryOptimisticRead();
        // Synchronization overhead is very low if validate() succeeds
        // Always return true if stamp is non zero (as not acquired by write lock)
        if (lock.validate(stamp))
            return availablePlayer.get(name);
        // Only in the case when write lock is acquired we need to apply read lock
        stamp = lock.readLock();
        try {
            return availablePlayer.get(name);
        } finally {
            lock.unlockRead(stamp);
        }
    }

    @Override
    public Player findAvailable(String name) {
        // return zero if it acquire by a write lock (exclusive locked)
        long stamp = lock.tryOptimisticRead();
        // Synchronization overhead is very low if validate() succeeds
        // Always return true if stamp is non zero (as not acquired by write lock)
        if (lock.validate(stamp))
            return find(name);
        // Only in the case when write lock is acquired we need to apply read lock
        stamp = lock.readLock();
        try {
            return find(name);
        } finally {
            lock.unlockRead(stamp);
        }

    }

    private Player find(String name) {
        return availablePlayer.values().stream()
                .filter(x -> x.getPlayerStatus() == PlayerStatus.AVAILABLE && !x.getName().equals(name))
                .findFirst().orElse(null);
    }

    @Override
    public void delete(String name) {
        long stamp = lock.writeLock();
        try {
            availablePlayer.remove(name);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public boolean isExists(String name) {
        // return zero if it acquire by a write lock (exclusive locked)
        long stamp = lock.tryOptimisticRead();
        // Synchronization overhead is very low if validate() succeeds
        // Always return true if stamp is non zero (as not acquired by write lock)
        if (lock.validate(stamp))
            return availablePlayer.containsKey(name);

        stamp = lock.readLock();
        try {
            return availablePlayer.containsKey(name);
        } finally {
            lock.unlockRead(stamp);
        }
    }
}
