package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    @Transactional
    public void deductBalance(Long id, Integer money) {
        // 1.查询用户
        User user = getById(id);
        /*User user = lambdaQuery()
                .eq(User::getId, id)
                .last("FOR UPDATE")  // ✅ 查询加锁！
                .one();*/
        // 2.判断用户状态
        if (user == null || user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户状态异常");
        }
        // 3.判断用户余额
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足");
        }
        // 4.扣减余额
//        baseMapper.deductMoneyById(id, money);
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getBalance, remainBalance)
                .set(remainBalance == 0, User::getStatus, UserStatus.FROZEN)
                .eq(User::getId, id)
                .eq(User::getBalance, user.getBalance()) // 乐观锁
                .update();
    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
        return lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .ge(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();
    }

    @Override
    public UserVO queryUserAddressById(Long id) {
        // 查询用户
        User user = getById(id);
        if(user == null || user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户状态异常");
        }

        // 查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id)
                .list();

        // 封装VO
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        if (CollUtil.isNotEmpty(addresses)) {
            userVO.setAddresses(BeanUtil.copyToList(addresses, AddressVO.class));
        }
        return userVO;
    }

    @Override
    public List<UserVO> queryUserAndAddressByIds(List<Long> ids) {
        // 查询用户
        List<User> users = listByIds(ids);
        if (CollUtil.isEmpty(users)) {
            return Collections.emptyList();
        }

        // 查询地址
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, userIds)
                .list();
        Map<Long, List<AddressVO>> addressMap = new HashMap<>();
        if (CollUtil.isNotEmpty(addresses)) {
            List<AddressVO> addressVOList = BeanUtil.copyToList(addresses, AddressVO.class);
            addressMap = addressVOList.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }

        // 封装VO
        List<UserVO> list = new ArrayList<>(users.size());
        for (User user : users) {
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            userVO.setAddresses(addressMap.get(user.getId()));
            list.add(userVO);
        }
        return list;
    }

    @Override
    public PageDTO<UserVO> queryUsersPage(UserQuery query) {
        String name = query.getName();
        Integer status = query.getStatus();

        /*Page<User> page = Page.of(query.getPageNo(), query.getPageSize());
//        page.addOrder(new OrderItem(query.getSortBy(), query.getIsAsc()));
        if(StrUtil.isNotBlank(query.getSortBy())) {
            page.addOrder(query.getIsAsc() ? OrderItem.asc(query.getSortBy()) : OrderItem.desc(query.getSortBy()));
        }else {
//            page.addOrder(query.getIsAsc() ? OrderItem.asc("update_time") : OrderItem.desc("update_time"));
            page.addOrder(OrderItem.desc("update_time"));
        }*/
        Page<User> page = query.toMpPageDefaultSortByUpdateTimeDesc();

        Page<User> p = lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .page(page);

        /*PageDTO<UserVO> dto = new PageDTO<>();
        dto.setTotal(p.getTotal());
        dto.setPages(p.getPages());
        List<User> records = p.getRecords();
        if(CollUtil.isEmpty(records)) {
            dto.setList(Collections.emptyList());
            return dto;
        }
        List<UserVO> list = BeanUtil.copyToList(records, UserVO.class);
        dto.setList(list);
        return dto;*/
//        return PageDTO.of(p, UserVO.class);
//        return PageDTO.of(p, user -> BeanUtil.copyProperties(user, UserVO.class));
        return PageDTO.of(p,user -> {
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            userVO.setUsername(user.getUsername().substring(0, userVO.getUsername().length() - 2) + "**");
            return userVO;
        });
    }
}
