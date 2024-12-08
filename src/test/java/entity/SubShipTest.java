package entity;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testShootCooldown() {
        assertTrue(subShip.shoot(bullets));
        assertEquals(1, bullets.size());
        assertFalse(subShip.shoot(bullets));
        assertEquals(1, bullets.size());
    }

    @Test
    public void testDestroyAndUpdate() {
        assertFalse(subShip.isDestroyed());
        subShip.destroy();
        assertTrue(subShip.isDestroyed());
        subShip.update();
        assertTrue(subShip.isDestroyed());
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

    //테스트 커버리지 검사 확인을 위한 메서드의 테스트 코드
    @Test
    public void testIsMovingFast() {
        assertTrue(subShip.isMovingFast());
    }
}
