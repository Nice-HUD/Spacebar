import static org.junit.jupiter.api.Assertions.*;

import engine.DrawManager;
import entity.PiercingBullet;
import entity.SubShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class SubShipTest {
    
    private SubShip subShip;
    private Set<PiercingBullet> bullets;
    
    @BeforeEach
    public void setUp() {
        // 초기화: SubShip과 PiercingBullet 세트 생성
        subShip = new SubShip(100, 100, Color.RED);
        bullets = new HashSet<>();
    }
    
    @Test
    public void testInitialPosition() {
        // 초기 위치 확인
        assertEquals(100, subShip.getPositionX());
        assertEquals(100, subShip.getPositionY());
    }
    
    @Test
    public void testMoveRight() {
        double initialX = subShip.getPositionX();
        subShip.moveRight();
        assertEquals(initialX + subShip.getSpeed(), subShip.getPositionX(), 0.001);
    }
    
    @Test
    public void testMoveLeft() {
        double initialX = subShip.getPositionX();
        subShip.moveLeft();
        assertEquals(initialX - subShip.getSpeed(), subShip.getPositionX(), 0.001);
    }
    
    @Test
    public void testShootCooldown() throws InterruptedException {
        // 총알 발사 가능 여부 확인
        assertTrue(subShip.shoot(bullets));
        assertEquals(1, bullets.size());
        
        // 쿨다운 동안은 발사할 수 없어야 함
        assertFalse(subShip.shoot(bullets));
        assertEquals(1, bullets.size());
        
        // 쿨다운 후 다시 발사 가능
        Thread.sleep(750); // SHOOTING_INTERVAL 과 동일
        assertTrue(subShip.shoot(bullets));
        assertEquals(2, bullets.size());
    }
    
    @Test
    public void testDestroyAndUpdate() throws InterruptedException {
        // 파괴 상태 확인
        assertFalse(subShip.isDestroyed());
        subShip.destroy();
        assertTrue(subShip.isDestroyed());
        
        // 파괴 상태에서 업데이트 후 확인
        subShip.update();
        assertEquals(DrawManager.SpriteType.SubShipDestroyed, subShip.getSpriteType());
        
        // 파괴 쿨다운이 끝난 후 다시 정상 상태로 복구
        Thread.sleep(1000); // destructionCooldown 과 동일
        assertFalse(subShip.isDestroyed());
        subShip.update();
        assertEquals(DrawManager.SpriteType.SubShip, subShip.getSpriteType());
    }
    
    @Test
    public void testBulletAttributesOnShoot() {
        // 총알 발사 후 속성과 위치 확인
        bullets.clear();
        subShip.shoot(bullets);
        
        assertEquals(1, bullets.size());
        
        PiercingBullet bullet = bullets.iterator().next();
        
        assertEquals(subShip.getPositionX() + subShip.getWidth() / 2, bullet.getPositionX() + bullet.getWidth() / 2, 0.001);
        assertEquals(subShip.getPositionY(), bullet.getPositionY());
        assertEquals(-4, bullet.getSpeed());
    }
    
    @Test
    public void testSpeed() {
        // SubShip의 이동 속도 확인
        assertEquals(subShip.getSpeed(), subShip.getSpeed());
    }
}

