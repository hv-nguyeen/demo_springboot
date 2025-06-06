package com.demo.take_home.api;

import com.demo.take_home.application.HouseFacade;
import com.demo.take_home.domain.Appliance;
import com.demo.take_home.domain.enums.ApplianceType;
import com.demo.take_home.domain.enums.UsageMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/add")
public class AddController {

    @Autowired
    private HouseFacade facade;

    @PostMapping("/room")
    public String addRoom(@RequestParam String roomName) {
        if (!facade.addRoom(roomName)) {
            return "Room with this name already exists.";
        }
        return "Added a new room: " + roomName;
    }

    @PostMapping("/appliance")
    public String addAppliance(
            @RequestParam String roomName,
            @RequestParam String applianceModel,
            @RequestParam ApplianceType applianceType,
            @RequestParam double embodiedEmissions,
            @RequestParam double powerConsumption,
            @RequestParam UsageMode mode,
            @RequestParam(required = false) Integer startTime,
            @RequestParam(required = false) Integer endTime
    ) {
        if (facade.getRoom(roomName) == null) {
            return "Room '" + roomName + "' does not exist. Please add the room first.";
        }

        if (mode == UsageMode.HOURLY) {
            if (startTime == null || endTime == null || startTime < 0 || startTime > 23 || endTime < 1 || endTime > 24 || startTime >= endTime) {
                return "Invalid start or end time. Start must be 0-23, end must be 1-24, and start < end.";
            }
        } else {
            startTime = 0;
            endTime = 23;
        }

        Appliance newAppliance = new Appliance(applianceModel, applianceType, embodiedEmissions, powerConsumption, mode, startTime, endTime);

        if (!facade.addApplianceToRoom(roomName, newAppliance)) {
            return "Appliance with this name already exists in room: " + roomName;
        }

        return "Added appliance: " + applianceModel + " to room: " + roomName + " with usage mode: " + mode;
    }
}