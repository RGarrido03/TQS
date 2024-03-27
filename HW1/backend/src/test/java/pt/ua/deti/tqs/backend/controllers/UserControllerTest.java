package pt.ua.deti.tqs.backend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.ua.deti.tqs.backend.entities.Reservation;
import pt.ua.deti.tqs.backend.entities.User;
import pt.ua.deti.tqs.backend.services.ReservationService;
import pt.ua.deti.tqs.backend.services.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @MockBean
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void whenPostUser_thenCreateUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setName("John Doe");
        user.setEmail("johndoe@ua.pt");
        user.setPassword("password");

        when(service.createUser(Mockito.any())).thenReturn(user);

        mvc.perform(post("/api/user").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(user)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id", is(1)))
           .andExpect(jsonPath("$.name", is("John Doe")))
           .andExpect(jsonPath("$.email", is("johndoe@ua.pt")))
           .andExpect(jsonPath("$.username", is("johndoe")));

        verify(service, times(1)).createUser(Mockito.any());
    }

    @Test
    void whenGetUserById_thenGetUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setName("John Doe");
        user.setEmail("johndoe@ua.pt");
        user.setPassword("password");

        when(service.getUser(1L)).thenReturn(user);

        mvc.perform(get("/api/user/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(1)))
           .andExpect(jsonPath("$.name", is("John Doe")))
           .andExpect(jsonPath("$.email", is("johndoe@ua.pt")))
           .andExpect(jsonPath("$.username", is("johndoe")));

        verify(service, times(1)).getUser(1L);
    }

    @Test
    void whenGetUserByInvalidId_thenGetNull() throws Exception {
        when(service.getUser(1L)).thenReturn(null);

        mvc.perform(get("/api/user/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound());

        verify(service, times(1)).getUser(1L);
    }

    @Test
    void whenGetUserReservationsByUserId_thenGetUserReservations() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setName("John Doe");
        user.setEmail("johhdoe@ua.pt");
        user.setPassword("password");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);

        when(reservationService.getReservationsByUserId(1L)).thenReturn(List.of(reservation));

        mvc.perform(get("/api/user/1/reservations").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].id", is(1)));

        verify(reservationService, times(1)).getReservationsByUserId(1L);
    }

    @Test
    void whenUpdateUser_thenUpdateUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setName("John Doe");
        user.setEmail("johndoe@ua.pt");
        user.setPassword("password");

        when(service.updateUser(Mockito.any(User.class))).then(returnsFirstArg());

        mvc.perform(put("/api/user/1").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(user)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(1)))
           .andExpect(jsonPath("$.name", is("John Doe")))
           .andExpect(jsonPath("$.email", is("johndoe@ua.pt")))
           .andExpect(jsonPath("$.username", is("johndoe")));

        verify(service, times(1)).updateUser(Mockito.any(User.class));
    }

    @Test
    void whenDeleteUser_thenDeleteUser() throws Exception {
        mvc.perform(delete("/api/user/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk());

        verify(service, times(1)).deleteUser(1L);
    }
}
