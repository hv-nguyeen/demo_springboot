package nl.vu.cs.softwaredesign.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nl.vu.cs.softwaredesign.domain.enums.Region;

import java.io.FileReader;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class CarbonIntensityParser {

    public Map<Region, Map<Integer, Integer>> parseAllCI() {
        Map<Region, Map<Integer, Integer>> result = new EnumMap<>(Region.class);
        for (Region reg : Region.values()) {
            result.put(reg, parseCI(reg.getFilePath()));
        }
        return result;
    }

    private Map<Integer, Integer> parseCI(String filePath) {
        Map<Integer, Integer> carbonIntensityMap = new HashMap<>();

        try {
            Gson gson = new Gson();
            JsonObject rootNode = gson.fromJson(new FileReader(filePath), JsonObject.class);
            JsonArray historyArray = rootNode.getAsJsonArray("history");
            for (JsonElement historyRecord : historyArray) {
                JsonObject jsonRecord = historyRecord.getAsJsonObject();
                String datetime = jsonRecord.get("datetime").getAsString();
                int hour = Integer.parseInt(datetime.substring(11, 13));
                int carbonIntensity = jsonRecord.get("carbonIntensity").getAsInt();
                carbonIntensityMap.put(hour, carbonIntensity);
            }
        } catch (Exception ignored) {
        }

        return carbonIntensityMap;
    }
}