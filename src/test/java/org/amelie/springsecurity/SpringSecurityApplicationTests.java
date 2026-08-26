package org.amelie.springsecurity;

import jakarta.transaction.Transactional;
import org.amelie.springsecurity.Controller.AuthController;
import org.amelie.springsecurity.Repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SpringSecurityApplicationTests {

    @Autowired
    private AuthController authController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void contextLoads() {
        assertThat(authController).isNotNull();
    }

    //Un utilisateur User peut récupérer la liste des livres
    @Test
    @WithMockUser(authorities = {"SCOPE_ROLE_USER"})
    public void shouldGetBooks() throws Exception {
        this.mockMvc.perform(get("/api/books"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //Un utilisateur User ne peut pas supprimer un livre
    @Test
    @WithMockUser(authorities = {"SCOPE_ROLE_USER"})
    public void shouldFailDeleteBooksById() throws Exception {
        UUID bookId = UUID.fromString(bookRepository.findAll().get(0).getId());
        this.mockMvc.perform(delete("/api/books/" + bookId)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    //Un utilisateur Admin peut créer un livre
    @Test
    @WithMockUser(authorities = {"SCOPE_ROLE_ADMIN"})
    public void shouldCreateBook() throws Exception {
        String bookJson = """
                {
                    "title": "Autre Monde",
                    "author": "Maxime CHATTAM",
                    "category": "Thriller",
                    "publicationYear": 2008,
                    "numberOfCopies": 600000
                }
                """;
        this.mockMvc.perform(post("/api/books")
                        .contentType("application/json")
                        .content(bookJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    //Un utilisateur Admin peut modifier un livre
    @Test
    @WithMockUser(authorities = {"SCOPE_ROLE_ADMIN"})
    public void shouldUpdateBook() throws Exception {
        UUID existingBookId = UUID.fromString(bookRepository.findAll().get(0).getId());
        String bookJson = """
                {
                    "numberOfCopies": 310000  
                    }
                """;
        this.mockMvc.perform(put("/api/books/" + existingBookId)
                        .contentType("application/json")
                        .content(bookJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //Un utilisateur Admin peut supprimer un livre
    @Test
    @WithMockUser(authorities = {"SCOPE_ROLE_ADMIN"})
    public void shouldDeleteBook() throws Exception {
        UUID bookId = UUID.fromString(bookRepository.findAll().get(0).getId());
        this.mockMvc.perform(delete("/api/books/" + bookId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    //Test POST/api/auth/register : un utilisateur peut s'enregistrer avec un rôle USER, un JWT est retourné avec un statut 200
    @Test
    public void shouldRegisterUser() throws Exception {
        String userJson = """
                {
                    "username": "testuser",
                    "password": "TestUser123!",
                    "email": "testuser@example.com"
                }
                """;
        this.mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(userJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //Test POST/api/auth/login : un utilisateur peut se connecter avec des identifiants valides
    @Test
    public void shouldLoginWithValidCredentials() throws Exception {
        String loginJson = """
            {
                "username": "admin",
                "password": "securepassword"
            }
            """;
        this.mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(loginJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

//Test POST/api/auth/login : identifiants invalides la connexion échoue avec un statut 401
@Test
public void shouldFailLoginWithInvalidCredentials() throws Exception {
    String userJson = """
                {
                    "username": "invaliduser",
                    "password": "invalidpassword"
                }
                """;
        this.mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(userJson))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


}
