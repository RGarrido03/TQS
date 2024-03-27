package pt.ua.deti.tqs.backend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.deti.tqs.backend.entities.City;
import pt.ua.deti.tqs.backend.services.CityService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CityController.class)
class CityControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CityService service;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void whenPostCity_thenCreateCity() throws Exception {
        City city = new City();
        city.setId(1L);
        city.setName("Aveiro");

        when(service.createCity(Mockito.any())).thenReturn(city);

        mvc.perform(post("/api/city").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(city)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.name", is("Aveiro")));

        verify(service, times(1)).createCity(Mockito.any());
    }

    @Test
    void givenManyCities_whenGetCities_thenReturnJsonArray() throws Exception {
        City city1 = new City();
        city1.setId(1L);
        city1.setName("Aveiro");
        City city2 = new City();
        city2.setId(2L);
        city2.setName("Porto");
        City city3 = new City();
        city3.setId(3L);
        city3.setName("Lisboa");

        when(service.getAllCities()).thenReturn(Arrays.asList(city1, city2, city3));

        mvc.perform(get("/api/city").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(3)))
           .andExpect(jsonPath("$[0].name", is(city1.getName())))
           .andExpect(jsonPath("$[1].name", is(city2.getName())))
           .andExpect(jsonPath("$[2].name", is(city3.getName())));

        verify(service, times(1)).getAllCities();
    }

    @Test
    void whenGetCityById_thenReturnCity() throws Exception {
        City city = new City();
        city.setId(1L);
        city.setName("Aveiro");

        when(service.getCity(1L)).thenReturn(city);

        mvc.perform(get("/api/city/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is(city.getName())));

        verify(service, times(1)).getCity(1L);
    }

    @Test
    void whenGetCityByName_thenReturnCity() throws Exception {
        City city = new City();
        city.setId(1L);
        city.setName("Aveiro");

        when(service.getCitiesByName("Aveiro")).thenReturn(List.of(city));

        mvc.perform(get("/api/city?name=Aveiro").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].name", is(city.getName())));

        verify(service, times(1)).getCitiesByName("Aveiro");
    }

    @Test
    void whenGetCityByInvalidId_thenReturnNotFound() throws Exception {
        when(service.getCity(1L)).thenReturn(null);

        mvc.perform(get("/api/city/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound());

        verify(service, times(1)).getCity(1L);
    }

    @Test
    void whenUpdateCity_thenUpdateCity() throws Exception {
        City city = new City();
        city.setId(1L);
        city.setName("Aveiro");

        when(service.updateCity(Mockito.any(City.class))).then(returnsFirstArg());

        mvc.perform(put("/api/city/1").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(city)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is("Aveiro")));

        verify(service, times(1)).updateCity(Mockito.any());
    }

    @Test
    void whenDeleteCity_thenDeleteCity() throws Exception {
        mvc.perform(delete("/api/city/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk());

        verify(service, times(1)).deleteCity(1L);
    }
}
