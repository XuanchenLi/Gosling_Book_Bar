package com.dazzle.book_bar_back.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dazzle.book_bar_back.dao.entity.Department;
import com.dazzle.book_bar_back.dao.entity.DepartmentPositionPair;
import com.dazzle.book_bar_back.dao.entity.Position;
import com.dazzle.book_bar_back.response.ResponseResult;
import com.dazzle.book_bar_back.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName:PositionController
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/27 下午 6:11
 * Version V1.0
 */
@RestController
public class PositionController {
    @Autowired
    private PositionService positionService;

    @GetMapping("/position/all_basic_{departmentId}")
    @ResponseResult
    public List<Position>
    getAllBasicByDepartmentId(@PathVariable("departmentId") Long id) {
        return positionService.selectBasicByDepartmentId(id);
    }
    @GetMapping("/position/delete_{id}")
    @ResponseResult
    public int deletePositionById(@PathVariable("id") Long id) {

        return positionService.deleteById(id);
    }
    @PostMapping("/position/update_{id}")
    @ResponseResult
    public boolean updateById(@PathVariable("id") Long id, @RequestBody Position position) {
        LambdaUpdateWrapper<Position> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Position::getDescription, position.getDescription())
                .set(Position::getName, position.getName())
                .eq(Position::getId, id);
        return positionService.update(wrapper);
    }
    @GetMapping("/position/get_info_{id}")
    @ResponseResult
    public Position getPositionById(@PathVariable("id") Long id) {
        return positionService.getById(id);
    }

    @PostMapping("/position/save_or_update")
    @ResponseResult
    public boolean updateOrSave(@RequestBody Position position) {
        return positionService.saveOrUpdate(position);
    }

}
