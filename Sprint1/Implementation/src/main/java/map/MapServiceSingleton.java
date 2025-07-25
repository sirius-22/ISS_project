package map;
import java.io.IOException;

public class MapServiceSingleton {
    private static MapService instance;

    public static void init(String jsonPath) throws IOException {
        if (instance == null) {
            instance = new MapService(jsonPath);
        }
    }

    public static MapService getInstance() {
        return instance;
    }
}
