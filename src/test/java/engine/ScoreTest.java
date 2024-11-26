package engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ScoreTest {
    @Test
    void testScoreCreation() {
        Score score = new Score("TST", 100);
        assertEquals("TST", score.getName());
        assertEquals(100, score.getScore());
    }
}