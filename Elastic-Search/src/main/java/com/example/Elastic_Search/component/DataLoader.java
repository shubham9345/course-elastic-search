package com.example.Elastic_Search.component;

import com.example.Elastic_Search.document.CourseDocument;
import com.example.Elastic_Search.repository.CourseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Loads sample course data into Elasticsearch on application startup, but only if no courses
 * are already indexed. This component is active exclusively in "dev" and "local" profiles.
 */
@Slf4j
@Component
@Profile({"dev", "local"})
public class DataLoader implements CommandLineRunner {

    private final CourseRepository repo;
    private final ObjectMapper mapper;

    @Value("classpath:data/sample-courses.json")
    private Resource sampleCourses;

    /**
     * Constructs a DataLoader with the required dependencies.
     *
     * @param repo   the repository for interacting with Elasticsearch
     * @param mapper the Jackson ObjectMapper for JSON deserialization
     */
    public DataLoader(CourseRepository repo, ObjectMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    /**
     * Executes the data loading logic when the application starts.
     * Checks if courses are already indexed and, if not, loads sample data from a JSON file.
     *
     * @param args command-line arguments (not used)
     */
    @Override
    public void run(String... args) {
        // Check if courses are already indexed
        long count = repo.count();
        if (count > 0) {
            log.info("{} courses already indexed, skipping sample load", count);
            return;
        }

        // Load sample courses from JSON file
        log.info("Loading sample data from {}", sampleCourses.getFilename());
        try (InputStream is = sampleCourses.getInputStream()) {
            List<CourseDocument> courses = mapper.readValue(
                    is,
                    new TypeReference<List<CourseDocument>>() {
                    }
            );
            // Save courses to Elasticsearch
            repo.saveAll(courses);
            log.info("Indexed {} sample courses", courses.size());
        } catch (JsonProcessingException e) {
            log.error("Failed to parse sample data JSON", e);
        } catch (IOException e) {
            log.error("Failed to read sample data file", e);
        }
    }
}