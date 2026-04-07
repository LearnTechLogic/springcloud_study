package com.zxh.springbootmybatis.service;

import com.zxh.springbootmybatis.entity.Student;
import com.zxh.springbootmybatis.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学生服务类
 */
@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 查询所有学生
     */
    public List<Student> listAll() {
        return studentMapper.listAll();
    }

    /**
     * 根据 ID 查询学生
     */
    public Student getById(Long id) {
        return studentMapper.getById(id);
    }

    /**
     * 添加学生
     */
    public boolean add(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        return studentMapper.add(student) > 0;
    }

    /**
     * 更新学生信息
     */
    public boolean update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        return studentMapper.update(student) > 0;
    }

    /**
     * 删除学生
     */
    public boolean delete(Long id) {
        return studentMapper.delete(id) > 0;
    }

    /**
     * 批量删除学生
     */
    public boolean batchDelete(List<Long> ids) {
        return studentMapper.batchDelete(ids) > 0;
    }

    /**
     * 根据条件查询学生
     */
    public List<Student> listByCondition(String name, String className) {
        return studentMapper.listByCondition(name, className);
    }
}
