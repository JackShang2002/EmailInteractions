package cpen221.mp2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


public class OurTask3Tests {

    /*TASK 3 DW TESTS: */
    private static DWInteractionGraph dwig1_3;
    private static DWInteractionGraph dwig2_3;

    @BeforeAll
    public static void setupTests() {
        dwig1_3 = new DWInteractionGraph("resources/myTask3Transactions1.txt");
        dwig2_3 = new DWInteractionGraph("resources/Task3Transactions1.txt");
    }

    @Test
    public void testBFSNull() {
        Assertions.assertEquals(null, dwig2_3.BFS(1, 100));
        Assertions.assertEquals(null, dwig2_3.BFS(100, 1));
    }

    @Test
    public void testDFSNull() {
        Assertions.assertEquals(null, dwig2_3.DFS(1, 100));
        Assertions.assertEquals(null, dwig2_3.DFS(100, 1));
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

}
