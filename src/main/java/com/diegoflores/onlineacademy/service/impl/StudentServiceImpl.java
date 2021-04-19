package com.diegoflores.onlineacademy.service.impl;

import com.diegoflores.onlineacademy.domain.Student;
import com.diegoflores.onlineacademy.repository.StudentRepository;
import com.diegoflores.onlineacademy.service.StudentService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Student}.
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student save(Student student) {
        log.debug("Request to save Student : {}", student);
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> partialUpdate(Student student) {
        log.debug("Request to partially update Student : {}", student);

        return studentRepository
            .findById(student.getId())
            .map(
                existingStudent -> {
                    if (student.getUsername() != null) {
                        existingStudent.setUsername(student.getUsername());
                    }
                    if (student.getPassword() != null) {
                        existingStudent.setPassword(student.getPassword());
                    }
                    if (student.getName() != null) {
                        existingStudent.setName(student.getName());
                    }
                    if (student.getLastname() != null) {
                        existingStudent.setLastname(student.getLastname());
                    }
                    if (student.getImage() != null) {
                        existingStudent.setImage(student.getImage());
                    }

                    return existingStudent;
                }
            )
            .map(studentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Student> findAll(Pageable pageable) {
        log.debug("Request to get all Students");
        return studentRepository.findAll(pageable);
    }

    public Page<Student> findAllWithEagerRelationships(Pageable pageable) {
        return studentRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findOne(Long id) {
        log.debug("Request to get Student : {}", id);
        return studentRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Student : {}", id);
        studentRepository.deleteById(id);
    }
}
