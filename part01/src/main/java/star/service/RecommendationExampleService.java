package star.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import star.model.Recommendation;
import star.model.Rule;
import star.model.RuleToRecommendation;
import star.repository.RecommendationExampleRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationExampleService {
    private final RecommendationExampleRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationExampleService.class);

    public RecommendationExampleService(RecommendationExampleRepository repository) {
        this.repository = repository;
    }


    public void addExample(){
        repository.deleteAll();
        addRecommendation();
        addRules();
    }

    public void addRecommendation() {
        repository.addRecommendation(new Recommendation(
            UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"),
                "Invest 500",
                """
                        Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! 
                        Воспользуйтесь налоговыми льготами и начните инвестировать с умом. 
                        Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом 
                        периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за 
                        актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к 
                        финансовой независимости!"""
        ));

        repository.addRecommendation(new Recommendation(
                UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"),
                "Top Saving",
                """
                        Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский 
                        инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше 
                        никаких забытых чеков и потерянных квитанций — всё под контролем!
                        \n\n 
                        Преимущества «Копилки»:
                        \n\n
                        Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет 
                        автоматически переводить определенную сумму на ваш счет.
                        \n\n
                        Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления 
                        и корректируйте стратегию при необходимости.
                        \n\n
                        Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним 
                        возможен только через мобильное приложение или интернет-банкинг.
                        \n\n
                        Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!"""
        ));

        repository.addRecommendation(new Recommendation(
                UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"),
                "Простой кредит",
                """
                        Откройте мир выгодных кредитов с нами!
                        \n\n
                        Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — 
                        именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и 
                        индивидуальный подход к каждому клиенту.
                        \n\n
                        Почему выбирают нас:
                        \n\n
                        Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки 
                        занимает всего несколько часов.
                        \n\n
                        Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в 
                        мобильном приложении.
                        \n\n
                        Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку 
                        недвижимости, автомобиля, образование, лечение и многое другое.
                        \n\n
                        Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!"""
        ));
    }

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
