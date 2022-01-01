package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class OurTask2Tests {
    private static DWInteractionGraph mydwig_2;
    private static DWInteractionGraph mydwig1_2;
    private static DWInteractionGraph mydwig2_2;
    private static UDWInteractionGraph udwig_2;
    private static UDWInteractionGraph udwig1_2;
    private static UDWInteractionGraph udwig2_2;
    private static DWInteractionGraph emptyDwig_2;
    private static UDWInteractionGraph emptyUdwig_2;

    @BeforeAll
    public static void setupTests() {
        mydwig_2 = new DWInteractionGraph("resources/myTask1-2Transactions.txt");
        mydwig1_2 = new DWInteractionGraph(mydwig_2, new int[]{0, 11});
        mydwig2_2 = new DWInteractionGraph(mydwig_2, Arrays.asList(20000, 4));
        udwig_2 = new UDWInteractionGraph("resources/myTask1-2UDWTransactions.txt");
        udwig1_2 = new UDWInteractionGraph(udwig_2, new int[]{5, 10});
        udwig2_2 = new UDWInteractionGraph(mydwig_2);
        emptyDwig_2 = new DWInteractionGraph("resources/Empty.txt");
        emptyUdwig_2 = new UDWInteractionGraph("resources/Empty.txt");
    }
    @Test
    public void testReportActivityInTimeWindowGraphDwig() {
        int[] expected1 = new int[]{9, 9, 16};
        Assertions.assertArrayEquals(expected1, mydwig_2.ReportActivityInTimeWindow(new int[]{0, 50000}));
        int[] expected2 = new int[]{3, 3, 5};
        Assertions.assertArrayEquals(expected2, mydwig1_2.ReportActivityInTimeWindow(new int[]{3, 50000}));
    }

    @Test
    public void testReportOnUserDwig() {
        int[] result = mydwig_2.ReportOnUser(20000);
        Assertions.assertEquals(3, result[0]);
        Assertions.assertEquals(1, result[1]);
        Assertions.assertEquals(2, result[2]);
    }
    @Test
    public void testReportOnUserDwig1() {
        int[] result = mydwig_2.ReportOnUser(2);
        Assertions.assertEquals(4, result[0]);
        Assertions.assertEquals(0, result[1]);
        Assertions.assertEquals(1, result[2]);
    }
    @Test
    public void testReportOnUserDwig2() {
        int[] result = mydwig_2.ReportOnUser(4);
        Assertions.assertEquals(3, result[0]);
        Assertions.assertEquals(2, result[1]);
        Assertions.assertEquals(3, result[2]);
    }
    @Test
    public void testReportOnUserDwig3() {
        int[] result = mydwig1_2.ReportOnUser(4);
        Assertions.assertEquals(2, result[0]);
        Assertions.assertEquals(2, result[1]);
        Assertions.assertEquals(3, result[2]);
    }

        @Test
    public void testNthMostActiveUserGraph() {
        Assertions.assertEquals(12, mydwig_2.NthMostActiveUser(6, SendOrReceive.SEND));
        Assertions.assertEquals(21, mydwig_2.NthMostActiveUser(7, SendOrReceive.SEND));
        Assertions.assertEquals(23, mydwig_2.NthMostActiveUser(8, SendOrReceive.SEND));
        Assertions.assertEquals(3, mydwig_2.NthMostActiveUser(1, SendOrReceive.RECEIVE));
    }

    @Test
    public void testNthMostActiveUserGraph1() {
        Assertions.assertEquals(2, mydwig1_2.NthMostActiveUser(1, SendOrReceive.SEND));
        Assertions.assertEquals(3, mydwig1_2.NthMostActiveUser(1, SendOrReceive.RECEIVE));
    }

    @Test
    public void testNthMostActiveUserGraph2() {
        Assertions.assertEquals(4, mydwig2_2.NthMostActiveUser(1, SendOrReceive.SEND));
        Assertions.assertEquals(20000, mydwig2_2.NthMostActiveUser(3, SendOrReceive.RECEIVE));
    }

    @Test
    public void testReportActivityInTimeWindowUdwig() {
        int[] result = udwig_2.ReportActivityInTimeWindow(new int[]{3, 8});
        Assertions.assertEquals(4, result[0]);
        Assertions.assertEquals(4, result[1]);
    }

    @Test
    public void testReportActivityInTimeWindowUdwig1() {
        int[] result = udwig1_2.ReportActivityInTimeWindow(new int[]{6, 15});
        Assertions.assertEquals(3, result[0]);
        Assertions.assertEquals(2, result[1]);
    }

    @Test
    public void testReportActivityInTimeWindowEmptyUdwig() {
        int[] result = emptyUdwig_2.ReportActivityInTimeWindow(new int[]{0, 10});
        Assertions.assertEquals(0, result[0]);
        Assertions.assertEquals(0, result[1]);
    }

    @Test
    public void testReportActivityInTimeWindowUdwig2() {
        int[] result = udwig2_2.ReportActivityInTimeWindow(new int[]{51000, 52000});
        Assertions.assertEquals(0, result[0]);
        Assertions.assertEquals(0, result[1]);
    }

    @Test
    public void testReportOnUserUdwigUserfilter() {
        List<Integer> userFilter = Arrays.asList(0, 110);
        UDWInteractionGraph t = new UDWInteractionGraph(udwig_2, userFilter);
        int[] result = t.ReportOnUser(110);
        Assertions.assertEquals(5, result[0]);
        Assertions.assertEquals(3, result[1]);
    }

    @Test
    public void testReportOnUserUdwig() {
        int[] result = udwig_2.ReportOnUser(12345);
        Assertions.assertEquals(0, result[0]);
        Assertions.assertEquals(0, result[1]);
    }

    @Test
    public void testNthActiveUserUdwigTimefilter() {
        UDWInteractionGraph t = new UDWInteractionGraph(udwig_2, new int[]{12, 20});
        Assertions.assertEquals(0, t.NthMostActiveUser(1));
    }
    @Test
    public void testNthActiveUserUdwig() {
        Assertions.assertEquals(2, udwig_2.NthMostActiveUser(3));
    }
    @Test
    public void testNthActiveUserUdwig1() {
        Assertions.assertEquals(1, udwig1_2.NthMostActiveUser(2));
        Assertions.assertEquals(-1, udwig1_2.NthMostActiveUser(33));
    }
    @Test
    public void testNthActiveUserUdwig2() {
        Assertions.assertEquals(0, udwig2_2.NthMostActiveUser(6));
    }
}
