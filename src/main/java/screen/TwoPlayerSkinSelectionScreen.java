package screen;

import engine.Core;

import java.awt.event.KeyEvent;

/**
 * 2인 모드 스킨 선택 클래스
 */
public class TwoPlayerSkinSelectionScreen extends Screen {
    
    /**
     * 스킨 코드 : 코드 값에 따라 스킨의 모양이 달라짐
     */
    private int skincode = 0;
    
    /**
     * 생성자 returnCode 값 4로 초기화
     * 입력 지연 시간 0.2초
     *
     * @param width 화면 너비
     *
     * @param height 화면 높이
     *
     * @param fps 주사율
     *
     */
    public TwoPlayerSkinSelectionScreen(final int width, final int height, final int fps) {
        super(width, height, fps);

        this.returnCode = 4;
        this.inputDelay = Core.getCooldown(200);
        this.inputDelay.reset();
    }
    
    /**
     * 화면 실행
     *
     * @return 다음 화면 코드
     */
    @Override
    public final int run() {
        super.run();
        return this.returnCode;
    }
    
    /**
     * 화면 변화와 이벤트를 확인하고 업데이트 함.
     */
    @Override
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
}
