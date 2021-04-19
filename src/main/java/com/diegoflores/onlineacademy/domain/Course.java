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
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "image", nullable = false)
    private String image;

    @OneToMany(mappedBy = "course")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "course" }, allowSetters = true)
    private Set<Lesson> lessons = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "courses" }, allowSetters = true)
    private Instructor instructor;

    @ManyToMany(mappedBy = "courses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "courses" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Course name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Course description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return this.image;
    }

    public Course image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Lesson> getLessons() {
        return this.lessons;
    }

    public Course lessons(Set<Lesson> lessons) {
        this.setLessons(lessons);
        return this;
    }

    public Course addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.setCourse(this);
        return this;
    }

    public Course removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.setCourse(null);
        return this;
    }

    public void setLessons(Set<Lesson> lessons) {
        if (this.lessons != null) {
            this.lessons.forEach(i -> i.setCourse(null));
        }
        if (lessons != null) {
            lessons.forEach(i -> i.setCourse(this));
        }
        this.lessons = lessons;
    }

    public Instructor getInstructor() {
        return this.instructor;
    }

    public Course instructor(Instructor instructor) {
        this.setInstructor(instructor);
        return this;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public Course students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public Course addStudent(Student student) {
        this.students.add(student);
        student.getCourses().add(this);
        return this;
    }

    public Course removeStudent(Student student) {
        this.students.remove(student);
        student.getCourses().remove(this);
        return this;
    }

    public void setStudents(Set<Student> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.removeCourse(this));
        }
        if (students != null) {
            students.forEach(i -> i.addCourse(this));
        }
        this.students = students;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
