/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.sets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ico0
 */
class BoundedSetOfNaturalsTest {
    private BoundedSetOfNaturals setA;
    private BoundedSetOfNaturals setB;
    private BoundedSetOfNaturals setC;


    @BeforeEach
    public void setUp() {
        setA = new BoundedSetOfNaturals(1);
        setB = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30, 40, 50, 60});
        setC = BoundedSetOfNaturals.fromArray(new int[]{50, 60});
    }

    @AfterEach
    public void tearDown() {
        setA = setB = setC = null;
    }

    @DisplayName("Add elements")
    @Test
    public void testAddElement() {
        setA.add(99);
        assertTrue(setA.contains(99), "add: added element not found in the set.");
        assertEquals(1, setA.size());

        assertThrows(IllegalArgumentException.class, () -> setB.add(11));
        assertFalse(setB.contains(11), "add: added element found in the set.");

        assertEquals(6, setB.size(), "add: elements count not as expected.");
    }

    @DisplayName("Add elements from a bad array")
    @Test
    public void testAddFromBadArray() {
        int[] elems = new int[]{10, -20, -30};

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> setA.add(elems));
    }

    @DisplayName("Add a duplicated element")
    @Test
    public void testAddDuplicatedElement() {
        BoundedSetOfNaturals setD = new BoundedSetOfNaturals(2);
        setD.add(10);
        assertThrows(IllegalArgumentException.class, () -> setD.add(10));
    }

    @DisplayName("Add a negative element")
    @Test
    public void testAddNegativeElement() {
        BoundedSetOfNaturals setD = new BoundedSetOfNaturals(2);
        setD.add(10);
        assertThrows(IllegalArgumentException.class, () -> setD.add(-1));
    }

    @DisplayName("Intersect sets")
    @Test
    public void testIntersect() {
        assertTrue(setB.intersects(setC));

        setA.add(99);
        assertFalse(setB.intersects(setA));

        assertFalse(setA.intersects(setB));
    }

    @DisplayName("Equal sets")
    @Test
    public void testEqualSets() {
        BoundedSetOfNaturals setD = BoundedSetOfNaturals.fromArray(new int[]{50, 60});
        assertEquals(setC, setD);
        assertEquals(setC.hashCode(), setD.hashCode());

        assertNotEquals(setA, setB);
    }

    @DisplayName("Equal sanity check")
    @Test
    public void testEqualSanity() {
        assertEquals(setC, setC);
        assertNotEquals(setC, null);
        assertNotEquals(setC, 1);
    }
}
