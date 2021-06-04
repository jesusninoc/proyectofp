package com.diegoflores.onlineacademy.service.impl;

import com.diegoflores.onlineacademy.domain.Course;
import com.diegoflores.onlineacademy.domain.Lesson;
import com.diegoflores.onlineacademy.helpers.Helper;
import com.diegoflores.onlineacademy.repository.CourseRepository;
import com.diegoflores.onlineacademy.repository.LessonRepository;
import com.diegoflores.onlineacademy.service.CourseService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.ITemplateContext;

/**
 * Service Implementation for managing {@link Course}.
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public CourseServiceImpl(CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Course save(Course course) {
        log.debug("Request to save Course : {}", course);
        final String KEY = "AIzaSyAPXBj0L-1JpadFkiEKxFDGag50Ifakp6w";
        final String BASE_URL = "https://youtube.googleapis.com/youtube/v3/";
        // final String CHANNEL_ID = "UCQpUnXIJ8mQLTzQOoNkxzUg";
        // final String API_URL = BASE_URL+"playlists?part=snippet&channelId="+CHANNEL_ID+"&key="+KEY;

        Course courseGuardo = courseRepository.save(course);

        Set<Lesson> lessons = getVideos(courseGuardo, courseGuardo.getLink(), BASE_URL, KEY);
        lessons.forEach(
            lesson -> {
                lessonRepository.save(lesson);
            }
        );

        return courseGuardo;
    }

    @Override
    public Optional<Course> partialUpdate(Course course) {
        log.debug("Request to partially update Course : {}", course);

        return courseRepository
            .findById(course.getId())
            .map(
                existingCourse -> {
                    if (course.getName() != null) {
                        existingCourse.setName(course.getName());
                    }
                    if (course.getDescription() != null) {
                        existingCourse.setDescription(course.getDescription());
                    }
                    if (course.getImage() != null) {
                        existingCourse.setImage(course.getImage());
                    }

                    return existingCourse;
                }
            )
            .map(courseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Course> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");
        return courseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findOne(Long id) {
        log.debug("Request to get Course : {}", id);
        return courseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Course : {}", id);
        courseRepository.deleteById(id);
    }

    private Set<Lesson> getVideos(Course course, String url, String baseUrl, String key) {
        String idPlaylist = url.split("=")[1];
        String urlVideos = baseUrl + "playlistItems?part=snippet&playlistId=" + idPlaylist + "&maxResults=50&key=" + key;
        JsonObject res = Helper.obtieneJson(urlVideos);
        JsonArray arr = res.get("items").getAsJsonArray();
        Set<Lesson> lessons = new HashSet<Lesson>();

        int i = 0;
        while (arr.iterator().next() != null) {
            JsonObject item = arr.get(i).getAsJsonObject().get("snippet").getAsJsonObject();
            Lesson lesson = new Lesson();
            lesson.setName(item.get("title").toString());
            lesson.setDescription(item.get("description").toString());
            lesson.setLink("https://www.youtube.com/watch?v=" + item.get("resourceId").getAsJsonObject().get("videoId").toString());
            lesson.setCourse(course);
            lessons.add(lesson);
            i++;
        }
        return lessons;
    }
}
