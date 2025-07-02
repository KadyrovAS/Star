package star.part01.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import star.part01.model.Recommendation;
import star.part01.model.Recommendations;
import star.part01.model.Rule;
import star.part01.model.Transaction;
import star.part01.repository.StarRepositoryPart01;

import java.util.*;

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

@Component("recommendationsChoice")

public class RecommendationsChoice implements RecommendationRuleSet {
    private final StarRepositoryPart01 repository;
    private List<Transaction> transactions;
    private String[] keyWords = {"DEPOSIT", "WITHDRAW", "AMOUNT", "DIFFERENCE"};
    private static final Logger logger = LoggerFactory.getLogger(RecommendationsChoice.class);

    public RecommendationsChoice(StarRepositoryPart01 repository) {
        this.repository = repository;
    }

    /**
     *
     * @param id Идентификатор клиент банка
     * @return Возвращает список рекомендаций
     */
    @Override
    public Optional<Recommendations> getRecommendation(UUID id) {
        Map<UUID, Boolean> mapWithResult = new HashMap<>();
        List<Recommendation> recommendations = new LinkedList<>();

        this.transactions = repository.getAmountsByTypes(id);

        for (Rule rule: repository.getAllRules()){
            mapWithResult.put(rule.getId(), toEvaluate(rule.getInstruction()));
        }

        for (Recommendation recommendation : repository.getRecommendations()) {
            boolean isRule = true;
            for (UUID uuid : repository.getRulesById(recommendation.getId())) {
                if (!mapWithResult.get(uuid)) {
                    isRule = false;
                    break;
                }
            }
            if (isRule) {
                recommendations.add(recommendation);
            }
        }

        return Optional.of(new Recommendations(id, recommendations));
    }


    public boolean toEvaluate(String rule){
        rule = toTranslate(rule);
        rule = toSimplify(rule);
    return toCompare(rule);
    }

    /**
     * Рассчитывает значение всех функций. Перечень операторов содержится в массиве keyWords.
     * Вместо функций с аргументами в текст подставляется рассчитанное значение.
     * Например, имеется строка: "AMOUNT(CREDIT) > 10". Результат AMOUNT(CREDIT) = 100. После
     * получения результата строка будет преобразована в "100 > 10"
     * @param rule Текст банковского правила, формализованного с помощью разработанного языка
     * @return
     */
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

    /**
     * Вычисляет обороты по счетам клиента в зависимости от типа транзакции и типа товара.
     *
     * @param rule формализованная инструкция правила, например: DEPOSIT(CREDIT). Рассчитает сумму транзакций с типом
     *             DEPOSIT в отношении продуктов с типом CREDIT.
     *             AMOUNT(CREDIT, SAVE) - посчитает сумму всех транзакций в отношении продуктов с типами: CREDIT и SAVE.
     * @return обороты по счетам, в соответствии с правилом
     */
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

    /**
     * Вычисляет сложное выражение по частям. Рекурсивно сначала раскрывает скобки, затем выполняет операцию AND и
     * в конце OR.
     * Вместо выражений в скобках в текст подставляется true или false
     * @param text Выражение, которое необходимо упростить: раскрыть скобки, выполнить операцию AND или OR
     * @return результат false или true
     */
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

        String[] operators = {"AND", "OR"};
        for (String op: operators) {
            if (text.indexOf(op) > 0) {
                indStart = text.indexOf(op) + op.length();
                indEnd = text.length();
                for (int i = indStart; i < text.length(); i++) {
                    if (text.startsWith(operators[0], i) || text.startsWith(operators[1], i)) {
                        indEnd = i;
                        break;
                    }
                }
                evaluate1 = text.substring(indStart, indEnd);

                indStart = 0;
                for (int i = indStart - op.length() - 1; i >= 0; i--) {
                    if (text.startsWith(op, i)) {
                        indStart = i + op.length();
                        break;
                    }
                }
                evaluate2 = text.substring(indStart, text.indexOf(op));

                text = text.substring(0, indStart) +
                        checkCondition(evaluate1, evaluate2, op) +
                        text.substring(indEnd);
                text = toSimplify(text);
            }

        }

        return text;
    }

    /**
     * На вход поступает два логических выражения, соединенных логическим оператором AND или OR
     * Результатом является
     * @param evaluate1 Логическое выражение 1
     * @param evaluate2 Логическое выражение 2
     * @param operation Логический оператор
     * @return результат строка tue или false
     * @throws IllegalArgumentException, если логический оператор не установлен
     */
    private String checkCondition(String evaluate1, String evaluate2, String operation){
        boolean arg1 = toCompare(evaluate1);
        boolean arg2 = toCompare(evaluate2);

        if (operation.equals("AND")){
            return String.valueOf(arg1 && arg2) ;
        }else if(operation.equals("OR")){
            return String.valueOf(arg1 || arg2);
        }

        logger.info("Неверный логический оператор: '{}'", operation);
        throw new IllegalArgumentException("Неверный логический оператор: " + operation);
    }

    /**
     * Получает на вход выражение типа x > y, где x и y - целые числа.
     * Возвращает результат сравнения (true или false).
     * @param evaluate
     * @return результат сравнения (true или false)
     * @throws - IllegalArgumentException, если знак сравнения не установлен
     */
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
        logger.info("Ошибка в выражении: '{}'", evaluate);
        throw new IllegalArgumentException("Ошибка в выражении: " + evaluate);
    }

    @Override
    public String toString() {
        return "Evaluate{" +
                "transactions=" + transactions +
                '}';
    }

}
