package com.tcc.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.backend.config.JwtRequestFilter;
import com.tcc.backend.config.JwtUtil;
import com.tcc.backend.controllers.UserController;
import com.tcc.backend.dtos.user.UserRequest;
import com.tcc.backend.enums.UserType;
import com.tcc.backend.models.User;
import com.tcc.backend.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)  // desativa filtros de segurança (JwtRequestFilter)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    // Moca o serviço de usuário
    @MockBean
    private UserService service;

    // Caso queira mesmo assim criar beans (não usados, pois addFilters=false)
    @MockBean
    private JwtRequestFilter jwtRequestFilter;
    @MockBean
    private JwtUtil jwtUtil;

    private User buildUser() {
        return User.builder()
                .idUser(1L)
                .type(UserType.PACIENTE)
                .name("Guilherme")
                .email("gui@exemplo.com")
                .password("senha123")
                .cpf("12345678901")
                .phone("11999990000")
                .cep("01234-567")
                .city("São Paulo")
                .neighborhood("Centro")
                .street("Rua A")
                .number(100)
                .build();
    }

    private UserRequest buildRequest() {
        return UserRequest.builder()
                .type(UserType.PACIENTE)
                .name("Guilherme")
                .email("gui@exemplo.com")
                .password("senha123")
                .cpf("12345678901")
                .phone("11999990000")
                .cep("01234-567")
                .city("São Paulo")
                .neighborhood("Centro")
                .street("Rua A")
                .number(100)
                .build();
    }

    @Test @DisplayName("POST /users → 201 + corpo JSON")
    void create_devolve201ECorpo() throws Exception {
        User u = buildUser();
        when(service.create(any(UserRequest.class))).thenReturn(u);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idUser").value(u.getIdUser()))
                .andExpect(jsonPath("$.name").value(u.getName()))
                .andExpect(jsonPath("$.email").value(u.getEmail()))
                .andExpect(jsonPath("$.cpf").value(u.getCpf()))
                .andExpect(jsonPath("$.phone").value(u.getPhone()));
    }

    @Test
    @DisplayName("PUT /users/{id} → 200")
    void update_devolve200() throws Exception {
        // supondo que update retorne um User
        User updated = buildUser(); // ou null, se você não se importa
        when(service.update(eq(1L), any(UserRequest.class)))
                .thenReturn(updated);

        mvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildRequest())))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("DELETE /users/{id} → 200")
    void delete_devolve200() throws Exception {
        doNothing().when(service).delete(1L);

        mvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("GET /users/search/{id} → 200 + corpo JSON")
    void getById_devolve200EUsuario() throws Exception {
        User u = buildUser();
        when(service.getById(u.getIdUser())).thenReturn(u);

        mvc.perform(get("/users/search/{id}", u.getIdUser()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idUser").value(u.getIdUser()));
    }

    @Test @DisplayName("GET /users/cpf?cpf={cpf} → 200 + corpo JSON")
    void getByCpf_devolve200EUsuario() throws Exception {
        User u = buildUser();
        when(service.getByCpf(u.getCpf())).thenReturn(u);

        mvc.perform(get("/users/cpf").param("cpf", u.getCpf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cpf").value(u.getCpf()));
    }

    @Test @DisplayName("GET /users → 200 + página de usuários")
    void list_devolve200EPagina() throws Exception {
        User u = buildUser();
        Page<User> page = new PageImpl<>(List.of(u), PageRequest.of(0, 10), 1);
        when(service.list(eq(u.getName()), any(Pageable.class))).thenReturn(page);

        mvc.perform(get("/users")
                        .param("name", u.getName())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].idUser").value(u.getIdUser()));
    }

    @Test @DisplayName("GET /users/patients → 200 + lista JSON")
    void getPatients_devolve200ELista() throws Exception {
        User u = buildUser();
        when(service.getUsersByType(UserType.PACIENTE)).thenReturn(List.of(u));

        mvc.perform(get("/users/patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idUser").value(u.getIdUser()));
    }
}
