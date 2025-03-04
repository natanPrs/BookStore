package com.bookstore.jpa;

import com.bookstore.jpa.dtos.AuthorRecordDto;
import com.bookstore.jpa.dtos.BookRecordDto;
import com.bookstore.jpa.dtos.PublisherRecordDto;
import com.bookstore.jpa.models.AuthorModel;
import com.bookstore.jpa.models.BookModel;
import com.bookstore.jpa.models.PublisherModel;
import com.bookstore.jpa.models.ReviewModel;
import com.bookstore.jpa.repositories.AuthorRepository;
import com.bookstore.jpa.repositories.BookRepository;
import com.bookstore.jpa.repositories.PublisherRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Autowired
    protected BookRepository bookRepository;

    @Autowired
    protected AuthorRepository authorRepository;

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected PublisherRepository publisherRepository;


    @Test
    @DisplayName("should get Book Successfully from DB")
    public void findBookByTitleCase1() throws Exception {

        String bookTitle = "Dexter is delicious";

        AuthorRecordDto authorData = new AuthorRecordDto("Yuri");
        this.createAuthor(authorData);
        PublisherRecordDto publisherData = new PublisherRecordDto("Privalia");
        this.createPublisher(publisherData);
        BookRecordDto bookData = new BookRecordDto(
                bookTitle,
                publisherRepository.findByName("Privalia").get().getId(),
                Set.of(authorRepository.findByName("Yuri").get().getId()),
                "Pretty nice!");
        this.createBook(bookData);

        Optional<BookModel> result = Optional.ofNullable(this.bookRepository.findBookModelByTitle(bookTitle));

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("should not get Book from DB when book not exists")
    public void findBookByTitleCase2() throws Exception {

        String bookTitle = "Dexter is delicious";

        Optional<BookModel> result = Optional.ofNullable(this.bookRepository.findBookModelByTitle(bookTitle));

        assertThat(result.isEmpty()).isTrue();
    }

    protected PublisherModel createPublisher(PublisherRecordDto data) {
        PublisherModel newPublisher = new PublisherModel();
        newPublisher.setName(data.name());
        this.entityManager.persist(newPublisher);
        return newPublisher;
    }

    protected AuthorModel createAuthor(AuthorRecordDto data) {
        AuthorModel newAuthor = new AuthorModel();
        newAuthor.setName(data.name());
        this.entityManager.persist(newAuthor);
        return newAuthor;
    }

    protected BookModel createBook(BookRecordDto data) {
        BookModel newBook = new BookModel();
        newBook.setTitle(data.title());
        newBook.setPublisher(publisherRepository.findById(data.publiserIds()).get());
        newBook.setAuthors(authorRepository.findAllById(data.authorIds()).stream().collect(Collectors.toSet()));

        ReviewModel newReview = new ReviewModel();
        newReview.setComment(data.reviewComment());
        newReview.setBook(newBook);
        newBook.setReview(newReview);

        this.entityManager.persist(newBook);
        return newBook;
    }

}

