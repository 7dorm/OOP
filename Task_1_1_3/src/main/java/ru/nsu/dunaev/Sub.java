package ru.nsu.dunaev;

public class Sub extends Expression{
    private Expression left, right;

    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("-");
        right.print();
        System.out.print(")");
    }

    @Override
    public Expression derivative(String variableName) {
        return new Sub(left.derivative(variableName), right.derivative(variableName));
    }

    @Override
    public int eval(String variableAssignments) {
        return left.eval(variableAssignments)-right.eval(variableAssignments);
    }
    @Override
    public String toString() {
        return "("+this.left.toString()+"-"+this.right.toString()+")";
    }
}
