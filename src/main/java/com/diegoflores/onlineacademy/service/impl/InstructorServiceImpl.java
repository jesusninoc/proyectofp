package com.diegoflores.onlineacademy.service.impl;

import com.diegoflores.onlineacademy.domain.Instructor;
import com.diegoflores.onlineacademy.repository.InstructorRepository;
import com.diegoflores.onlineacademy.service.InstructorService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Instructor}.
 */
@Service
@Transactional
public class InstructorServiceImpl implements InstructorService {

    private final Logger log = LoggerFactory.getLogger(InstructorServiceImpl.class);

    private final InstructorRepository instructorRepository;

    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public Instructor save(Instructor instructor) {
        log.debug("Request to save Instructor : {}", instructor);
        return instructorRepository.save(instructor);
    }

    @Override
    public Optional<Instructor> partialUpdate(Instructor instructor) {
        log.debug("Request to partially update Instructor : {}", instructor);

        return instructorRepository
            .findById(instructor.getId())
            .map(
                existingInstructor -> {
                    if (instructor.getUsername() != null) {
                        existingInstructor.setUsername(instructor.getUsername());
                    }
                    if (instructor.getPassword() != null) {
                        existingInstructor.setPassword(instructor.getPassword());
                    }
                    if (instructor.getName() != null) {
                        existingInstructor.setName(instructor.getName());
                    }
                    if (instructor.getLastname() != null) {
                        existingInstructor.setLastname(instructor.getLastname());
                    }
                    if (instructor.getImage() != null) {
                        existingInstructor.setImage(instructor.getImage());
                    }

                    return existingInstructor;
                }
            )
            .map(instructorRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Instructor> findAll(Pageable pageable) {
        log.debug("Request to get all Instructors");
        return instructorRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Instructor> findOne(Long id) {
        log.debug("Request to get Instructor : {}", id);
        return instructorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Instructor : {}", id);
        instructorRepository.deleteById(id);
    }
}
