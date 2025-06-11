package ru.nsu.dunaev;

public class Order {
    private final int id;
    private OrderState state;

    public Order(int id) {
        this.id = id;
        this.state = OrderState.RECEIVED;
    }

    public int getId() { return id; }
    public OrderState getState() { return state; }
    public void setState(OrderState state) { this.state = state; }
}

