package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.Student;
import com.geekbrains.geekspring.entities.User;
import com.geekbrains.geekspring.services.StudentsService;
import com.geekbrains.geekspring.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping("/students")
@Transactional
public class StudentsController {
    private StudentsService studentsService;
    private UserServiceImpl userService;

    @Autowired
    public void setStudentsService(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @Autowired
    public void setUserService(UserServiceImpl userService){this.userService = userService;}


    @RequestMapping("/list")
    @Transactional
    public String showStudentsList(Model model, Principal principal) {
        List<Student> allStudents = studentsService.getAllStudentsList();
        model.addAttribute("studentsList", allStudents);
        User user = userService.findByUserName(principal.getName());
        model.addAttribute("user",user);
        return "students-list";
    }

    @Secured({"ROLE_ADMIN","ROLE_MANAGER"})
    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String showAddForm(Model model, Principal principal) {
        Student student = new Student();
        student.setName("Unknown");
        student.setDate(new Date());
        model.addAttribute("student", student);
        return "add-student-form";
    }
    @Secured({"ROLE_ADMIN","ROLE_MANAGER"})
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String showAddForm(@Valid @ModelAttribute("student")Student student,
                                                     Principal principal,
                                                     Model model ,
                                                     BindingResult theBindingResult) {
        if (theBindingResult.hasErrors()) {
            return "add-student-form";
        }
        User user = userService.findByUserName(principal.getName());
        Student dataBaseStudent = new Student();
        dataBaseStudent.setName(student.getName());
        dataBaseStudent.setLastname(student.getLastname());
        dataBaseStudent.setEmail(student.getEmail());
        dataBaseStudent.setPhone(student.getPhone());
        dataBaseStudent.setUser(student.getUser());
        student.setUser(user);
        studentsService.addStudent(dataBaseStudent);
        model.addAttribute("student", student);
        model.addAttribute("user",user);
        return "redirect:/students/list";
    }

    @Secured({"ROLE_ADMIN","ROLE_MANAGER"})
    @RequestMapping(path = "/remove/{id}", method = RequestMethod.GET)
    public String removeById(@PathVariable(value = "id") Long studentId) {
        studentsService.removeById(studentId);
        return "redirect:/students/list";
    }

    @RequestMapping(path = "/courses/{id}", method = RequestMethod.GET)
    public String showStudentsCoursesInfo(Model model, @PathVariable(value = "id") Long studentId) {
        model.addAttribute("studentId", studentId);
        model.addAttribute("studentCourses", studentsService.getCoursesByStudentId(studentId));
        model.addAttribute("studentMissingCourses", studentsService.getMissingCoursesByStudentId(studentId));
        return "student-courses-list";
    }
    @Secured({"ROLE_ADMIN","ROLE_MANAGER"})
    @RequestMapping(path = "/courses/add/{studentId}/{courseId}", method = RequestMethod.GET)
    public String addCourseToStudent(Model model, @PathVariable(value = "studentId") Long studentId, @PathVariable(value = "courseId") Long courseId) {
        studentsService.addCourseToStudent(studentId, courseId);
        return "redirect:/students/courses/" + studentId;
    }
    @Secured({"ROLE_ADMIN","ROLE_MANAGER"})
    @RequestMapping(path = "/courses/remove/{studentId}/{courseId}", method = RequestMethod.GET)
    public String removeCourseFromStudent(Model model, @PathVariable(value = "studentId") Long studentId, @PathVariable(value = "courseId") Long courseId) {
        studentsService.removeCourseFromStudent(studentId, courseId);
        return "redirect:/students/courses/" + studentId;
    }
}
