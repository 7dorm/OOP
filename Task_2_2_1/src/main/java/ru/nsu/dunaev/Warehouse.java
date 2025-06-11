package ru.nsu.dunaev;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private final int capacity;
    private final List<Order> pizzas;

    public Warehouse(int capacity) {
        this.capacity = capacity;
        this.pizzas = new ArrayList<>();
    }

    public boolean isFull() { return pizzas.size() >= capacity; }
    public boolean isEmpty() { return pizzas.isEmpty(); }
    public int getFreeSpace() { return capacity - pizzas.size(); }
    public void addPizza(Order order) { pizzas.add(order); }
    public List<Order> takePizzas(int count) {
        int actualCount = Math.min(count, pizzas.size());
        List<Order> taken = new ArrayList<>(pizzas.subList(0, actualCount));
        pizzas.subList(0, actualCount).clear();
        return taken;
    }
}