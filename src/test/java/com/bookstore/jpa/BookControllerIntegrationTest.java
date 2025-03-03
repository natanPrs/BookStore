package com.bookstore.jpa;

import com.bookstore.jpa.dtos.BookRecordDto;
import com.bookstore.jpa.models.BookModel;
import com.bookstore.jpa.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookService bookService;

    @Test
    public void shouldSaveBookSuccessfully() throws Exception {

        Set<UUID> authorIds = new HashSet<>();
        authorIds.add(UUID.fromString("3f808812-68cf-4e7e-87e2-33dbdb884ee2"));
        UUID publisherId = UUID.fromString("a75764a9-e8ac-45be-963f-52084181c59e");


        // Given - Criamos um JSON representando um novo livro
        BookRecordDto bookDto = new BookRecordDto("Ratolomeu", publisherId, authorIds, "Bom dms");
        String bookJson = objectMapper.writeValueAsString(bookDto);

        // When - Enviamos a requisição POST para adicionar o livro
        mockMvc.perform(MockMvcRequestBuilders.post("/bookstore/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isCreated()); // Verifica se retornou 201 Created

        // Then - Verificamos se o livro foi salvo no banco
        List<BookModel> books = bookService.getAllBooks();
        boolean bookExists = books.stream().anyMatch(book ->
                book.getTitle().equals("Ratolomeu") &&
                        book.getPublisher().getName().equals("Alta Books"));


        Assertions.assertTrue(bookExists, "O livro não foi salvo no banco de dados corretamente!");
    }

    @Test
    public void shouldDeleteBookSuccessfully() throws Exception {
        UUID bookToDelete = UUID.fromString("ded0c179-c2e3-4c88-b400-8a411aaffd9b");

        mockMvc.perform(MockMvcRequestBuilders.delete("/bookstore/books/" + bookToDelete))
                .andExpect(status().isOk());

        List<BookModel> books = bookService.getAllBooks();
        boolean bookExist = books.stream().anyMatch(book ->
                book.getTitle().equals("O Senhor dos Anéis"));

        Assertions.assertFalse(bookExist, "O livro não foi deletado corretamente!");
    }
}
