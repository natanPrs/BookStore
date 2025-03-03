package com.bookstore.jpa.services;

import com.bookstore.jpa.dtos.BookRecordDto;
import com.bookstore.jpa.models.AuthorModel;
import com.bookstore.jpa.repositories.AuthorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    @Transactional
    public List<AuthorModel> getAllAuthors() { return authorRepository.findAll(); }

    public void deleteAuthor(UUID id) { authorRepository.deleteById(id); }

    @Transactional
    public AuthorModel saveAuthor(BookRecordDto bookRecordDto) {
        AuthorModel author = new AuthorModel();
        author.setName(bookRecordDto.title());

        return authorRepository.save(author);
    }
}
