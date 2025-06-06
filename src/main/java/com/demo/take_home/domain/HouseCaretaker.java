package nl.vu.cs.softwaredesign.domain;

public class HouseCaretaker {
    private House.HouseMemento savedState = null;

    public boolean saveState(House house) {
        if (savedState != null) {
            return false;
        }
        savedState = house.save();
        return true;
    }

    public boolean restoreState() {
        House house = House.getInstance();
        if (savedState == null) {
            return false;
        }
        house.restore(savedState);
        savedState = null;
        return true;
    }
}
