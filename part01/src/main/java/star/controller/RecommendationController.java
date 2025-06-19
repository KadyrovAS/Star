package star.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import star.model.Recommendation;
import star.model.Recommendations;
import star.service.RecommendationRuleSet;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationRuleSet service;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    public RecommendationController(@Qualifier("recommendationsChoice") RecommendationRuleSet service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Получить клиента по ID",
            description = "Возвращает список рекомендаций для клиента"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Сформирован список рекомендаций, либо пустой массив, если рекомендаций нет"
    )

    public ResponseEntity<Recommendations> getRecommendation(@PathVariable UUID id){
        logger.info("Запрос рекомендации для {}", id);
        return ResponseEntity.ok(service.getRecommendation(id).orElse(new Recommendations(id, new Recommendation[0])));
    }

}
