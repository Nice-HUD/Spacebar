package screen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FrameTest {

    private JFrame frame;

    @BeforeEach
    public void setUp() {
        frame = new JFrame();
    }

    @Test
    public void testUpdateSize() {
        int newWidth = 1280;
        int newHeight = 720;

        frame.setSize(newWidth, newHeight);
        frame.setPreferredSize(new Dimension(newWidth, newHeight));
        frame.pack();

        assertEquals(newWidth, frame.getWidth());
        assertEquals(newHeight, frame.getHeight());
    }
}
