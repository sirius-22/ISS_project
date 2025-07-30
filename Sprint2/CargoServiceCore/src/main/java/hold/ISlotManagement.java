package main.java.hold;

import main.java.domain.Product;

/**
 * ISlotManagement defines the contract for managing cargo slots inside a hold.
 * This allows for flexibility in changing the implementation in the future.
 */
public interface ISlotManagement {

    /**
     * Checks if there is at least one free slot in the hold.
     * @return true if at least one slot is empty, false otherwise
     */
    boolean freeSlot();

    /**
     * Returns the total weight of all products currently stored in the hold.
     * @return total weight in kilograms
     */
    int totalWeightReq();

    /**
     * Updates a specific slot with a given product.
     * @param product the product to insert (can be null to empty the slot)
     * @param slotName the name of the slot (e.g., "Slot1")
     * @return true if update was successful, false if the slot does not exist
     */
    boolean updateHold(Product product, String slotName);

    /**
     * Returns the state of the hold either as plain text or JSON.
     * @param asJson true for JSON output, false for text output
     * @return string representation of the hold state
     */
    String getHoldState(boolean asJson);
}
