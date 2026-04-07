package com.zxh.springbootmybatis.controller;

import com.zxh.springbootmybatis.entity.Student;
import com.zxh.springbootmybatis.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生控制器
 */
@RestController
@RequestMapping("/api/students")
@Tag(name = "学生管理", description = "学生信息的增删改查操作")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 查询所有学生
     */
    @GetMapping
    @Operation(summary = "查询所有学生")
    public List<Student> listAll() {
        return studentService.listAll();
    }

    /**
     * 根据 ID 查询学生
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 查询学生")
    public Student getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    /**
     * 添加学生
     */
    @PostMapping
    @Operation(summary = "添加学生")
    public boolean add(@RequestBody Student student) {
        return studentService.add(student);
    }

    /**
     * 更新学生信息
     */
    @PutMapping
    @Operation(summary = "更新学生信息")
    public boolean update(@RequestBody Student student) {
        return studentService.update(student);
    }

    /**
     * 删除学生
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除学生")
    public boolean delete(@PathVariable Long id) {
        return studentService.delete(id);
    }

    /**
     * 批量删除学生
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除学生")
    public boolean batchDelete(@RequestParam List<Long> ids) {
        return studentService.batchDelete(ids);
    }

    /**
     * 根据条件查询学生
     */
    @GetMapping("/condition")
    @Operation(summary = "根据条件查询学生")
    public List<Student> listByCondition(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String className) {
        return studentService.listByCondition(name, className);
    }
}
