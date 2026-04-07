-- 创建数据库
CREATE DATABASE IF NOT EXISTS `springboot_mybatis` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `springboot_mybatis`;

-- 创建学生表
DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `student_no` varchar(50) DEFAULT NULL COMMENT '学号',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- 插入测试数据
INSERT INTO `student` (`student_no`, `name`, `age`, `gender`, `class_name`) VALUES
('2024001', '张三', 20, '男', '计算机 1 班'),
('2024002', '李四', 21, '男', '计算机 1 班'),
('2024003', '王五', 19, '女', '计算机 2 班'),
('2024004', '赵六', 22, '男', '计算机 2 班'),
('2024005', '孙七', 20, '女', '软件 1 班');
