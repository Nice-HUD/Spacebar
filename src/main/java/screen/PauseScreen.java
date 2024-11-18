package screen;

import java.awt.event.KeyEvent;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import engine.InputManager;

/**
 * Implements the Pause screen.
 */
public class PauseScreen extends Screen {

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;
    /** Current selected option. */
    private int selectedOption;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width
     *            Screen width.
     * @param height
     *            Screen height.
     * @param fps
     *            Frames per second, frame rate at which the game is run.
     */
    public PauseScreen(final int width, final int height, final int fps) {
        super(width, height, fps);
        this.selectedOption = 0;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
    }

    /**
     * Activates the screen.
     *
     * @return Next screen code.
     */
    @Override
    public int run() {
        super.run();

        return this.returnCode;
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    @Override
    protected void update() {
        super.update();

        draw();

        if (this.selectionCooldown.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W)) {
                previousOption();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                nextOption();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
                    || inputManager.isKeyDown(KeyEvent.VK_ENTER)) {
                selectOption();
                this.selectionCooldown.reset();
            }
        }
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);
        drawManager.drawPauseMenu(this, this.selectedOption);
        drawManager.completeDrawing(this);
    }

    /**
     * Moves the selection to the previous option.
     */
    private void previousOption() {
        if (this.selectedOption == 0) {
            this.selectedOption = 3;
        } else {
            this.selectedOption--;
        }
    }

    /**
     * Moves the selection to the next option.
     */
    private void nextOption() {
        if (this.selectedOption == 3) {
            this.selectedOption = 0;
        } else {
            this.selectedOption++;
        }
    }

    /**
     * Executes the action for the selected option.
     */
    private void selectOption() {
        switch (this.selectedOption) {
            case 0: // Continue
                this.isRunning = false;
                break;
            case 1: // Restart
                this.returnCode = 2;
                this.isRunning = false;
                break;
            case 2: // Settings
                this.returnCode = 1;
                this.isRunning = false;
                break;
            case 3: // Exit
                this.returnCode = 1;
                this.isRunning = false;
                break;
            default:
                break;
        }
    }
}