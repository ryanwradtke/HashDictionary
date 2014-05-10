/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
public class ADTHashDictTest {

    public ADTHashDictTest() {
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
    public void ADTHashDictTest() {

        ADTHashDict<Integer, String> directory = newDict(new File("Directory.txt"));

        //testing getSize()
        assertEquals(8, directory.getSize());

        //testing contains()
        assertTrue(directory.contains(444));
        assertFalse(directory.contains(1234));

        //printing directory
        prntDict(directory);

        //testing remove()
        assertEquals("Collier", directory.remove(444));
        prntDict(directory);
        System.out.println("Key: 444 removed.");
        assertEquals(7, directory.getSize());

        //testing getValue()
        assertEquals("Jones", directory.getValue(324));

        //testing add() and rehash()
        assertEquals("Lovelace", directory.add(324, "Lovelace"));

        prntDict(directory);

        assertEquals("Bonnie", directory.add(112, "Bonnie"));

        prntDict(directory);

        //testing Max Load Factor, cannot have more than 2 of exact key

        assertNull(directory.add(324, "Simons"));


        

        prntDict(directory);
        
        //testing getAllValues(K key) and remove(K key, int givenPosition
        ADTListInterface allValues = directory.getAllValues(324);
        int i = 0;
        
        System.out.println("\n\nList of values for key: 324");
        
        for (Object o : allValues) {
            i++;
            System.out.println(i + ": " + o.toString());
        }
        
        System.out.println("Removing Lovelace");
        assertEquals("Lovelace", directory.remove(324, 2));
        
        System.out.println("");
        prntDict(directory);
        
        

    }

    public ADTHashDict newDict(File file) {
        ADTHashDict<Integer, String> newDict = new ADTHashDict<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while (line != null) {

                if (!line.isEmpty()) {
                    String[] splitString = line.split("\\s");
                    assertEquals(splitString[1],
                            newDict.add(Integer.parseInt(splitString[0]), splitString[1]));
                }
                line = br.readLine();

            }
        } catch (FileNotFoundException ex) {
            System.out.println("File does not exist.");
        } catch (IOException ex) {
            System.out.println("Error reading file.");
        }

        return newDict;

    }

    public void prntDict(ADTHashDict dict) {
        Iterator keyItr = dict.getKeyIterator();
        Iterator valItr = dict.getValueIterator();

        System.out.println("-------------------------------");
        System.out.println("Count " + dict.getSize());

        while (keyItr.hasNext() && valItr.hasNext()) {
            System.out.println("Key: " + keyItr.next() + "\t Value: " + valItr.next());
        }
    }
}
