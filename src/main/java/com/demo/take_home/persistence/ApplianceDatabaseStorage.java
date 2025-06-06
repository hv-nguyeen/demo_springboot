package com.demo.take_home.persistence;



import com.demo.take_home.domain.Appliance;
import com.demo.take_home.domain.EmissionIterator;
import com.demo.take_home.domain.enums.ApplianceType;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ApplianceDatabaseStorage implements Iterable<Appliance> {
    private static ApplianceDatabaseStorage instance;
    private final List<Appliance> applianceStorageList;

    private ApplianceDatabaseStorage() {
        ApplianceDatabaseParser applianceParser = new ApplianceDatabaseParser();
        this.applianceStorageList = applianceParser.parseApplianceDatabase();
    }

    public static ApplianceDatabaseStorage getInstance() {
        if (instance == null) {
            instance = new ApplianceDatabaseStorage();
        }
        return instance;
    }

    @Override
    public Iterator<Appliance> iterator() {
        return new EmissionIterator<>(applianceStorageList);
    }

    public List<Appliance> getAppliancesByType(ApplianceType type) {
        return applianceStorageList.stream()
                .filter(a -> a.getType() == type)
                .collect(Collectors.toList());
    }

    public Iterator<Appliance> getApplianceIteratorByType(ApplianceType type) {
        return new EmissionIterator<>(applianceStorageList, appliance -> appliance.getType() == type);
    }


}
