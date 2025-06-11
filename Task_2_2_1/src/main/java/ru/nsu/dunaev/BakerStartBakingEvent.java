package ru.nsu.dunaev;


import ru.nsu.dunaev.Baker;
import ru.nsu.dunaev.Event;
import ru.nsu.dunaev.Order;
import ru.nsu.dunaev.Simulation;

public class BakerStartBakingEvent implements Event {
    private final int time;
    private final Baker baker;

    public BakerStartBakingEvent(int time, Baker baker) {
        this.time = time;
        this.baker = baker;
    }

    @Override
    public int getTime() { return time; }

    @Override
    public void execute(Simulation simulation) {
        Order order = simulation.takeOrder();
        if (order != null) {
            baker.setBusy(true);
            baker.setCurrentOrder(order);
            order.setState(OrderState.BAKING);
            System.out.println("[" + order.getId() + "] Baking");
            simulation.addEvent(new BakerFinishBakingEvent(time + baker.getSpeed(), baker));
        }
    }
}