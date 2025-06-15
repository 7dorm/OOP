package ru.nsu.dunaev;

import ru.nsu.dunaev.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Simulation {
    private final List<Baker> bakers;
    private final List<Courier> couriers;
    private final Warehouse warehouse;
    private final Queue<Order> orderQueue;
    private final PriorityQueue<Event> eventQueue;
    private final List<Baker> waitingBakers;
    private final int simulationTime;
    private int currentOrderId = 1;

    public Simulation(String configFile) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(configFile)));
        JSONObject config = new JSONObject(content);

        bakers = new ArrayList<>();
        JSONArray bakersArray = config.getJSONArray("bakers");
        for (int i = 0; i < bakersArray.length(); i++) {
            bakers.add(new Baker(bakersArray.getJSONObject(i).getInt("speed")));
        }

        couriers = new ArrayList<>();
        JSONArray couriersArray = config.getJSONArray("couriers");
        for (int i = 0; i < couriersArray.length(); i++) {
            couriers.add(new Courier(couriersArray.getJSONObject(i).getInt("capacity")));
        }

        warehouse = new Warehouse(config.getInt("warehouse_capacity"));
        simulationTime = config.getInt("simulation_time");
        int orderInterval = config.getInt("order_interval");

        orderQueue = new LinkedList<>();
        eventQueue = new PriorityQueue<>();
        waitingBakers = new ArrayList<>();

        for (int time = 0; time < simulationTime; time += orderInterval) {
            eventQueue.add(new OrderArrivalEvent(time, new Order(currentOrderId++)));
        }
    }

    public void addEvent(Event event) { eventQueue.add(event); }
    public void addOrder(Order order) { orderQueue.add(order); tryStartBaker(); }
    public Order takeOrder() { return orderQueue.poll(); }
    public Warehouse getWarehouse() { return warehouse; }
    public void addWaitingBaker(Baker baker) { waitingBakers.add(baker); }

    public void tryStartBaker() {
        for (Baker baker : bakers) {
            if (!baker.isBusy() && !orderQueue.isEmpty()) {
                addEvent(new BakerStartBakingEvent(eventQueue.peek().getTime(), baker));
                break;
            }
        }
    }

    public void tryStartCourier() {
        for (Courier courier : couriers) {
            if (!courier.isBusy() && !warehouse.isEmpty()) {
                courier.setBusy(true);
                addEvent(new CourierPickUpEvent(eventQueue.peek().getTime(), courier));
                break;
            }
        }
    }

    public void tryReleaseWaitingBakers() {
        int freeSpace = warehouse.getFreeSpace();
        Iterator<Baker> iterator = waitingBakers.iterator();
        while (iterator.hasNext() && freeSpace > 0) {
            Baker baker = iterator.next();
            addEvent(new BakerStorePizzaEvent(eventQueue.peek().getTime(), baker));
            iterator.remove();
            freeSpace--;
        }
    }

    public void run() {
        tryStartBaker();
        tryStartCourier();

        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.poll();
            event.execute(this);
        }
    }
}