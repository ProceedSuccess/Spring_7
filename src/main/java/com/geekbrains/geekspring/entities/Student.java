package com.geekbrains.geekspring.entities;

import com.geekbrains.geekspring.validation.ValidEmail;
import com.geekbrains.geekspring.validation.ValidPhone;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull(message = "is required")
    @Size(min = 2, message = "is required")
    private String name;

    @Column(name = "lastname")
    @NotNull(message = "is required")
    @Size(min = 2, message = "is required")
    private String lastname;
    @ValidPhone
    @Column(name = "phone")
    @NotNull(message = "is required")
    @Size(min = 10, message = "is required")
    private String phone;
    @ValidEmail
    @Column(name = "email")
    @NotNull(message = "is required")
    @Size(min = 5, message = "is required")
    private String email;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @Column(name = "date_of_addition")
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToMany
    @JoinTable(
            name = "students_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Cascade({org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.DETACH})
    private List<Course> courses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user){this.user = user;}

    public User getUser(){return user;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Student() {
    }

    @Override
    public String toString() {
        return "Student id=" + id + ", courses=" + courses.size() + '}';
    }
}
