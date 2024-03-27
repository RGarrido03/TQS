package pt.ua.deti.tqs.backend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.deti.tqs.backend.entities.Bus;
import pt.ua.deti.tqs.backend.services.BusService;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BusController.class)
class BusControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BusService service;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void whenPostBus_thenCreateBus() throws Exception {
        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        when(service.createBus(Mockito.any())).thenReturn(bus);

        mvc.perform(post("/api/bus").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(bus)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.capacity", is(50)));

        verify(service, times(1)).createBus(Mockito.any());
    }

    @Test
    void givenManyBuses_whenGetBuses_thenReturnJsonArray() throws Exception {
        Bus bus1 = new Bus();
        bus1.setId(1L);
        bus1.setCapacity(50);
        Bus bus2 = new Bus();
        bus2.setId(2L);
        bus2.setCapacity(60);
        Bus bus3 = new Bus();
        bus3.setId(3L);
        bus3.setCapacity(70);

        when(service.getAllBuses()).thenReturn(Arrays.asList(bus1, bus2, bus3));

        mvc.perform(get("/api/bus").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(3)))
           .andExpect(jsonPath("$[0].capacity", is(50)))
           .andExpect(jsonPath("$[1].capacity", is(60)))
           .andExpect(jsonPath("$[2].capacity", is(70)));

        verify(service, times(1)).getAllBuses();
    }

    @Test
    void whenGetBusById_thenReturnBus() throws Exception {
        Bus bus = new Bus();
        bus.setId(1L);
        bus.setCapacity(50);

        when(service.getBus(1L)).thenReturn(bus);

        mvc.perform(get("/api/bus/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.capacity", is(50)));

        verify(service, times(1)).getBus(1L);
    }

    @Test
    void whenGetBusByInvalidId_thenReturnNotFound() throws Exception {
        when(service.getBus(1L)).thenReturn(null);

        mvc.perform(get("/api/bus/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound());

        verify(service, times(1)).getBus(1L);
    }

    @Test
    void whenUpdateBus_thenUpdateBus() throws Exception {
        Bus bus = new Bus();
        bus.setCapacity(50);

        when(service.updateBus(Mockito.any(Bus.class))).then(returnsFirstArg());

        mvc.perform(put("/api/bus/1").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(bus)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.capacity", is(50)));

        verify(service, times(1)).updateBus(Mockito.any(Bus.class));
    }

    @Test
    void whenDeleteBusById_thenDeleteBus() throws Exception {
        mvc.perform(delete("/api/bus/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk());

        verify(service, times(1)).deleteBus(1L);
    }
}
