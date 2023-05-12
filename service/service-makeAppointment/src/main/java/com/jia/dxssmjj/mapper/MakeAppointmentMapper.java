package com.jia.dxssmjj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jia.dxssmjj.model.dto.MakeAppointmentDTO;
import com.jia.dxssmjj.model.po.makeAppointment.MakeAppointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MakeAppointmentMapper extends BaseMapper<MakeAppointment> {

    List<String> getHourStringList(@Param("userId") Long userId);

    List<MakeAppointment> findComplete(@Param("tutorId") Long tutorId);
}
