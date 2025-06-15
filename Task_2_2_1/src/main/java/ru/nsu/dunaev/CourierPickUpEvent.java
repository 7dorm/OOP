package ru.nsu.dunaev;

import ru.nsu.dunaev.Courier;
import ru.nsu.dunaev.Event;
import ru.nsu.dunaev.Order;
import ru.nsu.dunaev.Simulation;

import java.util.List;

public class CourierPickUpEvent implements Event {
    private final int time;
    private final Courier courier;

    public CourierPickUpEvent(int time, Courier courier) {
        this.time = time;
        this.courier = courier;
    }

    @Override
    public int getTime() { return time; }

    @Override
    public void execute(Simulation simulation) {
        if (!simulation.getWarehouse().isEmpty()) {
            List<Order> pizzas = simulation.getWarehouse().takePizzas(courier.getCapacity());
            for (Order pizza : pizzas) {
                pizza.setState(OrderState.DELIVERED);
                System.out.println("[" + pizza.getId() + "] Delivered");
            }
            courier.setBusy(false);
            simulation.tryReleaseWaitingBakers();
            simulation.tryStartCourier();
        }
    }
}