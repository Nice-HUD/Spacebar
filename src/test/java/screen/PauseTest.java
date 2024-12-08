package screen;

import engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PauseTest {

    private InputManager inputManager;
    private GameState gameState;
    private GameSettings gameSettings;
    private GameScreen gameScreen;
    private DrawManager drawManagerMock;
    private KeyEvent p;
    private KeyEvent r;
    private KeyEvent m;
    private KeyEvent q;

    /**
     * 설정 메서드: 테스트 실행 전 모의 객체와 스파이 객체를 초기화하고 필요한 키 이벤트를 생성합니다.
     */
    @BeforeEach
    void setUp() {
        inputManager = Core.getInputManager();
        
        // 모의 객체 생성
        drawManagerMock = Mockito.mock(DrawManager.class);
        gameState = Mockito.mock(GameState.class);
        gameSettings = Mockito.mock(GameSettings.class);

        // gameScreen을 Spy 객체로 설정
        gameScreen = spy(new GameScreen(gameState, gameSettings, false, 630, 720, 60, false));

        gameScreen.setInputManager(inputManager);
        gameScreen.setDrawManager(drawManagerMock);
        gameScreen.setTestMode(true);

        TextField mockField = mock(TextField.class);
        // KeyEvent 객체 생성
        p = new KeyEvent(
                mockField, // 소스 객체 (이벤트가 발생한 컴포넌트)
                KeyEvent.KEY_PRESSED,     // 이벤트 타입 (KEY_PRESSED)
                System.currentTimeMillis(), // 이벤트가 발생한 시간
                0,                          // 수정자 키 (예: Shift, Ctrl 등의 상태)
                KeyEvent.VK_P,              // 눌린 키의 코드 (예: 'P' 키)
                'P'                         // 키의 문자 표현
        );
        // KeyEvent 객체 생성
        r = new KeyEvent(
                mockField, // 소스 객체 (이벤트가 발생한 컴포넌트)
                KeyEvent.KEY_PRESSED,     // 이벤트 타입 (KEY_PRESSED)
                System.currentTimeMillis(), // 이벤트가 발생한 시간
                0,                          // 수정자 키 (예: Shift, Ctrl 등의 상태)
                KeyEvent.VK_R,              // 눌린 키의 코드 (예: 'P' 키)
                'R'                         // 키의 문자 표현
        );
        // KeyEvent 객체 생성
        m = new KeyEvent(
                mockField, // 소스 객체 (이벤트가 발생한 컴포넌트)
                KeyEvent.KEY_PRESSED,     // 이벤트 타입 (KEY_PRESSED)
                System.currentTimeMillis(), // 이벤트가 발생한 시간
                0,                          // 수정자 키 (예: Shift, Ctrl 등의 상태)
                KeyEvent.VK_M,              // 눌린 키의 코드 (예: 'P' 키)
                'M'                         // 키의 문자 표현
        );
        // KeyEvent 객체 생성
        q = new KeyEvent(
                mockField, // 소스 객체 (이벤트가 발생한 컴포넌트)
                KeyEvent.KEY_PRESSED,     // 이벤트 타입 (KEY_PRESSED)
                System.currentTimeMillis(), // 이벤트가 발생한 시간
                0,                          // 수정자 키 (예: Shift, Ctrl 등의 상태)
                KeyEvent.VK_Q,              // 눌린 키의 코드 (예: 'P' 키)
                'Q'                         // 키의 문자 표현
        );

    }

    /**
     * 테스트 메서드: 일시정지 상태에서 재개 키(R)를 누르면 게임이 다시 실행 상태로 전환되는지 확인합니다.
     */
    @Test
    void testPauseKeyPausesGame() {
        // 초기 상태: 게임은 일시정지 상태가 아님
        assertFalse(gameScreen.getIsPaused(), "초기 상태에서는 게임이 일시정지 상태가 아님");

        inputManager.keyPressed(p);

        // Pause 시도
        if (inputManager.isKeyDown(KeyEvent.VK_P)) {
            gameScreen.pauseGame();
        }

        inputManager.keyReleased(p);

        // 일시정지 상태 확인
        assertTrue(gameScreen.getIsPaused(), "게임은 일시 정지 상태여야 합니다.");

        // GameScreen Spy 객체의 pauseGame() 메서드가 호출됐는지 검증
        verify(gameScreen, times(1)).pauseGame();
        // pause 메뉴 UI가 제대로 그려졌는지 확인
        verify(drawManagerMock, times(1)).drawPauseOverlay(gameScreen);
    }

    /**
     * 테스트 메서드: 일시정지 상태에서 재개 키(R)를 누르면 게임이 다시 실행 상태로 전환되는지 확인합니다.
     */
    @Test
    void testResumeKeyResumesGame() {
        // 초기 상태: 게임은 일시정지 상태가 아님
        assertFalse(gameScreen.getIsPaused(), "초기 상태에서는 게임이 일시정지 상태가 아님");

        inputManager.keyPressed(p);

        // Pause 시도
        if (inputManager.isKeyDown(KeyEvent.VK_P)) {
            gameScreen.pauseGame();
        }

        inputManager.keyReleased(p);

        // 일시정지 상태 확인
        assertTrue(gameScreen.getIsPaused(), "게임은 일시 정지 상태여야 합니다.");

        // Resume 시도
        inputManager.keyPressed(r);

        if (gameScreen.getIsPaused() && inputManager.isKeyDown(KeyEvent.VK_R)) {
            gameScreen.pauseGame();
        }

        inputManager.keyReleased(r);

        // 게임이 다시 실행되는지 확인
        assertFalse(gameScreen.getIsPaused(), "게임이 다시 실행되어야 합니다.");

        // GameScreen Spy 객체의 resumeGame() 메서드가 호출됐는지 검증
        verify(gameScreen, times(1)).resumeGame();
    }

    /**
     * 테스트 메서드: 일시정지 상태에서 종료 키(M)를 누르면 게임이 종료되고 메인 메뉴로 돌아가는지 확인합니다.
     */
    @Test
    void testExitKeyEndsGame() {
        // 초기 상태: 게임은 일시정지 상태가 아님
        assertFalse(gameScreen.getIsPaused(), "초기 상태에서는 게임이 일시정지 상태가 아님");

        inputManager.keyPressed(p);

        // Pause 시도
        if (inputManager.isKeyDown(KeyEvent.VK_P)) {
            gameScreen.pauseGame();
        }

        inputManager.keyReleased(p);

        // 일시정지 상태 확인
        assertTrue(gameScreen.getIsPaused(), "게임은 일시 정지 상태여야 합니다.");

        // Exit 시도
        inputManager.keyPressed(m);

        if (gameScreen.getIsPaused() && inputManager.isKeyDown(KeyEvent.VK_M)) {
            gameScreen.pauseGame();
        }

        inputManager.keyReleased(m);

        // 게임이 종료됐는지 확인
        assertFalse(gameScreen.getIsRunning(), "게임이 실행 종료되어야 합니다.");
        assertFalse(gameScreen.getIsPaused(), "일시정지 상태에서 벗어나, 실행 종료 후 메인 메뉴로 나가야합니다.");
        assertEquals(1, gameScreen.getReturnCode(), "exit 시에 returnCode가 1로 설정되어야 합니다.");

        // GameScreen Spy 객체의 exitGame() 메서드가 호출됐는지 검증
        verify(gameScreen, times(1)).exitGame();
    }

    /**
     * 테스트 메서드: 일시정지 상태에서 재시작 키(Q)를 누르면 게임이 재시작되는지 확인합니다.
     */
    @Test
    void testRestartKeyRestartsGame() {
        // 초기 상태: 게임은 일시정지 상태가 아님
        assertFalse(gameScreen.getIsPaused(), "초기 상태에서는 게임이 일시정지 상태가 아님");

        inputManager.keyPressed(p);

        // Pause 시도
        if (inputManager.isKeyDown(KeyEvent.VK_P)) {
            gameScreen.pauseGame();
        }

        inputManager.keyReleased(p);

        // 일시정지 상태 확인
        assertTrue(gameScreen.getIsPaused(), "게임은 일시 정지 상태여야 합니다.");

        // Restart 시도
        inputManager.keyPressed(q);

        if (gameScreen.getIsPaused() && inputManager.isKeyDown(KeyEvent.VK_Q)) {
            gameScreen.pauseGame();
        }

        inputManager.keyReleased(q);

        // 게임이 재시작되는지 확인
        assertFalse(gameScreen.getIsPaused(), "일시정지 상태에서 벗어나, 재시작되어야 합니다.");
        assertEquals(2, gameScreen.getReturnCode(), "restart 시에 returnCode가 2로 설정되어야 합니다.");

        // GameScreen Spy 객체의 restartGame() 메서드가 호출됐는지 검증
        verify(gameScreen, times(1)).restartGame();
    }

}
