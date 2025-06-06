package com.demo.take_home.domain;


import com.demo.take_home.domain.enums.ApplianceType;
import com.demo.take_home.domain.enums.UsageMode;
import com.demo.take_home.service.CarbonFootprintCalculator;

public class Appliance implements Emissions {
    private String model;
    private ApplianceType type;
    private double embodiedEmissions;
    private double powerConsumption;
    private UsageMode mode;
    private int startTime;
    private int endTime;

    public Appliance(String model, ApplianceType type, double embodiedEmissions, double powerConsumption, UsageMode mode, int startTime, int endTime) {
        this.model = model;
        this.type = type;
        this.embodiedEmissions = embodiedEmissions;
        this.powerConsumption = powerConsumption;
        this.mode = mode;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Appliance(Appliance other) {
        this.model = other.model;
        this.type = other.type;
        this.embodiedEmissions = other.embodiedEmissions;
        this.powerConsumption = other.powerConsumption;
        this.mode = other.mode;
        this.startTime = other.startTime;
        this.endTime = other.endTime;
    }

    @Override
    public double calculateCF() {
        return CarbonFootprintCalculator.calculateApplianceCF(this);
    }

    public String getModel() {
        return model;
    }

    public ApplianceType getType() {
        return type;
    }

    public double getEmbodiedEmissions() {
        return embodiedEmissions;
    }

    public double getPowerConsumption() {
        return powerConsumption;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setType(ApplianceType type) {
        this.type = type;
    }

    public void setEmbodiedEmissions(double embodiedEmissions) {
        this.embodiedEmissions = embodiedEmissions;
    }

    public void setPowerConsumption(double powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public void setMode(UsageMode mode) {
        this.mode = mode;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public UsageMode getUsageMode() {
        return mode;
    }
}