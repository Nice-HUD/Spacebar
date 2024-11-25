package screen;

import engine.*;
import engine.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Implements the settings screen.
 */
public class SettingsScreen extends Screen {

    private final String[] resolutions = {"1024x576", "960x540", "800x600","630x720"};
    private int selectedResolutionIndex = 0;
    private final Frame frame; // Frame 객체
    private boolean resolutionChanged = false; // 해상도 변경 플래그




    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width  Screen width.
     * @param height Screen height.
     * @param fps    Frames per second, frame rate at which the game is run.
     */
    public SettingsScreen(final int width, final int height, final int fps, final Frame frame) {
        super(width, height, fps);
        this.frame = frame;
        this.isRunning = true; // 초기 상태 활성화
        this.returnCode = 1; // Return to main menu by default
    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        return this.returnCode; // Return code determines next screen
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final void update() {
        super.update();


        // 새 해상도에 맞춰 DrawManager 초기화
        if (resolutionChanged) {
            DrawManager.getInstance().initDrawing(this);
            resolutionChanged = false; // 초기화 완료 후 플래그 해제
        }


        draw();

        if (this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                // 메인 메뉴로 돌아가기
                this.isRunning = false;
            } else if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
                // 이전 해상도 선택
                selectedResolutionIndex = (selectedResolutionIndex - 1 + resolutions.length) % resolutions.length;
                this.inputDelay.reset();
            } else if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
                // 다음 해상도 선택
                selectedResolutionIndex = (selectedResolutionIndex + 1) % resolutions.length;
                this.inputDelay.reset();
            } else if (inputManager.isKeyDown(KeyEvent.VK_ENTER)) {
                // 설정 적용
                applyResolution();
                resolutionChanged = true; // 해상도 변경 플래그 설정
                this.inputDelay.reset();
            }
        }
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawSettingsMenu(this, resolutions, selectedResolutionIndex);


        drawManager.completeDrawing(this);
    }

    private void applyResolution() {
        String selectedResolution = resolutions[selectedResolutionIndex];
        String[] dimensions = selectedResolution.split("x");

        int newWidth = Integer.parseInt(dimensions[0].trim());
        int newHeight = Integer.parseInt(dimensions[1].trim());


        Core.setWidth(newWidth);
        Core.setHeight(newHeight);

        frame.updateSize(newWidth, newHeight);


        DrawManager.getInstance().initDrawing(this); // 새로운 해상도에 맞게 초기화

    }

    public void setInputManager(InputManager inputManagerMock) {
        this.inputManager = inputManagerMock;
    }

    public void setDrawManager(DrawManager drawManagerMock) {
        this.drawManager = drawManagerMock;
    }

    public int getReturnCode() {
        return this.returnCode;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void setInputDelay(Cooldown inputDelayMock) { this.inputDelay = inputDelayMock; }
}