package com.example.Elastic_Search.repository;

import com.example.Elastic_Search.document.CourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CourseRepository extends ElasticsearchRepository<CourseDocument, String> { }