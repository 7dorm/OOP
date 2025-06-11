package ru.nsu.dunaev;

public class Baker {
    private final int speed;
    private Order currentOrder;
    private boolean isBusy;

    public Baker(int speed) {
        this.speed = speed;
        this.isBusy = false;
    }

    public int getSpeed() { return speed; }
    public boolean isBusy() { return isBusy; }
    public void setBusy(boolean busy) { isBusy = busy; }
    public Order getCurrentOrder() { return currentOrder; }
    public void setCurrentOrder(Order order) { currentOrder = order; }
}