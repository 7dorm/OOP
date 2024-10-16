package ru.nsu.dunaev;

public class Variable extends Expression{
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public void print() {
        System.out.print(name);
    }

    @Override
    public Expression derivative(String variableName) {
        if (name.equals(variableName))
            return new Number(1);
        else
            return new Number(0);
    }

    @Override
    public int eval(String variableAssignments) {
        String[] assignments = variableAssignments.split(";");
        for (String assignment : assignments)
        {
            String[] parts = assignment.strip().split("=");

            if(parts[0].strip().equals(name))
                return Integer.parseInt(parts[1].strip());
            System.out.println(parts[0]);
        }
        return 0;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
