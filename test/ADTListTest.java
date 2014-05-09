/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Ryan W Radtke <RyanWRadtke@gmail.com>
 */
public class ADTListTest {

    static int count = 0;

    public ADTListTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void ADTListTest() {
        ADTList<Integer> listTest = new ADTList<>();
        Integer[] test1 = {1, 2, 3, 4, 5};

        assertTrue(listTest.isEmpty());

        fillList(listTest, test1);

        Iterator itr = listTest.getIterator();

        while (itr.hasNext()) {
            System.out.print(itr.next() + " ");
        }

        assertFalse(listTest.isEmpty());

        verifyList(listTest, test1);

        assertEquals((Integer) 3, listTest.remove(3));

        assertEquals(count - 1, listTest.getLength());

        assertTrue(listTest.add(3, (Integer) 9));

        test1[2] = 9;

        verifyList(listTest, test1);

        assertEquals(count, listTest.getLength());

        assertTrue(listTest.replace(2, (Integer) 15));

        test1[1] = 15;

        verifyList(listTest, test1);

        assertFalse(listTest.replace(listTest.getLength() + 1, (Integer) 22));

        assertTrue(listTest.contains((Integer) 1));
        assertFalse(listTest.contains((Integer) 22));

        listTest.clear();

        assertTrue(listTest.isEmpty());

        fillList(listTest, test1);

        itr = listTest.getIterator();

        while (itr.hasNext()) {
            System.out.print(itr.next() + " ");
        }

        assertFalse(listTest.isEmpty());

        Object[] testArray = listTest.toArray();

        verifyList(listTest, test1);

        assertArrayEquals(testArray, test1);

        assertEquals((Integer) 5, listTest.remove(5));

        assertEquals((Integer) 1, listTest.remove(1));

        assertTrue(listTest.replace(1, (Integer) 1));

    }

    public void fillList(ADTList<Integer> listTest, Integer[] test1) {
        for (Integer i : test1) {
            listTest.add(i);
        }
    }

    public void verifyList(ADTList<Integer> listTest, Integer[] test1) {
        count = 0;

        System.out.println("\n---------------------\n");
        System.out.println("List\t\tArray");

        for (Integer data : listTest) {
            System.out.println(data + "\t\t" + test1[count]);
            assertEquals(test1[count], data);
            count++;
        }

    }
}
