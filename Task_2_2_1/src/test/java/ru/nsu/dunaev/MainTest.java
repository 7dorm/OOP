package ru.nsu.dunaev;

import ru.nsu.dunaev.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    private Simulation simulation;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() throws IOException {
        System.setOut(new PrintStream(outContent));
        simulation = new Simulation("src/main/resources/config.json");
        outContent.reset();
    }

    @Test
    void testOrderCreation() {
        Order order = new Order(1);
        assertEquals(1, order.getId());
        assertEquals(OrderState.RECEIVED, order.getState());
    }

    @Test
    void testOrderSetState() {
        Order order = new Order(2);
        order.setState(OrderState.BAKING);
        assertEquals(OrderState.BAKING, order.getState());
    }

    @Test
    void testBakerInitialization() {
        Baker baker = new Baker(5);
        assertEquals(5, baker.getSpeed());
        assertFalse(baker.isBusy());
        assertNull(baker.getCurrentOrder());
    }

    @Test
    void testBakerSetBusyAndOrder() {
        Baker baker = new Baker(3);
        Order order = new Order(1);
        baker.setBusy(true);
        baker.setCurrentOrder(order);
        assertTrue(baker.isBusy());
        assertEquals(order, baker.getCurrentOrder());
    }

    @Test
    void testCourierInitialization() {
        Courier courier = new Courier(2);
        assertEquals(2, courier.getCapacity());
        assertFalse(courier.isBusy());
    }

    @Test
    void testCourierSetBusy() {
        Courier courier = new Courier(3);
        courier.setBusy(true);
        assertTrue(courier.isBusy());
        courier.setBusy(false);
        assertFalse(courier.isBusy());
    }

    @Test
    void testWarehouseInitialization() {
        Warehouse warehouse = new Warehouse(2);
        assertTrue(warehouse.isEmpty());
        assertFalse(warehouse.isFull());
        assertEquals(2, warehouse.getFreeSpace());
    }

    @Test
    void testWarehouseAddAndTakePizzas() {
        Warehouse warehouse = new Warehouse(2);
        Order order1 = new Order(1);
        Order order2 = new Order(2);

        warehouse.addPizza(order1);
        assertFalse(warehouse.isEmpty());
        assertFalse(warehouse.isFull());
        assertEquals(1, warehouse.getFreeSpace());

        warehouse.addPizza(order2);
        assertTrue(warehouse.isFull());
        assertEquals(0, warehouse.getFreeSpace());

        List<Order> taken = warehouse.takePizzas(1);
        assertEquals(1, taken.size());
        assertEquals(order1, taken.get(0));
        assertFalse(warehouse.isEmpty());
        assertEquals(1, warehouse.getFreeSpace());

        taken = warehouse.takePizzas(2);
        assertEquals(1, taken.size());
        assertEquals(order2, taken.get(0));
        assertTrue(warehouse.isEmpty());
    }

    @Test
    void testSimulationInitialization() throws IOException {
        Simulation sim = new Simulation("src/main/resources/config.json");
        assertEquals(2, sim.getWarehouse().getFreeSpace());
    }

    @Test
    void testOrderArrivalEvent() {
        Order order = new Order(1);
        Event event = new OrderArrivalEvent(0, order);
        event.execute(simulation);
        assertTrue(outContent.toString().contains("[1] Received"));
    }

    @Test
    void testBakerStartBakingEvent() {
        Baker baker = new Baker(2);
        simulation.addOrder(new Order(1));
        Event event = new BakerStartBakingEvent(0, baker);
        event.execute(simulation);
        assertTrue(outContent.toString().contains("[1] Baking"));
        assertTrue(baker.isBusy());
        assertEquals(OrderState.BAKING, baker.getCurrentOrder().getState());
    }

    @Test
    void testWarehouseFullScenario() throws IOException {
        Simulation smallSim = new Simulation("src/main/resources/config.json");
        Warehouse warehouse = smallSim.getWarehouse();
        warehouse.addPizza(new Order(1));
        warehouse.addPizza(new Order(2));

        Baker baker = new Baker(2);
        baker.setBusy(true);
        baker.setCurrentOrder(new Order(3));
        Event event = new BakerTryStorePizzaEvent(0, baker);
        event.execute(smallSim);

        assertTrue(baker.isBusy());
        assertEquals(2, warehouse.takePizzas(999).size());
    }

    @Test
    void testFullSimulationRun() {
        simulation.run();
        String output = outContent.toString();
        System.out.println("Simulation Output:\n" + output);
        assertTrue(output.contains("Received"), "Output should contain Received state");
        assertTrue(output.contains("Baking"), "Output should contain Baking state");
        assertTrue(output.contains("Ready"), "Output should contain Ready state");
    }
}