package nl.vu.cs.softwaredesign.service;

import nl.vu.cs.softwaredesign.domain.Appliance;
import nl.vu.cs.softwaredesign.domain.House;
import nl.vu.cs.softwaredesign.domain.Room;
import nl.vu.cs.softwaredesign.persistence.CarbonIntensityStorage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Statistics {
    private final House house;

    public Statistics() {
        this.house = House.getInstance();
    }

    public Map<Integer, Integer> calculateOptimalTime() {
        Map<Integer, Integer> regionMap = CarbonIntensityStorage.getInstance().getRegionMap(house.getRegionName());
        Map<Integer, Integer> returnMap = new HashMap<>();

        int lowestCIKey = -1;
        int lowestCIValue = Integer.MAX_VALUE;

        for (Map.Entry<Integer, Integer> entry : regionMap.entrySet()) {
            if (entry.getValue() < lowestCIValue) {
                lowestCIValue = entry.getValue();
                lowestCIKey = entry.getKey();
            }
        }
        returnMap.put(lowestCIKey, lowestCIValue);

        return returnMap;

    }

    public Map<Integer, Integer> calculateWorstTime() {
        Map<Integer, Integer> regionMap = CarbonIntensityStorage.getInstance().getRegionMap(house.getRegionName());
        Map<Integer, Integer> returnMap = new HashMap<>();

        int hourKey = -1;
        int highestCIValue = -1;

        for (Map.Entry<Integer, Integer> entry : regionMap.entrySet()) {
            if (entry.getValue() > highestCIValue) {
                highestCIValue = entry.getValue();
                hourKey = entry.getKey();
            }
        }

        returnMap.put(hourKey, highestCIValue);

        return returnMap;
    }

    public List<Appliance> getTopContributors() {
        List<Appliance> allApplianceList = house.getAllAppliances();

        if (allApplianceList.isEmpty()) {
            return Collections.emptyList();
        }

        allApplianceList.sort((a1, a2) -> Double.compare(CarbonFootprintCalculator.calculateApplianceCF(a2), CarbonFootprintCalculator.calculateApplianceCF(a1)));

        return Collections.unmodifiableList(allApplianceList);

    }

    public double getContributionToTotal(String roomName, String applianceName) {
        Appliance queryAppliance = house.getRoom(roomName).getAppliance(applianceName);

        if (queryAppliance == null) {
            return -1;
        }
        double houseCF = house.calculateCF();
        double applianceCF = queryAppliance.calculateCF();

        return Math.round(applianceCF / houseCF * 100);

    }

    public double getContributionToRoom(String roomName, String applianceName) {
        if (roomName == null || applianceName == null) {
            return 0.0;
        }

        Room queryRoom = house.getRoom(roomName);
        if (queryRoom == null) {
            return 0.0;
        }

        Appliance queryAppliance = queryRoom.getAppliance(applianceName);
        if (queryAppliance == null) {
            return 0.0;
        }

        double roomCF = queryRoom.calculateCF();
        double applianceCF = queryAppliance.calculateCF();

        return Math.round((applianceCF / roomCF) * 100);
    }


}