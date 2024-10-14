package inventory_develop;
import engine.Core;
import engine.DrawManager;
import screen.GameScreen;
import entity.Ship;
import Enemy.PlayerGrowth;

import java.util.logging.Logger;

public class ItemBarrierAndHeart {

    private static final int barrier_DURATION = 3000;
    private static final int MAX_LIVES = 3;

    private boolean barrierActive = false;
    private long barrierActivationTime;
    protected Logger logger = Core.getLogger();

    // gamescreen에서 딱 한 번 생성되고 말음
    public ItemBarrierAndHeart() {
    }

    //barrier
    public void updateBarrierAndShip(Ship ship) {
        if (this.barrierActive) {   // 베리어 활성화
            ship.setSpriteType(DrawManager.SpriteType.ShipBarrierStatus);

            long currentTime = System.currentTimeMillis();

            if (currentTime - this.barrierActivationTime >= barrier_DURATION) {     // 베리어 비활성화
                ship.setSpriteType(DrawManager.SpriteType.Ship);
                deactivatebarrier();
            }
        } else {    // 베리어가 비활성화 되었을 때는 ship.update()호출
            ship.update();
        }
    }

    public void activatebarrier() {
        this.barrierActive = true;
        this.barrierActivationTime = System.currentTimeMillis();
    }

    public void deactivatebarrier() {
        this.barrierActive = false;
        this.logger.info("barrier effect ends");
    }

    public boolean isbarrierActive() {
        return barrierActive;
    }

    //heart
    public void activeheart(GameScreen gameScreen, Ship ship, PlayerGrowth growth) {
        if (gameScreen.getLives() < MAX_LIVES) {
            gameScreen.setLives(gameScreen.getLives() + 1);
        }
    }
}