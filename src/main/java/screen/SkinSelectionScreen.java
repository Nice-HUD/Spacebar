package screen;

import engine.*;

import java.awt.event.KeyEvent;

/**
 * 스킨 선택 화면을 구성하는 클래스
 */
public class SkinSelectionScreen extends Screen {
    
    /**
     * 스킨 코드 : 코드 값에 따라 스킨의 모양이 달라짐
     */
    private int skincode = 0;
    
    /**
     * 생성자 returnCode 값 2로 초기화
     * 입력 지연 시간 0.2초
     *
     * @param width 화면 너비
     *
     * @param height 화면 높이
     *
     * @param fps 주사율
     *
     */
    public SkinSelectionScreen(final int width, final int height, final int fps) {
        super(width, height, fps);

        this.returnCode = 2;
        this.inputDelay = Core.getCooldown(200);
        this.inputDelay.reset();
    }
    
    /**
     * 화면 실행
     *
     * @return 다음 화면 코드
     */
    public final int run() {
        super.run();
        return this.returnCode;
    }
    
    /**
     * 화면 변화와 이벤트를 확인하고 업데이트 함.
     */
    protected final void update() {
        super.update();
        draw();
        if (this.inputDelay.checkFinished()) {
            if(inputManager.isKeyDown(KeyEvent.VK_ESCAPE)){
                this.returnCode = 1;
                isRunning = false;
            }
            if (inputManager.isKeyDown(KeyEvent.VK_UP) && skincode > 0) {
                skincode--;
                this.inputDelay.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN) && skincode < 6) {
                skincode++;
                this.inputDelay.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                this.isRunning = false;
            }
        }
    }
    
    /**
     * 화면을 그림
     */
    public void draw(){
        drawManager.initDrawing(this);
        drawManager.drawSkinSelectionMenu(this, skincode);
        drawManager.completeDrawing(this);
    }
    
    public int getSkincode() {
        return skincode;
    }
    
    public int getReturnCode() {
        return returnCode;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    
    public void setDrawManager(DrawManager drawManager) {
        this.drawManager = drawManager;
    }
    
    public void setInputDelay(Cooldown inputDelayMock) {
        this.inputDelay = inputDelayMock;
    }
}
