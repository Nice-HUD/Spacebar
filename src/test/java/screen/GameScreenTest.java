package screen;

import entity.EnemyShipFormation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import engine.GameState;
import engine.GameSettings;
import engine.DrawManager;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameScreenTest {

    private GameScreen gameScreen;
    private GameState gameStateMock;
    private GameSettings gameSettingsMock;

    @BeforeEach
    public void setUp() {
        gameStateMock = mock(GameState.class);
        gameSettingsMock = mock(GameSettings.class);

        when(gameStateMock.getLevel()).thenReturn(1);
        when(gameStateMock.getScore()).thenReturn(0);
        when(gameStateMock.getLivesRemaining()).thenReturn(3);
        when(gameStateMock.getLivesTwoRemaining()).thenReturn(3);

        gameScreen = new GameScreen(gameStateMock, gameSettingsMock, false, 800, 600, 60, false);
        // Mock DrawManager
        DrawManager drawManagerMock = mock(DrawManager.class);
        gameScreen.setDrawManager(drawManagerMock);

        gameScreen.initialize();
    }

    @Test
    public void testEnemyReachesGreenLineEndsGame() {
        // Mock enemyShipFormation
        EnemyShipFormation enemyShipFormationMock = mock(EnemyShipFormation.class);
        when(enemyShipFormationMock.hasEnemyReachedBottom(600 - 65)).thenReturn(true);
        when(enemyShipFormationMock.iterator()).thenReturn(Collections.emptyIterator()); // Empty formation for getRemainingEnemies()

        gameScreen.enemyShipFormation = enemyShipFormationMock;

        // Call update method to check behavior
        gameScreen.update();

        assertTrue(gameScreen.getLevelFinished());
    }

    @Test
    public void testEnemyDoesNotReachGreenLineGameContinues() {
        // Mock enemyShipFormation
        EnemyShipFormation enemyShipFormationMock = mock(EnemyShipFormation.class);
        when(enemyShipFormationMock.hasEnemyReachedBottom(600 - 65)).thenReturn(false);
        when(enemyShipFormationMock.iterator()).thenReturn(Collections.emptyIterator()); // Empty formation for getRemainingEnemies()

        gameScreen.enemyShipFormation = enemyShipFormationMock;

        // Call update method to check behavior
        gameScreen.update();

        assertFalse(gameScreen.getLevelFinished());
    }

}
