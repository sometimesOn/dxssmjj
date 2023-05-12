package com.jia.dxssmjj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jia.dxssmjj.model.po.user.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
