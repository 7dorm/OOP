package ru.nsu.dunaev;

public class Mul extends Expression{
    private Expression left, right;

    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("*");
        right.print();
        System.out.print(")");
    }

    @Override
    public Expression derivative(String variableName) {
        return new Add(new Mul(left.derivative(variableName), right),
                new Mul(left, right.derivative(variableName)));
    }

    @Override
    public int eval(String variableAssignments) {
        return left.eval(variableAssignments)*right.eval(variableAssignments);
    }
    @Override
    public String toString() {
        return "("+this.left.toString()+"*"+this.right.toString()+")";
    }
}
