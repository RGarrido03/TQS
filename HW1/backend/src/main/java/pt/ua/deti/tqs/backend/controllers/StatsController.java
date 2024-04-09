package pt.ua.deti.tqs.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ua.deti.tqs.backend.entities.Stats;
import pt.ua.deti.tqs.backend.services.StatsService;

@RestController
@RequestMapping("api/stats")
@Tag(name = "Stats")
@AllArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping()
    @Operation(summary = "Get stats")
    public ResponseEntity<Stats> getStats() {
        int totalRequests = statsService.getTotalRequests();
        int cacheMisses = statsService.getCacheMisses();
        Stats stats = new Stats(totalRequests, cacheMisses);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
