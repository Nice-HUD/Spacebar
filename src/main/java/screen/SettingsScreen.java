package screen;

import engine.Cooldown;
import engine.DrawManager;
import engine.InputManager;

import java.awt.event.KeyEvent;

/**
 * Implements the settings screen.
 */
public class SettingsScreen extends Screen {

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
            } else if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                // Apply settings (you can add additional logic here if needed)
                this.isRunning = false;
            }
        }
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