package pt.ua.deti.tqs.backend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ua.deti.tqs.backend.entities.City;
import pt.ua.deti.tqs.backend.services.CityService;

import java.util.List;

@RestController
@RequestMapping("api/city")
@AllArgsConstructor
public class CityController {
    private final CityService cityService;

    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody City city) {
        return new ResponseEntity<>(cityService.createCity(city), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<City>> getCities() {
        return new ResponseEntity<>(cityService.getAllCities(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<City> getCity(@PathVariable Long id) {
        City city = cityService.getCity(id);
        HttpStatus status = city != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(city, status);
    }

    @PutMapping("{id}")
    public ResponseEntity<City> updateCity(@PathVariable("id") Long id, @RequestBody City city) {
        city.setId(id);
        City updated = cityService.updateCity(city);
        HttpStatus status = updated != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(updated, status);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable("id") Long id) {
        cityService.deleteCity(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
