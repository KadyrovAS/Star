package star.part01.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import star.part01.service.RecommendationExampleService;

@RestController
@RequestMapping("/part01/example")
public class RecommendationExamplesController {
    private final RecommendationExampleService service;

    public RecommendationExamplesController(RecommendationExampleService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "Сформировать базу данных с тестовыми рекомендациями",
            description = "Формирует базу из 3-х рекомендации с правилами: Invest 500, Top Saving, Простой кредит"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Сформирована база из 3-х рекомендаций и правил к ним"
    )

    public ResponseEntity<String> insertExampleRecords(){
        service.addExample();
        return ResponseEntity.ok("Записи добавлены");
    }

}
