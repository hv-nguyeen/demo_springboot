package com.demo.take_home.api;

import com.demo.take_home.application.HouseFacade;
import com.demo.take_home.domain.Appliance;
import com.demo.take_home.domain.Room;
import com.demo.take_home.domain.enums.ApplianceType;
import com.demo.take_home.persistence.ApplianceDatabaseStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/list")
public class ListController {

    @Autowired
    private HouseFacade facade;

    @GetMapping("/all-rooms")
    public List<String> listAllRooms() {
        List<String> rooms = new ArrayList<>();
        for (Room room : facade.getAllRooms()) {
            rooms.add(room.getName());
        }
        return rooms;
    }

    @GetMapping("/all-appliances")
    public Map<String, List<Appliance>> listAllAppliances() {
        Map<String, List<Appliance>> result = new HashMap<>();
        for (Room room : facade.getAllRooms()) {
            result.put(room.getName(), room.getApplianceList());
        }
        return result;
    }

    @GetMapping("/appliances")
    public Object listRoomAppliances(@RequestParam String roomName) {
        Room room = facade.getRoom(roomName);
        if (room == null) {
            return "Room does not exist";
        }
        return room.getApplianceList();
    }

    @GetMapping("/database")
    public Map<ApplianceType, List<Appliance>> listDatabaseAppliances() {
        Map<ApplianceType, List<Appliance>> result = new HashMap<>();
        for (ApplianceType type : ApplianceType.values()) {
            List<Appliance> appliances = new ArrayList<>();
            Iterator<Appliance> iterator = ApplianceDatabaseStorage.getInstance().getApplianceIteratorByType(type);
            while (iterator.hasNext()) {
                appliances.add(iterator.next());
            }
            result.put(type, appliances);
        }
        return result;
    }
}