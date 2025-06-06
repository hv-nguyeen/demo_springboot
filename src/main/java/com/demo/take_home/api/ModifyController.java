package com.demo.take_home.api;

import com.demo.take_home.application.HouseFacade;
import com.demo.take_home.domain.Room;
import com.demo.take_home.domain.Appliance;
import com.demo.take_home.domain.enums.Region;
import com.demo.take_home.domain.enums.UsageMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/modify")
public class ModifyController {

    @Autowired
    private HouseFacade facade;

    @PutMapping("/appliance")
    public String modifyAppliance(
            @RequestParam String roomName,
            @RequestParam String applianceModel,
            @RequestParam String attribute,
            @RequestParam String value
    ) {
        Room queryRoom = facade.getRoom(roomName);
        if (queryRoom == null) {
            return "Room does not exist";
        }
        Appliance queryAppliance = queryRoom.getAppliance(applianceModel);
        if (queryAppliance == null) {
            return "Appliance does not exist";
        }
        if (queryAppliance.getUsageMode() == UsageMode.ALWAYS_ON &&
                (attribute.equalsIgnoreCase("start") || attribute.equalsIgnoreCase("end"))) {
            return "You cannot change the start or end time of an ALWAYS_ON appliance.";
        }
        if (!facade.modifyApplianceAttribute(roomName, applianceModel, attribute, value)) {
            return "Something went wrong, you most likely wrote one of the fields wrong, try again";
        }
        return "Appliance " + applianceModel + " successfully modified";
    }

    @PutMapping("/region")
    public String modifyRegion(@RequestParam String regionName) {
        try {
            facade.setRegion(Region.valueOf(regionName));
            return "House region successfully set to: " + regionName;
        } catch (IllegalArgumentException e) {
            return "Invalid region name";
        }
    }
}