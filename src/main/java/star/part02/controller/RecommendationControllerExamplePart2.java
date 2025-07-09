package star.part02.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import star.part02.service.RecommendationServiceExamplePart2;

/**
 * Это вспомогательный класс. В работе приложения не участвует. Он необходим для заполнения базы данных
 * star в postgresql со списком рекомендаций. Все рекомендации соответствуют ТЗ банка для второго этапа работы.
 */
@RestController
@RequestMapping("/ExamplePart2")

public class RecommendationControllerExamplePart2{
    private final RecommendationServiceExamplePart2 service;
    Logger logger = LoggerFactory.getLogger(RecommendationControllerExamplePart2.class);

    public RecommendationControllerExamplePart2(RecommendationServiceExamplePart2 service) {
        this.service = service;
    }

    @Operation(
            summary = "Сформировать базу данных с тестовыми рекомендациями",
            description = "Формирует базу из 3-х рекомендации с правилами: Invest 500, Top Saving, Простой кредит"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Сформирована база из 3-х рекомендаций и правил к ним"
    )

    @PostMapping
    public void createDb() {
        service.createDb();
    }
}
