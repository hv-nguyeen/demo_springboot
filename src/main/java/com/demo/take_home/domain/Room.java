package nl.vu.cs.softwaredesign.domain;

import nl.vu.cs.softwaredesign.domain.enums.ApplianceType;
import nl.vu.cs.softwaredesign.domain.enums.UsageMode;
import nl.vu.cs.softwaredesign.service.CarbonFootprintCalculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Room implements Emissions, Iterable<Appliance> {
    private final String name;
    private final List<Appliance> applianceList;

    public Room(String inputName) {
        this.name = inputName;
        this.applianceList = new ArrayList<>();
    }

    public Room(Room other) {
        this.name = other.name;
        this.applianceList = new ArrayList<>();

        for (Appliance appliance : other) {
            applianceList.add(new Appliance(appliance));
        }
    }

    @Override
    public double calculateCF() {
        return CarbonFootprintCalculator.calculateRoomCF(this);
    }

    @Override
    public Iterator<Appliance> iterator() {
        return new EmissionIterator<>(applianceList);
    }

    public boolean addAppliance(Appliance appliance) {
        if (getAppliance(appliance.getModel()) != null) {
            return false;
        }
        return applianceList.add(appliance);
    }

    public boolean removeAppliance(String applianceModel) {
        Appliance appliance = getAppliance(applianceModel);
        if (appliance != null) {
            return applianceList.remove(appliance);
        }
        return false;
    }

    public Appliance getAppliance(String applianceModel) {
        for (Appliance appliance : applianceList) {
            if (appliance.getModel().equals(applianceModel)) {
                return appliance;
            }
        }
        return null;
    }

    public boolean modifyAppliance(Appliance inputAppliance, String attribute, String value) {

        switch (attribute.toLowerCase()) {
            case "model":
                inputAppliance.setModel(value);
                break;
            case "type":
                try {
                    inputAppliance.setType(ApplianceType.valueOf(attribute.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return false;
                }
                break;
            case "em":
                try {
                    inputAppliance.setEmbodiedEmissions(Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    return false;
                }
                break;
            case "power":
                try {
                    inputAppliance.setPowerConsumption(Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    return false;
                }
                break;
            case "mode":
                try {
                    int dayStartTime = 0;
                    int dayEndTime = 23;
                    UsageMode mode = UsageMode.valueOf(value.toUpperCase());
                    inputAppliance.setMode(mode);

                    if (mode == UsageMode.ALWAYS_ON) {
                        inputAppliance.setStartTime(dayStartTime);
                        inputAppliance.setEndTime(dayEndTime);
                    }
                } catch (IllegalArgumentException e) {
                    return false;
                }
                break;
            case "start":
                try {
                    inputAppliance.setStartTime(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    return false;
                }
                break;
            case "end":
                try {
                    inputAppliance.setEndTime(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    return false;
                }
                break;
            default:
                return false;
        }
        return true;
    }

    // deep copy to prevent unintended modifications
    public List<Appliance> getApplianceList() {
        List<Appliance> deepCopy = new ArrayList<>();
        for (Appliance appliance : applianceList) {
            deepCopy.add(new Appliance(appliance));
        }
        return deepCopy;
    }

    public String getName() {
        return name;
    }

}