package com.demo.take_home.api;

import com.demo.take_home.application.HouseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delete")
public class DeleteController {

    @Autowired
    private HouseFacade facade;

    @DeleteMapping("/room")
    public String deleteRoom(@RequestParam String roomName) {
        if (facade.removeRoom(roomName)) {
            return "Room: " + roomName + " successfully removed";
        }
        return "Room does not exist";
    }

    @DeleteMapping("/appliance")
    public String deleteAppliance(
            @RequestParam String roomName,
            @RequestParam String applianceModel
    ) {
        if (facade.removeApplianceFromRoom(roomName, applianceModel)) {
            return "Deleted Appliance " + applianceModel;
        }
        return "Room or appliance does not exist";
    }
}