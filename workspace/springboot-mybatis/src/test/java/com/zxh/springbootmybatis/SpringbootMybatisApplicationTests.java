package com.zxh.springbootmybatis;

import com.zxh.springbootmybatis.entity.Student;
import com.zxh.springbootmybatis.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringbootMybatisApplicationTests {

    @Autowired
    private StudentService studentService;

    @Test
    void contextLoads() {
        assertNotNull(studentService);
    }

    @Test
    void testAddStudent() {
        Student student = new Student();
        student.setStudentNo("2024006");
        student.setName("测试学生");
        student.setAge(21);
        student.setGender("男");
        student.setClassName("计算机 3 班");
        
        boolean result = studentService.add(student);
        assertTrue(result, "添加学生失败");
        System.out.println("添加学生成功，ID: " + student.getId());
    }

    @Test
    void testGetById() {
        Student student = studentService.getById(1L);
        assertNotNull(student, "查询学生为空");
        System.out.println("查询结果：" + student.getName());
    }

    @Test
    void testListAll() {
        List<Student> students = studentService.listAll();
        assertNotNull(students, "学生列表为空");
        System.out.println("学生总数：" + students.size());
        students.forEach(s -> System.out.println(s.getName()));
    }

    @Test
    void testUpdateStudent() {
        Student student = studentService.getById(1L);
        assertNotNull(student, "查询学生为空");
        
        student.setName("张三丰");
        student.setAge(25);
        
        boolean result = studentService.update(student);
        assertTrue(result, "更新学生失败");
        System.out.println("更新学生成功");
    }

    @Test
    void testDeleteStudent() {
        // 先添加一个学生
        Student student = new Student();
        student.setStudentNo("2024007");
        student.setName("待删除学生");
        student.setAge(20);
        student.setGender("女");
        student.setClassName("测试班级");
        studentService.add(student);
        
        // 删除该学生
        boolean result = studentService.delete(student.getId());
        assertTrue(result, "删除学生失败");
        System.out.println("删除学生成功");
    }

    @Test
    void testListByCondition() {
        List<Student> students = studentService.listByCondition("张", null);
        assertNotNull(students, "条件查询结果为空");
        System.out.println("姓张的学生数：" + students.size());
        students.forEach(s -> System.out.println(s.getName()));
    }
}

