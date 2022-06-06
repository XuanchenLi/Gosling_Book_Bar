package com.dazzle.book_bar_back.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dazzle.book_bar_back.controller.request.StringPair;
import com.dazzle.book_bar_back.controller.response.AllocateHistoryResponse;
import com.dazzle.book_bar_back.dao.entity.*;
import com.dazzle.book_bar_back.response.ResponseResult;
import com.dazzle.book_bar_back.service.EmployeeService;
import com.dazzle.book_bar_back.utils.Constants;
import com.dazzle.book_bar_back.utils.FileUtil;
import com.dazzle.book_bar_back.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName:EmployeeController
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/30 上午 11:43
 * Version V1.0
 */
@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private FileUtil fileUtil;

    @GetMapping("/employee/position_{id}")
    @PreAuthorize("hasAuthority('employee::list')")
    @ResponseResult
    public List<DepartmentPositionPair> getPositionList(@PathVariable("id") Long id) {
        return employeeService.getPositionListById(id);
    }

    @GetMapping("/employee/delete_position/{employee_id}_{position_id}")
    @PreAuthorize("hasAuthority('employee::position::delete')")
    @ResponseResult
    public boolean
    deletePosition(
            @PathVariable("employee_id") Long eId,
            @PathVariable("position_id") Long pId
    ) {
        return employeeService.deletePosition(eId, pId);
    }
    @GetMapping("/employee/add_position/{employee_id}_{position_id}")
    @PreAuthorize("hasAuthority('employee::position::add')")
    @ResponseResult
    public boolean
    addPosition(
            @PathVariable("employee_id") Long eId,
            @PathVariable("position_id") Long pId
    ) {
        return employeeService.addPosition(eId, pId);
    }

    @GetMapping("/employee/allocate_history/{id}")
    @PreAuthorize("hasAuthority('allocate::history::list')")
    @ResponseResult
    public List<AllocateHistoryResponse>
    getAllocateHistory(@PathVariable("id") Long id) {
        return employeeService.selectAllocateHistory(id);
    }

    @GetMapping("/employee/delete/{id}")
    @PreAuthorize("hasAuthority('employee::delete')")
    @ResponseResult
    public boolean deleteEmployee(@PathVariable("id") Long id) {
        return employeeService.removeById(id);
    }

    @GetMapping("/employee/profile")
    @ResponseResult
    public Employee getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginEmployee employee = (LoginEmployee) authentication.getPrincipal();
        return employee.getEmployee();
    }

    @PostMapping("/employee/edit_profile")
    @ResponseResult
    public boolean editProfile(@RequestBody Employee employee) {
        return employeeService.editProfile(employee);
    }

    @PostMapping("/employee/change_password")
    @ResponseResult
    public boolean changePassword(@RequestBody StringPair pair) {
        return employeeService.changePassword(pair.getStr1(), pair.getStr2());
    }

    @PostMapping("/employee/uploadAvatar")
    @ResponseResult
    public String uploadAvatar(@RequestParam("file") MultipartFile file) throws Exception {
        String path = fileUtil.uploadFile(file, fileUtil.AVATAR_PATH);
        //更新数据库
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = ((LoginEmployee) authentication.getPrincipal()).getEmployee().getId();
        LambdaUpdateWrapper<Employee> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Employee::getId, id)
                .set(Employee::getAvatar, path);
        employeeService.update(wrapper);
        return path;
    }
}
