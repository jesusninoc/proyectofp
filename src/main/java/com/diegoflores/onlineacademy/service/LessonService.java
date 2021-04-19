package com.diegoflores.onlineacademy.service;

import com.diegoflores.onlineacademy.domain.Lesson;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Lesson}.
 */
public interface LessonService {
    /**
     * Save a lesson.
     *
     * @param lesson the entity to save.
     * @return the persisted entity.
     */
    Lesson save(Lesson lesson);

    /**
     * Partially updates a lesson.
     *
     * @param lesson the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Lesson> partialUpdate(Lesson lesson);

    /**
     * Get all the lessons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Lesson> findAll(Pageable pageable);

    /**
     * Get the "id" lesson.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Lesson> findOne(Long id);

    /**
     * Delete the "id" lesson.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
