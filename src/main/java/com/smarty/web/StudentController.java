package com.smarty.web;

import com.smarty.domain.student.model.PasswordDTO;
import com.smarty.domain.student.model.StudentRequestDTO;
import com.smarty.domain.student.model.StudentResponseDTO;
import com.smarty.domain.student.model.StudentUpdateDTO;
import com.smarty.domain.student.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO studentDTO) {
        return ResponseEntity.ok(studentService.createStudent(studentDTO));
    }

    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(Pageable pageable) {
        return ResponseEntity.ok(studentService.getAllStudents(pageable));
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/by-major/{majorId}")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByMajor(@PathVariable Long majorId) {
        return ResponseEntity.ok(studentService.getStudentsByMajor(majorId));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/by-status/{statusId}")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByStudyStatus(@PathVariable Long statusId) {
        return ResponseEntity.ok(studentService.getStudentsByStudyStatus(statusId));
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/by-course-passed/{courseId}")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsWhoPassedCertainCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.getStudentsWhoPassedCertainCourse(courseId));
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentUpdateDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentDTO));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordDTO passwordDTO) {
        return ResponseEntity.ok(studentService.updatePassword(id, passwordDTO));
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

}
