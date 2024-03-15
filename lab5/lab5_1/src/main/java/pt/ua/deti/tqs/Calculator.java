package pt.ua.deti.tqs;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

public class Calculator {
    private static final List<String> OPS = asList("-", "+", "*", "/");
    private final Deque<Number> stack = new LinkedList<>();

    public void push(Object arg) {
        if (OPS.contains(arg)) {
            String argStr = (String) arg;
            Number y = stack.removeLast();
            Number x = stack.isEmpty() ? 0 : stack.removeLast();
            Double val = switch (argStr) {
                case "-" -> x.doubleValue() - y.doubleValue();
                case "+" -> x.doubleValue() + y.doubleValue();
                case "*" -> x.doubleValue() * y.doubleValue();
                case "/" -> x.doubleValue() / y.doubleValue();
                default -> null;
            };
            push(val);
        } else {
            stack.add((Number) arg);
        }
    }

    public Number value() {
        return stack.getLast();
    }
}