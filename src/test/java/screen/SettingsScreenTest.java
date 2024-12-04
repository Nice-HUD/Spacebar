package screen;

import engine.Cooldown;
import engine.DrawManager;
import engine.Frame;
import engine.InputManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SettingsScreenTest {

    private SettingsScreen settingsScreen;
    private InputManager inputManagerMock;
    private DrawManager drawManagerMock;
    private Cooldown inputDelayMock;
    private Cooldown selectionCooldownMock;  // 추가된 부분
    private Frame frameMock;

    @BeforeEach
    public void setUp() {
        inputManagerMock = mock(InputManager.class);
        drawManagerMock = mock(DrawManager.class);
        inputDelayMock = mock(Cooldown.class);
        selectionCooldownMock = mock(Cooldown.class);  // 추가된 부분
        frameMock = mock(Frame.class);

        // SettingsScreen 초기화
        settingsScreen = new SettingsScreen(800, 600, 60, frameMock);

        // SettingsScreen에 Mock InputManager, DrawManager, Cooldown 설정
        settingsScreen.setInputManager(inputManagerMock);
        settingsScreen.setDrawManager(drawManagerMock);
        settingsScreen.setInputDelay(inputDelayMock);
        settingsScreen.setSelectionCooldown(selectionCooldownMock);

        // Mock Cooldown 설정
        when(inputDelayMock.checkFinished()).thenReturn(true);
        when(selectionCooldownMock.checkFinished()).thenReturn(true);
    }
/*
    @Test
    public void testInitialState() {
        // 초기 상태 확인
        assertTrue(settingsScreen.isRunning(), "SettingsScreen은 초기 상태에서 실행 중이어야 합니다.");
        assertEquals(1, settingsScreen.getReturnCode(), "초기 returnCode는 1이어야 합니다.");
    }

    @Test
    public void testExitOnEscapeKey() {
        // ESC 키 입력 시 SettingsScreen 종료
        when(inputManagerMock.isKeyDown(KeyEvent.VK_ESCAPE)).thenReturn(true);

        settingsScreen.update(); // update 호출

        // isRunning이 false로 업데이트되었는지 확인
        assertFalse(settingsScreen.isRunning(), "ESC 키를 누르면 SettingsScreen이 종료되어야 합니다.");
        assertEquals(1, settingsScreen.getReturnCode(), "ESC 키를 누른 후 returnCode는 1이어야 합니다.");
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
        verify(drawManagerMock, times(1)).completeDrawing(settingsScreen);
    }

    @Test
    public void testNextSettingMenu() {
        // 초기 settingCode 확인 (0이어야 함)
        int initialSettingCode = settingsScreen.getSettingCode();
        assertEquals(0, initialSettingCode, "초기 settingCode는 0이어야 합니다.");

        // 다음 메뉴로 이동
        settingsScreen.nextSettingMenu();

        // settingCode가 1로 변경되었는지 확인
        int newSettingCode = settingsScreen.getSettingCode();
        assertEquals(1, newSettingCode, "다음 설정 메뉴로 이동해야 합니다.");
    }

    @Test
    public void testPreviousSettingMenu() {
        // 초기 settingCode를 1로 설정한 뒤 이전 메뉴로 이동 테스트
        settingsScreen.nextSettingMenu(); // settingCode를 1로 변경
        int initialSettingCode = settingsScreen.getSettingCode();
        assertEquals(1, initialSettingCode, "settingCode는 1이어야 합니다.");

        // 이전 메뉴로 이동
        settingsScreen.previousSettingMenu();

        // settingCode가 0으로 변경되었는지 확인
        int newSettingCode = settingsScreen.getSettingCode();
        assertEquals(0, newSettingCode, "이전 설정 메뉴로 이동해야 합니다.");
    }
 */

    @Test
    public void testThemeColorChangeOnRightKey() {
        settingsScreen.setSettingCode(2);
        // Arrange: 초기 테마 색상 인덱스 확인
        int initialColorIndex = settingsScreen.getSelectedColorIndex();
        assertEquals(0, initialColorIndex, "초기 테마 색상 인덱스는 0 (Green)이어야 합니다.");

        // Mock 오른쪽 키 입력
        when(inputManagerMock.isKeyDown(KeyEvent.VK_RIGHT)).thenReturn(true);

        // Act: update 호출
        settingsScreen.update();

        // Assert: 테마 색상 인덱스가 변경되었는지 확인
        int newColorIndex = settingsScreen.getSelectedColorIndex();
        assertEquals(1, newColorIndex, "오른쪽 키 입력 시 테마 색상 인덱스가 다음 값으로 변경되어야 합니다.");
    }

    @Test
    public void testThemeColorChangeOnLeftKey() {
        settingsScreen.setSettingCode(2);
        // Arrange: 초기 테마 색상 인덱스를 1로 설정
        settingsScreen.selectedColorIndex = 1; // 초기 색상 인덱스를 1로 설정
        assertEquals(1, settingsScreen.getSelectedColorIndex(), "초기 테마 색상 인덱스는 1이어야 합니다.");

        // Mock 왼쪽 키 입력
        when(inputManagerMock.isKeyDown(KeyEvent.VK_LEFT)).thenReturn(true);

        // Act: update 호출
        settingsScreen.update();

        // Assert: 테마 색상 인덱스가 이전 값으로 변경되었는지 확인
        int newColorIndex = settingsScreen.getSelectedColorIndex();
        assertEquals(0, newColorIndex, "왼쪽 키 입력 시 테마 색상 인덱스가 이전 값으로 변경되어야 합니다.");
    }

    @Test
    public void testThemeColorAppliedToDrawManager() {
        settingsScreen.setSettingCode(2);
        // Arrange: 특정 테마 색상 인덱스를 선택
        settingsScreen.selectedColorIndex = 2; // "Red" 선택
        settingsScreen.applyThemeColor();

        // Act: DrawManager의 테마 색상 확인
        verify(drawManagerMock, times(1)).setThemeColor(Color.RED);

        // Assert: 색상이 제대로 설정되었는지 확인
        Color currentColor = drawManagerMock.getThemeColor();
        assertEquals(Color.RED, currentColor, "테마 색상이 Red로 설정되어야 합니다.");
    }

}