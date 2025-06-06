package com.demo.take_home.service;



import com.demo.take_home.domain.Appliance;
import com.demo.take_home.domain.House;
import com.demo.take_home.domain.Room;
import com.demo.take_home.persistence.CarbonIntensityStorage;

import java.util.List;
import java.util.Map;

public class CarbonFootprintCalculator {

    private CarbonFootprintCalculator() {
    }

    public static double calculateApplianceCF(Appliance appliance) {
        return appliance.getEmbodiedEmissions() + emissionsOverTimeWindow(appliance);
    }

    public static double calculateRoomCF(Room room) {
        double accumulator = 0;
        List<Appliance> applianceCollection = room.getApplianceList();

        for (Appliance appliance : applianceCollection) {
            accumulator += calculateApplianceCF(appliance);
        }
        return accumulator;
    }

    public static double calculateHouseCF(House house) {
        double accumulator = 0;

        for (Room room : house) {
            accumulator += calculateRoomCF(room);
        }
        return accumulator;
    }

    private static double emissionsOverTimeWindow(Appliance inputAppliance) {
        double accumulator = 0;
        int startTime = inputAppliance.getStartTime();
        int endTime = inputAppliance.getEndTime();


        for (int currentPeriod = startTime; currentPeriod < endTime; currentPeriod++) {
            accumulator += emissionAtGivenHour(inputAppliance, currentPeriod);
        }
        return accumulator;
    }


    private static double emissionAtGivenHour(Appliance inputAppliance, int givenHour) {
        Map<Integer, Integer> regionMap = CarbonIntensityStorage.getInstance().getRegionMap(House.getInstance().getRegionName());
        return inputAppliance.getPowerConsumption() * regionMap.get(givenHour);
    }


}