package com.documentsearch.docsearchbackend.controller;

import com.documentsearch.docsearchbackend.model.DocumentModel;
import com.documentsearch.docsearchbackend.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentController {

    private final DocumentService service;

    @Autowired
    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @PostMapping("/index")
    public ResponseEntity<?> index(@RequestBody DocumentModel doc) {
        if (doc == null || !doc.isValid()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid document payload");
            return ResponseEntity.badRequest().body(error);
        }

        try {
            String id = service.indexDocument(doc);
            Map<String, Object> result = new HashMap<>();
            result.put("message", "Document indexed");
            result.put("id", id);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Indexing failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {

        if (limit < 1 || limit > 100) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Limit must be between 1 and 100");
            return ResponseEntity.badRequest().body(error);
        }

        try {
            List<DocumentModel> results = service.searchDocuments(query, limit);
            Map<String, Object> response = new HashMap<>();
            response.put("query", query == null ? "" : query);
            response.put("total", results.size());
            response.put("documents", results);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Search failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        try {
            if (service.isHealthy()) {
                Map<String, Object> status = new HashMap<>();
                status.put("status", "healthy");
                return ResponseEntity.ok(status);
            }
            Map<String, Object> status = new HashMap<>();
            status.put("status", "unhealthy");
            status.put("message", "OpenSearch unavailable");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(status);
        } catch (Exception e) {
            Map<String, Object> status = new HashMap<>();
            status.put("status", "error");
            status.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(status);
        }
    }
}
