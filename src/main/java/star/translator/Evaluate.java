package star.translator;

import star.model.Transaction;

import java.util.List;

/**
 *  Анализирует тип транзакции по ключевым словам: DEPOSIT, WITHDRAW, AMOUNT, DIFFERENCE
 *  DEPOSIT - операция пополнения;
 *  WITHDRAW - операция траты;
 *  AMOUNT - общие обороты;
 *  DIFFERENCE - суммирует amount для DEPOSIT и вычитает amount для WITHDRAW
 *  Например:
 *  1. DEPOSIT(SAVING) найдет сумму для транзакций DEPOSIT и типом продукта SAVING;
 *  2. AMOUNT(SAVING, CREDIT) - найдет сумму всех транзакций с типами продукта SAVING и CREDIT;
 *  3. Допускается использование следующих знаков сравнения:
 *  > - больше;
 *  < - меньше;
 *  = - равно;
 *  >= - больше или равно;
 *  <= - меньше или равно;
 *  <> - не равно.
 *  4. Допускается использование логических операций и скобки:
 *  AND - И;
 *  OR - ИЛИ.
 *
 *  Результатом логического выражения должна быть ложь или истина.
 */
public class Evaluate {
    private List<Transaction> transactions;
    private String[] keyWords = {"DEPOSIT", "WITHDRAW", "AMOUNT", "DIFFERENCE"};

    public Evaluate(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public boolean toEvaluate(String rule){
        rule = toTranslate(rule);
        rule = toSimplify(rule);
    return toCompare(rule);
    }

    private String toTranslate(String rule){
        int indStart;
        int indEnd;
        String expression;
        boolean wasFound = true;

        while (wasFound) {
            wasFound = false;
            for (String keyWord : keyWords) {
                indStart = rule.indexOf(keyWord);
                if (indStart >= 0) {
                    wasFound = true;
                    indEnd = rule.indexOf(")", indStart);
                    expression = rule.substring(indStart, indEnd + 1);
                    rule = rule.substring(0, indStart) + toCalculate(expression) + rule.substring(indEnd + 1);
                    break;
                }
            }
        }
    return rule;
    }

    private int toCalculate(String rule){
        int indStart;
        int indEnd;
        String argumentLine;
        String command;
        int amount = 0;

        indStart = rule.indexOf("(");
        indEnd = rule.indexOf(")");

        argumentLine = rule.substring(indStart + 1, indEnd);
        command = rule.substring(0, indStart).strip();
        String [] arguments = argumentLine.split(",");

        
        for (Transaction transaction: transactions){
            for (String argument: arguments){
                argument = argument.strip();
                if(transaction.transactionTypeEquals(command) && argument.equals(transaction.getProductType())){
                    if (command.equals("AMOUNT") || command.equals(transaction.getTransactionType())){
                        amount += transaction.getAmount();
                    } else if (command.equals("DIFFERENCE") && transaction.getTransactionType().equals("WITHDRAW")) {
                        amount -= transaction.getAmount();
                    } else if (command.equals("DIFFERENCE") && transaction.getTransactionType().equals("DEPOSIT")) {
                        amount += transaction.getAmount();
                    }
                }
            }
        }
        return amount;
    }

    private String toSimplify(String text){
        int indStart;
        int indEnd;
        String evaluate1;
        String evaluate2;
        while (text.contains("(")){
            int count = 1;
            indStart = text.indexOf("(");
            indEnd = indStart + 1;
            while (count > 0){
                if (text.charAt(indEnd) == '('){
                    count += 1;
                } else if (text.charAt(indEnd) == ')') {
                    count -= 1;
                }
                indEnd += 1;
            }
            text = text.substring(0, indStart) +
                    toSimplify(text.substring(indStart + 1, indEnd - 1)) +
            text.substring(indEnd);
        }


        if (text.indexOf("AND") > 0){
            indStart = text.indexOf("AND") + 3;
            indEnd = text.length();
            for (int i = indStart; i < text.length(); i++){
                if (text.startsWith("AND", i) || text.startsWith("OR", i)){
                    indEnd = i;
                    break;
                }
            }
            evaluate1 = text.substring(indStart, indEnd);

            indStart = 0;
            for (int i = indStart - 4; i >= 0; i--){
                if (text.startsWith("AND", i) ){
                    indStart = i + 3;
                    break;
                } else if (text.startsWith("OR", i)) {
                    indStart = i + 2;
                    break;
                }
            }
            evaluate2 = text.substring(indStart, text.indexOf("AND"));

            text = text.substring(0, indStart) +
            checkCondition(evaluate1, evaluate2, "AND") +
                    text.substring(indEnd);
            text = toSimplify(text);
        }

        if (text.indexOf("OR") > 0){
            indStart = text.indexOf("OR") + 3;
            indEnd = text.length();
            for (int i = indStart; i < text.length(); i++){
                if (text.startsWith("OR", i)){
                    indEnd = i;
                    break;
                }
            }
            evaluate1 = text.substring(indStart, indEnd);

            indStart = 0;
            for (int i = indStart - 4; i >= 0; i--){
                if (text.startsWith("OR", i)) {
                    indStart = i + 2;
                    break;
                }
            }
            evaluate2 = text.substring(indStart, text.indexOf("OR"));

            text = text.substring(0, indStart) +
                    checkCondition(evaluate1, evaluate2, "OR") +
                    text.substring(indEnd);
            text = toSimplify(text);
        }

        return text;
    }

    private String checkCondition(String evaluate1, String evaluate2, String operation){
        boolean arg1 = toCompare(evaluate1);
        boolean arg2 = toCompare(evaluate2);

        if (operation.equals("AND") && arg1 && arg2){
            return "true";
        }else if(operation.equals("OR") && (arg1 || arg2)){
            return "true";
        }
        return "false";
    }

    private boolean toCompare(String evaluate){
        String[] operations = {"<>", ">=", "<=", "<", ">", "="};
        if (evaluate.strip().equals("true")){
            return true;
        }
        if (evaluate.strip().equals("false")){
            return false;
        }

        for(String op: operations){
            String[] args = evaluate.split(op);
            if (args.length == 2){
                int arg1 = Integer.parseInt(args[0].strip());
                int arg2 = Integer.parseInt(args[1].strip());
                switch (op) {
                    case ">": {
                        return arg1 > arg2;
                    }
                    case "<": {
                        return arg1 < arg2;
                    }
                    case "=": {
                        return arg1 == arg2;
                    }
                    case "<>": {
                        return arg1 != arg2;
                    }
                    case ">=": {
                        return arg1 >= arg2;
                    }
                    case "<=": {
                        return arg1 <= arg2;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Evaluate{" +
                "transactions=" + transactions +
                '}';
    }
}
