package test.java;


import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import main.java.hold.*;
import main.java.domain.*;

public class HoldTest {
	
	private ISlotManagement slotManagement;

    @Before
    public void setup() {
        // Initialize fresh instance before each test
        slotManagement = new SlotManagement();
    }
    
    /**
     * A new hold should initialize with 4 empty slots.
     * freeSlot() must return the first available slot name.
     */
    @Test
    public void testInitialFreeSlotExists() {
        String free = slotManagement.freeSlot();
        assertFalse(free.equals("NONE"));
    }
    
    /**
     * totalWeightReq() must return 0 on an empty hold.
     */
    @Test
    public void testInitialWeightIsZero() {
        assertEquals("Total weight should be 0 when no product is stored",0, slotManagement.totalWeightReq());
    }

    /**
     * updateHold should successfully add a product in a valid slot
     * and increase the total weight accordingly.
     */
    @Test
    public void testUpdateHold() {
        Product p = new Product(1, "Prod1", 10);
        try {
            slotManagement.updateHold(new Product(1, "ProdA", 10), "Slot1");
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        
        assertEquals("Il peso totale deve riflettere il prodotto inserito",10, slotManagement.totalWeightReq());
        
        String state = slotManagement.getHoldState(false);

        assertNotNull(state);
        
        // At least PID should appear, even if we don't know the format yet
        assertTrue(state.contains("\"productId\":1"));
    }
    
    /**
     * freeSlot() should return "NONE" when all slots are filled.
     */
    @Test
    public void testFreeSlotNoneWhenFull() {
        slotManagement.updateHold(new Product(1, "ProdA", 5), "Slot1");
        slotManagement.updateHold(new Product(2, "ProdB", 5), "Slot2");
        slotManagement.updateHold(new Product(3, "ProdC", 5), "Slot3");
        slotManagement.updateHold(new Product(4, "ProdD", 5), "Slot4");

        assertEquals("No free slot should be available when all slots are occupied","NONE", slotManagement.freeSlot());
    }
    
    @Test
    public void testUpdateHoldInvalidSlot() {
        Product p = new Product(1, "InvalidProd", 50);
        try {
            slotManagement.updateHold(p, "InvalidSlot");
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("does not exist"));
        }
    }
    
    @Test
    public void testUpdateHoldOccupiedSlot() {
        Product p = new Product(1, "ProdA", 50);
        slotManagement.updateHold(p, "Slot1");
        
        Product p2 = new Product(2, "ProdB",30);
        try {
            slotManagement.updateHold(p2, "Slot1");
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("is already occupied"));
        }
    }

    
}
