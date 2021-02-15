package org.got.takeaway.unit.repositories;

import org.got.takeaway.domain.player.Player;
import org.got.takeaway.repositories.impl.PlayerRepositoryImpl;
import org.got.takeaway.utility.DataHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlayerRepositoryImplUnitTest {

    private final DataHelper dataHelper = DataHelper.getInstance();

    @Mock
    private PlayerRepositoryImpl playerRepository;

    @Test
    public void testSave() {
        doNothing().when(playerRepository).save(any(Player.class));
        playerRepository.save(dataHelper.player());
        verify(playerRepository, times(1)).save(dataHelper.player());
    }

    @Test
    public void testFindByName() {
        when(playerRepository.findByName(anyString())).thenReturn(dataHelper.player());
        Player player = playerRepository.findByName("Ahmad");
        assertEquals(dataHelper.player(), player);
        verify(playerRepository, times(1)).findByName("Ahmad");
    }

    @Test
    public void testFindAvailable() {
        when(playerRepository.findAvailable(anyString())).thenReturn(dataHelper.player());
        Player player = playerRepository.findAvailable("Ahmad");
        assertEquals(dataHelper.player(), player);
        verify(playerRepository, times(1)).findAvailable("Ahmad");
    }

    @Test
    public void testDelete() {
        doNothing().when(playerRepository).delete(anyString());
        playerRepository.delete("Ahmad");
        verify(playerRepository, times(1)).delete("Ahmad");
    }

    @Test
    public void testIsExists() {
        when(playerRepository.isExists(anyString())).thenReturn(true);
        assertTrue(playerRepository.isExists("Ahmad"));
        verify(playerRepository, times(1)).isExists("Ahmad");
    }
}

