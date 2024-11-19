package screen;

import engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PauseTest {

    private InputManager inputManager;
    private GameState gameState;
    private GameSettings gameSettings;
    private GameScreen gameScreen;
    private DrawManager drawManagerMock;

    @BeforeEach
    public void setUp() {
        // 모든 객체를 모의 객체로 설정합니다.
        inputManager = Mockito.mock(InputManager.class);
        drawManagerMock = mock(DrawManager.class);
        gameState = Mockito.mock(GameState.class);
        gameSettings = Mockito.mock(GameSettings.class);

        // gameScreen을 Spy 객체로 설정
        gameScreen = spy(new GameScreen(gameState, gameSettings, false, 630, 720, 60, false));

        gameScreen.setDrawManager(drawManagerMock);
        gameScreen.setTestMode(true);

        // 특정 키 입력시 처리
        when(inputManager.isKeyDown(KeyEvent.VK_P)).thenReturn(true);
        when(inputManager.isKeyDown(KeyEvent.VK_R)).thenReturn(true);
        when(inputManager.isKeyDown(KeyEvent.VK_M)).thenReturn(true);
    }

    @Test
    public void testPauseKeyPausesGame() {
        // 초기 상태: 게임은 일시정지 상태가 아님
        assertFalse(gameScreen.getIsPaused());

        // Pause 시도
        if (inputManager.isKeyDown(KeyEvent.VK_P)) {
            gameScreen.pauseGame();
        }

        // 일시정지 상태 확인
        assertTrue(gameScreen.getIsPaused());

        // GameScreen Spy 객체의 pauseGame() 메서드가 호출됐는지 검증
        verify(gameScreen, times(1)).pauseGame();
    }

    @Test
    public void testResumeKeyResumesGame() {
        // 초기 상태: 게임은 일시정지 상태가 아님
        assertFalse(gameScreen.getIsPaused());

        // Pause 시도
        if (inputManager.isKeyDown(KeyEvent.VK_P)) {
            gameScreen.pauseGame();
        }

        // 일시정지 상태 확인
        assertTrue(gameScreen.getIsPaused());

        // Resume 시도
        if (gameScreen.getIsPaused() && inputManager.isKeyDown(KeyEvent.VK_R)) {
            gameScreen.resumeGame();
        }

        // 게임이 다시 실행되는지 확인
        assertFalse(gameScreen.getIsPaused());

        // Mock 객체의 특정 메서드가 호출됐는지 검증
        verify(gameScreen, times(1)).resumeGame();
    }

    @Test
    public void testExitKeyEndsGame() {
        // 초기 상태: 게임은 일시정지 상태가 아님
        assertFalse(gameScreen.getIsPaused());

        // Pause 시도
        if (inputManager.isKeyDown(KeyEvent.VK_P)) {
            gameScreen.pauseGame();
        }

        // 일시정지 상태 확인
        assertTrue(gameScreen.getIsPaused());

        // Resume 시도
        if (gameScreen.getIsPaused() && inputManager.isKeyDown(KeyEvent.VK_M)) {
            gameScreen.exitGame();
        }

        // 게임이 종료됐는지 확인
        assertFalse(gameScreen.getIsRunning());
        assertFalse(gameScreen.getIsPaused());

        // Mock 객체의 특정 메서드가 호출됐는지 검증
        verify(gameScreen, times(1)).exitGame();
    }

//    @Test
//    public void testGameStopsDuringPause() {
//        // 초기 상태: 게임은 일시정지 상태가 아님
//        assertFalse(gameScreen.getIsPaused());
//
//        int preTime = gameState.getTime();
//        int preLevel = gameState.getLevel();
//        int preLives = gameState.getLivesRemaining();
//
//        // Pause 시도
//        if (inputManager.isKeyDown(KeyEvent.VK_P)) {
//            gameScreen.pauseGame();
//        }
//
//        // 일시정지 상태 확인
//        assertTrue(gameScreen.getIsPaused());
//
//        // Resume 시도
//        doAnswer(invocation -> {
//            when(gameScreen.getIsPaused()).thenReturn(false);
//            return null;
//        }).when(gameScreen).resumeGame();
//
//        // Resume 메서드 호출
//        gameScreen.resumeGame();
//
//        // 게임이 다시 실행되는지 확인
//        assertFalse(gameScreen.getIsPaused());
//
//        // 게임이 일시정지된 동안 GameState가 바뀌지 않았는지 확인
//        assertEquals(preTime, gameState.getTime());
//        assertEquals(preLevel, gameState.getLevel());
//        assertEquals(preLives, gameState.getLivesRemaining());
//    }
}
