package entity;

import engine.DrawManager;
import engine.GameSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for EnemyShipFormation.
 */
public class EnemyShipFormationTest {

    private EnemyShipFormation enemyShipFormation;
    private GameSettings gameSettingsMock;

    @BeforeEach
    public void setUp() {
        gameSettingsMock = mock(GameSettings.class);
        when(gameSettingsMock.getFormationWidth()).thenReturn(5);
        when(gameSettingsMock.getFormationHeight()).thenReturn(3);
        when(gameSettingsMock.getBaseSpeed()).thenReturn(10);
        when(gameSettingsMock.getShootingFrecuency()).thenReturn(1000);

        enemyShipFormation = spy(new EnemyShipFormation(gameSettingsMock));
    }

    @Test
    public void testHasEnemyReachedBottomWhenEnemyBelowBoundary() {
        // 경계 아래에 적선 추가
        EnemyShip enemyBelowBoundary = new EnemyShip(100, 850, DrawManager.SpriteType.EnemyShipA1, 1, 0, 0);
        enemyShipFormation.getEnemyShips().get(0).add(enemyBelowBoundary); // 첫 번째 열에 추가

        // 테스트
        boolean result = enemyShipFormation.hasEnemyReachedBottom(800);
        assertTrue(result, "적이 경계를 초과했을 경우 true를 반환해야 합니다.");

        // 로깅
        System.out.println("EnemyShip positionY: " + enemyBelowBoundary.getPositionY());
    }

    @Test
    public void testHasEnemyReachedBottomWhenEnemyAtBoundary() {
        // 경계에 정확히 위치한 적선 추가
        EnemyShip enemyAtBoundary = new EnemyShip(100, 800, DrawManager.SpriteType.EnemyShipA1, 1, 0, 0);
        enemyShipFormation.getEnemyShips().get(0).add(enemyAtBoundary); // 첫 번째 열에 추가

        // 테스트
        boolean result = enemyShipFormation.hasEnemyReachedBottom(800);
        assertTrue(result, "적이 경계에 도달했을 경우 true를 반환해야 합니다.");

        // 로깅
        System.out.println("EnemyShip positionY: " + enemyAtBoundary.getPositionY());
    }
}
