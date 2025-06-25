package star.part02.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import star.part01.service.RecommendationByName;
import star.part02.model.Recommendation;
import star.part02.model.Rule;
import star.part02.repository.StarRepositoryPart02;

import java.util.UUID;

@Service
public class RecommendationServiceExamplePart2 {
    private final StarRepositoryPart02 repository;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationServiceExamplePart2.class);

    public RecommendationServiceExamplePart2(StarRepositoryPart02 repository) {
        this.repository = repository;
    }

    public void createDb(){
        repository.deleteAll();
        Rule[] rules = new Rule[3];

        rules[0] = new Rule(
            "USER_OF",
                new String[]{"CREDIT"},
                true
        );
        rules[1] = new Rule(
                "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW",
                new String[]{"DEBIT", ">"},
                false
        );
        rules[2] = new Rule(
                "TRANSACTION_SUM_COMPARE",
                new String[]{"DEBIT", "WITHDRAW", ">", "100000"},
                false
        );
        String name = "Простой кредит";
        Recommendation recommendation = new Recommendation(
                UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"),
                name,
                RecommendationByName.getRecommendationByName(name),
                rules
        );
        repository.insertRecommendation(recommendation);

        rules[0] = new Rule(
                "USER_OF",
                new String[]{"DEBIT"},
                false
        );
        rules[1] = new Rule(
                "USER_OF",
                new String[]{"INVEST"},
                true
        );
        rules[2] = new Rule(
                "TRANSACTION_SUM_COMPARE",
                new String[]{"SAVING", "DEPOSIT", ">", "1000"},
                false
        );
        name = "Invest 500";
        recommendation = new Recommendation(
                UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"),
                name,
                RecommendationByName.getRecommendationByName(name),
                rules
        );
        repository.insertRecommendation(recommendation);

        rules[0] = new Rule(
                "USER_OF",
                new String[]{"DEBIT"},
                false
        );
        rules[1] = new Rule(
          "TRANSACTION_SUM_COMPARE",
                new String[]{"SAVING", "DEPOSIT", ">=", "50000"},
                false
        );
        rules[2] = new Rule(
                "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW",
                new String[]{"DEBIT", ">"},
                false
        );
        name = "Top Saving";
        recommendation = new Recommendation(
                UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"),
                name,
                RecommendationByName.getRecommendationByName(name),
                rules
        );
        repository.insertRecommendation(recommendation);

        rules[0] = new Rule(
                "USER_OF",
                new String[]{"DEBIT"},
                false
        );
        rules[1] = new Rule(
                "TRANSACTION_SUM_COMPARE",
                new String[]{"DEBIT", "DEPOSIT", ">=", "50000"},
                false
        );
        rules[2] = new Rule(
                "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW",
                new String[]{"DEBIT", ">"},
                false
        );
        name = "Top Saving";
        recommendation = new Recommendation(
                UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"),
                name,
                RecommendationByName.getRecommendationByName(name),
                rules
        );
        repository.insertRecommendation(recommendation);

    }
}
