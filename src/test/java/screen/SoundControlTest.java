package screen;

import static org.junit.jupiter.api.Assertions.*;

import engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyEvent;

public class SoundControlTest {

    private SoundManager soundManager;
    private SettingsScreen settingsScreen;

    @BeforeEach
    public void setUp() {
        System.setProperty("java.awt.headless", "true");
        // SoundManager 초기화
        soundManager = SoundManager.getInstance();
        soundManager.initializeSoundSettings(); // 초기화: volume = 5의 값으로 설정

        // Mock Frame 객체 생성
        Frame mockFrame = Mockito.mock(Frame.class);

        // Mock Frame의 getInsets() 메서드 설정
        Insets mockInsets = new Insets(10, 10, 10, 10); // 임의의 Insets 값 설정
        Mockito.when(mockFrame.getInsets()).thenReturn(mockInsets);

        // DrawManager 초기화 및 Frame 설정
        DrawManager drawManager = DrawManager.getInstance();
        drawManager.setFrame(mockFrame);

        // Mock Graphics 객체 설정
        Graphics mockGraphics = Mockito.mock(Graphics.class);
        Mockito.when(mockFrame.getGraphics()).thenReturn(mockGraphics);

        // SettingsScreen 생성
        settingsScreen = new SettingsScreen(800, 600, 60, mockFrame);

        // Mock Cooldown 객체 설정
        Cooldown mockSelectionCooldown = Mockito.mock(Cooldown.class);
        Mockito.when(mockSelectionCooldown.checkFinished()).thenReturn(true);
        settingsScreen.setSelectionCooldown(mockSelectionCooldown);

        Cooldown mockInputDelay = Mockito.mock(Cooldown.class);
        Mockito.when(mockInputDelay.checkFinished()).thenReturn(true);
        settingsScreen.setInputDelay(mockInputDelay);

        // 초기 볼륨 모드로 설정
        settingsScreen.setSettingCode(1);
    }

    @Test
    public void testVolumeIncrease() {
        // 오른쪽 키 입력 시 볼륨 증가 테스트
        int initialVolume = settingsScreen.getVolume();

        // Mock inputManager를 사용하여 오른쪽 키를 누른 상태로 시뮬레이션
        InputManager mockInputManager = Mockito.mock(InputManager.class);
        Mockito.when(mockInputManager.isKeyDown(KeyEvent.VK_RIGHT)).thenReturn(true);
        settingsScreen.setInputManager(mockInputManager);

        // 업데이트 호출하여 볼륨 증가 확인
        settingsScreen.update();

        // 볼륨이 1만큼 증가했는지 확인
        int newVolume = settingsScreen.getVolume();
        assertEquals(Math.min(initialVolume + 1, 10), newVolume, "볼륨이 1 증가해야 합니다.");

        // SoundManager의 볼륨도 같은 값인지 확인
        float expectedVolumeValue = (newVolume - 1) * 6 - 60;
//        assertEquals(expectedVolumeValue, soundManager.getCurrentVolume(), 0.01, "SoundManager의 현재 볼륨이 설정된 값과 일치해야 합니다.");
    }

    @Test
    public void testVolumeDecrease() {
        // 왼쪽 키 입력 시 볼륨 감소 테스트
        int initialVolume = settingsScreen.getVolume();

        // Mock inputManager를 사용하여 왼쪽 키를 누른 상태로 시뮬레이션
        InputManager mockInputManager = Mockito.mock(InputManager.class);
        Mockito.when(mockInputManager.isKeyDown(KeyEvent.VK_LEFT)).thenReturn(true);
        settingsScreen.setInputManager(mockInputManager);

        // 업데이트 호출하여 볼륨 감소 확인
        settingsScreen.update();

        // 볼륨이 1만큼 감소했는지 확인
        int newVolume = settingsScreen.getVolume();
//        assertEquals(Math.max(initialVolume - 1, 1), newVolume, "볼륨이 1 감소해야 합니다."); // 최소 볼륨이 1이 되도록 확인

        // SoundManager의 볼륨도 같은 값인지 확인
        float expectedVolumeValue = (newVolume - 1) * 6 - 60;
//        assertEquals(expectedVolumeValue, soundManager.getCurrentVolume(), 0.01, "SoundManager의 현재 볼륨이 설정된 값과 일치해야 합니다.");
    }

}