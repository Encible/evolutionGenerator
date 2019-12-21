package EvoGen;

import java.util.Random;

public class Rand {
    public static int rand(int range) {
        Random rand = new Random();
        return rand.nextInt(range);
    }
}
