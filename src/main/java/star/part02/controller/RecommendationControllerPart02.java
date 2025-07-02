package star.part02.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import star.part02.model.Recommendation;
import star.part02.model.Transaction;
import star.part02.service.RecommendationRuleSet;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/part02")
public class RecommendationControllerPart02 {
    private final RecommendationRuleSet service;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationControllerPart02.class);

    public RecommendationControllerPart02(@Qualifier("servicePart02") RecommendationRuleSet service) {
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
    /**
     * Возвращает список рекомендаций для клиента банка с заданным id
     * @param id Id клиента банка
     * @return список рекомендаций
     */
    public ResponseEntity<List<Recommendation>> findRecommendationById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findRecommendationById(id).orElse(Collections.emptyList()));
    }

    @GetMapping(value = "/allRecommendations")
    @Operation(
            summary = "Получить список всех рекомендаций банка",
            description = "Возвращает список всех рекомендаций банка со списком правил для каждой рекомендации"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Сформирован полный список всех имеющихся рекомендаций"
    )
    /**
     * Возвращает список всех рекомендаций банка
     */
    public List<Recommendation>findAllRecommendations(){
        return service.findAllRecommendations();
    }

    @PutMapping
    @Operation(
            summary = "Добавить рекомендацию со списком правил",
            description = "Добавляет рекомендацию со списком правил"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Рекомендация со списком правил добавлена"
    )

    /**
     * Добавляет рекомендацию вместе с правилами
     * @param recommendation Рекомендация вместе с правилами
     */
    public void insertRecommendation(@RequestBody Recommendation recommendation) {
        logger.info("insertRecommendation: {}", recommendation);
        service.addRecommendation(recommendation);
    }


    @DeleteMapping
    @Operation(
            summary = "Удалить рекомендацию вместе со списком правил, которые должны выполняться",
            description = "Удаляет рекомендацию вместе со списком правил по заданному id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Рекомендация с заданным id удалена"
    )
    /**
     * Удаляет рекомендацию
     * @param id Идентификатор рекомендации
     */
    public void deleteRecommendation(@RequestBody UUID id) {
        logger.info("deleteRecommendation: {}", id);
        service.deleteRecommendation(id);
    }

}
