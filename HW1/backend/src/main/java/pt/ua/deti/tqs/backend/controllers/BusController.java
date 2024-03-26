package pt.ua.deti.tqs.backend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.tqs.backend.entities.Bus;
import pt.ua.deti.tqs.backend.services.BusService;

import java.util.List;

@RestController
@RequestMapping("api/bus")
@AllArgsConstructor
public class BusController {
    private final BusService busService;

    @PostMapping
    public ResponseEntity<Bus> createBus(@RequestBody Bus bus) {
        return new ResponseEntity<>(busService.createBus(bus), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Bus>> getBuses() {
        return new ResponseEntity<>(busService.getAllBuses(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Bus> getBus(@PathVariable("id") Long id) {
        Bus bus = busService.getBus(id);
        HttpStatus status = bus != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(bus, status);
    }

    @PutMapping("{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable("id") Long id, @RequestBody Bus bus) {
        bus.setId(id);
        Bus updated = busService.updateBus(bus);
        HttpStatus status = updated != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(updated, status);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable("id") Long id) {
        busService.deleteBus(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
