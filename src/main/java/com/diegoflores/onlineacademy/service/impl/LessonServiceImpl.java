package com.diegoflores.onlineacademy.service.impl;

import com.diegoflores.onlineacademy.domain.Lesson;
import com.diegoflores.onlineacademy.repository.LessonRepository;
import com.diegoflores.onlineacademy.service.LessonService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Lesson}.
 */
@Service
@Transactional
public class LessonServiceImpl implements LessonService {

    private final Logger log = LoggerFactory.getLogger(LessonServiceImpl.class);

    private final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Lesson save(Lesson lesson) {
        log.debug("Request to save Lesson : {}", lesson);
        return lessonRepository.save(lesson);
    }

    @Override
    public Optional<Lesson> partialUpdate(Lesson lesson) {
        log.debug("Request to partially update Lesson : {}", lesson);

        return lessonRepository
            .findById(lesson.getId())
            .map(
                existingLesson -> {
                    if (lesson.getName() != null) {
                        existingLesson.setName(lesson.getName());
                    }
                    if (lesson.getDescription() != null) {
                        existingLesson.setDescription(lesson.getDescription());
                    }
                    if (lesson.getLink() != null) {
                        existingLesson.setLink(lesson.getLink());
                    }

                    return existingLesson;
                }
            )
            .map(lessonRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Lesson> findAll(Pageable pageable) {
        log.debug("Request to get all Lessons");
        return lessonRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lesson> findOne(Long id) {
        log.debug("Request to get Lesson : {}", id);
        return lessonRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lesson : {}", id);
        lessonRepository.deleteById(id);
    }
}
