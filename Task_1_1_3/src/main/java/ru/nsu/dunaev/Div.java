package ru.nsu.dunaev;

// Class for Div expression
public class Div extends Expression {
    private final Expression left;
    private final Expression right;

    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + "/" + right + ")";
    }

    @Override
    public Expression derivative(String var) {
        return new Sub(new Div(left.derivative(var), right), new Div(left, right.derivative(var)));
    }

    @Override
    public int eval(String variables) {
        try {
            return left.eval(variables) / right.eval(variables);
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}
