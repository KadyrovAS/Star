package star.part01.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import star.part01.model.Recommendation;
import star.part01.model.Rule;
import star.part01.model.RuleToRecommendation;
import star.part01.repository.RecommendationExampleRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Сервисный класс. Используется для формирования базы данных recommendationsPart01.mv.db
 */
@Service
public class RecommendationExampleService {
    private final RecommendationExampleRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationExampleService.class);

    public RecommendationExampleService(RecommendationExampleRepository repository) {
        this.repository = repository;
    }

    /**
     * Очищает базу данных и добавляет в нее рекомендации банка
     */
    public void addExample(){
        repository.deleteAll();
        addRecommendation();
        addRules();
    }

    /**
     * Добавляет в базу данных список всех рекомендаций.
     * Id, краткое название рекомендации и текст рекомендации устанавливаются банком в ТЗ
     */
    public void addRecommendation() {
        String name = "Invest 500";
        repository.addRecommendation(new Recommendation(
            UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"),
                name,
                RecommendationByName.getRecommendationByName(name)
        ));

        name = "Top Saving";
        repository.addRecommendation(new Recommendation(
                UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"),
                name,
                RecommendationByName.getRecommendationByName(name)
        ));

        name = "Простой кредит";
        repository.addRecommendation(new Recommendation(
                UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"),
                name,
                RecommendationByName.getRecommendationByName(name)
        ));
    }

    /**
     * 1. Формирует список правил банка.
     * - Для каждого правила формируется свой id.
     * - Текст инструкции формализован с помощью разработанного языка
     * - Текст правила установлен банком в ТЗ
     * 2. Заполняет таблицу, обеспечивающую связь "many to many" между таблицами рекомендаций и правилами
     */
    public void addRules(){
        List<Rule>rules = new LinkedList<>();

        //0
        rules.add(new Rule(
                UUID.randomUUID(),
                "AMOUNT (DEBIT) > 0",
                "Пользователь использует как минимум один продукт с типом DEBIT."
        ));

        //1
        rules.add(new Rule(
                UUID.randomUUID(),
                "AMOUNT(INVEST) = 0",
                "Пользователь не использует продукты с типом INVEST."
        ));

        //2
        rules.add(new Rule(
                UUID.randomUUID(),
                "DEPOSIT(SAVING) > 1000",
                "Сумма пополнений продуктов с типом SAVING больше 1000 ₽"
        ));

        //3
        rules.add(new Rule(
                UUID.randomUUID(),
                "DEPOSIT(DEBIT) >= 50000 OR DEPOSIT(SAVING) >= 50000",
                """
                        Сумма пополнений по всем продуктам типа DEBIT больше или равна 50 000 ₽ ИЛИ 
                        Сумма пополнений по всем продуктам типа SAVING больше или равна 50 000 ₽."""
        ));

        //4
        rules.add(new Rule(
                UUID.randomUUID(),
                "DIFFERENCE(DEBIT) > 0",
                """
                        Сумма пополнений по всем продуктам типа DEBIT больше, 
                        чем сумма трат по всем продуктам типа DEBIT."""
        ));

        //5
        rules.add(new Rule(
                UUID.randomUUID(),
                "AMOUNT(CREDIT) = 0",
                "Пользователь не использует продукты с типом CREDIT."
        ));

        //6
        rules.add(new Rule(
                UUID.randomUUID(),
                "WITHDRAW(DEBIT) > 100000",
                "Сумма трат по всем продуктам типа DEBIT больше, чем 100 000 ₽."
        ));
        for (Rule rule: rules){
            repository.addRule(rule);
        }


        List<RuleToRecommendation>ruleToRecommendations = new LinkedList<>();
        UUID recommendationUUID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
        ruleToRecommendations.add(new RuleToRecommendation(
                recommendationUUID,
                rules.get(0).getId()
        ));
        ruleToRecommendations.add(new RuleToRecommendation(
                recommendationUUID,
                rules.get(1).getId()
        ));
        ruleToRecommendations.add(new RuleToRecommendation(
                recommendationUUID,
                rules.get(2).getId()
        ));

        recommendationUUID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
        ruleToRecommendations.add(new RuleToRecommendation(
                recommendationUUID,
                rules.get(0).getId()
        ));
        ruleToRecommendations.add(new RuleToRecommendation(
                recommendationUUID,
                rules.get(3).getId()
        ));
        ruleToRecommendations.add(new RuleToRecommendation(
                recommendationUUID,
                rules.get(4).getId()
        ));

        recommendationUUID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
        ruleToRecommendations.add(new RuleToRecommendation(
                recommendationUUID,
                rules.get(5).getId()
        ));
        ruleToRecommendations.add(new RuleToRecommendation(
                recommendationUUID,
                rules.get(4).getId()
        ));
        ruleToRecommendations.add(new RuleToRecommendation(
                recommendationUUID,
                rules.get(6).getId()
        ));
        for (RuleToRecommendation ruleToRecommendation: ruleToRecommendations){
            repository.addRecommendationRules(ruleToRecommendation);
        }
    }

}
