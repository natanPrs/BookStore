package com.bookstore.jpa.services;

import com.bookstore.jpa.dtos.PublisherRecordDto;
import com.bookstore.jpa.models.PublisherModel;
import com.bookstore.jpa.repositories.PublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<PublisherModel> getAllPublishers() { return publisherRepository.findAll(); }

    @Transactional
    public void deletePublisher(UUID id) { publisherRepository.deleteById(id); }

    @Transactional
    public PublisherModel savePublisher(PublisherRecordDto publisherRecordDto) {
        PublisherModel publisher = new PublisherModel();

        publisher.setName(publisherRecordDto.name());

        return publisherRepository.save(publisher);
    }
}
