import engine.Core;
import engine.GameSettings;
import engine.GameState;
import engine.InputManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import screen.GameScreen;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PauseTest {
    private InputManager inputManager;
    private GameState gameState;
    private GameSettings gameSettings;
    private GameScreen gameScreen;
    /** Difficulty settings for level 1. */
    private static final GameSettings SETTINGS_LEVEL_1 =
            new GameSettings(5, 4, 60, 2000, 1);
    private Core core;

    @BeforeEach
    public void setUp() {
        // 이 메서드는 각 테스트 메서드가 실행되기 전에 실행됩니다.
        inputManager = Core.getInputManager();
        gameState = new GameState(1, 0,3, 3,0, 0, 0, 0, 0, 0, 0);
        gameScreen = new GameScreen(gameState, SETTINGS_LEVEL_1, false, 630, 720, 60, false);
        gameScreen.initialize();
    }

    @Test
    public void testPauseKeyPausesGame() throws AWTException {
        assertFalse(gameScreen.getIsPaused());

        // KeyEvent 객체 생성
        KeyEvent keyEvent = new KeyEvent(
                new java.awt.TextField(), // 소스 객체 (이벤트가 발생한 컴포넌트)
                KeyEvent.KEY_PRESSED,     // 이벤트 타입 (KEY_PRESSED)
                System.currentTimeMillis(), // 이벤트가 발생한 시간
                0,                          // 수정자 키 (예: Shift, Ctrl 등의 상태)
                KeyEvent.VK_P,              // 눌린 키의 코드 (예: 'A' 키)
                'P'                         // 키의 문자 표현
        );

        inputManager.keyPressed(keyEvent);

        assertTrue(inputManager.isKeyDown(KeyEvent.VK_P));
        assertTrue(gameScreen.getIsPaused());   // 게임이 일시 정지 상태인지 확인
    }

    @Test
    public void testResumeKeyResumesGame() {
        assertFalse(gameScreen.getIsPaused());

        // KeyEvent 객체 생성
        KeyEvent keyEvent = new KeyEvent(
                new java.awt.TextField(), // 소스 객체 (이벤트가 발생한 컴포넌트)
                KeyEvent.KEY_PRESSED,     // 이벤트 타입 (KEY_PRESSED)
                System.currentTimeMillis(), // 이벤트가 발생한 시간
                0,                          // 수정자 키 (예: Shift, Ctrl 등의 상태)
                KeyEvent.VK_P,              // 눌린 키의 코드 (예: 'A' 키)
                'P'                         // 키의 문자 표현
        );

        inputManager.keyPressed(keyEvent); // 'ESC' or 'P' 키를 누름
        assertTrue(gameScreen.getIsPaused());   // 게임이 일시 정지 상태인지 확인


        inputManager.isKeyDown(KeyEvent.VK_R); // 'R' 키를 누름
        assertFalse(gameScreen.getIsPaused());  // 게임이 재개되었는지 확인
    }

    @Test
    public void testRestartKeyRestartGame() {
        assertFalse(gameScreen.getIsPaused());

        // KeyEvent 객체 생성
        KeyEvent keyEvent = new KeyEvent(
                new java.awt.TextField(), // 소스 객체 (이벤트가 발생한 컴포넌트)
                KeyEvent.KEY_PRESSED,     // 이벤트 타입 (KEY_PRESSED)
                System.currentTimeMillis(), // 이벤트가 발생한 시간
                0,                          // 수정자 키 (예: Shift, Ctrl 등의 상태)
                KeyEvent.VK_P,              // 눌린 키의 코드 (예: 'A' 키)
                'P'                         // 키의 문자 표현
        );

        inputManager.keyPressed(keyEvent); // 'ESC' or 'P' 키를 누름
        assertTrue(gameScreen.getIsPaused());   // 게임이 일시 정지 상태인지 확인

        // 같은 레벨로 재시작 되는지 확인
        int preLevel = gameState.getLevel();

        inputManager.isKeyDown(KeyEvent.VK_Q); // 'Q' 키를 누름
        assertFalse(gameScreen.getIsPaused());  // isPaused 초기화 확인
        assertFalse(gameScreen.getIsRunning()); // gameScreen 종료 확인
        assertEquals(preLevel, gameState.getLevel());   // 같은 레벨로 동작함
    }

    @Test
    public void testExitKeyExitGame() {
        assertFalse(gameScreen.getIsPaused());

        // KeyEvent 객체 생성
        KeyEvent keyEvent = new KeyEvent(
                new java.awt.TextField(), // 소스 객체 (이벤트가 발생한 컴포넌트)
                KeyEvent.KEY_PRESSED,     // 이벤트 타입 (KEY_PRESSED)
                System.currentTimeMillis(), // 이벤트가 발생한 시간
                0,                          // 수정자 키 (예: Shift, Ctrl 등의 상태)
                KeyEvent.VK_P,              // 눌린 키의 코드 (예: 'A' 키)
                'P'                         // 키의 문자 표현
        );

        inputManager.keyPressed(keyEvent);; // 'ESC' or 'P' 키를 누름
        assertTrue(gameScreen.getIsPaused());   // 게임이 일시 정지 상태인지 확인

        inputManager.isKeyDown(KeyEvent.VK_M); // 'M' 키를 누름
        assertFalse(gameScreen.getIsPaused());  // isPaused 초기화 확인
        assertFalse(gameScreen.getIsRunning()); // gameScreen 종료 확인
    }

    @Test
    public void testGameStopsDuringPause() {
        assertFalse(gameScreen.getIsPaused());

        inputManager.isKeyDown(KeyEvent.VK_ESCAPE); // 'ESC' or 'P' 키를 누름
        assertTrue(gameScreen.getIsPaused());   // 게임이 일시 정지 상태인지 확인

        // 게임 내 요소들이 업데이트되지 않아야 함
        int prePositionX = gameScreen.getShip().getPositionX();
        int prePositionY = gameScreen.getShip().getPositionY();
        int preLevel = gameState.getLevel();
        int preScore = gameState.getScore();
        int preLivesRemaining = gameState.getLivesRemaining();
        int prePlaytime = gameState.getTime();

        inputManager.isKeyDown(KeyEvent.VK_R); // 'R' 키를 누름
        assertFalse(gameScreen.getIsPaused());  // 게임이 재개되었는지 확인

        assertEquals(prePositionX, gameScreen.getShip().getPositionX()); // 위치 변화 없음
        assertEquals(prePositionY, gameScreen.getShip().getPositionY()); // 위치 변화 없음
        assertEquals(preLevel, gameScreen.getGameState().getLevel());
        assertEquals(preScore, gameScreen.getGameState().getScore());
        assertEquals(preLivesRemaining, gameScreen.getGameState().getLivesRemaining());
        assertEquals(prePlaytime, gameScreen.getGameState().getTime()); // gameState 변화 없음
    }

}
