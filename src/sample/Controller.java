package sample;

import javafx.scene.control.TreeItem;
import java.text.ParseException;
import java.util.Collections;
import java.util.Stack;
import java.util.StringTokenizer;

public class Controller
{
    private Model model = new Model(); private final String[] FUNCTIONS = {"pow", "sqrt", "inverse", "%"};
    private final String OPERATORS = "+-*/"; private final String SEPARATOR = ","; private final String VARIABLE = "var";
    private Stack<String> stackOperations = new Stack<String>();
    private Stack<String> stackRPN = new Stack<String>();
    private Stack<String> stackAnswer = new Stack<String>();
    private Stack<TreeItem<String>> stackTree = new Stack<>();
    private TreeItem<String> tree = new TreeItem<>();

    public String getFormula() { return this.model.getFormula(); }
    public void addSymbol(String symbolToAdd) { this.model.addSymbol(symbolToAdd); }
    public void clearFormula() { this.model.clearFormula(); }
    public void removeLast() { this.model.removeLast(); }
    public void convert() { try { parse(getFormula()); } catch (ParseException e) { e.printStackTrace(); } }

    private boolean isNumber(String token)
    {
        try { Double.parseDouble(token); } catch (Exception e)
        {
            if (token.equals(VARIABLE)) { return true; }
            return false;
        }
        return true;
    }
    private boolean isFunction(String token)
    {
        for (String item : FUNCTIONS)
        {
            if (item.equals(token)) { return true; }
        }
        return false;
    }

    private boolean isSeparator(String token) { return token.equals(SEPARATOR); }
    private boolean isOpenBracket(String token) { return token.equals("("); }
    private boolean isCloseBracket(String token) { return token.equals(")"); }
    private boolean isOperator(String token) { return OPERATORS.contains(token); }
    private byte getPrecedence(String token)
    {
        if (token.equals("+") || token.equals("-")) { return 1; }
        return 2;
    }
    public Stack<String> parse(String expression) throws ParseException
    {
        stackOperations.clear(); stackRPN.clear();
        expression = expression.replace("(-", "(0-");
        expression = expression.replace(",-", ",0-");
        if (expression.charAt(0) == '-') { expression = "0" + expression; }
        StringTokenizer stringTokenizer = new StringTokenizer(expression,OPERATORS + SEPARATOR + "()", true);
        while (stringTokenizer.hasMoreTokens())
        {
            String token = stringTokenizer.nextToken();
            if (isSeparator(token))
            {
                while (!stackOperations.empty() && !isOpenBracket(stackOperations.lastElement())) { stackRPN.push(stackOperations.pop()); }
            }
            else if (isOpenBracket(token)) { stackOperations.push(token); }
            else if (isCloseBracket(token))
            {
                while (!stackOperations.empty() && !isOpenBracket(stackOperations.lastElement())) { stackRPN.push(stackOperations.pop()); }
                stackOperations.pop();
                if (!stackOperations.empty() && isFunction(stackOperations.lastElement())) { stackRPN.push(stackOperations.pop()); }
            }
            else if (isNumber(token)) { stackRPN.push(token); }
            else if (isOperator(token))
            {
                while (!stackOperations.empty() && isOperator(stackOperations.lastElement()) && getPrecedence(token) <= getPrecedence(stackOperations.lastElement())) { stackRPN.push(stackOperations.pop()); }
                stackOperations.push(token);
            }
            else if (isFunction(token)) { stackOperations.push(token); }
        }
        while (!stackOperations.empty()) { stackRPN.push(stackOperations.pop()); }
        System.out.println(stackRPN); Collections.reverse(stackRPN); return stackRPN;
    }

