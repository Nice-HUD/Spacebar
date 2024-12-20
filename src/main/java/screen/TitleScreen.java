package screen;

import engine.*;
import entity.ShipStatus;

import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Implements the title screen.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class TitleScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;

	/** Time between changes in user selection. */
	private Cooldown selectionCooldown;

	// CtrlS
	private int coin;
	private int gem;

	// select One player or Two player
	private int pnumSelectionCode; //produced by Starter
	private int merchantState;
	//inventory
	private ShipStatus shipStatus;
	private int selectedLevel = 1;
	private GameState gameState;


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
	public TitleScreen(final int width, final int height, final int fps, final GameState gameState) {
		super(width, height, fps);

		this.gameState = gameState;
		// Defaults to play.
		this.merchantState = 0;
		this.pnumSelectionCode = 0;
		this.returnCode = 2;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();


		// CtrlS: Set user's coin, gem
        try {
            this.coin = Core.getCurrencyManager().getCoin();
			this.gem = Core.getCurrencyManager().getGem();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Sound Operator
		SoundManager.getInstance().playBGM("mainMenu_bgm");

		// inventory load upgrade price
		shipStatus = new ShipStatus();
		shipStatus.loadPrice();
	}

	/**
	 * Starts the action.
	 *
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		// produced by Starter
		if (this.pnumSelectionCode == 0 && this.returnCode == 7) {
			Core.setTwoPlayerMode(false);
			return 7; // 스킨 설정 화면으로 넘어가는 returnCode
		} else if (this.pnumSelectionCode == 1 && this.returnCode == 7) {
			Core.setTwoPlayerMode(true);
			return 7;
		}

		if (this.returnCode == 6) {
			return 6; // Return 6 for SettingsScreen
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
			if ((inputManager.isKeyDown(KeyEvent.VK_UP)
					|| inputManager.isKeyDown(KeyEvent.VK_W)) && returnCode != 7) {
				previousMenuItem();
				this.selectionCooldown.reset();
				// Sound Operator
				SoundManager.getInstance().playES("menuSelect_es");
			}
			if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
					|| inputManager.isKeyDown(KeyEvent.VK_S)) {
				nextMenuItem();
				this.selectionCooldown.reset();
				// Sound Operator
				SoundManager.getInstance().playES("menuSelect_es");
			}

			// produced by Starter
			if (returnCode == 2) {
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
						|| inputManager.isKeyDown(KeyEvent.VK_A)) {
					moveMenuLeft();
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
						|| inputManager.isKeyDown(KeyEvent.VK_D)) {
					moveMenuRight();
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}
			}

			if(returnCode == 4) {
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
						|| inputManager.isKeyDown(KeyEvent.VK_A)) {
					nextMerchantState();
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
						|| inputManager.isKeyDown(KeyEvent.VK_D)) {
					previousMerchantState();
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}

			}

			if (returnCode == 7) {
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
						|| inputManager.isKeyDown(KeyEvent.VK_A)) {
					moveLevelLeft();
					gameState.setLevel(selectedLevel);
					this.selectionCooldown.reset();
					SoundManager.getInstance().playES("menuSelect_es");
				}
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
						|| inputManager.isKeyDown(KeyEvent.VK_D)) {
					moveLevelRight();
					gameState.setLevel(selectedLevel);
					this.selectionCooldown.reset();
					SoundManager.getInstance().playES("menuSelect_es");
				}
			}

			if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
				if (returnCode == 2) {
					returnCode = 7;
					this.selectionCooldown.reset();
					SoundManager.getInstance().playES("menuSelect_es");
				} else if (returnCode == 4) {
					testStatUpgrade();
                    this.selectionCooldown.reset();
				}
				else this.isRunning = false;
		}
	}
	// Use later if needed. -Starter
	// public int getPnumSelectionCode() {return this.pnumSelectionCode;}

	/**
	 * runs when player do buying things
	 * when store system is ready -- unwrap annotated code and rename this method
	 */
//	private void testCoinDiscounter(){
//		if(this.currentCoin > 0){
//			this.currentCoin -= 50;
//		}

//		try{
//			Core.getFileManager().saveCurrentCoin();
//		} catch (IOException e) {
//			logger.warning("Couldn't save current coin!");
//		}
//	}

	/**
	 * Shifts the focus to the next menu item.
	 */
	
	private void testStatUpgrade() {
		// CtrlS: testStatUpgrade should only be called after coins are spent
		if (this.merchantState == 1) { // bulletCount
			try {
				if (Core.getUpgradeManager().LevelCalculation(Core.getUpgradeManager().getBulletCount()) > 3){
					Core.getLogger().info("The level is already Max!");
				}

				else if (!(Core.getUpgradeManager().getBulletCount() % 2 == 0)
						&& Core.getCurrencyManager().spendCoin(Core.getUpgradeManager().Price(1))) {

					Core.getUpgradeManager().addBulletNum();
					Core.getLogger().info("Bullet Number: " + Core.getUpgradeManager().getBulletNum());

					Core.getUpgradeManager().addBulletCount();

				} else if ((Core.getUpgradeManager().getBulletCount() % 2 == 0)
						&& Core.getCurrencyManager().spendGem((Core.getUpgradeManager().getBulletCount() + 1) * 10)) {

					Core.getUpgradeManager().addBulletCount();
					Core.getLogger().info("Upgrade has been unlocked");

				} else {
					Core.getLogger().info("you don't have enough");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		} else if (this.merchantState == 2) { // shipSpeed
			try {
				if (Core.getUpgradeManager().LevelCalculation(Core.getUpgradeManager().getSpeedCount()) > 10){
					Core.getLogger().info("The level is already Max!");
				}

				else if (!(Core.getUpgradeManager().getSpeedCount() % 4 == 0)
						&& Core.getCurrencyManager().spendCoin(Core.getUpgradeManager().Price(2))) {

					Core.getUpgradeManager().addMovementSpeed();
					Core.getLogger().info("Movement Speed: " + Core.getUpgradeManager().getMovementSpeed());

					Core.getUpgradeManager().addSpeedCount();

				} else if ((Core.getUpgradeManager().getSpeedCount() % 4 == 0)
						&& Core.getCurrencyManager().spendGem(Core.getUpgradeManager().getSpeedCount() / 4 * 5)) {

					Core.getUpgradeManager().addSpeedCount();
					Core.getLogger().info("Upgrade has been unlocked");

				} else {
					Core.getLogger().info("you don't have enough");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		} else if (this.merchantState == 3) { // attackSpeed
			try {
				if (Core.getUpgradeManager().LevelCalculation(Core.getUpgradeManager().getAttackCount()) > 10){
					Core.getLogger().info("The level is already Max!");
				}

				else if (!(Core.getUpgradeManager().getAttackCount() % 4 == 0)
						&& Core.getCurrencyManager().spendCoin(Core.getUpgradeManager().Price(3))) {

					Core.getUpgradeManager().addAttackSpeed();
					Core.getLogger().info("Attack Speed: " + Core.getUpgradeManager().getAttackSpeed());

					Core.getUpgradeManager().addAttackCount();

				} else if ((Core.getUpgradeManager().getAttackCount() % 4 == 0)
						&& Core.getCurrencyManager().spendGem(Core.getUpgradeManager().getAttackCount() / 4 * 5)) {

					Core.getUpgradeManager().addAttackCount();
					Core.getLogger().info("Upgrade has been unlocked");

				} else {
					Core.getLogger().info("you don't have enough");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		} else if (this.merchantState == 4) { // coinGain
			try {
				if (Core.getUpgradeManager().LevelCalculation(Core.getUpgradeManager().getCoinCount()) > 10){
					Core.getLogger().info("The level is already Max!");
				}

				else if (!(Core.getUpgradeManager().getCoinCount() % 4 == 0)
						&& Core.getCurrencyManager().spendCoin(Core.getUpgradeManager().Price(4))) {

					Core.getUpgradeManager().addCoinAcquisitionMultiplier();
					Core.getLogger().info("CoinBonus: " + Core.getUpgradeManager().getCoinAcquisitionMultiplier());

					Core.getUpgradeManager().addCoinCount();

				} else if ((Core.getUpgradeManager().getCoinCount() % 4 == 0)
						&& Core.getCurrencyManager().spendGem(Core.getUpgradeManager().getCoinCount() / 4 * 5)) {

					Core.getUpgradeManager().addCoinCount();
					Core.getLogger().info("Upgrade has been unlocked");

				} else {
					Core.getLogger().info("you don't have enough");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
		try{
			this.coin = Core.getCurrencyManager().getCoin();
			this.gem = Core.getCurrencyManager().getGem();

		} catch (IOException e){
			throw new RuntimeException(e);
		}
	}

	private void nextMenuItem() {
		// 순환: Exit -> 1/2 Player Mode
		if (this.returnCode == 0)
			this.returnCode = 2; // Exit에서 1/2 Player Mode로 이동
		else if (this.returnCode == 6)
			this.returnCode = 0; // Settings에서 Exit로 이동
		else if (this.returnCode == 7)
			this.returnCode = 2;
		else
			this.returnCode++; // 다음 항목으로 이동
	}

	/**
	 * Shifts the focus to the previous menu item.
	 */
	private void previousMenuItem() {
		// 순환: 1/2 Player Mode -> Exit
		if (this.returnCode == 2)
			this.returnCode = 0; // 1/2 Player Mode에서 Exit로 이동
		else if (this.returnCode == 0)
			this.returnCode = 6; // Exit에서 Settings로 이동
		else
			this.returnCode--; // 이전 항목으로 이동
	}

	private void moveLevelLeft() {
		if (selectedLevel == 1)
			selectedLevel = 7;
		else
			selectedLevel--;
	}

	private void moveLevelRight() {
		if (selectedLevel == 7)
			selectedLevel = 1;
		else
			selectedLevel++;
	}

	// left and right move -- produced by Starter
	private void moveMenuLeft() {
		if (this.returnCode == 2 ) {
			if (this.pnumSelectionCode == 0)
				this.pnumSelectionCode++;
			else
				this.pnumSelectionCode--;
		}

	}

	private void moveMenuRight() {
		if (this.returnCode == 2) {
			if (this.pnumSelectionCode == 0)
				this.pnumSelectionCode++;
			else
				this.pnumSelectionCode--;
		}
	}

	private void nextMerchantState() {
		if (this.returnCode == 4) {
			if (this.merchantState == 4)
				this.merchantState = 0;
			else
				this.merchantState++;
		}
	}

	private void previousMerchantState() {
		if (this.returnCode == 4) {
			if (this.merchantState == 0)
				this.merchantState = 4;
			else
				this.merchantState--;
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawTitle(this);
		drawManager.drawMenu(this, this.returnCode, this.pnumSelectionCode, this.merchantState);
		if (returnCode == 7) {
			drawManager.drawLevelMenu(this, selectedLevel);
		}
		// CtrlS
		drawManager.drawCurrentCoin(this, coin);
		drawManager.drawCurrentGem(this, gem);

		super.drawPost();
		drawManager.completeDrawing(this);
	}

	public int getReturnCode(){return returnCode;}
	public void setReturnCode(int returnCode){this.returnCode = returnCode;}

	public int getSelectedLevel(){return selectedLevel;}
	public void setSelectedLevel(int selectedLevel){this.selectedLevel = selectedLevel;}

	public void setInputManager(InputManager inputManager){this.inputManager = inputManager;}
	public void setDrawManager(DrawManager drawManager){this.drawManager = drawManager;}
	public void setInputDelay(Cooldown inputDelay){this.inputDelay = inputDelay;}

}
