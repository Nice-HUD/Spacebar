package engine;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import engine.achievement.AchievementManager;
import engine.achievement.Statistics;
import screen.ReceiptScreen;
import screen.*;
import io.sentry.Sentry;


/**
 * Implements core game logic.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public final class Core {
	
	/** Width of current screen. */
	private static int WIDTH = 630;
	/** Height of current screen. */
	private static int HEIGHT = 720;

	/** Max fps of current screen. */
	private static final int FPS = 60;
	
	/** Max lives. */
	public static final int MAX_LIVES = 3; // TEAM CLOVER: Fixed MAX_LIVES from private to public for usage in achievement
	/** Levels between extra life. */
	private static final int EXTRA_LIFE_FRECUENCY = 3;
	/** Total number of levels. */
	public static final int NUM_LEVELS = 7; // TEAM CLOVER : Fixed NUM_LEVELS from privated to public for usage in achievement
	
	/** Difficulty settings for level 1. */
	private static final GameSettings SETTINGS_LEVEL_1 =
			new GameSettings(5, 4, 60, 2000, 1);
	/** Difficulty settings for level 2. */
	private static final GameSettings SETTINGS_LEVEL_2 =
			new GameSettings(5, 5, 50, 2500, 1);
	/** Difficulty settings for level 3. */
	private static final GameSettings SETTINGS_LEVEL_3 =
			new GameSettings(1, 1, -8, 500, 1);
	/** Difficulty settings for level 4. */
	private static final GameSettings SETTINGS_LEVEL_4 =
			new GameSettings(6, 6, 30, 1500, 2);
	/** Difficulty settings for level 5. */
	private static final GameSettings SETTINGS_LEVEL_5 =
			new GameSettings(7, 6, 20, 1000, 2);
	/** Difficulty settings for level 6. */
	private static final GameSettings SETTINGS_LEVEL_6 =
			new GameSettings(7, 7, 10, 1000, 3);
	/** Difficulty settings for level 7. */
	private static final GameSettings SETTINGS_LEVEL_7 =
			new GameSettings(8, 7, 2, 500, 1);
	
	/** Frame to draw the screen on. */
	private static Frame frame;
	/** Screen currently shown. */
	private static Screen currentScreen;
	/** Difficulty settings list. */
	private static List<GameSettings> gameSettings;
	/** Application logger. */
	private static final Logger LOGGER = Logger.getLogger(Core.class
			.getSimpleName());
	/** Logger handler for printing to disk. */
	private static Handler fileHandler;
	/** Logger handler for printing to console. */
	private static ConsoleHandler consoleHandler;
	// Sound Operator
	private static SoundManager sm;
	private static AchievementManager achievementManager; // Team CLOVER

	private static final Properties properties = new Properties(); // application.properties에서 값 가져오기
	
	private static boolean TwoPlayerMode = false;
	
	public static boolean isTwoPlayerMode() {
		return TwoPlayerMode;
	}
	
	public static void setTwoPlayerMode(boolean twoPlayerMode) {
		Core.TwoPlayerMode = twoPlayerMode;
	}

	/**
	 * Test implementation.
	 *
	 * @param args
	 *            Program args, ignored.
	 */
	public static void main(final String[] args) {
		try {
			properties.load(new FileInputStream("application.properties")); //
			Sentry.init(options -> {
				String dsn = System.getenv("SENTRY_DSN");
				if (dsn == null) {
					// 환경변수가 없으면 properties에서 읽기
					dsn = properties.getProperty("sentry.dsn");
				}
				options.setDsn(dsn);
				options.setTracesSampleRate(1.0);  // 성능 모니터링을 위한 샘플링 비율 추가
				options.setDebug(true);
				options.setEnvironment("development"); // 개발 환경
			});
//			try {
//				throw new RuntimeException("Sentry 연동 테스트 에러");
//			} catch (Exception e) {
//				Sentry.captureException(e);
//				LOGGER.severe("Test Error: " + e.getMessage());
//			}
//			// Sentry 테스트를 위한 강제 에러
//			GameState nullState = null;
//			try {
//				// NullPointerException 발생
//				nullState.getLevel();
//			} catch (Exception e) {
//				Sentry.captureException(e);
//				LOGGER.severe("Game State Error: " + e.getMessage());
//			}
//			// ArrayIndexOutOfBoundsException 발생
//			try {
//				gameSettings.get(999);
//			} catch (Exception e) {
//				Sentry.captureException(e);
//				LOGGER.severe("Settings Error: " + e.getMessage());
//			}
			LOGGER.setUseParentHandlers(false);
			
			fileHandler = new FileHandler("log");
			fileHandler.setFormatter(new MinimalFormatter());
			
			consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(new MinimalFormatter());
			// Sound Operator
			sm = SoundManager.getInstance();
			sm.initializeSoundSettings();
			
			LOGGER.addHandler(fileHandler);
			LOGGER.addHandler(consoleHandler);
			LOGGER.setLevel(Level.ALL);
			
			// TEAM CLOVER : Added log to check if function is working
			System.out.println("Initializing AchievementManager...");
			achievementManager = new AchievementManager(DrawManager.getInstance());
			System.out.println("AchievementManager initialized!");
			
			// CtrlS: Make instance of Upgrade Manager
			Core.getUpgradeManager();
			
			//Clove. Reset Player Statistics After the Game Starts
			Statistics statistics = new Statistics();
			statistics.resetStatistics();
			LOGGER.info("Reset Player Statistics");
			
		} catch (Exception e) {
			// TODO handle exception
			e.printStackTrace();
		}
		
		frame = new Frame(WIDTH, HEIGHT);
		DrawManager.getInstance().setFrame(frame);
		int width = frame.getWidth();
		int height = frame.getHeight();

		/** ### TEAM INTERNATIONAL ###*/
		/** Initialize singleton instance of a background*/
		Background.getInstance().initialize(frame);
		
		gameSettings = new ArrayList<GameSettings>();
		gameSettings.add(SETTINGS_LEVEL_1);
		gameSettings.add(SETTINGS_LEVEL_2);
		gameSettings.add(SETTINGS_LEVEL_3);
		gameSettings.add(SETTINGS_LEVEL_4);
		gameSettings.add(SETTINGS_LEVEL_5);
		gameSettings.add(SETTINGS_LEVEL_6);
		gameSettings.add(SETTINGS_LEVEL_7);
		
		GameState gameState;
		RoundState roundState;

		int level = 1;
		int returnCode = 1;
		do {
			// level 선택 기능과 병합하며, gameState 수정 필요(pause 기능 중 restart 관련)
			// Add playtime parameter - Soomin Lee / TeamHUD
			// Add hitCount parameter - Ctrl S
			// Add coinItemsCollected parameter - Ctrl S
			gameState = new GameState(level, 0
					, MAX_LIVES, MAX_LIVES,0, 0, 0, 0, 0, 0, 0, false);
			loopOut:
			switch (returnCode) {
				case 1:
					// Main menu.
					currentScreen = new TitleScreen(width, height, FPS, gameState);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " title screen at " + FPS + " fps.");
					returnCode = frame.setScreen(currentScreen);
					level = gameState.getLevel();
					LOGGER.info("Closing title screen.");
					break;
				case 2:
					// Game & score.
					LOGGER.info("Starting inGameBGM");
					// Sound Operator
					sm.playES("start_button_ES");
					sm.playBGM("inGame_bgm");
					
					do {
						// One extra live every few levels.
						boolean bonusLife = gameState.getLevel()
								% EXTRA_LIFE_FRECUENCY == 0
								&& gameState.getLivesRemaining() < MAX_LIVES;
						gameState.setTwoPlayerMode(false);
						GameState prevState = gameState;
						currentScreen = new GameScreen(gameState,
								gameSettings.get(gameState.getLevel() - 1),
								bonusLife, width, height, FPS, gameState.isTwoPlayerMode());
						LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
								+ " game screen at " + FPS + " fps.");
						returnCode = frame.setScreen(currentScreen);
						LOGGER.info("Closing game screen.");

						if (returnCode == 1) {
							level = 1;
							break;
						}
						if (returnCode == 2) break;
						
						achievementManager.updateAchievements(currentScreen); // TEAM CLOVER : Achievement
						
						Statistics statistics = new Statistics(); //Clove
						
						gameState = ((GameScreen) currentScreen).getGameState();
						
						roundState = new RoundState(prevState, gameState);
						
						// Add playtime parameter - Soomin Lee / TeamHUD
						gameState = new GameState(gameState.getLevel() + 1,
								gameState.getScore(),
								gameState.getLivesRemaining(),
								gameState.getLivesTwoRemaining(),
								gameState.getBulletsShot(),
								gameState.getShipsDestroyed(),
								gameState.getTime(),
								gameState.getCoin() + roundState.getRoundCoin(),
								gameState.getGem(),
								gameState.getHitCount(),
								gameState.getCoinItemsCollected(),
								gameState.isTwoPlayerMode());
						LOGGER.info("Round Coin: " + roundState.getRoundCoin());
						LOGGER.info("Round Hit Rate: " + roundState.getRoundHitRate());
						LOGGER.info("Round Time: " + roundState.getRoundTime());
						
						try { //Clove
							statistics.addTotalPlayTime(roundState.getRoundTime());
							LOGGER.info("RoundTime Saving");
						} catch (IOException e){
							LOGGER.info("Failed to Save RoundTime");
						}
						
						// Show receiptScreen
						// If it is not the last round and the game is not over
						// Ctrl-S
						if (gameState.getLevel() <= 7 && gameState.getLivesRemaining() > 0) {
							LOGGER.info("loading receiptScreen");
							currentScreen = new ReceiptScreen(width, height, FPS, roundState, gameState);
							
							LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
									+ " receipt screen at " + FPS + " fps.");
							returnCode = frame.setScreen(currentScreen);
							if(returnCode == 1) break loopOut;
							LOGGER.info("Closing receiptScreen.");
						}
						
						if (achievementManager != null) { // TEAM CLOVER : Added code
							achievementManager.updateAchievements(currentScreen);
						}
						
					} while (gameState.getLivesRemaining() > 0
							&& gameState.getLevel() <= NUM_LEVELS);

					if (returnCode == 1 || returnCode == 2) break;

					LOGGER.info("Stop InGameBGM");
					// Sound Operator
					sm.stopAllBGM();
					
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " score screen at " + FPS + " fps, with a score of "
							+ gameState.getScore() + ", "
							+ gameState.getLivesRemaining() + " lives remaining, "
							+ gameState.getBulletsShot() + " bullets shot and "
							+ gameState.getShipsDestroyed() + " ships destroyed.");
					currentScreen = new ScoreScreen(width, height, FPS, gameState);
					returnCode = frame.setScreen(currentScreen);
					level = 1;
					LOGGER.info("Closing score screen.");
					break;
				case 3:
					// High scores.
					currentScreen = new HighScoreScreen(width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " high score screen at " + FPS + " fps.");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing high score screen.");
					break;
				case 4:
					LOGGER.info("Starting inGameBGM");
					// Sound Operator
					sm.playES("start_button_ES");
					sm.playBGM("inGame_bgm");
					
					do {
						if (gameSettings == null || gameSettings.isEmpty()) {
							gameSettings = new ArrayList<>();
							gameSettings.add(SETTINGS_LEVEL_1);
							gameSettings.add(SETTINGS_LEVEL_2);
							gameSettings.add(SETTINGS_LEVEL_3);
							gameSettings.add(SETTINGS_LEVEL_4);
							gameSettings.add(SETTINGS_LEVEL_5);
							gameSettings.add(SETTINGS_LEVEL_6);
							gameSettings.add(SETTINGS_LEVEL_7);
						}
						
						GameSettings currentGameSettings = gameSettings.get(gameState.getLevel() - 1);
						
						int fps = FPS;
						boolean bonusLife = gameState.getLevel() % EXTRA_LIFE_FRECUENCY == 0 &&
								(gameState.getLivesRemaining() < MAX_LIVES || gameState.getLivesTwoRemaining() < MAX_LIVES);

						gameState.setTwoPlayerMode(true); // 2인용 모드 설정
						GameState prevState = gameState; // 이전 상태 저장

						boolean isTwoPlayerMode = gameState.isTwoPlayerMode(); // 현재 모드 저장
						// 새로운 GameScreen 객체 생성
						System.out.println("GameState twoPlayerMode: " + gameState.isTwoPlayerMode());
						currentScreen = new GameScreen(gameState, currentGameSettings, bonusLife, width, height, fps, isTwoPlayerMode);
						currentScreen.setTwoPlayerMode(isTwoPlayerMode);

						Statistics statistics = new Statistics(); // 추가 로직



						LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
								+ " game screen at " + FPS + " fps.");
						returnCode = frame.setScreen(currentScreen);

						if (returnCode == 1 || returnCode == 2) break;

						LOGGER.info("Closing game screen.");
						
						
						achievementManager.updateAchievements(currentScreen); // TEAM CLOVER : Achievement
						
						gameState = ((GameScreen) currentScreen).getGameState();
						
						roundState = new RoundState(prevState, gameState);
						
						// Add playtime parameter - Soomin Lee / TeamHUD
						gameState = new GameState(gameState.getLevel() + 1,
								gameState.getScore(),
								gameState.getLivesRemaining(),
								gameState.getLivesTwoRemaining(),
								gameState.getBulletsShot(),
								gameState.getShipsDestroyed(),
								gameState.getTime(),
								gameState.getCoin() + roundState.getRoundCoin(),
								gameState.getGem(),
								gameState.getHitCount(),
								gameState.getCoinItemsCollected(),
								gameState.isTwoPlayerMode());
						LOGGER.info("Round Coin: " + roundState.getRoundCoin());
						LOGGER.info("Round Hit Rate: " + roundState.getRoundHitRate());
						LOGGER.info("Round Time: " + roundState.getRoundTime());
						
						try { //Clove
							statistics.addTotalPlayTime(roundState.getRoundTime());
							LOGGER.info("RoundTime Saving");
						} catch (IOException e){
							LOGGER.info("Failed to Save RoundTime");
						}
						
						// Show receiptScreen
						// If it is not the last round and the game is not over
						// Ctrl-S
						if (gameState.getLevel() <= 7 && gameState.getLivesRemaining() > 0) {
							LOGGER.info("loading receiptScreen");
							currentScreen = new ReceiptScreen(width, height, FPS, roundState, gameState);
							
							LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
									+ " receipt screen at " + FPS + " fps.");
							returnCode = frame.setScreen(currentScreen);
							if(returnCode == 1) break loopOut;
							LOGGER.info("Closing receiptScreen.");
						}
						
						if (achievementManager != null) { // TEAM CLOVER : Added code
							achievementManager.updateAchievements(currentScreen);
						}
						
					} while ((gameState.getLivesRemaining() > 0 || gameState.getLivesTwoRemaining() > 0) && gameState.getLevel() <= NUM_LEVELS);

					if (returnCode == 1 || returnCode == 2) break;

					LOGGER.info("Stop InGameBGM");
					// Sound Operator
					sm.stopAllBGM();
					
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " score screen at " + FPS + " fps, with a score of "
							+ gameState.getScore() + ", "
							+ gameState.getLivesRemaining() + " lives remaining, "
							+ gameState.getBulletsShot() + " bullets shot and "
							+ gameState.getShipsDestroyed() + " ships destroyed.");
					currentScreen = new ScoreScreen(width, height, FPS, gameState);
					returnCode = frame.setScreen(currentScreen);
					level = 1;
					LOGGER.info("Closing score screen.");
					break;
				case 5: // 7 -> 5 replaced by Starter
					// Recent Records.
					currentScreen = new RecordScreen(width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " recent record screen at " + FPS + " fps.");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing recent record screen.");
					break;
				case 6: // Settings 화면
					currentScreen = new SettingsScreen(width, height, FPS,frame);
					LOGGER.info("Starting Settings screen.");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing Settings screen.");
					break;
				case 7: // 스킨 선택 화면
					currentScreen = new SkinSelectionScreen(width, height, FPS);
					LOGGER.info("스킨 선택 화면 시작");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("선택 화면 종료");
					break;
				default:
					break;
			}
			
		} while (returnCode != 0);
		
		fileHandler.flush();
		fileHandler.close();
		System.exit(0);
	}
	
	/**
	 * Constructor, not called.
	 */
	private Core() {
	
	}
	
	/**
	 * Controls access to the logger.
	 *
	 * @return Application logger.
	 */
	public static Logger getLogger() {
		return LOGGER;
	}
	
	/**
	 * Controls access to the drawing manager.
	 *
	 * @return Application draw manager.
	 */
	public static DrawManager getDrawManager() {
		return DrawManager.getInstance();
	}
	
	/**
	 * Controls access to the input manager.
	 *
	 * @return Application input manager.
	 */
	public static InputManager getInputManager() {
		return InputManager.getInstance();
	}
	
	/**
	 * Controls access to the file manager.
	 *
	 * @return Application file manager.
	 */
	public static FileManager getFileManager() {
		return FileManager.getInstance();
	}
	
	/**
	 * Controls creation of new cooldowns.
	 *
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @return A new cooldown.
	 */
	public static Cooldown getCooldown(final int milliseconds) {
		return new Cooldown(milliseconds);
	}
	
	/**
	 * Controls creation of new cooldowns with variance.
	 *
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @param variance
	 *            Variation in the cooldown duration.
	 * @return A new cooldown with variance.
	 */
	public static Cooldown getVariableCooldown(final int milliseconds,
											   final int variance) {
		return new Cooldown(milliseconds, variance);
	}
	
	/**
	 * Controls access to the currency manager.
	 *
	 * @return Application currency manager.
	 */
	// Team-Ctrl-S(Currency)
	public static CurrencyManager getCurrencyManager() {
		return CurrencyManager.getInstance();
	}
	
	/**
	 * Controls access to the currency manager.
	 *
	 * @return Application currency manager.
	 */
	// Team-Ctrl-S(Currency)
	public static UpgradeManager getUpgradeManager() {
		return UpgradeManager.getInstance();
	}

  public static int getWidth(){
		return WIDTH;
	}

	public static int getHeight(){
		return HEIGHT;
	}


	public static void setWidth(int width){
		WIDTH = width;
	}

	public static void setHeight(int height){
		HEIGHT = height;
	}
}