package com.demo.take_home.domain.enums;

public enum Region {
    NL("./src/main/java/nl/vu/cs/softwaredesign/resources/netherlandsCarbonIntensity.json"),
    UK("./src/main/java/nl/vu/cs/softwaredesign/resources/greatBritainCarbonIntensity.json"),
    FR("./src/main/java/nl/vu/cs/softwaredesign/resources/franceCarbonIntensity.json");

    private final String filePath;

    Region(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

}
