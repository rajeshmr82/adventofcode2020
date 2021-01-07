import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc18.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        int earliestTime = -1;
        List<String> input=new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        System.out.println("test 1 - " + aoc.evaluateExpression("2 * 3 + (4 * 5)")); //26
        System.out.println("test 2 - " + aoc.evaluateExpression("5 + (8 * 3 + 9 + 3 * 4 * 3)")); //437
        System.out.println("test 3 - " + aoc.evaluateExpression("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")); //12240
        System.out.println("test 4 - " + aoc.evaluateExpression("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")); //13632
        //Part 1
        System.out.println("Part 1 - " + aoc.findSumOfExpression(input));
        //Part 2
        aoc = new AOC(true);
        System.out.println("test 5 - " + aoc.evaluateExpression("1 + (2 * 3) + (4 * (5 + 6))")); //51
        System.out.println("test 6 - " + aoc.evaluateExpression("2 * 3 + (4 * 5)"));//46
        System.out.println("test 7 - " + aoc.evaluateExpression("5 + (8 * 3 + 9 + 3 * 4 * 3)")); //1445
        System.out.println("test 8 - " + aoc.evaluateExpression("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")); //669060
        System.out.println("test 9 - " + aoc.evaluateExpression("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")); //23340
        System.out.println("Part 2 - " + aoc.findSumOfExpression(input));
    }
}

class AOC {
    private boolean changePrecedence;
    public AOC() {

    }
    public AOC(boolean changePrecedence) {
        this.changePrecedence = changePrecedence;
    }


    interface Operator{
        public long operate(long a, long b);
    }

    class Add implements  Operator{
        @Override
        public long operate(long a, long b) {
            return a+b;
        }
    }

    class Multiply implements  Operator {
        @Override
        public long operate(long a, long b) {
            return a*b;
        }
    }

    class Expression{
        private List<Long> operandList = new ArrayList<>();
        private List<Operator> operatorList = new ArrayList<>();

        public void addOperands(long operand){
            operandList.add(operand);
        }

        public long popOperand(){
            return operandList.remove(operandList.size()-1);
        }

        public void addOperator(Operator operator){
            operatorList.add(operator);
        }

        public long evaluate(){
            long result=0;
            if(!changePrecedence) {
                result = this.operandList.get(0);
                for (int i = 1; i < this.operandList.size(); i++) {
                    result = operatorList.get(i - 1).operate(result, operandList.get(i));
                }
            }else{

                for (int i = 1; i < this.operandList.size(); i++) {
                    if(operatorList.get(i-1).getClass().equals(Add.class)){
                        long prev = this.operandList.get(i-1);
                        long curr = this.operandList.get(i);
                        long newValue = new Add().operate(prev,curr);
                        operandList.remove(i);
                        this.operandList.set(i-1,newValue);
                        operatorList.remove(i-1);
                        i--;
                    }
                }
                result = this.operandList.get(0);
                for (int i = 1; i < this.operandList.size(); i++) {
                    result = new Multiply().operate(result, operandList.get(i));
                }
            }
            return result;
        }
    }
    public long findSumOfExpression(List<String> input){
        long result = 0;
        for (String expression:
             input) {
            result += evaluateExpression(expression);
        }
        return result;
    }

    public long evaluateExpression(String expression) {
        Matcher matcher = Pattern.compile("(\\d+|[+*()])").matcher(expression.replaceAll("\\s+", ""));

        Expression lineExp = new Expression();
        Stack<Expression> stack = new Stack<>();
        stack.push(lineExp);
        while (matcher.find()){
            String token =matcher.group(1);

            switch (token){
                case "(":
                    stack.push(new Expression());
                    break;
                case ")":
                    Expression innerExpr = stack.pop();
                    stack.peek().addOperands(innerExpr.evaluate());
                    break;
                case "+":
                    stack.peek().addOperator(new Add());
                    break;
                case "*":
                    stack.peek().addOperator(new Multiply());
                    break;
                default:
                    stack.peek().addOperands(Long.parseLong(token));
                    break;
            }
        }

       return stack.pop().evaluate();
    }
}
