package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.dao.UserMapper;
import com.example.demo.pojo.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserMapper userMapper;

    @PostMapping
    public Result<?> save(@RequestBody User user){
        if(user.getPassword()==null)
        {
            user.setPassword("123");
        }
        User res=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if(res!=null){
           return  Result.error("-1","用户名已存在");
       }
       userMapper.insert(user);
       return Result.success();
    }
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user){
       User res=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword()));
       if(res==null){
           return  Result.error("-1","用户名与密码不一致");
       }
       return Result.success();
    }
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
       User res=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
       if(res!=null){
           return  Result.error("-1","用户名已存在");
       }
       if(user.getPassword()==null)
       {
           user.setPassword("123");
       }
       userMapper.insert(user);
       return Result.success();
    }
    @PutMapping
    public Result<?> update(@RequestBody User user){
        if(Objects.equals(user.getGender(), "1"))
        {
            user.setGender("男");
        }
        else if(Objects.equals(user.getGender(), "2")){
            user.setGender("女");
        }
        else{
             user.setGender("未知");
        }
       userMapper.updateById(user);
       return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> update(@PathVariable Long id){
       userMapper.deleteById(id);
       return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue ="1") Integer pageNum,
                              @RequestParam(defaultValue ="10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
       LambdaQueryWrapper<User> wrapper= Wrappers.<User>lambdaQuery();
       if(StrUtil.isNotBlank(search)){
            wrapper.like(User::getNickName,search);
       }
       Page<User> userPage=userMapper.selectPage(new Page<>(pageNum,pageSize),wrapper);
       return Result.success(userPage);
    }
}
