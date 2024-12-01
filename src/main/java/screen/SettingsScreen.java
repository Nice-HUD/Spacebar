package screen;

import engine.*;

import java.awt.event.KeyEvent;

/**
 * Implements the settings screen.
 */
public class SettingsScreen extends Screen {

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;
    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;

    private int settingCode;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width  Screen width.
     * @param height Screen height.
     * @param fps    Frames per second, frame rate at which the game is run.
     */
    public SettingsScreen(final int width, final int height, final int fps) {
        super(width, height, fps);
        this.isRunning = true; // 초기 상태 활성화
        this.returnCode = 1; // Return to main menu by default
        this.settingCode = 0;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
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

        draw();

        if (this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                // Exit settings and return to main menu
                this.isRunning = false;
            }
            if ((inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W))) {
                previousSettingMenu();
                this.selectionCooldown.reset();
                SoundManager.getInstance().playES("menuSelect_es");
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                nextSettingMenu();
                this.selectionCooldown.reset();
                SoundManager.getInstance().playES("menuSelect_es");
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                // Apply settings (you can add additional logic here if needed)
                this.isRunning = false;
            }
        }
    }

    /**
     * Shifts the focus to the next setting menu.
     */
    private void nextSettingMenu() {
        if (this.returnCode == 1) // 소리조절에서 화면크기 변경으로 이동
            this.returnCode = 0;
        else
            this.returnCode++; // 다음 항목으로 이동
    }

    /**
     * Shifts the focus to the previous setting menu.
     */
    private void previousSettingMenu() {
        if (this.returnCode == 0)
            this.returnCode = 1; // 화면 크기 변경에서 소리조절로 이동
        else
            this.returnCode--; // 이전 항목으로 이동
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawSettingsMenu(this);

        super.drawPost();
        drawManager.completeDrawing(this);
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