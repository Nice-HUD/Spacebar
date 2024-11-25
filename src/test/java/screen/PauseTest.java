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
        when(inputManager.isKeyDown(KeyEvent.VK_Q)).thenReturn(true);
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

        // GameScreen Spy 객체의 resumeGame() 메서드가 호출됐는지 검증
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

        // GameScreen Spy 객체의 exitGame() 메서드가 호출됐는지 검증
        verify(gameScreen, times(1)).exitGame();
    }

    @Test
    public void testRestartKeyRestartsGame() {
        // 초기 상태: 게임은 일시정지 상태가 아님
        assertFalse(gameScreen.getIsPaused());

        // Pause 시도
        if (inputManager.isKeyDown(KeyEvent.VK_P)) {
            gameScreen.pauseGame();
        }

        // 일시정지 상태 확인
        assertTrue(gameScreen.getIsPaused());

        // Resume 시도
        if (gameScreen.getIsPaused() && inputManager.isKeyDown(KeyEvent.VK_Q)) {
            gameScreen.restartGame();
        }

        // 게임이 재시작됐는지 확인
        assertFalse(gameScreen.getIsPaused());

        // GameScreen Spy 객체의 restartGame() 메서드가 호출됐는지 검증
        verify(gameScreen, times(1)).restartGame();
    }
}
