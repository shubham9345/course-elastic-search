package com.example.Elastic_Search.service;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchRequestParam {
    private  String q;
    private  Integer minAge;
    private  Integer maxAge;
    private  String category;
    private  String type;
    private  Double minPrice;
    private  Double maxPrice;
    private LocalDate nextSessionDate;
    private  String sort;
    private  int page;
    private  int size;

    public SearchRequestParam(
            String q,
            Integer minAge,
            Integer maxAge,
            String category,
            String type,
            Double minPrice,
            Double maxPrice,
            LocalDate nextSessionDate,
            String sort,
            int page,
            int size
    ) {
        this.q = q;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.category = category;
        this.type = type;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.nextSessionDate = nextSessionDate;
        this.sort = sort;
        this.page = page;
        this.size = size;
    }
}

