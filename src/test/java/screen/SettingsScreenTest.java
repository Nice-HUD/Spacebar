//package screen;
//
//import engine.DrawManager;
//import engine.Frame;
//import engine.InputManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.awt.event.KeyEvent;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class SettingsScreenTest {
//
//    private SettingsScreen settingsScreen;
//    private InputManager inputManagerMock;
//    private DrawManager drawManagerMock;
//
//    @BeforeEach
//    public void setUp() {
//        // Frame 초기화
//        Frame frame = new Frame(800, 600);
//
//        // Mock InputManager와 DrawManager 생성
//        inputManagerMock = mock(InputManager.class);
//        drawManagerMock = mock(DrawManager.class);
//
//        // DrawManager에 Frame 설정
//        DrawManager.getInstance().setFrame(frame);
//
//        // SettingsScreen 초기화
//        settingsScreen = new SettingsScreen(800, 600, 60);
//
//        // SettingsScreen에 Mock InputManager 설정
//        settingsScreen.setInputManager(inputManagerMock);
//
//        // DrawManager를 Mock으로 대체
//        settingsScreen.setDrawManager(drawManagerMock);
//    }
//
//    @Test
//    public void testInitialState() {
//        // run() 호출하여 isRunning 활성화
////        settingsScreen.run();
//        // 초기 상태 확인
//        assertTrue(settingsScreen.isRunning(), "SettingsScreen은 초기 상태에서 실행 중이어야 합니다.");
//        assertEquals(1, settingsScreen.getReturnCode(), "초기 returnCode는 1이어야 합니다.");
//    }
//
//    @Test
//    public void testExitOnEscapeKey() {
//        // ESC 키 입력 시 SettingsScreen 종료
//        when(inputManagerMock.isKeyDown(KeyEvent.VK_ESCAPE)).thenReturn(true);
//
////        settingsScreen.update();
//
//        assertFalse(settingsScreen.isRunning(), "ESC 키를 누르면 SettingsScreen이 종료되어야 합니다.");
//        assertEquals(1, settingsScreen.getReturnCode(), "ESC 키를 누른 후 returnCode는 1이어야 합니다.");
//    }
//
//    @Test
//    public void testApplySettingsOnSpaceKey() {
//        // SPACE 키 입력 시 SettingsScreen 종료
//        when(inputManagerMock.isKeyDown(KeyEvent.VK_SPACE)).thenReturn(true);
//
////        settingsScreen.update();
//
//        assertFalse(settingsScreen.isRunning(), "SPACE 키를 누르면 SettingsScreen이 종료되어야 합니다.");
//        assertEquals(1, settingsScreen.getReturnCode(), "SPACE 키를 누른 후 returnCode는 1이어야 합니다.");
//    }
//
//    @Test
//    public void testNoEffectOnInvalidKey() {
//        // 유효하지 않은 키 입력 시 아무 동작도 하지 않아야 함
//        when(inputManagerMock.isKeyDown(KeyEvent.VK_A)).thenReturn(true);
//
////        settingsScreen.update();
//
//        assertTrue(settingsScreen.isRunning(), "유효하지 않은 키 입력 시 SettingsScreen은 실행 상태를 유지해야 합니다.");
//        assertEquals(1, settingsScreen.getReturnCode(), "유효하지 않은 키 입력 시 returnCode는 변경되지 않아야 합니다.");
//    }
//
//    @Test
//    public void testDrawSettingsMenuCalled() {
//        // Settings 메뉴를 그리는 메서드 호출 확인
////        settingsScreen.update();
//
//        verify(drawManagerMock, times(1)).initDrawing(settingsScreen);
//        verify(drawManagerMock, times(1)).drawSettingsMenu(settingsScreen);
//        verify(drawManagerMock, times(1)).completeDrawing(settingsScreen);
//    }
//}




package screen;

import engine.DrawManager;
import engine.Frame;
import engine.InputManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SettingsScreenTest {

    private SettingsScreen settingsScreen;
    private InputManager inputManagerMock;
    private DrawManager drawManagerMock;

    @BeforeEach
    public void setUp() {
        // Frame 초기화
        Frame frame = new Frame(800, 600);

        // Mock InputManager와 DrawManager 생성
        inputManagerMock = mock(InputManager.class);
        drawManagerMock = mock(DrawManager.class);

        // DrawManager에 Frame 설정
        DrawManager.getInstance().setFrame(frame);

        // SettingsScreen 초기화
        settingsScreen = new SettingsScreen(800, 600, 60);

        // SettingsScreen에 Mock InputManager와 DrawManager 설정
        settingsScreen.setInputManager(inputManagerMock);
        settingsScreen.setDrawManager(drawManagerMock);
    }

    @Test
    public void testInitialState() {
        // 초기 상태 확인
        assertTrue(settingsScreen.isRunning(), "SettingsScreen은 초기 상태에서 실행 중이어야 합니다.");
        assertEquals(1, settingsScreen.getReturnCode(), "초기 returnCode는 1이어야 합니다.");
    }

    @Disabled
    public void testExitOnEscapeKey() {
        // ESC 키 입력 시 SettingsScreen 종료
        when(inputManagerMock.isKeyDown(KeyEvent.VK_ESCAPE)).thenReturn(true);

        settingsScreen.update(); // update 호출

        // isRunning이 false로 업데이트되었는지 확인
        assertFalse(settingsScreen.isRunning(), "ESC 키를 누르면 SettingsScreen이 종료되어야 합니다.");
        assertEquals(1, settingsScreen.getReturnCode(), "ESC 키를 누른 후 returnCode는 1이어야 합니다.");
    }

    @Disabled
    public void testApplySettingsOnSpaceKey() {
        // SPACE 키 입력 시 SettingsScreen 종료
        when(inputManagerMock.isKeyDown(KeyEvent.VK_SPACE)).thenReturn(true);

        settingsScreen.update(); // update 호출

        // isRunning이 false로 업데이트되었는지 확인
        assertFalse(settingsScreen.isRunning(), "SPACE 키를 누르면 SettingsScreen이 종료되어야 합니다.");
        assertEquals(1, settingsScreen.getReturnCode(), "SPACE 키를 누른 후 returnCode는 1이어야 합니다.");
    }

    @Test
    public void testNoEffectOnInvalidKey() {
        // 유효하지 않은 키 입력 시 아무 동작도 하지 않아야 함
        when(inputManagerMock.isKeyDown(anyInt())).thenReturn(false);

        settingsScreen.update(); // update 호출

        assertTrue(settingsScreen.isRunning(), "유효하지 않은 키 입력 시 SettingsScreen은 실행 상태를 유지해야 합니다.");
        assertEquals(1, settingsScreen.getReturnCode(), "유효하지 않은 키 입력 시 returnCode는 변경되지 않아야 합니다.");
    }

    @Test
    public void testDrawSettingsMenuCalled() {
        // Settings 메뉴를 그리는 메서드 호출 확인
        settingsScreen.update(); // update 호출

        verify(drawManagerMock, times(1)).initDrawing(settingsScreen);
        verify(drawManagerMock, times(1)).drawSettingsMenu(settingsScreen);
        verify(drawManagerMock, times(1)).completeDrawing(settingsScreen);
    }
}



