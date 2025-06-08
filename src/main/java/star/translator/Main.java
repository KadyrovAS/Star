package star.translator;

import star.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(93972, "DEPOSIT", "CREDIT"));
        transactions.add(new Transaction(77155, "DEPOSIT", "DEBIT"));
        transactions.add(new Transaction(175383, "DEPOSIT", "SAVING"));
        transactions.add(new Transaction(97219, "WITHDRAW", "CREDIT"));
        transactions.add(new Transaction(76165, "WITHDRAW", "DEBIT"));
        transactions.add(new Transaction(45627, "WITHDRAW", "SAVING"));


        List<String>rules = new ArrayList<>();
        rules.add("AMOUNT (DEBIT) > 0");
        rules.add("AMOUNT (INVEST) = 0");
        rules.add("DEPOSIT (SAVING) > 1000");
        rules.add("DIFFERENCE (DEBIT) >= 50000");

        Evaluate evaluate = new Evaluate(transactions);

//        evaluate.toEvaluate("(DEPOSIT (DEBIT, CREDIT) > DEPOSIT(CREDIT) AND WITHDRAW(DEBIT) < DEPOSIT(SAVING))");

//        System.out.println(evaluate.toEvaluate(rules.get(0)));
//        for (String rule: rules){
//            System.out.println("rule: " + rule);
//            System.out.println(evaluate.toEvaluate(rule));
//        }

        String line;
        line = "(20 > 4  AND 11 < 10 OR 5 < 3)";

        boolean res = evaluate.toEvaluate(line);
        System.out.println(res);
    }
}
