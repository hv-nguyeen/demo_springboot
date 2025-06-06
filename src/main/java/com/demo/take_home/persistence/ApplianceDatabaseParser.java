package com.demo.take_home.persistence;

import com.demo.take_home.domain.Appliance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ApplianceDatabaseParser {

    public List<Appliance> parseApplianceDatabase() {
        Gson gson = new Gson();
        Type applianceListType = new TypeToken<List<Appliance>>() {
        }.getType();
        List<Appliance> appliances = new ArrayList<>();

        try (FileReader reader = new FileReader("./src/main/java/nl/vu/cs/softwaredesign/resources/applianceDatabase.json")) {
            appliances = gson.fromJson(reader, applianceListType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(appliances);
    }
}