package ru.nsu.dunaev;

public interface Event extends Comparable<Event> {
    int getTime();
    void execute(Simulation simulation);
    @Override
    default int compareTo(Event other) { return Integer.compare(this.getTime(), other.getTime()); }
}