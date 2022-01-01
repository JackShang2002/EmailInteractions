package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class OurTask4Tests {

    private static DWInteractionGraph dwig1;
    private static DWInteractionGraph dwig2;



    @BeforeAll
    public static void setupTests() {
        dwig1 = new DWInteractionGraph("resources/myTask4Transactions1.txt");
        dwig2 = new DWInteractionGraph("resources/myTask4Transactions2.txt");
    }

    @Test
    public void testMaxedBreachedUserCountOnly3() {
        // Attacking any user any time at t = 0 will pollute 3 users in an any hour window to account for same-second pollution.
        Assertions.assertEquals(3, dwig1.MaxBreachedUserCount(1));
    }

    @Test
    public void testMaxedBreachedUserTimeNotInOrder() {
        // Attacking user 7 any time in the window [0,120] will pollute 8 users in a 2 hour window.
        Assertions.assertEquals(8, dwig2.MaxBreachedUserCount(2));
    }
}
