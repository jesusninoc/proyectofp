package com.diegoflores.onlineacademy.service;

import com.diegoflores.onlineacademy.domain.Instructor;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Instructor}.
 */
public interface InstructorService {
    /**
     * Save a instructor.
     *
     * @param instructor the entity to save.
     * @return the persisted entity.
     */
    Instructor save(Instructor instructor);

    /**
     * Partially updates a instructor.
     *
     * @param instructor the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Instructor> partialUpdate(Instructor instructor);

    /**
     * Get all the instructors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Instructor> findAll(Pageable pageable);

    /**
     * Get the "id" instructor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Instructor> findOne(Long id);

    /**
     * Delete the "id" instructor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
