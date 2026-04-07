package com.zxh.springbootmybatis.mapper;

import com.zxh.springbootmybatis.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生 Mapper 接口
 */
@Mapper
public interface StudentMapper {

    /**
     * 查询所有学生
     */
    List<Student> listAll();

    /**
     * 根据 ID 查询学生
     */
    Student getById(Long id);

    /**
     * 添加学生
     */
    int add(Student student);

    /**
     * 更新学生信息
     */
    int update(Student student);

    /**
     * 删除学生
     */
    int delete(Long id);

    /**
     * 批量删除学生
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 根据条件查询学生
     */
    List<Student> listByCondition(@Param("name") String name, @Param("className") String className);
}