    public void calculate() throws ParseException
    {
        while (!stackRPN.empty())
        {
            if(isNumber(stackRPN.peek()))
            {
                stackAnswer.push(stackRPN.pop()); stackTree.push(new TreeItem<>(stackAnswer.peek()));
            }
            else if (isFunction(stackRPN.peek()))
            {
                switch (stackRPN.pop())
                {
                    case "sqrt":
                        {
                        String result = sqrt(stackAnswer.pop()); stackAnswer.push(result);
                        TreeItem<String> sqrTree = new TreeItem<>(result+"(sqrt)");
                        sqrTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        stackTree.push(sqrTree); break;
                    }
                    case "pow":
                        {
                        String power = stackAnswer.pop(); String number = stackAnswer.pop();
                        String result = pow(number, power); stackAnswer.push(result);
                        TreeItem<String> powTree = new TreeItem<>(result+"(pow)");
                        powTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        powTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        stackTree.push(powTree); break;
                    }
                    case "inverse":
                        {
                        String result = inverse(stackAnswer.pop()); stackAnswer.push(result);
                        TreeItem<String> inverseTree = new TreeItem<>(result+"(inverse)");
                        inverseTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        stackTree.push(inverseTree); break;
                    }
                    case "%":
                        {
                        String result = percent(stackAnswer.pop()); stackAnswer.push(result);
                        TreeItem<String> percentTree = new TreeItem<>(result+"(%)");
                        percentTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        stackTree.push(percentTree); break;
                    }
                }
            }
            else if (isOperator(stackRPN.peek()))
            {
                switch (stackRPN.pop())
                {
                    case "+":
                        {
                        String secondNumber = stackAnswer.pop(); String firstNumber = stackAnswer.pop();
                        String result = plus(firstNumber, secondNumber); stackAnswer.push(result);
                        TreeItem<String> plusTree = new TreeItem<>(result+"(+)");
                        plusTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        plusTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        stackTree.push(plusTree); break;
                    }
                    case "-":
                        {
                        String secondNumber = stackAnswer.pop(); String firstNumber = stackAnswer.pop();
                        String result =minus(firstNumber, secondNumber); stackAnswer.push(result);
                        TreeItem<String> minusTree = new TreeItem<>(result+"(-)");
                        minusTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        minusTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        stackTree.push(minusTree); break;
                    }
                    case "*":
                        {
                        String secondNumber = stackAnswer.pop(); String firstNumber = stackAnswer.pop();
                        String result =multiply(firstNumber, secondNumber); stackAnswer.push(result);
                        TreeItem<String> multiplyTree = new TreeItem<>(result+"(*)");
                        multiplyTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        multiplyTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        stackTree.push(multiplyTree); break;
                    }
                    case "/":
                        {
                        String secondNumber = stackAnswer.pop(); String firstNumber = stackAnswer.pop();
                        String result =divison(firstNumber, secondNumber); stackAnswer.push(result);
                        TreeItem<String> divisionTree = new TreeItem<>(result+"(/)");
                        divisionTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        divisionTree.getChildren().add(stackTree.peek()); stackTree.pop();
                        stackTree.push(divisionTree); break;
                    }
                }
            }
        }
    }
    public String sqrt(String number) { Double result = Math.sqrt(Double.parseDouble(number)); return result.toString(); }
    public String pow(String number, String power) { Double result = Math.pow(Double.parseDouble(number), Double.parseDouble(power)); return result.toString(); }
    public String inverse(String number) { Double result = 1.0 / Double.parseDouble(number); return result.toString(); }
    public String percent(String number) { Double result = Double.parseDouble(number) / 100; return result.toString(); }
    public String plus(String firstNumber, String secondNumber) { Double result = Double.parseDouble(firstNumber) + Double.parseDouble(secondNumber); return result.toString(); }
    public String minus(String firstNumber, String secondNumber) { Double result = Double.parseDouble(firstNumber) - Double.parseDouble(secondNumber); return result.toString(); }
    public String multiply(String firstNumber, String secondNumber) { Double result = Double.parseDouble(firstNumber) * Double.parseDouble(secondNumber); return result.toString(); }
    public String divison(String firstNumber, String secondNumber) { Double result = Double.parseDouble(firstNumber) / Double.parseDouble(secondNumber); return result.toString(); }
    public String getResult(){ return stackAnswer.peek(); }
    public TreeItem getTree(){ return stackTree.peek(); }
}