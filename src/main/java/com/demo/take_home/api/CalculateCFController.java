package com.demo.take_home.api;

import com.demo.take_home.application.HouseFacade;
import com.demo.take_home.domain.Appliance;
import com.demo.take_home.domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculate")
public class CalculateCFController {

    @Autowired
    private HouseFacade facade;

    @GetMapping("/appliance")
    public String calculateApplianceCF(@RequestParam String roomName, @RequestParam String applianceModel) {
        Appliance appliance = facade.getApplianceFromRoom(roomName, applianceModel);
        if (appliance == null) {
            return "Appliance or Room does not exist";
        }
        double result = appliance.calculateCF();
        return "The carbon footprint of appliance " + applianceModel + " is: " + result;
    }

    @GetMapping("/room")
    public String calculateRoomCF(@RequestParam String roomName) {
        Room room = facade.getRoom(roomName);
        if (room == null) {
            return "Room does not exist";
        }
        double result = room.calculateCF();
        return "The carbon footprint of the room " + roomName + " is: " + result;
    }

    @GetMapping("/house")
    public String calculateHouseCF() {
        double result = facade.calculateHouseCF();
        return "The carbon footprint of the house is: " + result;
    }
}