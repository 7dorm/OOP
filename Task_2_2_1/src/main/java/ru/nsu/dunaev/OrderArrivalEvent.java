package ru.nsu.dunaev;

import ru.nsu.dunaev.Event;
import ru.nsu.dunaev.Order;
import ru.nsu.dunaev.Simulation;

public class OrderArrivalEvent implements Event {
    private final int time;
    private final Order order;

    public OrderArrivalEvent(int time, Order order) {
        this.time = time;
        this.order = order;
    }

    @Override
    public int getTime() { return time; }

    @Override
    public void execute(Simulation simulation) {
        simulation.addOrder(order);
        System.out.println("[" + order.getId() + "] Received");
    }
}