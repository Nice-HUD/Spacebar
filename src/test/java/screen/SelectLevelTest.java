package screen;

import engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class SelectLevelTest {

    private TitleScreen titleScreen;
    private GameState gameState;
    private InputManager inputManager;
    private DrawManager drawManager;
    private Cooldown inputDelay;

    @BeforeEach
    public void setUp() {
        // Mock InputManager와 DrawManager 생성
        gameState = mock(GameState.class);
        inputManager = mock(InputManager.class);
        drawManager = mock(DrawManager.class);
        inputDelay = mock(Cooldown.class);

        // TitleScreen 초기화
        titleScreen = new TitleScreen(630, 720, 60, gameState);

        // SettingsScreen에 Mock InputManager와 DrawManager 설정
        titleScreen.setInputManager(inputManager);
        titleScreen.setDrawManager(drawManager);
        titleScreen.setInputDelay(inputDelay);
    }

    @Test
    public void testShowLevelMenu() throws Exception {
        //플레이어 수를 선택하면(returnCode = 2일 때 스페이스바 클릭) level 선택 메뉴로 넘어감(returnCode = 7)
        when(inputDelay.checkFinished()).thenReturn(true);
        when(inputManager.isKeyDown(KeyEvent.VK_ESCAPE)).thenReturn(true);
        titleScreen.setReturnCode(2);

        Method updateMethod = TitleScreen.class.getDeclaredMethod("update");
        updateMethod.setAccessible(true); // private/protected 접근 가능 설정
        updateMethod.invoke(titleScreen);

        assertEquals(7, titleScreen.getReturnCode());
    }

    @Test
    public void testCancelLevelMenu(){
        //returnCode = 7일 때 아래방향키 누르면 플레이어 선택메뉴로 돌아감
    }

    @Test
    public void testSelectLevel(){
        //returnCode = 7일 때 방향키 클릭하여 원하는 level 선택
        //1 level에서 왼쪽 방향키 클릭-> 7 level
        //3 level에서 왼쪽 방향키 클릭-> 2 level
        //4 level에서 오른쪽 방향키 클릭-> 5 level
        //7 level에서 오른쪽 방향키 클릭-> 1 level
    }


    @Test
    public void test() {
        //Given: I am on the level selection screen
        System.out.println("test");
        //When: I select level 1

        //Then: I should be taken to level 1

   }

}

