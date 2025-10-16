package com.documentsearch.docsearchbackend.service;

import com.documentsearch.docsearchbackend.model.DocumentModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.index.query.MultiMatchQueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class DocumentService {

    private static final String INDEX = "documents";
    private final RestHighLevelClient client;
    @Autowired
    public DocumentService(RestHighLevelClient client) {
        this.client = client;
        try {
            ensureIndexExists();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize index", e);
        }
    }

    private void ensureIndexExists() throws IOException {
        if (client.indices().exists(new GetIndexRequest(INDEX), RequestOptions.DEFAULT)) return;

        String mapping = "{" +
                "\"mappings\": {" +
                "\"properties\": {" +
                "\"id\": { \"type\": \"keyword\" }," +
                "\"title\": { \"type\": \"text\", \"analyzer\": \"standard\", " +
                "\"fields\": { \"keyword\": { \"type\": \"keyword\" } } }," +
                "\"content\": { \"type\": \"text\", \"analyzer\": \"standard\" }," +
                "\"timestamp\": { \"type\": \"date\" }" +
                "}" +
                "}" +
                "}";

        CreateIndexRequest req = new CreateIndexRequest(INDEX).source(mapping, XContentType.JSON);
        client.indices().create(req, RequestOptions.DEFAULT);
    }

    public String indexDocument(DocumentModel doc) throws IOException {
        if (!doc.isValid()) throw new IllegalArgumentException("Missing required fields");

        Map<String, Object> data = new HashMap<>();
        data.put("id", doc.getId());
        data.put("title", doc.getTitle());
        data.put("content", doc.getContent());
        data.put("timestamp", System.currentTimeMillis());

        IndexRequest req = new IndexRequest(INDEX).id(doc.getId()).source(data);
        IndexResponse res = client.index(req, RequestOptions.DEFAULT);
        return res.getId();
    }

    public List<DocumentModel> searchDocuments(String query) throws IOException {
        return searchDocuments(query, 20);
    }

    public List<DocumentModel> searchDocuments(String query, int limit) throws IOException {
        SearchRequest req = new SearchRequest(INDEX);
        SearchSourceBuilder builder = new SearchSourceBuilder();

        if (query == null || query.trim().isEmpty()) {
            builder.query(QueryBuilders.matchAllQuery());
        } else {
            builder.query(QueryBuilders.multiMatchQuery(query, "title", "content")
                    .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
                    .fuzziness("AUTO"));
        }

        builder.size(limit);
        req.source(builder);

        SearchResponse res = client.search(req, RequestOptions.DEFAULT);
        return parseResults(res);
    }

    private List<DocumentModel> parseResults(SearchResponse res) {
        List<DocumentModel> docs = new ArrayList<>();
        for (SearchHit hit : res.getHits().getHits()) {
            try {
                Map<String, Object> src = hit.getSourceAsMap();
                DocumentModel doc = new DocumentModel();
                doc.setId(get(src, "id"));
                doc.setTitle(get(src, "title"));
                doc.setContent(get(src, "content"));
                docs.add(doc);
            } catch (Exception ignored) { }
        }
        return docs;
    }

    private String get(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return v == null ? "" : v.toString();
    }

    public boolean isHealthy() {
        try {
            return client.ping(RequestOptions.DEFAULT);
        } catch (IOException e) {
            return false;
        }
    }
}
