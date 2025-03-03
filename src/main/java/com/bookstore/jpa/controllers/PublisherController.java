package com.bookstore.jpa.controllers;

import com.bookstore.jpa.dtos.PublisherRecordDto;
import com.bookstore.jpa.models.PublisherModel;
import com.bookstore.jpa.services.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookstore/publishers")
public class PublisherController {

    private final PublisherService publisherService;
    public PublisherController(PublisherService publisherService) { this.publisherService = publisherService; }

    @GetMapping
    public ResponseEntity<List<PublisherModel>> getAllPublishers(){
        return ResponseEntity.status(HttpStatus.OK).body(publisherService.getAllPublishers());
    }

    @PostMapping
    public ResponseEntity<PublisherModel> savePublisher(@RequestBody PublisherRecordDto publisherRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.savePublisher(publisherRecordDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePublisher(@PathVariable UUID id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.status(HttpStatus.OK).body("Publisher has been deleted!");
    }
}
