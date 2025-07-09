package star.part03.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import star.part02.service.RecommendationRuleSet;
import star.part03.model.InformationAboutPackage;
import star.part03.model.Stat;

import java.util.Collections;
import java.util.List;

@RestController
public class RecommendationControllerPart03{
    private final RecommendationRuleSet service;
    private final BuildProperties buildProperties;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationControllerPart03.class);

    public RecommendationControllerPart03(@Qualifier("servicePart02") RecommendationRuleSet service,
                                          @Autowired BuildProperties buildProperties) {
        this.service = service;
        this.buildProperties = buildProperties;
    }

    @GetMapping(value = "/rule/stats")
    @Operation(
            summary = "Получить статистику выполнения правил",
            description = "Возвращает статистику выполнения правил"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Сформирована статистика выполнения правил"
    )
    /**
     * Возвращет статистику выполнения правил банка
     */
    public ResponseEntity<List<Stat>> findStat() {
        return ResponseEntity.ok(service.findStat().orElse(Collections.emptyList()));
    }

    @GetMapping(value = "/management/clear-caches")
    @Operation(
            summary = "Очистить кэш запросов рекомендаций",
            description = "Очищает кэш запросов рекомендаций"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Кэш запросов рекомендаций очищен"
    )
    /**
     * Очищает кэш запросов рекомендаций
     */
    public void clearCaches() {
        service.toClearCaches();
    }

    @GetMapping(value = "/management/info")
    @Operation(
            summary = "Получить информацию о сервисе",
            description = "Получает информацию о сервисе"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Получена информация о сервисе"
    )

    /**
     * Возвращает информацию о package: наименование и версия
     */
    public InformationAboutPackage aboutService() {
        return new InformationAboutPackage(
                buildProperties.getName(),
                buildProperties.getVersion()
        );
    }
}
