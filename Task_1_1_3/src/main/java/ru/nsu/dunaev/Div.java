package ru.nsu.dunaev;

public class Div extends Expression {
    private final Expression left;
    private final Expression right;

    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("/");
        right.print();
        System.out.print(")");
    }

    @Override
    public Expression derivative(String variableName) {
        return new Sub(new Mul(left.derivative(variableName), right),
                new Mul(left, right.derivative(variableName)));
    }

    @Override
    public int eval(String variableAssignments) {
        if (right.eval(variableAssignments) != 0)
            return left.eval(variableAssignments) / right.eval(variableAssignments);
        else {
            System.out.println("Division by 0");
            throw new ArithmeticException();
        }
    }

    @Override
    public String toString() {
        return "(" + this.left.toString() + "/" + this.right.toString() + ")";
    }
}
