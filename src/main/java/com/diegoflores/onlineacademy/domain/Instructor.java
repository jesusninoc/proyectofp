package com.diegoflores.onlineacademy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Instructor.
 */
@Entity
@Table(name = "instructor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Instructor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "instructor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "lessons", "instructor", "students" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instructor id(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public Instructor username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public Instructor password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public Instructor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return this.lastname;
    }

    public Instructor lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getImage() {
        return this.image;
    }

    public Instructor image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public Instructor courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Instructor addCourse(Course course) {
        this.courses.add(course);
        course.setInstructor(this);
        return this;
    }

    public Instructor removeCourse(Course course) {
        this.courses.remove(course);
        course.setInstructor(null);
        return this;
    }

    public void setCourses(Set<Course> courses) {
        if (this.courses != null) {
            this.courses.forEach(i -> i.setInstructor(null));
        }
        if (courses != null) {
            courses.forEach(i -> i.setInstructor(this));
        }
        this.courses = courses;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Instructor)) {
            return false;
        }
        return id != null && id.equals(((Instructor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Instructor{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", name='" + getName() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
