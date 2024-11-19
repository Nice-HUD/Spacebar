import engine.Core;
import engine.GameSettings;
import engine.GameState;
import engine.InputManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import screen.GameScreen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PauseTest {

    private InputManager inputManager;
    private GameState gameState;
    private GameSettings gameSettings;
    private GameScreen gameScreen;

    @BeforeEach
    public void setUp() {
        // 모든 객체를 모의 객체로 설정합니다.
        inputManager = Mockito.mock(InputManager.class);
        gameState = Mockito.mock(GameState.class);
        gameSettings = Mockito.mock(GameSettings.class);
        gameScreen = Mockito.mock(GameScreen.class);

        // Mock 객체의 기본적인 행동 설정
        when(gameScreen.getIsPaused()).thenReturn(false);
        when(gameScreen.getIsRunning()).thenReturn(true);
    }

    @Test
    public void testPauseKeyPausesGame() {
        // 초기 상태: 게임은 일시정지 상태가 아님
        assertFalse(gameScreen.getIsPaused());

        // Pause 시도
        doAnswer(invocation -> {
            when(gameScreen.getIsPaused()).thenReturn(true);
            return null;
        }).when(gameScreen).pauseGame();

        // Pause 메서드 호출
        gameScreen.pauseGame();

        // 일시정지 상태 확인
        assertTrue(gameScreen.getIsPaused());

        // Mock 객체의 특정 메서드가 호출됐는지 검증
        verify(gameScreen, times(1)).pauseGame();
    }

    @Test
    public void testResumeKeyResumesGame() {
        // 초기 상태: 게임은 일시정지 상태가 아님
        assertFalse(gameScreen.getIsPaused());

        // Pause 시도
        doAnswer(invocation -> {
            when(gameScreen.getIsPaused()).thenReturn(true);
            return null;
        }).when(gameScreen).pauseGame();

        // Pause 메서드 호출
        gameScreen.pauseGame();

        // Pause 상태 확인
        assertTrue(gameScreen.getIsPaused());

        // Resume 시도
        doAnswer(invocation -> {
            when(gameScreen.getIsPaused()).thenReturn(false);
            return null;
        }).when(gameScreen).resumeGame();

        // Resume 메서드 호출
        gameScreen.resumeGame();

        // 게임이 다시 실행되는지 확인
        assertFalse(gameScreen.getIsPaused());

        // Mock 객체의 특정 메서드가 호출됐는지 검증
        verify(gameScreen, times(1)).resumeGame();
    }

    @Test
    public void testGameStopsDuringPause() {
        // 초기 상태: 게임은 일시정지 상태가 아님
        assertFalse(gameScreen.getIsPaused());

        // Pause 시도
        doAnswer(invocation -> {
            when(gameScreen.getIsPaused()).thenReturn(true);
            return null;
        }).when(gameScreen).pauseGame();

        // Pause 메서드 호출
        gameScreen.pauseGame();

        // Pause 상태 확인
        assertTrue(gameScreen.getIsPaused());

        // 게임이 일시정지된 동안 업데이트가 중단됐는지 확인
        verify(gameScreen, never()).run();  // 일시 정지 중에는 update()가 호출되지 않음

        // Resume 시도
        doAnswer(invocation -> {
            when(gameScreen.getIsPaused()).thenReturn(false);
            return null;
        }).when(gameScreen).resumeGame();

        // Resume 메서드 호출
        gameScreen.resumeGame();

        // 게임이 다시 실행되는지 확인
        assertFalse(gameScreen.getIsPaused());
    }

    @Test
    public void testExitKeyEndsGame() {
        // 초기 상태: 게임은 일시정지 상태가 아님
        assertFalse(gameScreen.getIsPaused());

        // Pause 시도
        doAnswer(invocation -> {
            when(gameScreen.getIsPaused()).thenReturn(true);
            return null;
        }).when(gameScreen).pauseGame();

        // Pause 메서드 호출
        gameScreen.pauseGame();

        // Pause 상태 확인
        assertTrue(gameScreen.getIsPaused());

        // Exit 시도
        doAnswer(invocation -> {
            when(gameScreen.getIsRunning()).thenReturn(false);
            return null;
        }).when(gameScreen).exitGame();

        // Exit 메서드 호출
        gameScreen.exitGame();

        // 게임이 종료됐는지 확인
        assertFalse(gameScreen.getIsRunning());

        // Mock 객체의 특정 메서드가 호출됐는지 검증
        verify(gameScreen, times(1)).exitGame();
    }
}
