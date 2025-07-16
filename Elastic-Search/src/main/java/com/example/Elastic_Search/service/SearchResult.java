package com.example.Elastic_Search.service;

import com.example.Elastic_Search.document.CourseDocument;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchResult {
    private  long total;
    private List<CourseDocument> courses;

    public SearchResult(long total, List<CourseDocument> courses) {
        this.total = total;
        this.courses = courses;
    }

}