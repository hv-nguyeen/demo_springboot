package nl.vu.cs.softwaredesign.persistence;

import nl.vu.cs.softwaredesign.domain.enums.Region;

import java.util.Collections;
import java.util.Map;

public class CarbonIntensityStorage {
    private static CarbonIntensityStorage instance;

    private Map<Region, Map<Integer, Integer>> carbonIntensityMap;

    private CarbonIntensityStorage() {
        CarbonIntensityParser parser = new CarbonIntensityParser();
        this.carbonIntensityMap = parser.parseAllCI();
        this.carbonIntensityMap = Collections.unmodifiableMap(carbonIntensityMap);
    }

    public static CarbonIntensityStorage getInstance() {
        if (instance == null) {
            instance = new CarbonIntensityStorage();
        }
        return instance;
    }

    public Map<Integer, Integer> getRegionMap(Region regionName) {
        return carbonIntensityMap.get(regionName);
    }
}
