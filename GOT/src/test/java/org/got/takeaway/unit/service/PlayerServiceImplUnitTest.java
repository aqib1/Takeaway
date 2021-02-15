package org.got.takeaway.unit.service;

import org.got.takeaway.domain.player.Player;
import org.got.takeaway.service.Impl.PlayerServiceImpl;
import org.got.takeaway.utility.DataHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlayerServiceImplUnitTest {

    private final DataHelper dataHelper = DataHelper.getInstance();
    @Mock
    private PlayerServiceImpl playerService;

    @Test
    public void testSave() {
        doNothing().when(playerService).save(any(Player.class));
        playerService.save(dataHelper.player());
        verify(playerService, times(1)).save(dataHelper.player());
    }

    @Test
    public void testFindByName() {
        when(playerService.findByName(anyString())).thenReturn(dataHelper.optionalPlayer());
        assertEquals(dataHelper.optionalPlayer(), playerService.findByName("Ahmad"));
        verify(playerService, times(1)).findByName("Ahmad");
    }

    @Test
    public void testFindAvailable() {
        when(playerService.findAvailable(anyString())).thenReturn(dataHelper.optionalPlayer());
        assertEquals(dataHelper.optionalPlayer(), playerService.findAvailable("Ahmad"));
        verify(playerService, times(1)).findAvailable("Ahmad");
    }

    @Test
    public void testDelete() {
        doNothing().when(playerService).delete(anyString());
        playerService.delete("any");
        verify(playerService, times(1)).delete("any");
    }

    @Test
    public void testIsExists() {
        when(playerService.isExists(anyString())).thenReturn(true);
        assertTrue(playerService.isExists("Ahmad"));
        verify(playerService, times(1)).isExists("Ahmad");
    }
}
