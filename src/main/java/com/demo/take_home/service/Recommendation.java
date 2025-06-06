package nl.vu.cs.softwaredesign.service;

import nl.vu.cs.softwaredesign.domain.Appliance;
import nl.vu.cs.softwaredesign.domain.House;
import nl.vu.cs.softwaredesign.persistence.ApplianceDatabaseStorage;

import java.util.Iterator;

public class Recommendation {
    private final House house;
    private static final double ELECTRICITY_PRICE_PER_KWH = 0.25538;

    public Recommendation(House house) {
        this.house = house;
    }

    public Appliance giveAlternativeSolutions(String roomName, String applianceName) {
        Appliance[] appliances = getValidApplianceAndSuggestion(roomName, applianceName);

        return appliances[1];
    }

    public double calculateYearlySavedCF(String roomName, String applianceName) {
        Appliance[] appliances = getValidApplianceAndSuggestion(roomName, applianceName);

        Appliance queryAppliance = appliances[0];
        Appliance suggestAppliance = appliances[1];

        if (queryAppliance == null) {
            return -1;
        }
        if (suggestAppliance == null) {
            return -2;
        }

        return (queryAppliance.calculateCF() - suggestAppliance.calculateCF()) * 365;

    }

    public double calculateYearlySavedMoney(String roomName, String applianceName) {
        Appliance[] appliances = getValidApplianceAndSuggestion(roomName, applianceName);
        if (appliances[0] == null || appliances[1] == null) {
            return -1;
        }

        Appliance queryAppliance = appliances[0];
        Appliance suggestAppliance = appliances[1];

        return ((queryAppliance.getPowerConsumption() - suggestAppliance.getPowerConsumption()) * (queryAppliance.getEndTime() - queryAppliance.getStartTime()) * ELECTRICITY_PRICE_PER_KWH) * 365;


    }

    private Appliance[] getValidApplianceAndSuggestion(String roomName, String applianceName) {
        Appliance queryAppliance = house.getRoom(roomName).getAppliance(applianceName);
        if (queryAppliance == null) {
            return new Appliance[]{null, null};
        }
        Appliance suggestAppliance = findBetterAppliance(queryAppliance);
        if (suggestAppliance == queryAppliance) {
            suggestAppliance = null;
        }

        return new Appliance[]{queryAppliance, suggestAppliance};
    }

    private Appliance findBetterAppliance(Appliance inputAppliance) {
        Appliance suggestAppliance = inputAppliance;

        Iterator<Appliance> iterator = ApplianceDatabaseStorage.getInstance().getApplianceIteratorByType(inputAppliance.getType());

        while (iterator.hasNext()) {
            Appliance appliance = iterator.next();

            Appliance copyAppliance = new Appliance(appliance);
            copyAppliance.setStartTime(inputAppliance.getStartTime());
            copyAppliance.setEndTime(inputAppliance.getEndTime());

            if (CarbonFootprintCalculator.calculateApplianceCF(suggestAppliance) >
                    CarbonFootprintCalculator.calculateApplianceCF(copyAppliance)) {
                suggestAppliance = copyAppliance;
            }
        }

        return suggestAppliance;
    }
}