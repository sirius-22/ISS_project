package main.java.map;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MapService {
		//Map of map Object -> map of its coordinates "x" ->0 "y" ->0
    private final Map<String, Map<String, Integer>> lookupMap = new HashMap<>();

    public MapService(String jsonFilePath) throws IOException {
        String content = Files.readString(Paths.get(jsonFilePath));
        JsonObject jsonData = JsonParser.parseString(content).getAsJsonObject();

        // Carica Home
        lookupMap.put("Home", extractCoords(jsonData.getAsJsonObject("Home")));
        // Carica IoPort e PickupContainerPosition
        lookupMap.put("IO", extractCoords(jsonData.getAsJsonObject("IoPort")));
        lookupMap.put("Pickup", extractCoords(jsonData.getAsJsonObject("PickupContainerPosition")));

        // Carica SlotObstacles 
        //real positiones of the slots
        for (JsonElement e : jsonData.getAsJsonArray("SlotsObstacles")) {
            JsonObject obj = e.getAsJsonObject();
            lookupMap.put(obj.get("name").getAsString(), extractCoords(obj));
        }

        // Carica LaydownPositions
        //we name them as "Slot1", "Slot2", etc.
        // This is a list of positions where the robot can lay down containers
        for (JsonElement e : jsonData.getAsJsonArray("LaydownPositions")) {
            JsonObject obj = e.getAsJsonObject();
            lookupMap.put(obj.get("name").getAsString(), extractCoords(obj));
        }
    }

    public Map<String, Integer> getCoordinates(String name) {
        return lookupMap.get(name);
    }

    private Map<String, Integer> extractCoords(JsonObject obj) {
        Map<String, Integer> coords = new HashMap<>();
        coords.put("x", obj.get("x").getAsInt());
        coords.put("y", obj.get("y").getAsInt());
        return coords;
    }


    // test
//    public static void main(String[] args) throws IOException {
//        MapService service = new MapService("map.json");
//
//        System.out.println("Home:");
//        System.out.println(service.getCoordinates("Home"));  // {x=0, y=0}
//        System.out.println("SlotObstacle1:");
//        System.out.println(service.getCoordinates("SlotObstacle1"));  // {x=2, y=1}
//        System.out.println("Slot3:");
//        System.out.println(service.getCoordinates("Slot3"));     // {x=1, y=3}
//        System.out.println("Io port:");
//        System.out.println(service.getCoordinates("IO"));     // {x=0, y=5}
//        System.out.println("Pickup Container Position:");
//        System.out.println(service.getCoordinates("Pickup")); // {x=0, y=4}
//    }
}
