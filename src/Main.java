public class Main {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        String expression = "-4 + (-3 - (-2 - -3 * -4)) * (-5 - -2) * -3";
        //System.out.println(calc.infixToPostfix(expression));
        System.out.println(calc.calculate(expression));
    }
}
