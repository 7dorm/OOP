package ru.nsu.dunaev;


import ru.nsu.dunaev.Baker;
import ru.nsu.dunaev.Event;
import ru.nsu.dunaev.Simulation;

public class BakerFinishBakingEvent implements Event {
    private final int time;
    private final Baker baker;

    public BakerFinishBakingEvent(int time, Baker baker) {
        this.time = time;
        this.baker = baker;
    }

    @Override
    public int getTime() { return time; }

    @Override
    public void execute(Simulation simulation) {
        simulation.addEvent(new BakerTryStorePizzaEvent(time, baker));
    }
}