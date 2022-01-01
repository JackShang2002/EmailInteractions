package cpen221.mp2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OurTask1Tests {
/*TASK 1 TESTS: */
    private static DWInteractionGraph mydwig;
    private static DWInteractionGraph mydwig1;
    private static DWInteractionGraph mydwig2;
    private static DWInteractionGraph emptydwig;
    private static DWInteractionGraph emptydwig1;
    private static DWInteractionGraph emptydwig2;
    private static UDWInteractionGraph udwig;
    private static UDWInteractionGraph udwig1;
    private static UDWInteractionGraph udwig2;
    private static UDWInteractionGraph udwig3;
    private static UDWInteractionGraph udwig4;


    @BeforeAll
    public static void setupTests() {
        mydwig = new DWInteractionGraph("resources/myTask1-2Transactions.txt");
        mydwig1 = new DWInteractionGraph(mydwig, new int[]{56, 50000});
        mydwig2 = new DWInteractionGraph(mydwig, Arrays.asList(20000, 12, 21, 0));
        emptydwig = new DWInteractionGraph("resources/Empty.txt");
        emptydwig1 = new DWInteractionGraph(emptydwig, new int[]{56, 50000});
        emptydwig2 = new DWInteractionGraph(emptydwig, Arrays.asList(20000, 12, 21, 0));
        udwig = new UDWInteractionGraph("resources/myTask1-2UDWTransactions.txt");
        udwig1 = new UDWInteractionGraph(udwig, new int[]{0, 10});
        udwig2 = new UDWInteractionGraph("resources/myTask1-2UDWTransactions.txt", new int[]{20, 30});
        udwig3 = new UDWInteractionGraph(udwig, new int[]{8, 8});
        udwig4 = new UDWInteractionGraph(mydwig);
    }
    @Test
    public void test1GetUserIDsBase() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 12, 21, 23, 45, 20000));
        Assertions.assertEquals(expected, mydwig.getUserIDs());
    }

    @Test
    public void test1GetUserIDsGraph1() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(1, 20000, 4, 5, 45));
        Assertions.assertEquals(expected, mydwig1.getUserIDs());
    }
    @Test
    public void test1GetUserIDsGraph2() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(0, 21, 20000, 4, 5, 6, 12));
        Assertions.assertEquals(expected, mydwig2.getUserIDs());
    }
    @Test
    public void testGetUserIDsEmptyBase() {
        Set<Integer> expected = new HashSet<>(Arrays.asList());
        Assertions.assertEquals(expected, emptydwig.getUserIDs());
    }
    @Test
    public void testGetUserIDsEmptyGraph1() {
        Set<Integer> expected = new HashSet<>(Arrays.asList());
        Assertions.assertEquals(expected, emptydwig1.getUserIDs());
    }

    @Test
    public void testGetUserIDsEmptyGraph2() {
        Set<Integer> expected = new HashSet<>(Arrays.asList());
        Assertions.assertEquals(expected, emptydwig2.getUserIDs());
    }

    @Test
    public void test1GetEmailCountBase() {
        Assertions.assertEquals(4, mydwig.getEmailCount(2, 3));
        Assertions.assertEquals(0, mydwig.getEmailCount(23, 20000));
    }
    @Test
    public void test1GetEmailGraph1() {
        Assertions.assertEquals(0, mydwig1.getEmailCount(20000, 4));
        Assertions.assertEquals(1, mydwig1.getEmailCount(4, 5));
    }
    @Test
    public void test1GetEmailGraph2() {
        Assertions.assertEquals(1, mydwig2.getEmailCount(20000, 4));
        Assertions.assertEquals(1, mydwig2.getEmailCount(4, 20000));
    }

    @Test
    public void UDWIGtestGetUserIds() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3, 110)), udwig.getUserIDs());
    }

    @Test
    public void UDWIGtestGetUserIds1() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(110, 0, 1,2)), udwig1.getUserIDs());
    }

    @Test
    public void UDWIGtestGetUserIds2() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(110,2,1,0)), udwig2.getUserIDs());
    }

    @Test
    public void UDWIGtestGetUserIds3() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(110,1,0)), udwig3.getUserIDs());
    }
    @Test
    public void UDWIGtestGetUserIds4() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 12, 21, 23, 45, 20000)), udwig4.getUserIDs());
    }

}
