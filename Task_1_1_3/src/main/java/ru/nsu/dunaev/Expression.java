package ru.nsu.dunaev;

public abstract class Expression {
    public abstract void print();
    public abstract Expression derivative(String variableName);
    public abstract int eval(String variableAssignments);
}