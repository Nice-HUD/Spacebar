package screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import engine.*;
import engine.achievement.AchievementConditions;
import engine.achievement.ScoreManager;
import engine.achievement.Statistics;
import entity.*;
// shield and heart recovery
// Sound Operator
import engine.SoundManager;

import java.awt.event.KeyEvent;


/**
 * Implements the game screen, where the action happens.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class GameScreen extends Screen {

    private boolean isPaused = false;
    private long pausedTime = 0;

    /**
     * Milliseconds until the screen accepts user input.
     */
    private static final int INPUT_DELAY = 6000;
    /**
     * Bonus score for each life remaining at the end of the level.
     */
    private static final int LIFE_SCORE = 100;
    /**
     * Minimum time between bonus ship's appearances.
     */
    private static final int BONUS_SHIP_INTERVAL = 20000;
    /**
     * Maximum variance in the time between bonus ship's appearances.
     */
    private static final int BONUS_SHIP_VARIANCE = 10000;
    /**
     * Time until bonus ship explosion disappears.
     */
    private static final int BONUS_SHIP_EXPLOSION = 500;
    /**
     * Time from finishing the level to screen change.
     */
    private static final int SCREEN_CHANGE_INTERVAL = 1500;
    /**
     * Height of the interface separation line.
     */
    private static final int SEPARATION_LINE_HEIGHT = 40;

    /**
     * Current game difficulty settings.
     */
    private GameSettings gameSettings;
    /**
     * Current difficulty level number.
     */
    private int level;
    /**
     * Formation of enemy ships.
     */
    EnemyShipFormation enemyShipFormation;
    /**
     * Player's ship.
     */
    private Ship ship;
    // 오른쪽 보조 비행기 생성
    private SubShip rightSubShip;
    // 왼쪽 보조 비행기 생성
    private SubShip leftSubShip;

    public Ship player2;
    /**
     * Bonus enemy ship that appears sometimes.
     */
    private EnemyShip enemyShipSpecial;
    /**
     * Minimum time between bonus ship appearances.
     */
    private Cooldown enemyShipSpecialCooldown;
    /**
     * Time until bonus ship explosion disappears.
     */
    private Cooldown enemyShipSpecialExplosionCooldown;
    /**
     * Time from finishing the level to screen change.
     */
    private Cooldown screenFinishedCooldown;
    /**
     * Set of all bullets fired by on screen ships.
     */
    public Set<PiercingBullet> bullets; //by Enemy team
    /**
     * Add an itemManager Instance
     */
    public static ItemManager itemManager; //by Enemy team
    /**
     * Shield item
     */
    private ItemBarrierAndHeart item;    // team Inventory
    private FeverTimeItem feverTimeItem;
    /**
     * Speed item
     */
    private SpeedItem speedItem;
    /**
     * Current score.
     */
    private int score;
    /**
     * Player lives left.
     */
    private int lives;
    /**
     * player2 lives left.
     */
    private int livestwo;
    /**
     * Total bullets shot by the player.
     */
    private int bulletsShot;
    /**
     * Total ships destroyed by the player.
     */
    private int shipsDestroyed;
    /**
     * Moment the game starts.
     */
    private long gameStartTime;
    /**
     * Checks if the level is finished.
     */
    private boolean levelFinished;
    /**
     * Checks if a bonus life is received.
     */
    private boolean bonusLife;
    /**
     * Added by the Level Design team
     * <p>
     * Counts the number of waves destroyed
     **/
    private int waveCounter;

    /** ### TEAM INTERNATIONAL ### */
    /**
     * Booleans for horizontal background movement
     */
    private boolean backgroundMoveLeft = false;
    private boolean backgroundMoveRight = false;


    // --- OBSTACLES
    public Set<Obstacle> obstacles; // Store obstacles
    private Cooldown obstacleSpawnCooldown; //control obstacle spawn speed
    /** Shield item */


    // Soomin Lee / TeamHUD
    /**
     * Moment the user starts to play
     */
    private long playStartTime;
    /**
     * Total time to play
     */
    private int playTime = 0;
    /**
     * Play time on previous levels
     */
    private int playTimePre = 0;

    // Sound Operator
    private static SoundManager sm;

    // Team-Ctrl-S(Currency)
    /**
     * Total coin
     **/
    private int coin;
    /**
     * Total gem
     **/
    private int gem;
    /**
     * Total hitCount
     **/        //CtrlS
    private int hitCount;
    /**
     * Unique id for shot of bullets
     **/ //CtrlS
    private int fire_id;
    /**
     * Set of fire_id
     **/
    private Set<Integer> processedFireBullet;

    /**
     * Score calculation.
     */
    private ScoreManager scoreManager;    //clove
    /**
     * Check start-time
     */
    private long startTime;    //clove
    /**
     * Check end-time
     */
    private long endTime;    //clove

    private Statistics statistics; //Team Clove
    private AchievementConditions achievementConditions;
    private int fastKill;

    /**
     * CtrlS: Count the number of coin collected in game
     */
    private int coinItemsCollected;

    private boolean twoPlayerMode;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param gameState    Current game state.
     * @param gameSettings Current game settings.
     * @param bonusLife    Checks if a bonus life is awarded this level.
     * @param width        Screen width.
     * @param height       Screen height.
     * @param fps          Frames per second, frame rate at which the game is run.
     */
    public GameScreen(final GameState gameState,
                      final GameSettings gameSettings, final boolean bonusLife,
                      final int width, final int height, final int fps, boolean twoPlayerMode) {
        super(width, height, fps);

        this.gameSettings = gameSettings;
        this.bonusLife = bonusLife;
        this.level = gameState.getLevel();
        this.score = gameState.getScore();
        this.lives = gameState.getLivesRemaining();
        if (this.bonusLife)
            this.lives++;
        this.livestwo = gameState.getLivesTwoRemaining();
        if (this.bonusLife)
            this.livestwo++;
        this.bulletsShot = gameState.getBulletsShot();
        this.shipsDestroyed = gameState.getShipsDestroyed();
        this.item = new ItemBarrierAndHeart();   // team Inventory
        this.feverTimeItem = new FeverTimeItem(); // team Inventory
        this.speedItem = new SpeedItem();   // team Inventory
        this.coin = gameState.getCoin(); // Team-Ctrl-S(Currency)
        this.gem = gameState.getGem(); // Team-Ctrl-S(Currency)
        this.hitCount = gameState.getHitCount(); //CtrlS
        this.fire_id = 0; //CtrlS - fire_id means the id of bullet that shoot already. It starts from 0.
        this.processedFireBullet = new HashSet<>(); //CtrlS - initialized the processedFireBullet
        this.twoPlayerMode = twoPlayerMode;

        /**
         * Added by the Level Design team
         *
         * Sets the wave counter
         * **/
        this.waveCounter = 1;

        // Soomin Lee / TeamHUD
        this.playTime = gameState.getTime();
        this.scoreManager = gameState.scoreManager; //Team Clove
        this.statistics = new Statistics(); //Team Clove
        this.achievementConditions = new AchievementConditions();
        this.coinItemsCollected = gameState.getCoinItemsCollected(); // CtrlS
    }

    /**
     * Initializes basic screen properties, and adds necessary elements.
     */
    public void initialize() {
        super.initialize();
        /** initialize background **/
        drawManager.loadBackground(this.level);

        enemyShipFormation = new EnemyShipFormation(this.gameSettings);
        enemyShipFormation.setScoreManager(this.scoreManager);//add by team Enemy
        enemyShipFormation.attach(this);
        this.ship = new Ship(this.width / 2, this.height - 30, Color.RED); // add by team HUD
        this.rightSubShip = new SubShip(this.width / 2 + 25, this.height - 90, Color.RED);//보조 비행기 생성
        this.leftSubShip = new SubShip(this.width / 2 - 10, this.height - 90, Color.RED);//보조 비행기 생성

        /** initialize itemManager */
        this.itemManager = new ItemManager(this.height, drawManager, this); //by Enemy team
        this.itemManager.initialize(); //by Enemy team
        enemyShipFormation.setItemManager(this.itemManager);//add by team Enemy

        if (twoPlayerMode) {
            this.setTwoPlayerMode(true);
            this.player2 = new Ship(this.width / 4, this.height - 30, Color.BLUE); // add by team HUD
        } else {
            this.player2 = null;
        }

        Set<EnemyShip> enemyShipSet = new HashSet<>();
        for (EnemyShip enemyShip : this.enemyShipFormation) {
            enemyShipSet.add(enemyShip);
        }
        this.itemManager.setEnemyShips(enemyShipSet);

        // Appears each 10-30 seconds.
        this.enemyShipSpecialCooldown = Core.getVariableCooldown(
                BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
        this.enemyShipSpecialCooldown.reset();
        this.enemyShipSpecialExplosionCooldown = Core
                .getCooldown(BONUS_SHIP_EXPLOSION);
        this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
        this.bullets = new HashSet<PiercingBullet>(); // Edited by Enemy

        this.startTime = System.currentTimeMillis();    //clove

        // Special input delay / countdown.
        this.gameStartTime = System.currentTimeMillis();
        this.inputDelay = Core.getCooldown(INPUT_DELAY);
        this.inputDelay.reset();

        // Soomin Lee / TeamHUD
        this.playStartTime = gameStartTime + INPUT_DELAY;
        this.playTimePre = playTime;


        // 	// --- OBSTACLES - Initialize obstacles
        this.obstacles = new HashSet<>();
        this.obstacleSpawnCooldown = Core.getCooldown(Math.max(2000 - (level * 200), 500)); // Minimum 0.5s
    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
		super.run();

        this.score += LIFE_SCORE * (this.lives - 1);
        this.logger.info("Screen cleared with a score of " + this.scoreManager.getAccumulatedScore());

        return this.returnCode;
    }

    private void handleGameOver() {
        this.levelFinished = true; // 레벨 종료
        this.isRunning = false; // 게임 루프 종료
        this.logger.info("Game Over. Returning to main menu.");
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected void update() {
        super.update();

        if (this.inputDelay.checkFinished() && !this.levelFinished) {
            // --- OBSTACLES
            if (this.obstacleSpawnCooldown.checkFinished()) {
                // Adjust spawn amount based on the level
                int spawnAmount = Math.min(level, 3); // Spawn up to 3 obstacles at higher levels
                for (int i = 0; i < spawnAmount; i++) {
                    int randomX = new Random().nextInt(this.width - 30);
                    obstacles.add(new Obstacle(randomX, 50)); // Start each at the top of the screen
                }
                this.obstacleSpawnCooldown.reset();
            }

            // --- OBSTACLES
            Set<Obstacle> obstaclesToRemove = new HashSet<>();
            for (Obstacle obstacle : this.obstacles) {
                obstacle.update(this.level); // Make obstacles move or perform actions
                if (obstacle.shouldBeRemoved()) {
                    obstaclesToRemove.add(obstacle);  // Mark obstacle for removal after explosion
                }
            }
            this.obstacles.removeAll(obstaclesToRemove);

            if (!this.ship.isDestroyed() && this.ship.isActive() ) {

                boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                        || inputManager.isKeyDown(KeyEvent.VK_D);
                boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT)
                        || inputManager.isKeyDown(KeyEvent.VK_A);

                boolean isRightBorder = this.ship.getPositionX()
                        + this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
                boolean isLeftBorder = this.ship.getPositionX()
                        - this.ship.getSpeed() < 1;

                if (moveRight && !isRightBorder) {
                    this.ship.moveRight();
                    this.rightSubShip.moveRight();
                    this.leftSubShip.moveRight();
                    this.backgroundMoveRight = true;
                }
                if (moveLeft && !isLeftBorder) {
                    this.ship.moveLeft();
                    this.rightSubShip.moveLeft();
                    this.leftSubShip.moveLeft();
                    this.backgroundMoveLeft = true;
                }
                if (inputManager.isKeyDown(KeyEvent.VK_ENTER)) {
                    if (this.ship.shoot(this.bullets)) {
                        this.bulletsShot++;
                        this.fire_id++;
                        this.logger.info("Bullet's fire_id is " + fire_id);
                    }
                    if (this.rightSubShip.shoot(this.bullets)) {
                        this.bulletsShot++;
                    }
                    if (this.leftSubShip.shoot(this.bullets)) {
                        this.bulletsShot++;
                    }
                }
            } else if (this.lives <= 0 && this.ship.isActive()){
                this.ship.setActive(false);
            }

            if (this.enemyShipSpecial != null) {
                if (!this.enemyShipSpecial.isDestroyed())
                    this.enemyShipSpecial.move(2, 0);
                else if (this.enemyShipSpecialExplosionCooldown.checkFinished())
                    this.enemyShipSpecial = null;

            }
            if (this.enemyShipSpecial == null
                    && this.enemyShipSpecialCooldown.checkFinished()) {
                this.enemyShipSpecial = new EnemyShip();
                this.enemyShipSpecialCooldown.reset();
                //Sound Operator
                sm = SoundManager.getInstance();
                sm.playES("UFO_come_up");
                this.logger.info("A special ship appears");
            }
            if (this.enemyShipSpecial != null
                    && this.enemyShipSpecial.getPositionX() > this.width) {
                this.enemyShipSpecial = null;
                this.logger.info("The special ship has escaped");
            }

            this.item.updateBarrierAndShip(this.ship);   // team Inventory
            this.speedItem.update();         // team Inventory
            this.rightSubShip.update();
            this.leftSubShip.update();
            this.feverTimeItem.update();
            this.enemyShipFormation.update();
            this.enemyShipFormation.shoot(this.bullets);

            if (player2 != null) {
                // Player 2 movement and shooting
                boolean moveRight2 = inputManager.isKeyDown(KeyEvent.VK_C);
                boolean moveLeft2 = inputManager.isKeyDown(KeyEvent.VK_Z);

                if (moveRight2 && player2.getPositionX() + player2.getWidth() < width) {
                    player2.moveRight();
                }
                if (moveLeft2 && player2.getPositionX() > 0) {
                    player2.moveLeft();
                }
                if (inputManager.isKeyDown(KeyEvent.VK_X)) {
                    player2.shoot(bullets);
                }

                // Player 2 bullet collision handling
                handleBulletCollisionsForPlayer2(this.bullets, player2);

                // 장애물과 아이템 상호작용 추가
                handleObstacleCollisionsForPlayer2(this.obstacles, player2);
                handleItemCollisionsForPlayer2(player2);
            }
        }
        //manageCollisions();
        manageCollisions_add_item(); //by Enemy team
        cleanBullets();
        cleanObstacles();
        this.itemManager.cleanItems(); //by Enemy team

        if (inputManager.isKeyDown(KeyEvent.VK_P) || inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
            pauseGame();
        }

        /**
         * Added by the Level Design team and edit by team Enemy
         * Changed the conditions for the game to end  by team Enemy
         *
         * Counts and checks if the number of waves destroyed match the intended number of waves for this level
         * Spawn another wave
         **/
        if (getRemainingEnemies() == 0 && waveCounter < this.gameSettings.getWavesNumber()) {

            waveCounter++;
            this.initialize();

        }

    /**
     * Checks if any enemy ship has reached the bottom line of the screen.
     *
     * If any enemy ship's position overlaps or exceeds the screen's bottom line,
     * the level is marked as finished, and a log message is recorded to indicate
     * that the game is over.
     */
    if (this.enemyShipFormation.hasEnemyReachedBottom(this.height - 65)) {
        this.lives = 0; // 목숨을 0으로 설정하여 게임 오버 조건 충족
        this.logger.info("Enemies have reached the bottom. Game Over!"); // 로그 기록

        handleGameOver(); // 게임 오버 처리 메서드 호출
        return; // 이후 로직을 실행하지 않도록 종료
    }


    if (areAllPlayersDestroyed()) {
        handleGameOver(); // 모든 플레이어가 파괴된 경우 게임 오버 처리
        return; // 이후 로직 실행 방지
    }


        /**
         * Wave counter condition added by the Level Design team*
         * Changed the conditions for the game to end  by team Enemy
         *
         * Checks if the intended number of waves for this level was destroyed
         * **/
        if ((getRemainingEnemies() == 0
                && !this.levelFinished
                && waveCounter == this.gameSettings.getWavesNumber())) {
            this.levelFinished = true;
            this.logger.info("All enemies have been destroyed. Level complete!");
        } else if (areAllPlayersDestroyed()) {
            this.levelFinished = true;
            this.logger.info("All players have been destroyed. Game Over!");
        }

        if (this.levelFinished && this.screenFinishedCooldown.checkFinished()) {
            //this.logger.info("Final Playtime: " + playTime + " seconds");    //clove
            achievementConditions.checkNoDeathAchievements(lives);
            achievementConditions.score(score);
            try { //Team Clove
                statistics.comHighestLevel(level);
                statistics.addBulletShot(bulletsShot);
                statistics.addShipsDestroyed(shipsDestroyed);

                achievementConditions.onKill();
                achievementConditions.onStage();
                achievementConditions.trials();
                achievementConditions.killStreak();
                achievementConditions.fastKill(fastKill);
                achievementConditions.score(score);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.isRunning = false;
        }

        draw();

    }

    /**
     * Draws the elements associated with the screen.
     */
    public void draw() {
        drawManager.initDrawing(this);

        /** ### TEAM INTERNATIONAL ### */
        drawManager.drawBackground(backgroundMoveRight, backgroundMoveLeft);
        this.backgroundMoveRight = false;
        this.backgroundMoveLeft = false;

        drawManager.drawRect(0, 0, this.width, SEPARATION_LINE_HEIGHT, Color.BLACK);
        drawManager.drawRect(0, this.height - 70, this.width, 70, Color.BLACK); // by Saeum Jung - TeamHUD

        if (this.ship != null && this.ship.isActive()){
            drawManager.drawEntity(this.ship, this.ship.getPositionX(),
                    this.ship.getPositionY());
            drawManager.drawEntity(this.rightSubShip, this.rightSubShip.getPositionX(), this.rightSubShip.getPositionY());
            drawManager.drawEntity(this.leftSubShip, this.leftSubShip.getPositionX(), this.leftSubShip.getPositionY());
        }

        if (player2 != null) {
            drawManager.drawEntity(player2, player2.getPositionX(), player2.getPositionY());
        }
        if (this.enemyShipSpecial != null)
            drawManager.drawEntity(this.enemyShipSpecial,
                    this.enemyShipSpecial.getPositionX(),
                    this.enemyShipSpecial.getPositionY());

        enemyShipFormation.draw();

        drawManager.drawSpeed(this, ship.getSpeed()); // Ko jesung / HUD team
        drawManager.drawSeparatorLine(this, this.height - 65); // Ko jesung / HUD team


        for (PiercingBullet bullet : this.bullets)
            drawManager.drawEntity(bullet, bullet.getPositionX(),
                    bullet.getPositionY());

        this.itemManager.drawItems(); //by Enemy team

        // --- OBSTACLES - Draw Obstaacles
        if (!this.levelFinished) {
            for (Obstacle obstacle : this.obstacles) {
                drawManager.drawEntity(obstacle, obstacle.getPositionX(), obstacle.getPositionY());
            }
        }


        // Interface.
//		drawManager.drawScore(this, this.scoreManager.getAccumulatedScore());    //clove -> edit by jesung ko - TeamHUD(to udjust score)
//		drawManager.drawScore(this, this.score); // by jesung ko - TeamHUD
        drawManager.drawScore2(this, this.score); // by jesung ko - TeamHUD
        drawManager.drawLives(this, this.lives);
        drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);
        drawManager.drawRemainingEnemies(this, getRemainingEnemies()); // by HUD team SeungYun
        drawManager.drawLevel(this, this.level);
        drawManager.drawBulletSpeed(this, ship.getBulletSpeed());
        // Call the method in drawManager - Lee Hyun Woo TeamHud
        drawManager.drawTime(this, this.playTime);
        // Call the method in drawManager - Soomin Lee / TeamHUD
        drawManager.drawItem(this); // HUD team - Jo Minseo

        if (player2 != null) {
            drawManager.drawBulletSpeed2P(this, player2.getBulletSpeed());
            drawManager.drawSpeed2P(this, player2.getSpeed());
            drawManager.drawLives2P(this, livestwo);
            if (livestwo == 0) {
                player2 = null;
            }
        } // by HUD team HyunWoo

        // Countdown to game start.
        if (!this.inputDelay.checkFinished()) {
            int countdown = (int) ((INPUT_DELAY
                    - (System.currentTimeMillis()
                    - this.gameStartTime)) / 1000);

            /**
             * Wave counter condition added by the Level Design team
             *
             * Display the wave number instead of the level number
             * **/
            if (waveCounter != 1) {
                drawManager.drawWave(this, waveCounter, countdown);
            } else {
                drawManager.drawCountDown(this, this.level, countdown,
                        this.bonusLife);
            }

            drawManager.drawHorizontalLine(this, this.height / 2 - this.height
                    / 12);
            drawManager.drawHorizontalLine(this, this.height / 2 + this.height
                    / 12);
        }

        // Soomin Lee / TeamHUD
        if (this.inputDelay.checkFinished()) {
            playTime = (int) ((System.currentTimeMillis() - playStartTime) / 1000) + playTimePre;
        }

        super.drawPost();
        drawManager.completeDrawing(this);
    }

    /**
     * Cleans bullets that go off screen.
     */
    private void cleanBullets() {
        Set<PiercingBullet> recyclable = new HashSet<PiercingBullet>(); // Edited by Enemy
        for (PiercingBullet bullet : this.bullets) { // Edited by Enemy
            bullet.update();
            if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
                    || bullet.getPositionY() > this.height - 70) // ko jesung / HUD team
            {
                //Ctrl-S : set true of CheckCount if the bullet is planned to recycle.
                bullet.setCheckCount(true);
                recyclable.add(bullet);
            }
        }
        this.bullets.removeAll(recyclable);
        PiercingBulletPool.recycle(recyclable); // Edited by Enemy
    }

    /**
     * Clean obstacles that go off screen.
     */
    private void cleanObstacles() { //added by Level Design Team
        Set<Obstacle> removableObstacles = new HashSet<>();
        for (Obstacle obstacle : this.obstacles) {
            obstacle.update(this.level);
            if (obstacle.getPositionY() > this.height - 70 ||
                    obstacle.getPositionY() < SEPARATION_LINE_HEIGHT) {
                removableObstacles.add(obstacle);
            }
        }
        this.obstacles.removeAll(removableObstacles);
    }

    /**
     * Manages collisions between bullets and ships. -original code
     */
    private void manageCollisions() {
        Set<Bullet> recyclable = new HashSet<Bullet>();
        for (Bullet bullet : this.bullets)
            if (bullet.getSpeed() > 0) {
                if (checkCollision(bullet, this.ship) && !this.levelFinished) {
                    recyclable.add(bullet);
                    if (!this.ship.isDestroyed() && !this.item.isbarrierActive()) {    // team Inventory
                        this.ship.destroy();
                        this.rightSubShip.destroy();
                        this.leftSubShip.destroy();
                        this.lives--;
                        this.logger.info("Hit on player ship, " + this.lives
                                + " lives remaining.");
                    }
                }
            } else {
                for (EnemyShip enemyShip : this.enemyShipFormation)
                    if (!enemyShip.isDestroyed()
                            && checkCollision(bullet, enemyShip)) {
                        this.scoreManager.addScore(enemyShip.getPointValue());    //clove
                        this.shipsDestroyed++;
                        // CtrlS - increase the hitCount for 1 kill
                        this.hitCount++;
                        this.enemyShipFormation.destroy(enemyShip);
                        recyclable.add(bullet);
                    }
                if (this.enemyShipSpecial != null
                        && !this.enemyShipSpecial.isDestroyed()
                        && checkCollision(bullet, this.enemyShipSpecial)) {
                    this.scoreManager.addScore(this.enemyShipSpecial.getPointValue());    //clove
                    this.shipsDestroyed++;
                    // CtrlS - increase the hitCount for 1 kill
                    this.hitCount++;
                    this.enemyShipSpecial.destroy();
                    this.enemyShipSpecialExplosionCooldown.reset();
                    recyclable.add(bullet);
                }
            }
        this.bullets.removeAll(recyclable);
        BulletPool.recycle(recyclable);
    }


    /**
     * Manages collisions between bullets and ships. -Edited code for Drop Item
     * Manages collisions between bullets and ships. -Edited code for Piercing Bullet
     */
    //by Enemy team
    public void manageCollisions_add_item() {
        Set<PiercingBullet> recyclable = new HashSet<PiercingBullet>();
        for (PiercingBullet bullet : this.bullets)
            if (bullet.getSpeed() > 0) {
                if (checkCollision(bullet, this.ship) && !this.levelFinished) {
                    recyclable.add(bullet);
                    if (!this.ship.isDestroyed() && !this.item.isbarrierActive()) {    // team Inventory
                        this.ship.destroy();
                        this.rightSubShip.destroy();
                        this.leftSubShip.destroy();
                        this.lives--;
                        this.logger.info("Hit on player ship, " + this.lives
                                + " lives remaining.");

                        // Sound Operator
                        if (this.lives == 0) {
                            sm = SoundManager.getInstance();
                            sm.playShipDieSounds();
                        }
                    }
                }
            } else {
                // CtrlS - set fire_id of bullet.
                bullet.setFire_id(fire_id);
                for (EnemyShip enemyShip : this.enemyShipFormation) {
                    if (!enemyShip.isDestroyed()
                            && checkCollision(bullet, enemyShip)) {
                        int CntAndPnt[] = this.enemyShipFormation._destroy(bullet, enemyShip, false);    // team Inventory
                        this.shipsDestroyed += CntAndPnt[0];
                        int feverScore = CntAndPnt[0]; //TEAM CLOVE //Edited by team Enemy

                        if (enemyShip.getHp() <= 0) {
                            //inventory_f fever time is activated, the score is doubled.
                            if (feverTimeItem.isActive()) {
                                feverScore = feverScore * 10;
                            }
                            this.shipsDestroyed++;
                        }

                        this.scoreManager.addScore(feverScore); //clove
                        this.score += CntAndPnt[1];

                        // CtrlS - If collision occur then check the bullet can process
                        if (!processedFireBullet.contains(bullet.getFire_id())) {
                            // CtrlS - increase hitCount if the bullet can count
                            if (bullet.isCheckCount()) {
                                hitCount++;
                                bullet.setCheckCount(false);
                                this.logger.info("Hit count!");
                                processedFireBullet.add(bullet.getFire_id()); // mark this bullet_id is processed.
                            }
                        }

                        bullet.onCollision(enemyShip); // Handle bullet collision with enemy ship

                        // Check PiercingBullet piercing count and add to recyclable if necessary
                        if (bullet.getPiercingCount() <= 0) {
                            //Ctrl-S : set true of CheckCount if the bullet is planned to recycle.
                            bullet.setCheckCount(true);
                            recyclable.add(bullet);
                        }
                    }
                    // Added by team Enemy.
                    // Enemy killed by Explosive enemy gives points too
                    if (enemyShip.isChainExploded()) {
                        if (enemyShip.getColor() == Color.MAGENTA) {
                            this.itemManager.dropItem(enemyShip, 1, 1);
                        }
                        this.score += enemyShip.getPointValue();
                        this.shipsDestroyed++;
                        enemyShip.setChainExploded(false); // resets enemy's chain explosion state.
                    }
                }
                if (this.enemyShipSpecial != null
                        && !this.enemyShipSpecial.isDestroyed()
                        && checkCollision(bullet, this.enemyShipSpecial)) {
                    int feverSpecialScore = enemyShipSpecial.getPointValue();
                    // inventory - Score bonus when acquiring fever items
                    if (feverTimeItem.isActive()) {
                        feverSpecialScore *= 10;
                    } //TEAM CLOVE //Team inventory

                    // CtrlS - If collision occur then check the bullet can process
                    if (!processedFireBullet.contains(bullet.getFire_id())) {
                        // CtrlS - If collision occur then increase hitCount and checkCount
                        if (bullet.isCheckCount()) {
                            hitCount++;
                            bullet.setCheckCount(false);
                            this.logger.info("Hit count!");
                        }

                    }
                    this.scoreManager.addScore(feverSpecialScore); //clove
                    this.shipsDestroyed++;
                    this.enemyShipSpecial.destroy();
                    this.enemyShipSpecialExplosionCooldown.reset();

                    bullet.onCollision(this.enemyShipSpecial); // Handle bullet collision with special enemy

                    // Check PiercingBullet piercing count for special enemy and add to recyclable if necessary
                    if (bullet.getPiercingCount() <= 0) {
                        //Ctrl-S : set true of CheckCount if the bullet is planned to recycle.
                        bullet.setCheckCount(true);
                        recyclable.add(bullet);
                    }

                    //// Drop item to 100%
                    this.itemManager.dropItem(enemyShipSpecial, 1, 2);
                }

                for (Obstacle obstacle : this.obstacles) {
                    if (!obstacle.isDestroyed() && checkCollision(bullet, obstacle)) {
                        obstacle.destroy();  // Destroy obstacle
                        recyclable.add(bullet);  // Remove bullet

                        // Sound Operator
                        sm = SoundManager.getInstance();
                        sm.playES("obstacle_explosion");
                    }
                }
            }

        for (Obstacle obstacle : this.obstacles) {
            if (!obstacle.isDestroyed() && checkCollision(this.ship, obstacle)) {
                //Obstacles ignored when barrier activated_team inventory
                if (!this.item.isbarrierActive()) {
                    this.lives--;
                    if (!this.ship.isDestroyed()) {
                        this.ship.destroy();  // Optionally, destroy the ship or apply other effects.
                        this.rightSubShip.destroy();
                        this.leftSubShip.destroy();
                    }
                    obstacle.destroy();  // Destroy obstacle
                    this.logger.info("Ship hit an obstacle, " + this.lives + " lives remaining.");
                } else {
                    obstacle.destroy();  // Destroy obstacle
                    this.logger.info("Shield blocked the hit from an obstacle, " + this.lives + " lives remaining.");
                }

                break;  // Stop further collisions if the ship is destroyed.
            }
        }

        this.bullets.removeAll(recyclable);
        PiercingBulletPool.recycle(recyclable);

        //Check item and ship collision
        for (Item item : itemManager.items) {
            if (checkCollision(item, ship)) {
                itemManager.OperateItem(item);
                // CtrlS: Count coin
                if (item.getSpriteType() == DrawManager.SpriteType.ItemCoin) coinItemsCollected++;
                Core.getLogger().info("coin: " + coinItemsCollected);
            }
        }
        itemManager.removeAllReItems();


    }


    /**
     * Checks if two entities are colliding.
     *
     * @param a First entity, the bullet.
     * @param b Second entity, the ship.
     * @return Result of the collision test.
     */
    public static boolean checkCollision(final Entity a, final Entity b) {
        // Calculate center point of the entities in both axis.
        int centerAX = a.getPositionX() + a.getWidth() / 2;
        int centerAY = a.getPositionY() + a.getHeight() / 2;
        int centerBX = b.getPositionX() + b.getWidth() / 2;
        int centerBY = b.getPositionY() + b.getHeight() / 2;
        // Calculate maximum distance without collision.
        int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
        int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
        // Calculates distance.
        int distanceX = Math.abs(centerAX - centerBX);
        int distanceY = Math.abs(centerAY - centerBY);

        return distanceX < maxDistanceX && distanceY < maxDistanceY;
    }

    public int getBulletsShot() {    //clove
        return this.bulletsShot;    //clove
    }                               //clove

    /**
     * Add playtime parameter - Soomin Lee / TeamHUD
     * Returns a GameState object representing the status of the game.
     *
     * @return Current game state.
     */
    public final GameState getGameState() {
        return new GameState(this.level, this.scoreManager.getAccumulatedScore(), this.lives, this.livestwo,
                this.bulletsShot, this.shipsDestroyed, this.playTime, this.coin, this.gem, this.hitCount, this.coinItemsCollected,
                this.twoPlayerMode); // Team-Ctrl-S(Currency)
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public Ship getShip() {
        return ship;
    }    // Team Inventory(Item)

    public ItemBarrierAndHeart getItem() {
        return item;
    }    // Team Inventory(Item)

    public FeverTimeItem getFeverTimeItem() {
        return feverTimeItem;
    } // Team Inventory(Item)

    /**
     * Check remaining enemies
     *
     * @return remaining enemies count.
     */
    private int getRemainingEnemies() {
        int remainingEnemies = 0;
        for (EnemyShip enemyShip : this.enemyShipFormation) {
            if (!enemyShip.isDestroyed()) {
                remainingEnemies++;
            }
        }
        return remainingEnemies;
    } // by HUD team SeungYun


    public SpeedItem getSpeedItem() {
        return this.speedItem;
    }

    // Player 2의 총알과 충돌 처리
    private void handleBulletCollisionsForPlayer2(Set<PiercingBullet> bullets, Ship player2) {
        if (player2 == null) return;
        Set<Bullet> recyclable = new HashSet<>();
        for (Bullet bullet : bullets) {
            if (bullet.getSpeed() > 0 && checkCollision(bullet, player2)) {
                recyclable.add(bullet);
                if (!player2.isDestroyed()) {
                    player2.destroy();
                    livestwo--;
                    System.out.println("Player 2 hit, lives remaining: " + livestwo);
                    if (livestwo <= 0) {
                        player2 = null; // Player 2가 파괴된 경우
                    }
                }
            }
        }
        bullets.removeAll(recyclable);
        BulletPool.recycle(recyclable);
    }

    // Player 2의 장애물과 충돌 처리
    private void handleObstacleCollisionsForPlayer2(Set<Obstacle> obstacles, Ship player2) {
        if (player2 == null) return;
        for (Obstacle obstacle : obstacles) {
            if (!obstacle.isDestroyed() && checkCollision(player2, obstacle)) {
                livestwo--;
                if (!player2.isDestroyed()) {
                    player2.destroy(); // Player 2가 파괴됨
                }
                obstacle.destroy(); // 장애물 제거
                System.out.println("Player 2 hit an obstacle, lives remaining: " + livestwo);

                if (livestwo <= 0) {
                    player2 = null; // Player 2가 파괴된 경우
                }
                break;
            }
        }
    }

    // Player 2의 아이템과 충돌 처리
    private void handleItemCollisionsForPlayer2(Ship player2) {
        if (player2 == null) return;
        for (Item item : itemManager.items) {
            if (checkCollision(item, player2)) {
                itemManager.OperateItem(item); // 아이템 효과 적용
                System.out.println("Item collected by Player 2.");
            }
        }
    }
    // 플레이어 상태 확인 메서드 추가
    private boolean areAllPlayersDestroyed() {
        return this.lives == 0 && (!this.twoPlayerMode || this.livestwo == 0);
    }

    /**
     * Draw pause menu overlay, if game is paused.
     */
    protected void drawPauseOverlay() {
        drawManager.initDrawing(this);
        if (this.isPaused) {
            drawManager.drawPauseOverlay(this);
        }
        drawManager.completeDrawing(this);
    }

    /**
     * Pause a game.
     * pause menu 제어
     * 게임을 일시 정지 상태로 전환하고, 재개, 재시작, 종료 로직을 제어
     */
    protected void pauseGame() {
        this.isPaused = true;
        try {
            while (this.isPaused) {
                drawPauseOverlay();

                if (inputManager.isKeyDown(KeyEvent.VK_R)) {
                    resumeGame();
                    break;
                } else if (inputManager.isKeyDown(KeyEvent.VK_Q)) {
                    restartGame();
                    break;
                } else if (inputManager.isKeyDown(KeyEvent.VK_M)) {
                    exitGame();
                    break;
                }
                if (isTestMode) break;
                Thread.sleep(100);
                pausedTime += 100;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warning("Thread interrupted.");
        }
    }

    /**
     * Resume a game
     * 일시 정지 상태에서 게임을 다시 실행 상태로 전환
     */
    protected void resumeGame() {
        logger.info("Resume a game.");
        this.isPaused = false;
    }

    /**
     * Restart a game
     * 재시작을 나타내는 반환 코드를 설정
     */
    protected void restartGame() {
        logger.info("Restart a game.");
        this.returnCode = 2;
        this.isRunning = false;
        this.isPaused = false;
    }

    /**
     * Exit to main menu
     * 게임을 종료하고 메인 메뉴로 나감
     */
    protected void exitGame() {
        logger.info("Exit to main menu.");
        this.returnCode = 1;
        this.isRunning = false;
        this.isPaused = false;
    }

    /**
     * Get isPaused
     */
    public boolean getIsPaused() {
        return this.isPaused;
    }

    /**
     * Set isPaused
     */
    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    /**
     * Get isRunning
     */
    public boolean getIsRunning() {
        return this.isRunning;
    }

    /**
     * Set isRunning
     */
    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    /**
     * Get returnCode
     */
    public int getReturnCode() {
        return this.returnCode;
    }

    /**
     * Set InputManager (의존성 주입 또는 테스트 목적으로 사용).
     */
    protected void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    /**
     * Set DrawManager (의존성 주입 또는 테스트 목적으로 사용).
     */
    protected void setDrawManager(DrawManager drawManagerMock) {
        this.drawManager = drawManagerMock;
    }

    protected boolean isTestMode = false;

    /**
     * Set testMode
     * 테스트 중 루프를 우회하는 데 사용
     */
    protected void setTestMode(boolean isTestMode) {
        this.isTestMode = isTestMode;
    }

    public boolean getLevelFinished() {
        return this.levelFinished;
    }

}
