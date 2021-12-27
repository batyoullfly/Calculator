
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;

public class Calculator {

    public Stack<String> infixToPostfix(String expression) {
        Stack<String> operatorStack = new Stack<>();
        Stack<String> postfixExpression = new Stack<>();

        String[] formattedExpression = formatExpression(expression);
        formattedExpression = handleNegatives(formattedExpression);

        for (String element : formattedExpression) {
            if (isNumber(element)) {
                postfixExpression.push(element);
            } else {
                if (operatorStack.isEmpty()) {
                    operatorStack.push(element);
                } else if (operatorStack.peek().equals("(")) {
                    operatorStack.push(element);
                } else if (element.equals(")")) {
                    // clear opening parenthesis
                    while (!operatorStack.peek().equals("("))
                        postfixExpression.push(operatorStack.pop());
                    operatorStack.pop();
                } else if (comparePriority(element, operatorStack.peek())) {
                    operatorStack.push(element);
                } else {
                    postfixExpression.push(operatorStack.pop());
                    operatorStack.push(element);
                }
            }
        }
        while (operatorStack.size() > 0) {
            postfixExpression.push(operatorStack.pop());
        }
        return postfixExpression;
    }

    public double evaluateExpression(Stack<String> expression) {
        Stack<String> operandStack = new Stack<>();
        for (String element : expression) {
            if (isNumber(element)) {
                operandStack.push(element);
            } else {
                double operand2 = Double.parseDouble(operandStack.pop());
                double operand1 = Double.parseDouble(operandStack.pop());
                double result;
                switch (element) {
                    case "+":
                        result = operand1 + operand2;
                        break;
                    case "-":
                        result = operand1 - operand2;
                        break;
                    case "*":
                        result = operand1 * operand2;
                        break;
                    case "/":
                        result = operand1 / operand2;
                        break;
                    default:
                        throw new RuntimeException("Invalid syntax");
                }
                operandStack.push(String.valueOf(result));
            }
        }
        return Double.parseDouble(operandStack.peek());
    }

    public double calculate(String expression) {
        checkForParenthesesMismatch(expression);
        return evaluateExpression(infixToPostfix(expression));
    }

    private String[] formatExpression(String expression) {
        String[] operators = {"+", "-", "*", "/", "(", ")"};
        expression = expression.replaceAll(" ", "");
        for (String s : operators) {
            if (expression.contains(s))
                expression = expression.replace(s, " %s ".formatted(s));
        }
        expression = expression.replaceAll("\\s+", " ");
        String[] split = expression.trim().split(" ");
        checkForUnsupportedCharacters(split);
        return split;
    }

    private String[] handleNegatives(String[] expression) {
        ArrayList<String> expressionList = new ArrayList<>(Arrays.asList(expression));
        System.out.println(expressionList);
        String firstChar = expressionList.get(0);
        String secondChar = expressionList.get(1);
        if (firstChar.equals("-") && isNumber(expressionList.get(1))) {
            expressionList.set(1, String.valueOf(Double.parseDouble(secondChar) * -1));
            expressionList.remove(0);
        }
        for (int i = expressionList.size() - 1; i > 1; i--) {
            String current = expressionList.get(i);
            String previous = expressionList.get(i - 1);
            if (isNumber(current) && previous.equals("-")) {
                if (isOperator(expressionList.get(i - 2))) {
                    expressionList.set(i, String.valueOf(Double.parseDouble(current) * -1));
                    expressionList.remove(i - 1);
                }
            }
        }
        System.out.println(expressionList);
        String[] result = new String[expressionList.size()];
        result = expressionList.toArray(result);
        return result;
    }

    private boolean isOperator(String s) {
        String[] operators = {"+", "-", "*", "/", "(", ")"};
        for (String operator : operators) {
            if (s.equals(operator)) return true;
        }
        return false;
    }

    private boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private int calcPriority(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "(":
                return 3;
            default:
                throw new RuntimeException("Invalid character");
        }
    }

    // returns true if operator 1 has higher priority than operator 2
    private boolean comparePriority(String operator1, String operator2) {
        return calcPriority(operator1) > calcPriority(operator2);
    }

    private void checkForParenthesesMismatch(String expression) {
        Stack<Character> parentheses = new Stack<>();
        try {
            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);
                if (c == '(') parentheses.push(c);
                if (c == ')') parentheses.pop();
            }
        } catch (EmptyStackException e) {
            parentheses.push(null);
        }
        if (!parentheses.isEmpty())
            throw new ArithmeticException("Mismatched parentheses");
    }

    private void checkForUnsupportedCharacters(String[] formattedExpression) {
        for (String s : formattedExpression) {
            if (!isNumber(s) && !isOperator(s))
                throw new ArithmeticException(String.format("Unsupported operand: \"%s\"", s));
        }
    }
}

/*  System.out.println(element);
    System.out.println(operatorStack);
    System.out.println(postfixExpression);
    System.out.println("---------------------------"); */