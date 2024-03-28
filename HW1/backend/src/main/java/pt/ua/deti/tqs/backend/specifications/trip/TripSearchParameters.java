package pt.ua.deti.tqs.backend.specifications.trip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TripSearchParameters {
    private Long departure;
    private Long arrival;
    private LocalDateTime departureTime;
    private Long freeSeats;
}