package screen;

import engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.event.KeyEvent;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class SelectLevelTest {

    private GameState gameState;
    private TitleScreen titleScreen;
    private InputManager inputManager;
    private DrawManager drawManager;
    private Cooldown inputDelay;

    @BeforeEach
    public void setUp() {
        gameState = Mockito.mock(GameState.class);
        inputManager = mock(InputManager.class);
        drawManager = mock(DrawManager.class);
        inputDelay = mock(Cooldown.class);

        // SettingsScreen 초기화
        titleScreen = new TitleScreen(800, 600, 60, gameState);

        // titleScreen에 Mock InputManager와 DrawManager 설정
        titleScreen.setInputManager(inputManager);
        titleScreen.setDrawManager(drawManager);
        titleScreen.setInputDelay(inputDelay);
    }

    @Test
    public void testShowLevelMenu() throws Exception {
        //플레이어 수를 선택하면(returnCode = 2일 때 스페이스바 클릭) level 선택 메뉴로 넘어감(returnCode = 7)
        when(inputManager.isKeyDown(KeyEvent.VK_SPACE)).thenReturn(true);
        when(inputDelay.checkFinished()).thenReturn(true);
        titleScreen.setReturnCode(2);

        Method updateMethod = TitleScreen.class.getDeclaredMethod("update");
        updateMethod.setAccessible(true); // private/protected 접근 가능 설정
        updateMethod.invoke(titleScreen);

        assertEquals(7, titleScreen.getReturnCode());
    }


    @Test
    public void testCalcelLevel() throws Exception{
        //레벨 선택을 취소하면(returnCode = 7일 때 아래쪽 방향키 클릭) 플레이어 선택 메뉴로 돌아감(returnCode = 2)
        when(inputManager.isKeyDown(KeyEvent.VK_DOWN)).thenReturn(true);
        when(inputDelay.checkFinished()).thenReturn(true);
        titleScreen.setReturnCode(7);

        Method updateMethod = TitleScreen.class.getDeclaredMethod("update");
        updateMethod.setAccessible(true); // private/protected 접근 가능 설정
        updateMethod.invoke(titleScreen);

        assertEquals(2, titleScreen.getReturnCode());
    }

    /*
    @Test
    public void testSelectLevel() throws Exception{
        //방향키를 눌러서 선택할 레벨을 변경
        // private/protected 접근 가능 설정
        Method moveLevelLeftMethod = TitleScreen.class.getDeclaredMethod("moveLevelLeft");
        Method moveLevelRightMethod = TitleScreen.class.getDeclaredMethod("moveLevelRight");
        moveLevelLeftMethod.setAccessible(true);
        moveLevelRightMethod.setAccessible(true);

        //레벨 1에서 왼쪽 방향키 클릭 -> 레벨 7
        titleScreen.setSelectedLevel(1);
        moveLevelLeftMethod.invoke(titleScreen);
        assertEquals(7, titleScreen.getSelectedLevel());

        //레벨 3에서 왼쪽 방향키 클릭 -> 레벨 2
        titleScreen.setSelectedLevel(3);
        moveLevelLeftMethod.invoke(titleScreen);
        assertEquals(2, titleScreen.getSelectedLevel());

        //레벨 4에서 오른쪽 방향키 클릭 -> 레벨 5
        titleScreen.setSelectedLevel(4);
        moveLevelRightMethod.invoke(titleScreen);
        assertEquals(5, titleScreen.getSelectedLevel());

        //레벨 7에서 오른쪽 방향키 클릭 -> 레벨 1
        titleScreen.setSelectedLevel(7);
        moveLevelRightMethod.invoke(titleScreen);
        assertEquals(1, titleScreen.getSelectedLevel());
    }
    */
}
