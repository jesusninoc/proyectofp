package com.diegoflores.onlineacademy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diegoflores.onlineacademy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InstructorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Instructor.class);
        Instructor instructor1 = new Instructor();
        instructor1.setId(1L);
        Instructor instructor2 = new Instructor();
        instructor2.setId(instructor1.getId());
        assertThat(instructor1).isEqualTo(instructor2);
        instructor2.setId(2L);
        assertThat(instructor1).isNotEqualTo(instructor2);
        instructor1.setId(null);
        assertThat(instructor1).isNotEqualTo(instructor2);
    }
}
