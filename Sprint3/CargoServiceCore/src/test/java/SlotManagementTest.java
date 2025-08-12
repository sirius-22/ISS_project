package test.java;

import static org.junit.Assert.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.Before;

import main.java.hold.*;
import main.java.domain.*;

public class SlotManagementTest2 {
	
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
        assertTrue(free.equals("Slot1"));
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

    
    @Test
    public void testEmptyHoldJsonRepresentation() {
        String stateJson = slotManagement.getHoldState(true);
        assertNotNull("Lo stato JSON non deve essere null", stateJson);

        try {
            JSONObject root = (JSONObject) new JSONParser().parse(stateJson);

            // Verifica totalWeight
            assertEquals("Il peso totale deve essere 0", 0, ((Long) root.get("totalWeight")).intValue());

            // Verifica slots array
            JSONArray slots = (JSONArray) root.get("slots");
            assertEquals("Devono esserci esattamente 4 slot", 4, slots.size());

            // Ogni slot deve avere product = null
            for (Object obj : slots) {
                JSONObject slotObj = (JSONObject) obj;
                assertTrue("Ogni slot deve avere un nome valido", ((String) slotObj.get("slotName")).startsWith("Slot"));
                assertNull("Ogni slot deve essere vuoto (product = null)", slotObj.get("product"));
            }

        } catch (ParseException e) {
            fail("Formato JSON non valido: " + e.getMessage());
        }
    }
    
    @Test
    public void testJsonStateAfterUpdate() {
        Product prod = new Product(5, "SpecialItem", 42);

        try {
            slotManagement.updateHold(prod, "Slot1");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        String stateJson = slotManagement.getHoldState(true);
        assertNotNull("Lo stato JSON non deve essere null", stateJson);

        try {
            JSONObject root = (JSONObject) new JSONParser().parse(stateJson);

            // Verifica peso totale aggiornato
            assertEquals("Il peso totale deve riflettere il prodotto inserito",
                    42, ((Long) root.get("totalWeight")).intValue());

            // Verifica che Slot1 contenga il prodotto corretto
            JSONArray slots = (JSONArray) root.get("slots");
            JSONObject slot1 = null;
            for (Object obj : slots) {
                JSONObject slotObj = (JSONObject) obj;
                if ("Slot1".equals(slotObj.get("slotName"))) {
                    slot1 = slotObj;
                    break;
                }
            }
            assertNotNull("Slot1 deve esistere", slot1);

            JSONObject product = (JSONObject) slot1.get("product");
            assertNotNull("Slot1 deve contenere un prodotto", product);
            assertEquals(5, ((Long) product.get("productId")).intValue());
            assertEquals("SpecialItem", product.get("name"));
            assertEquals(42, ((Long) product.get("weight")).intValue());

            // Verifica che gli altri slot siano vuoti
            for (Object obj : slots) {
                JSONObject slotObj = (JSONObject) obj;
                if (!"Slot1".equals(slotObj.get("slotName"))) {
                    assertNull(slotObj.get("slotName") + " deve essere vuoto",slotObj.get("product"));
                }
            }

        } catch (ParseException e) {
            fail("Formato JSON non valido: " + e.getMessage());
        }
    }

}
