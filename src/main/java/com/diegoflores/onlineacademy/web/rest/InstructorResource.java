package com.diegoflores.onlineacademy.web.rest;

import com.diegoflores.onlineacademy.domain.Instructor;
import com.diegoflores.onlineacademy.repository.InstructorRepository;
import com.diegoflores.onlineacademy.service.InstructorService;
import com.diegoflores.onlineacademy.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.diegoflores.onlineacademy.domain.Instructor}.
 */
@RestController
@RequestMapping("/api")
public class InstructorResource {

    private final Logger log = LoggerFactory.getLogger(InstructorResource.class);

    private static final String ENTITY_NAME = "instructor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstructorService instructorService;

    private final InstructorRepository instructorRepository;

    public InstructorResource(InstructorService instructorService, InstructorRepository instructorRepository) {
        this.instructorService = instructorService;
        this.instructorRepository = instructorRepository;
    }

    /**
     * {@code POST  /instructors} : Create a new instructor.
     *
     * @param instructor the instructor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instructor, or with status {@code 400 (Bad Request)} if the instructor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/instructors")
    public ResponseEntity<Instructor> createInstructor(@Valid @RequestBody Instructor instructor) throws URISyntaxException {
        log.debug("REST request to save Instructor : {}", instructor);
        if (instructor.getId() != null) {
            throw new BadRequestAlertException("A new instructor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Instructor result = instructorService.save(instructor);
        return ResponseEntity
            .created(new URI("/api/instructors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /instructors/:id} : Updates an existing instructor.
     *
     * @param id the id of the instructor to save.
     * @param instructor the instructor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instructor,
     * or with status {@code 400 (Bad Request)} if the instructor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instructor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/instructors/{id}")
    public ResponseEntity<Instructor> updateInstructor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Instructor instructor
    ) throws URISyntaxException {
        log.debug("REST request to update Instructor : {}, {}", id, instructor);
        if (instructor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instructor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!instructorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Instructor result = instructorService.save(instructor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, instructor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /instructors/:id} : Partial updates given fields of an existing instructor, field will ignore if it is null
     *
     * @param id the id of the instructor to save.
     * @param instructor the instructor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instructor,
     * or with status {@code 400 (Bad Request)} if the instructor is not valid,
     * or with status {@code 404 (Not Found)} if the instructor is not found,
     * or with status {@code 500 (Internal Server Error)} if the instructor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/instructors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Instructor> partialUpdateInstructor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Instructor instructor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Instructor partially : {}, {}", id, instructor);
        if (instructor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instructor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!instructorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Instructor> result = instructorService.partialUpdate(instructor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, instructor.getId().toString())
        );
    }

    /**
     * {@code GET  /instructors} : get all the instructors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instructors in body.
     */
    @GetMapping("/instructors")
    public ResponseEntity<List<Instructor>> getAllInstructors(Pageable pageable) {
        log.debug("REST request to get a page of Instructors");
        Page<Instructor> page = instructorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /instructors/:id} : get the "id" instructor.
     *
     * @param id the id of the instructor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instructor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/instructors/{id}")
    public ResponseEntity<Instructor> getInstructor(@PathVariable Long id) {
        log.debug("REST request to get Instructor : {}", id);
        Optional<Instructor> instructor = instructorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(instructor);
    }

    /**
     * {@code DELETE  /instructors/:id} : delete the "id" instructor.
     *
     * @param id the id of the instructor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/instructors/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        log.debug("REST request to delete Instructor : {}", id);
        instructorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
