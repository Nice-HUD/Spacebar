package screen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import engine.Cooldown;
import engine.DrawManager;
import engine.InputManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.event.KeyEvent;

public class SkinSelectionScreenTest {
    
    @Mock
    private InputManager inputManagerMock;
    
    @Mock
    private DrawManager drawManagerMock;
    
    @Mock
    private Cooldown inputDelayMock;
    
    private SkinSelectionScreen skinSelectionScreen;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 수동으로 Mock 객체를 주입
        skinSelectionScreen = new SkinSelectionScreen(800, 600, 60);
        skinSelectionScreen.setInputManager(inputManagerMock);
        skinSelectionScreen.setDrawManager(drawManagerMock);
        skinSelectionScreen.setInputDelay(inputDelayMock);
    }
    
    /**
     * 초기 상태 확인 테스트
     */
    @Test
    public void testInitialState() {
        assertEquals(0, skinSelectionScreen.getSkincode(), "초기화 시 skincode는 0이어야 합니다.");
        assertEquals(2, skinSelectionScreen.returnCode, "초기화 시 returnCode는 2이어야 합니다.");
    }
    
    /**
     * 스킨 코드 값이 입력의 맞게 잘 돌아가는 지 확인
     */
    @Test
    public void testSkincodeChangesWithArrowKeys() {
        when(inputManagerMock.isKeyDown(KeyEvent.VK_DOWN)).thenReturn(true);
        when(inputDelayMock.checkFinished()).thenReturn(true);
        skinSelectionScreen.update();
        assertEquals(1, skinSelectionScreen.getSkincode(), "스킨 코드가 1로 증가해야 합니다.");
        
        when(inputManagerMock.isKeyDown(KeyEvent.VK_DOWN)).thenReturn(false);
        when(inputManagerMock.isKeyDown(KeyEvent.VK_UP)).thenReturn(true);
        skinSelectionScreen.update();
        assertEquals(0, skinSelectionScreen.getSkincode(), "스킨 코드가 0으로 감소해야 합니다.");
    }
    
    /**
     * 스킨 코드 최댓 값 확인 테스트
     */
    @Test
    public void testSkincodeBoundaries() {
        when(inputManagerMock.isKeyDown(KeyEvent.VK_DOWN)).thenReturn(true);
        when(inputDelayMock.checkFinished()).thenReturn(true);
        for (int i = 0; i < 10; i++) {
            skinSelectionScreen.update();
        }
        assertEquals(6, skinSelectionScreen.getSkincode(), "스킨 코드가 6 이상으로 증가하지 않아야 합니다.");
        
        when(inputManagerMock.isKeyDown(KeyEvent.VK_DOWN)).thenReturn(false);
        when(inputManagerMock.isKeyDown(KeyEvent.VK_UP)).thenReturn(true);
        for (int i = 0; i < 10; i++) {
            skinSelectionScreen.update();
        }
        assertEquals(0, skinSelectionScreen.getSkincode(), "스킨 코드가 0 이하로 감소하지 않아야 합니다.");
    }
    
    /**
     * 나가기 확인 테스트
     */
    @Test
    public void testExitWithEscape() {
        when(inputManagerMock.isKeyDown(KeyEvent.VK_ESCAPE)).thenReturn(true);
        when(inputDelayMock.checkFinished()).thenReturn(true);
        skinSelectionScreen.update();
        assertEquals(1, skinSelectionScreen.getReturnCode(), "종료 시 returnCode는 1이어야 합니다.");
        assertFalse(skinSelectionScreen.isRunning(), "ESC 키 입력 시 화면 실행이 중단되어야 합니다.");
    }
    
    /**
     * 스페이스 키 입력시 실행 종료 확인 메서드
     */
    @Test
    public void testSpaceEndsSelection() {
        when(inputManagerMock.isKeyDown(KeyEvent.VK_SPACE)).thenReturn(true);
        skinSelectionScreen.update();
        assertFalse(skinSelectionScreen.isRunning(), "SPACE 키 입력 시 화면 실행이 중단되어야 합니다.");
    }
    
    /**
     * 드로우 메서드가 잘 실행 됐는지 확인.
     */
    @Test
    public void testDrawSkinSelectionMenu() {
        skinSelectionScreen.draw();
        verify(drawManagerMock, times(1)).drawSkinSelectionMenu(skinSelectionScreen, 0);
    }
}
