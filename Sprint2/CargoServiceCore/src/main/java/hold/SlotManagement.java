package main.java.hold;

import main.java.domain.Product;
import main.java.exceptions.HoldUpdateException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * SlotManagement is a POJO component responsible for managing
 * cargo slots in a hold. It provides passive behaviors:
 * - Initialize slots and track total weight
 * - Respond to queries from CargoService
 * - Update slots based on external requests
 * - Export current state as text or JSON
 *
 * Future extension: Load configuration from a file.
 */
public class SlotManagement implements ISlotManagement {

    private final Map<String, Product> slots = new HashMap<>();
    private int totalWeight;

    /**
     * Default constructor.
     * Initializes 4 empty slots (Slot1, Slot2, Slot3, Slot4).
     */
    public SlotManagement() {
        initializeSlots();
    }

    /**
     * Constructor that initializes the hold from a JSON configuration.
     * Expected JSON format:
     * {
     *   "slots": [
     *     {"slotName":"Slot1","product":{"productId":1,"name":"ProdA","weight":10}},
     *     {"slotName":"Slot2","product":null},
     *     {"slotName":"Slot3","product":{"productId":2,"name":"ProdB","weight":20}}
     *   ]
     * }
     *
     * @param jsonConfig JSON string representing initial slot configuration
     */
    public SlotManagement(String jsonConfig) {
        initializeSlots();
        try {
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonConfig);
            JSONArray slotsArray = (JSONArray) jsonObj.get("slots");

            for (Object obj : slotsArray) {
                JSONObject slotObj = (JSONObject) obj;
                String slotName = (String) slotObj.get("slotName");
                JSONObject productJson = (JSONObject) slotObj.get("product");

                if (slotName != null && slots.containsKey(slotName)) {
                    Product product = null;
                    // If product is not null, create a Product instance
                    if (productJson != null) {
                        int productId = ((Long) productJson.get("productId")).intValue();
                        String name = (String) productJson.get("name");
                        int weight = ((Long) productJson.get("weight")).intValue();
                        product = new Product(productId, name, weight);
                    }
                    updateHold(product, slotName);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the slots as empty and reset total weight to zero.
     */
    private void initializeSlots() {
        slots.put("Slot1", null);
        slots.put("Slot2", null);
        slots.put("Slot3", null);
        slots.put("Slot4", null);
        totalWeight = 0;
    }

    /**
     * @return the name of a free slot or "NONE" if no slot is available
     */
    @Override
    public String freeSlot() {
        return slots.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() == null)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("NONE");
    }


    /**
     * @return the total weight of all products in the hold
     */
    @Override
    public int totalWeightReq() {
        return totalWeight;
    }

    /**
     * Update a slot with a new product or empty it if product is null.
     * Adjusts the total weight accordingly.
     *
     * @param product  product to store in the slot (null means empty the slot)
     * @param slotName the name of the slot (e.g., "Slot1")
     * @return true if successful, false if slot does not exist
     */
    @Override
    public void updateHold(Product product, String slotName) throws HoldUpdateException {
        if (!slots.containsKey(slotName)) {
            throw new HoldUpdateException("Slot '" + slotName + "' does not exist.");
        }

        // Remove old product weight if present
        Product oldProduct = slots.get(slotName);
        if (oldProduct != null) {
        	throw new HoldUpdateException("Slot '" + slotName + "' is already occupied");
        }

        // Insert new product
        slots.put(slotName, product);

        // Add new product weight if present
        if (product != null) {
            totalWeight += product.getWeight();
        }
    }


    /**
     * Return the hold state as either text or JSON.
     *
     * @param asJson if true returns JSON, otherwise returns formatted text
     * @return String representation of the hold
     */
    @Override
    public String getHoldState(boolean asJson) {
        if (!asJson) {
            // Human-readable format
            StringBuilder sb = new StringBuilder("Hold State:\n");
            for (Map.Entry<String, Product> entry : slots.entrySet()) {
                sb.append(entry.getKey())
                  .append(" -> ")
                  .append(entry.getValue() == null ? "EMPTY" : entry.getValue().toString())
                  .append("\n");
            }
            sb.append("Total Weight: ").append(totalWeight).append(" kg");
            return sb.toString();
        } else {
            // JSON format
            JSONObject root = new JSONObject();
            JSONArray slotsArray = new JSONArray();

            for (Map.Entry<String, Product> entry : slots.entrySet()) {
                JSONObject slotObj = new JSONObject();
                slotObj.put("slotName", entry.getKey());

                // Convert Product into JSON object instead of string
                if (entry.getValue() != null) {
                    JSONObject productObj = new JSONObject();
                    productObj.put("productId", entry.getValue().getProductId());
                    productObj.put("name", entry.getValue().getName());
                    productObj.put("weight", entry.getValue().getWeight());
                    slotObj.put("product", productObj);
                } else {
                    slotObj.put("product", null);
                }

                slotsArray.add(slotObj);
            }

            root.put("slots", slotsArray);
            root.put("totalWeight", totalWeight);
            return root.toJSONString();
        }
    }

    /**
     * Simple main test for quick verification.
     */
    public static void main(String[] args) {
        String jsonConfig = "{ \"slots\": [" +
                "{\"slotName\":\"Slot1\",\"product\":{\"productId\":1,\"name\":\"ProdA\",\"weight\":10}}," +
                "{\"slotName\":\"Slot2\",\"product\":null}," +
                "{\"slotName\":\"Slot3\",\"product\":{\"productId\":2,\"name\":\"ProdB\",\"weight\":20}}" +
                "]}";

        SlotManagement sm = new SlotManagement(jsonConfig);
        System.out.println(sm.getHoldState(false)); // Text output
        System.out.println(sm.getHoldState(true));  // JSON output
    }
}
