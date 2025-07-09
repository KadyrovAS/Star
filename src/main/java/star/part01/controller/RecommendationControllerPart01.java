package star.part01.controller;

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
import star.part01.model.Recommendation;
import star.part01.model.Recommendations;
import star.part01.service.RecommendationRuleSet;

import java.util.UUID;

/**
 * В классе имеется один эндпоинт, запрашивающий рекомендации банка для клиента по его id.
 * Рекомендации и правила содержатся в базе данных H2 recommendationsPart01.mv.db, подготовленным на первом этапе работы.
 * Массив рекомендаций, для которых все правила выполняются, возвращается клиенту.
 * Если для клиента не найдено ни одной рекомендации, то ему возвращается постой массив
 */
@RestController
@RequestMapping("/part01")
public class RecommendationControllerPart01{
    private final RecommendationRuleSet service;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationControllerPart01.class);

    public RecommendationControllerPart01(@Qualifier("recommendationsChoice") RecommendationRuleSet service) {
        this.service = service;
    }

    @GetMapping(value = "/recommendation/{id}")
    @Operation(
            summary = "Получить рекомендации для клиента по ID",
            description = "Возвращает список рекомендаций для клиента"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Сформирован список рекомендаций, либо пустой массив, если рекомендаций нет"
    )
    public ResponseEntity<Recommendations> getRecommendation(@PathVariable UUID id) {
        logger.info("Запрос рекомендации для {}", id);
        return ResponseEntity.ok(service.getRecommendation(id).orElse(new Recommendations(id, new Recommendation[0])));
    }

}
