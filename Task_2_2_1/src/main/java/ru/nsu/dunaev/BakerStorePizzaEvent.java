package ru.nsu.dunaev;

import ru.nsu.dunaev.Baker;
import ru.nsu.dunaev.Event;
import ru.nsu.dunaev.Order;
import ru.nsu.dunaev.Simulation;

public class BakerStorePizzaEvent implements Event {
    private final int time;
    private final Baker baker;

    public BakerStorePizzaEvent(int time, Baker baker) {
        this.time = time;
        this.baker = baker;
    }

    @Override
    public int getTime() { return time; }

    @Override
    public void execute(Simulation simulation) {
        Order order = baker.getCurrentOrder();
        if (order == null) {
            return;
        }
        order.setState(OrderState.READY);
        simulation.getWarehouse().addPizza(order);
        System.out.println("[" + order.getId() + "] Ready");
        baker.setBusy(false);
        baker.setCurrentOrder(null);
        simulation.tryStartBaker();
    }
}