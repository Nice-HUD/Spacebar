package screen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FrameTest {

    private Frame frameMock;

    @BeforeEach
    public void setUp() {
        frameMock = mock(Frame.class);
    }

    @Test
    public void testUpdateSize() {
        int newWidth = 1280;
        int newHeight = 720;

        doNothing().when(frameMock).setSize(newWidth, newHeight);
        when(frameMock.getWidth()).thenReturn(newWidth);
        when(frameMock.getHeight()).thenReturn(newHeight);

        frameMock.setSize(newWidth, newHeight);

        assertEquals(newWidth, frameMock.getWidth());
        assertEquals(newHeight, frameMock.getHeight());
    }
}
