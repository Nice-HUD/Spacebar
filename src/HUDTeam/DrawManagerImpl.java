package HUDTeam;

import engine.DrawManager;
import screen.Screen;

import java.awt.*;

public class DrawManagerImpl extends DrawManager {

    // 메서드 추가
    public static void drawLevel(final Screen screen, final int level){
        String levelText = "Level: " + level;

        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);

        int xPosition = screen.getWidth() - fontRegularMetrics.stringWidth(levelText) - 100;
        int yPosition = 25;

        backBufferGraphics.drawString(levelText, xPosition, yPosition);
    } // Lee Hyun Woo - level
    
    public static void drawAttackSpeed(final Screen screen, final double attackSpeed) {
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.setColor(Color.WHITE);
        String attackSpeedText = String.format("AS: %.2f ", attackSpeed);
        backBufferGraphics.drawString(attackSpeedText, 10, screen.getHeight() - 25); // 화면 상단 좌측에 표시
    }

    public static void drawSpeed(final Screen screen, final int speed) {
        String speedString = "MS : " + speed;
        backBufferGraphics.setColor(Color.WHITE);
        backBufferGraphics.setFont(fontRegular);
        backBufferGraphics.drawString(speedString, 85, screen.getHeight() - 25);
    }
}
