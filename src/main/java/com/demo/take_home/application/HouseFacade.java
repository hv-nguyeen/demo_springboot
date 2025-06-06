package com.demo.take_home.application;

import com.demo.take_home.domain.Appliance;
import com.demo.take_home.domain.House;
import com.demo.take_home.domain.Room;
import com.demo.take_home.domain.enums.Region;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HouseFacade {
    private final House house;

    public HouseFacade() {
        this.house = House.getInstance();
    }

    public boolean addRoom(String roomName) {
        return house.addRoom(roomName);
    }

    public boolean removeRoom(String roomName) {
        return house.removeRoom(roomName);
    }

    public Room getRoom(String roomName) {
        Room queryRoom = house.getRoom(roomName);

        if (queryRoom == null) {
            return null;
        }

        // Facade will return only the copy to prevent unintended modifications from client code
        return new Room(queryRoom);
    }

    public boolean modifyApplianceAttribute(String roomName, String applianceName, String attribute, String value) {
        Room room = house.getRoom(roomName);
        if (room == null) {
            return false;
        }

        Appliance appliance = room.getAppliance(applianceName);
        if (appliance == null) {
            return false;
        }

        return room.modifyAppliance(appliance, attribute, value);
    }

    // returns a deep copy rather than a shallow one
    public List<Room> getAllRooms() {
        List<Room> deepCopy = new ArrayList<>();
        for (Room room : house.getRoomList()) {
            deepCopy.add(new Room(room));
        }
        return deepCopy;
    }

    public boolean addApplianceToRoom(String roomName, Appliance inputAppliance) {
        Room room = house.getRoom(roomName);
        if (room != null) {
            return room.addAppliance(inputAppliance);
        }
        return false;
    }

    public boolean removeApplianceFromRoom(String roomName, String applianceModel) {
        Room room = house.getRoom(roomName);
        if (room != null) {
            return room.removeAppliance(applianceModel);
        }
        return false;
    }

    public Room getRoomLocationOfAppliance(String applianceModel) {
        for (Room room : house) {
            for (Appliance appliance : room.getApplianceList()) {
                if (appliance.getModel().equalsIgnoreCase(applianceModel)) {
                    return room;
                }
            }
        }
        return null;
    }

    public Appliance getApplianceFromRoom(String roomName, String applianceName) {
        Room room = house.getRoom(roomName);
        if (room != null) {
            return room.getAppliance(applianceName);
        }
        return null;
    }

    public double calculateHouseCF() {
        return house.calculateCF();
    }

    public void setRegion(Region region) {
        house.setRegionName(region);
    }
}