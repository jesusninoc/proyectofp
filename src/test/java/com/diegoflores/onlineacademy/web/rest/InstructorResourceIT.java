package com.diegoflores.onlineacademy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diegoflores.onlineacademy.IntegrationTest;
import com.diegoflores.onlineacademy.domain.Instructor;
import com.diegoflores.onlineacademy.repository.InstructorRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InstructorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InstructorResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/instructors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstructorMockMvc;

    private Instructor instructor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instructor createEntity(EntityManager em) {
        Instructor instructor = new Instructor()
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .name(DEFAULT_NAME)
            .lastname(DEFAULT_LASTNAME)
            .image(DEFAULT_IMAGE);
        return instructor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instructor createUpdatedEntity(EntityManager em) {
        Instructor instructor = new Instructor()
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME)
            .lastname(UPDATED_LASTNAME)
            .image(UPDATED_IMAGE);
        return instructor;
    }

    @BeforeEach
    public void initTest() {
        instructor = createEntity(em);
    }

    @Test
    @Transactional
    void createInstructor() throws Exception {
        int databaseSizeBeforeCreate = instructorRepository.findAll().size();
        // Create the Instructor
        restInstructorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instructor)))
            .andExpect(status().isCreated());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeCreate + 1);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testInstructor.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testInstructor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstructor.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testInstructor.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void createInstructorWithExistingId() throws Exception {
        // Create the Instructor with an existing ID
        instructor.setId(1L);

        int databaseSizeBeforeCreate = instructorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstructorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instructor)))
            .andExpect(status().isBadRequest());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instructorRepository.findAll().size();
        // set the field null
        instructor.setUsername(null);

        // Create the Instructor, which fails.

        restInstructorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instructor)))
            .andExpect(status().isBadRequest());

        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = instructorRepository.findAll().size();
        // set the field null
        instructor.setPassword(null);

        // Create the Instructor, which fails.

        restInstructorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instructor)))
            .andExpect(status().isBadRequest());

        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instructorRepository.findAll().size();
        // set the field null
        instructor.setName(null);

        // Create the Instructor, which fails.

        restInstructorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instructor)))
            .andExpect(status().isBadRequest());

        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instructorRepository.findAll().size();
        // set the field null
        instructor.setLastname(null);

        // Create the Instructor, which fails.

        restInstructorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instructor)))
            .andExpect(status().isBadRequest());

        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInstructors() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        // Get all the instructorList
        restInstructorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instructor.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        // Get the instructor
        restInstructorMockMvc
            .perform(get(ENTITY_API_URL_ID, instructor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(instructor.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE));
    }

    @Test
    @Transactional
    void getNonExistingInstructor() throws Exception {
        // Get the instructor
        restInstructorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();

        // Update the instructor
        Instructor updatedInstructor = instructorRepository.findById(instructor.getId()).get();
        // Disconnect from session so that the updates on updatedInstructor are not directly saved in db
        em.detach(updatedInstructor);
        updatedInstructor
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME)
            .lastname(UPDATED_LASTNAME)
            .image(UPDATED_IMAGE);

        restInstructorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInstructor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInstructor))
            )
            .andExpect(status().isOk());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testInstructor.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testInstructor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstructor.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testInstructor.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void putNonExistingInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();
        instructor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstructorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, instructor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(instructor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();
        instructor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstructorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(instructor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();
        instructor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstructorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instructor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInstructorWithPatch() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();

        // Update the instructor using partial update
        Instructor partialUpdatedInstructor = new Instructor();
        partialUpdatedInstructor.setId(instructor.getId());

        partialUpdatedInstructor.username(UPDATED_USERNAME).password(UPDATED_PASSWORD).lastname(UPDATED_LASTNAME);

        restInstructorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstructor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstructor))
            )
            .andExpect(status().isOk());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testInstructor.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testInstructor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstructor.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testInstructor.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void fullUpdateInstructorWithPatch() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();

        // Update the instructor using partial update
        Instructor partialUpdatedInstructor = new Instructor();
        partialUpdatedInstructor.setId(instructor.getId());

        partialUpdatedInstructor
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME)
            .lastname(UPDATED_LASTNAME)
            .image(UPDATED_IMAGE);

        restInstructorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstructor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstructor))
            )
            .andExpect(status().isOk());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testInstructor.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testInstructor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstructor.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testInstructor.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void patchNonExistingInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();
        instructor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstructorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, instructor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(instructor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();
        instructor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstructorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(instructor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();
        instructor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstructorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(instructor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        int databaseSizeBeforeDelete = instructorRepository.findAll().size();

        // Delete the instructor
        restInstructorMockMvc
            .perform(delete(ENTITY_API_URL_ID, instructor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Instructor> instructorList = instructorRepository.findAll();
        assertThat(instructorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
