package screen;

import engine.*;

import java.awt.event.KeyEvent;

/**
 * Implements the high scores screen, it shows player records.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class ReceiptScreen extends Screen {
	
	private final RoundState roundState;
	private final GameState gameState;
	private Cooldown selectionCooldown;
	private int selectedLevel = 1;

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
	public ReceiptScreen(final int width, final int height, final int fps, final RoundState roundState, final GameState gameState) {
		super(width, height, fps);

		this.roundState = roundState;
		this.gameState = gameState;
		this.returnCode = 2;
		this.selectionCooldown = Core.getCooldown(200);
		this.selectionCooldown.reset();
	}

	/**
	 * Starts the action.
	 * 
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		if (this.returnCode == 4) {
			return 2;
		}

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		draw();
		if (this.selectionCooldown.checkFinished()
				&& this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_UP)
					|| inputManager.isKeyDown(KeyEvent.VK_W)) {
				previousMenuItem();
				this.selectionCooldown.reset();
				SoundManager.getInstance().playES("menuSelect_es");
			}
			if ((inputManager.isKeyDown(KeyEvent.VK_DOWN)
					|| inputManager.isKeyDown(KeyEvent.VK_S)) && returnCode != 4) {
				nextMenuItem();
				this.selectionCooldown.reset();
				SoundManager.getInstance().playES("menuSelect_es");
			}

			if (returnCode == 4) {
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
						|| inputManager.isKeyDown(KeyEvent.VK_A)) {
					if (selectedLevel == 1)
						selectedLevel = 7;
					else
						selectedLevel--;
					gameState.setLevel(selectedLevel);
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
						|| inputManager.isKeyDown(KeyEvent.VK_D)) {
					if (selectedLevel == 7)
						selectedLevel = 1;
					else
						selectedLevel++;
					gameState.setLevel(selectedLevel);
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}
			}
			if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
					&& this.inputDelay.checkFinished())
				if (returnCode == 3) {
					returnCode = 4;
					this.selectionCooldown.reset();
					SoundManager.getInstance().playES("menuSelect_es");
				}
				else this.isRunning = false;
		}
	}

	private void nextMenuItem() {
		if (this.returnCode == 3)
			this.returnCode = 1;
		else
			this.returnCode++;
	}

	/**
	 * Shifts the focus to the previous menu item.
	 */
	private void previousMenuItem() {
		if (this.returnCode == 1)
			this.returnCode = 3;
		else
			this.returnCode--; // go previous (Starter)
	}
	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawReceipt(this, this.roundState, this.gameState);
		drawManager.drawReceiptMenu(this, this.returnCode, this.selectedLevel);

		super.drawPost();
		drawManager.completeDrawing(this);
	}
}
