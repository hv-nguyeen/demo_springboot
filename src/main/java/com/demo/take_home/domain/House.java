package com.demo.take_home.domain;



import com.demo.take_home.domain.enums.Region;
import com.demo.take_home.service.CarbonFootprintCalculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class House implements Emissions, Iterable<Room> {
    private Region regionName = Region.NL;
    private List<Room> roomList;
    private static House instance;

    private House() {
        this.roomList = new ArrayList<>();

    }

    public static House getInstance() {
        if (instance == null) {
            instance = new House();
        }
        return instance;
    }

    public House deepCopy() {
        return new House(this);
    }

    private House(House other) {
        this.regionName = other.regionName;
        this.roomList = new ArrayList<>();


        for (Room room : other.roomList) {
            roomList.add(new Room(room));
        }
    }

    public class HouseMemento {
        private final House houseState;

        private HouseMemento(House house) {
            this.houseState = house.deepCopy();
        }

        private House getSavedState() {
            return houseState.deepCopy();
        }
    }

    public HouseMemento save() {
        return new HouseMemento(new House(this));
    }

    public void restore(HouseMemento memento) {
        if (memento != null) {
            House restoredHouse = memento.getSavedState();
            this.regionName = restoredHouse.regionName;
            this.roomList = restoredHouse.roomList;
        }
    }

    @Override
    public double calculateCF() {
        return CarbonFootprintCalculator.calculateHouseCF(this);
    }

    @Override
    public Iterator<Room> iterator() {
        return new EmissionIterator<>(roomList);
    }

    public boolean addRoom(String roomName) {
        if (getRoom(roomName) != null) {
            return false;
        }
        roomList.add(new Room(roomName));
        return true;
    }

    public boolean removeRoom(String roomName) {
        Room foundRoom = getRoom(roomName);

        if (foundRoom == null) {
            return false;
        }
        roomList.remove(foundRoom);
        return true;
    }

    public Room getRoom(String roomName) {
        for (Room room : roomList) {
            if (room.getName().equals(roomName)) {
                return room;
            }
        }
        return null;
    }

    public List<Room> getRoomList() {
        List<Room> deepCopy = new ArrayList<>();
        for (Room room : roomList) {
            deepCopy.add(new Room(room));
        }
        return deepCopy;
    }

    public List<Appliance>  getAllAppliances() {
        List<Appliance> allAppliances = new ArrayList<>();

        for (Room room : this) {
            allAppliances.addAll(room.getApplianceList());
        }
        return allAppliances;
    }

    public Region getRegionName() {
        return regionName;
    }

    public void setRegionName(Region regionName) {
        this.regionName = regionName;
    }
}