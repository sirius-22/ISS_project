//package main.java.map;
//
//import com.google.gson.*;
//import java.io.*;
//import java.nio.file.*;
//import java.util.*;
//
//public class MapService {
//
//    private final Map<String, Map<String, Integer>> lookupMap = new HashMap<>();
//
//    public MapService(String jsonFilePath) throws IOException {
//        String content = Files.readString(Paths.get(jsonFilePath));
//        JsonObject jsonData = JsonParser.parseString(content).getAsJsonObject();
//
//        // Carica Home
//        lookupMap.put("Home", extractCoords(jsonData.getAsJsonObject("Home")));
//        // Carica IoPort e PickupContainerPosition
//        lookupMap.put("IO", extractCoords(jsonData.getAsJsonObject("IoPort")));
//        lookupMap.put("Pickup", extractCoords(jsonData.getAsJsonObject("PickupContainerPosition")));
//
//        // Carica Slots
//        for (JsonElement e : jsonData.getAsJsonArray("Slots")) {
//            JsonObject obj = e.getAsJsonObject();
//            lookupMap.put(obj.get("name").getAsString(), extractCoords(obj));
//        }
//
//        // Carica LaydownPositions
//        for (JsonElement e : jsonData.getAsJsonArray("LaydownPositions")) {
//            JsonObject obj = e.getAsJsonObject();
//            lookupMap.put(obj.get("name").getAsString(), extractCoords(obj));
//        }
//    }
//
//    public Map<String, Integer> getCoordinates(String name) {
//        return lookupMap.get(name);
//    }
//
//    private Map<String, Integer> extractCoords(JsonObject obj) {
//        Map<String, Integer> coords = new HashMap<>();
//        coords.put("x", obj.get("x").getAsInt());
//        coords.put("y", obj.get("y").getAsInt());
//        return coords;
//    }
//
////    // Per test
////    public static void main(String[] args) throws IOException {
////        MapService service = new MapService("map.json");
////        System.out.println(service.getCoordinates("Slot1"));  // {x=2, y=1}
////        System.out.println(service.getCoordinates("L3"));     // {x=1, y=3}
////        System.out.println(service.getCoordinates("IO"));     // {x=0, y=5}
////    }
//}
