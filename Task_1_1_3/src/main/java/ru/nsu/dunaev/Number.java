package ru.nsu.dunaev;

public class Number extends Expression{
    private int value;

    public Number(int value) {
        this.value = value;
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    @Override
    public Expression derivative(String variableName) {
        return new Number(0);
    }

    @Override
    public int eval(String variableAssignments) {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}
