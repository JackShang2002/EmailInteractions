package cpen221.mp2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllOurTests {
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

    /* TASK 2 */
    private static DWInteractionGraph mydwig_2;
    private static DWInteractionGraph mydwig1_2;
    private static DWInteractionGraph mydwig2_2;
    private static UDWInteractionGraph udwig_2;
    private static UDWInteractionGraph udwig1_2;
    private static UDWInteractionGraph udwig2_2;
    private static DWInteractionGraph emptyDwig_2;
    private static UDWInteractionGraph emptyUdwig_2;

    /*TASK 3*/
    private static DWInteractionGraph dwig1_3;
    private static DWInteractionGraph dwig2_3;

    /*TASK 4*/
    private static DWInteractionGraph dwig1_4;
    private static DWInteractionGraph dwig2_4;

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
        /*TASK 2*/
        mydwig_2 = new DWInteractionGraph("resources/myTask1-2Transactions.txt");
        mydwig1_2 = new DWInteractionGraph(mydwig_2, new int[]{0, 11});
        mydwig2_2 = new DWInteractionGraph(mydwig_2, Arrays.asList(20000, 4));
        udwig_2 = new UDWInteractionGraph("resources/myTask1-2UDWTransactions.txt");
        udwig1_2 = new UDWInteractionGraph(udwig_2, new int[]{5, 10});
        udwig2_2 = new UDWInteractionGraph(mydwig_2);
        emptyDwig_2 = new DWInteractionGraph("resources/Empty.txt");
        emptyUdwig_2 = new UDWInteractionGraph("resources/Empty.txt");
        /*TASK 3*/
        dwig1_3 = new DWInteractionGraph("resources/myTask3Transactions1.txt");
        dwig2_3 = new DWInteractionGraph("resources/Task3Transactions1.txt");
        /*TASK 4*/
        dwig1_4 = new DWInteractionGraph("resources/myTask4Transactions1.txt");
        dwig2_4 = new DWInteractionGraph("resources/myTask4Transactions2.txt");
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

    /*TASK 2*/
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

    /* TASK 3 DW TESTS */
    @Test
    public void testBFSNull() {
        Assertions.assertNull(dwig2_3.BFS(1, 100));
        Assertions.assertNull(dwig2_3.BFS(100, 1));
        Assertions.assertNull(emptydwig.BFS(1, 2));
        Assertions.assertNull(emptyDwig_2.BFS(1, 2));
    }

    @Test
    public void testDFSNull() {
        Assertions.assertNull(dwig2_3.DFS(1, 100));
        Assertions.assertNull(dwig2_3.DFS(100, 1));
        Assertions.assertNull(emptydwig.DFS(1, 2));
        Assertions.assertNull(emptyDwig_2.DFS(1, 2));
    }

    @Test
    public void testBFSGraph1_1() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 5, 8, 9, 6);
        Assertions.assertEquals(expected, dwig1_3.BFS(1, 6));
    }

    @Test
    public void testBFSGraph1_2() {
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 5, 8, 9);
        Assertions.assertEquals(expected, dwig1_3.BFS(0, 9));
        Assertions.assertNull(dwig1_3.BFS(0, 7));
        Assertions.assertNull(dwig1_3.BFS(7, 0));
        Assertions.assertNull(dwig1_3.BFS(0, 17));
        Assertions.assertNull(dwig1_3.BFS(17, 0));
    }

    @Test
    public void testBFSGraph1_3() {
        List<Integer> expected = Arrays.asList(5, 6, 2, 8);
        Assertions.assertEquals(expected, dwig1_3.BFS(5, 8));
        Assertions.assertNull(dwig1_3.BFS(5, 9));
    }

    @Test
    public void testBFSGraph1_4() {
        List<Integer> expected = Arrays.asList(7, 17);
        Assertions.assertEquals(expected, dwig1_3.BFS(7, 17));
        Assertions.assertNull(dwig1_3.BFS(17, 7));
    }

    @Test
    public void testDFSdwig1_31() {
        List<Integer> expected = Arrays.asList(0, 1, 2, 5);
        Assertions.assertEquals(expected, dwig1_3.DFS(0, 5));
    }

    @Test
    public void testDFSdwig1_32() {
        List<Integer> expected = Arrays.asList(1, 2, 5, 6, 8, 3, 9);
        Assertions.assertEquals(expected, dwig1_3.DFS(1, 9));
    }
    @Test
    public void testDFSdwig1_33() {
        List<Integer> expected = Arrays.asList(7,17);
        Assertions.assertEquals(expected, dwig1_3.DFS(7, 17));
    }

    @Test
    public void testDFSdwig1_34() {
        List<Integer> expected = null;
        Assertions.assertEquals(expected, dwig1_3.DFS(17, 7));
    }

    @Test
    public void testDFSdwig1_35() {
        List<Integer> expected = Arrays.asList(0, 1, 2, 5, 6, 8);
        Assertions.assertEquals(expected, dwig1_3.DFS(0, 8));
    }

    /*TASK 3 UDW TESTS: */
    @Test
    public void testNumComponent() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/myTask3-components-test.txt");
        Assertions.assertEquals(4, t.NumberOfComponents());
    }

    @Test
    public void testNumComponent1() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/myTask3-components-test1.txt");
        Assertions.assertEquals(3, t.NumberOfComponents());
    }

    @Test
    public void testNumComponent2() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Empty.txt");
        Assertions.assertEquals(0, t.NumberOfComponents());
    }

    @Test
    public void testPathExists() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/myTask3-components-test.txt");
        Assertions.assertTrue(t.PathExists(1, 3));
        Assertions.assertTrue(t.PathExists(1, 2));
        Assertions.assertTrue(t.PathExists(1, 4));
        Assertions.assertTrue(t.PathExists(2, 3));
        Assertions.assertTrue(t.PathExists(2, 4));
        Assertions.assertTrue(t.PathExists(3, 4));
        Assertions.assertTrue(t.PathExists(3, 3));
        Assertions.assertTrue(t.PathExists(4, 3));
        Assertions.assertFalse(t.PathExists(1, 5));
        Assertions.assertFalse(t.PathExists(5, 1));
        Assertions.assertFalse(t.PathExists(8, 9));
        Assertions.assertFalse(t.PathExists(5, 11));
        Assertions.assertFalse(t.PathExists(0, 10));
        Assertions.assertFalse(t.PathExists(8, 9));
    }

    /*TASK 4*/
    @Test
    public void testMaxedBreachedUserCountOnly3() {
        // Attacking any user any time at t = 0 will pollute 3 users in an any hour window to account for same-second pollution.
        Assertions.assertEquals(3, dwig1_4.MaxBreachedUserCount(1));
    }

    @Test
    public void testMaxedBreachedUserTimeNotInOrder() {
        // Attacking user 7 any time in the window [0,120] will pollute 8 users in a 2 hour window.
        Assertions.assertEquals(8, dwig2_4.MaxBreachedUserCount(2));
    }
}
