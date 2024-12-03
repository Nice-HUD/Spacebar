package entity;

import java.awt.Color;
import java.util.Set;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;
import engine.PlayerGrowth;

public class SubShip extends Entity {
    
    private static final int SHOOTING_INTERVAL = 750;
    private static final int BULLET_SPEED = -4;
    private static final int MOVEMENT_SPEED = 2;
    
    
    private Cooldown shootingCooldown;
    private Cooldown destructionCooldown;
    
    public SubShip(final int positionX, final int positionY, final Color color) {
        super(positionX, positionY, 5 * 2, 5 * 2, color);
        
        this.spriteType = SpriteType.SubShip;
        this.shootingCooldown = Core.getCooldown(SHOOTING_INTERVAL);
        this.destructionCooldown = Core.getCooldown(1000);
    }
    
    public final void moveRight() {
        this.positionX += MOVEMENT_SPEED;
    }
    
    public final void moveLeft() {
        this.positionX -= MOVEMENT_SPEED;
    }
    
    public final boolean shoot(final Set<PiercingBullet> bullets) {
        if (this.shootingCooldown.checkFinished()) {
            this.shootingCooldown.reset();
            bullets.add(PiercingBulletPool.getPiercingBullet(positionX + this.width / 2,
                    positionY, BULLET_SPEED, 1));
            return true;
        }
        return false;
    }
    
    public final void update() {
        if (!this.destructionCooldown.checkFinished())
            this.spriteType = SpriteType.SubShipDestroyed;
        else
            this.spriteType = SpriteType.SubShip;
    }
    
    public final void destroy() {
        this.destructionCooldown.reset();
    }
    
    public final boolean isDestroyed() {
        return !this.destructionCooldown.checkFinished();
    }
    
    public final double getSpeed() {
        return MOVEMENT_SPEED;
    }

    // 테스트 커버리지 검사 확인을 위한 메서드 추가
    public boolean isMovingFast() {
        return MOVEMENT_SPEED > 1;
    }
}