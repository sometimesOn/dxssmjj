package com.jia.dxssmjj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.dxssmjj.model.dto.MakeAppointmentDTO;
import com.jia.dxssmjj.model.dto.MyAppointmentDTO;
import com.jia.dxssmjj.model.dto.TutorAcceptDTO;
import com.jia.dxssmjj.model.po.makeAppointment.MakeAppointment;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MakeAppointmentService extends IService<MakeAppointment> {

    List<MyAppointmentDTO> findMakeAppointmentByStatus(MakeAppointment makeAppointment, HttpServletRequest request);

    boolean deleteMakeAppointment(MyAppointmentDTO myAppointmentDTO);

    boolean cancelMakeAppointment(MyAppointmentDTO myAppointmentDTO);

    boolean AgainMakeAppointmentById(MyAppointmentDTO myAppointmentDTO);

    List<String> getHourStringList(Long userId);

    List<TutorAcceptDTO> findMakeAppointmentByStatusFromTutor(MakeAppointment makeAppointment, HttpServletRequest request);

    List<MakeAppointment> findComplete(Long tutorId);
}
