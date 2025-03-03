package ru.nsu.dunaev;


public class Courier {
    private final int capacity;
    private boolean isBusy;

    public Courier(int capacity) {
        this.capacity = capacity;
        this.isBusy = false;
    }

    public int getCapacity() { return capacity; }
    public boolean isBusy() { return isBusy; }
    public void setBusy(boolean busy) { isBusy = busy; }
}