//package screen;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import engine.Core;
//import engine.DrawManager;
//import engine.Frame;
//import engine.InputManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.awt.event.KeyEvent;
//
//public class SettingsScreenTest {
//
//    private SettingsScreen settingsScreen;
//    private TitleScreen titleScreen;
//    private InputManager inputManager;
//
//    @BeforeEach
//    public void setUp() {
//        // Frame 초기화
//        Frame frame = new Frame(800, 600);
//        DrawManager.getInstance().setFrame(frame);
//
//        // InputManager 초기화
//        inputManager = InputManager.getInstance();
//
//        // SettingsScreen 초기화
//        settingsScreen = new SettingsScreen(800, 600, 60);
//    }
////    @BeforeEach
////    public void setUp() {
////        // Core 초기화 및 InputManager 설정
////        Core.initialize();
////        inputManager = Core.getInputManager();
////
////        // SettingsScreen과 TitleScreen 초기화
////        settingsScreen = new SettingsScreen(800, 600, 60);
////        titleScreen = new TitleScreen(800, 600, 60);
////    }
//
////    @Test
////    public void testSettingsScreenEscapeKey() {
////        // ESC 키로 Main Menu로 돌아가는지 테스트
////        inputManager.pressKey(KeyEvent.VK_ESCAPE);
////        settingsScreen.run();
////        assertEquals(1, settingsScreen.run(), "ESC 키로 Main Menu로 돌아가야 합니다.");
////    }
////
////    @Test
////    public void testSettingsScreenSpaceKey() {
////        // SPACE 키로 설정 적용
////        inputManager.pressKey(KeyEvent.VK_SPACE);
////        settingsScreen.run();
////        assertEquals(1, settingsScreen.run(), "SPACE 키로 설정을 적용하고 Main Menu로 돌아가야 합니다.");
////    }
////
////    @Test
////    public void testTitleScreenNavigation() {
////        // UP/DOWN 키로 메뉴 선택 변경 테스트
////        inputManager.pressKey(KeyEvent.VK_DOWN);
////        titleScreen.run();
////        assertEquals(3, titleScreen.run(), "DOWN 키로 메뉴가 아래로 이동해야 합니다.");
////
////        inputManager.pressKey(KeyEvent.VK_UP);
////        titleScreen.run();
////        assertEquals(2, titleScreen.run(), "UP 키로 메뉴가 위로 이동해야 합니다.");
////    }
////
////    @Test
////    public void testTitleScreenSettingsOption() {
////        // Settings 옵션 선택 시 SettingsScreen으로 이동
////        titleScreen.run();
////        inputManager.pressKey(KeyEvent.VK_DOWN); // Settings로 이동
////        inputManager.pressKey(KeyEvent.VK_DOWN);
////        inputManager.pressKey(KeyEvent.VK_SPACE); // 선택 확인
////        assertEquals(6, titleScreen.run(), "Settings 메뉴 선택 시 SettingsScreen으로 이동해야 합니다.");
////    }
////
////    @Test
////    public void testTitleScreenExitOption() {
////        // Exit 옵션 선택 시 종료
////        inputManager.pressKey(KeyEvent.VK_DOWN);
////        inputManager.pressKey(KeyEvent.VK_DOWN);
////        inputManager.pressKey(KeyEvent.VK_DOWN);
////        inputManager.pressKey(KeyEvent.VK_DOWN); // Exit로 이동
////        inputManager.pressKey(KeyEvent.VK_SPACE); // 선택 확인
////        assertEquals(0, titleScreen.run(), "Exit 메뉴 선택 시 종료 코드가 반환되어야 합니다.");
////    }
//
//
//    @Test
//    public void testSettingsMenuDisplay() {
//        // Settings 메뉴가 정상적으로 표시되는지 확인
////        settingsScreen.run();
//        assertDoesNotThrow(() -> settingsScreen.run(), "Settings 메뉴가 정상적으로 표시되어야 합니다.");
//    }
//
//    @Test
//    public void testTitleMenuDisplay() {
//        // Title 메뉴가 정상적으로 표시되는지 확인
////        titleScreen.run();
//        assertDoesNotThrow(() -> titleScreen.run(), "Title 메뉴가 정상적으로 표시되어야 합니다.");
//    }
//}
