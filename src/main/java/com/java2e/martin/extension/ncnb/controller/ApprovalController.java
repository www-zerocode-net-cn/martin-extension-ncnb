package com.java2e.martin.extension.ncnb.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java2e.martin.common.bean.system.MultiDelete;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.log.annotation.MartinLog;
import com.java2e.martin.common.security.util.SecurityContextUtil;
import com.java2e.martin.extension.ncnb.command.DbSqlExecCommand;
import io.swagger.annotations.ApiOperation;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java2e.martin.extension.ncnb.entity.Approval;
import com.java2e.martin.extension.ncnb.service.ApprovalService;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.log.annotation.MartinLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * <p>
 * 元数据审批  路由
 * </p>
 *
 * @author 零代科技
 * @version 1.0
 * @date 2023-03-25
 * @describtion
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @ApiOperation(value = "元数据审批 ", nickname = "create", notes = "新增元数据审批 ", tags = {"approval",})
    @RequestMapping(value = "/approval", method = RequestMethod.POST)
    @MartinLog("添加元数据审批 ")
    public R create(@ApiParam(value = "", required = true) @Valid @RequestBody Approval approval) {
        String id = SecurityContextUtil.getAccessUser().getId();
        approval.setPromoter(id);
        return R.ok(approvalService.save(approval));
    }

    @ApiOperation(value = "元数据审批 ", nickname = "delete", notes = "删除元数据审批 ", tags = {"approval",})
    @RequestMapping(value = "/approval/{id}", method = RequestMethod.DELETE)
    @MartinLog("删除元数据审批 ")
    public R delete(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(approvalService.removeById(id));
    }

    @ApiOperation(value = "元数据审批 ", nickname = "list", notes = "查询我的工单", tags = {"approval",})
    @RequestMapping(value = "/approval/promote", method = RequestMethod.GET)
    @MartinLog("查询我的工单")
    @SneakyThrows
    public R order(@RequestParam Map<String, Object> map) {
        String id = SecurityContextUtil.getAccessUser().getId();
        //只能查看自己的工单
        map.put("promoter", id);
        return R.ok(approvalService.getPage(map));
    }

    @ApiOperation(value = "元数据审批 ", nickname = "list", notes = "查询我的审批", tags = {"approval",})
    @RequestMapping(value = "/approval/approve", method = RequestMethod.GET)
    @MartinLog("查询我的审批")
    @SneakyThrows
    public R approval(@RequestParam Map<String, Object> map) {
        String id = SecurityContextUtil.getAccessUser().getId();
        //只能查看自己的工单
        map.put("approver", id);
        return R.ok(approvalService.getPage(map));
    }

    @ApiOperation(value = "元数据审批 ", nickname = "multipleDelete", notes = "批量删除元数据审批 ", tags = {"approval",})
    @RequestMapping(value = "/approval/multiple_delete", method = RequestMethod.DELETE)
    @MartinLog("批量删除元数据审批 ")
    public R multipleDelete(@ApiParam(value = "", required = true) @Valid @RequestBody MultiDelete approval) {
        return R.ok(approvalService.removeByIds(approval.getKeys()));
    }

    @ApiOperation(value = "元数据审批 ", nickname = "partialUpdate", notes = "编辑元数据审批 ", tags = {"approval",})
    @RequestMapping(value = "/approval/{id}", method = RequestMethod.PATCH)
    @MartinLog("编辑元数据审批 ")
    public R partialUpdate(@ApiParam(value = "Id", required = true) @PathVariable("id") String id, @ApiParam(value = "", required = true) @Valid @RequestBody Approval approval) {
        return R.ok(approvalService.updateById(approval));
    }

    @ApiOperation(value = "元数据审批 ", nickname = "read", notes = "获取单个元数据审批 ", tags = {"approval",})
    @RequestMapping(value = "/approval/{id}", method = RequestMethod.GET)
    @MartinLog("获取单个元数据审批 ")
    public R read(@ApiParam(value = "Id", required = true) @PathVariable("id") String id) {
        return R.ok(approvalService.getById(id));
    }

    @ApiOperation(value = "元数据审批 ", nickname = "tree", notes = "获取元数据审批 树", tags = {"approval",})
    @RequestMapping(value = "/approval/tree", method = RequestMethod.GET)
    @MartinLog("获取元数据审批 树")
    public R tree(@ApiParam(value = "", required = true) Approval approval) {
        return R.ok(approvalService.tree(approval));
    }

    @ApiOperation(value = "元数据审批 ", nickname = "update", notes = "修改元数据审批 ", tags = {"approval",})
    @RequestMapping(value = "/approval/{id}", method = RequestMethod.PUT)
    @MartinLog("编辑元数据审批 ")
    public R update(@ApiParam(value = "Id", required = true) @PathVariable("id") String id, @ApiParam(value = "", required = true) @Valid @RequestBody Map param) {
        Approval approval = approvalService.getById(id);
        approval.setApproveStatus(param.get("approveStatus").toString());
        approval.setApproveResult(param.get("approveResult").toString());
        LambdaQueryWrapper<Approval> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Approval::getId, id);
        if (StrUtil.equalsAny(approval.getApproveStatus(), "1", "3")) {
            approval.setApproveTime(LocalDateTime.now());
            //通过
            if (StrUtil.equals(approval.getApproveStatus(), "1")) {
                if (StrUtil.isBlank(approval.getDbInfo())) {
                    return R.failed("未配置目标数据源信息");
                }
                //从版本页面发起的审核，需要处理保本信息
                if (StrUtil.isNotBlank(approval.getVersionId())) {
                    approvalService.syncBdVersion(approval.getVersionId());
                }
                JSONObject dbInfo = JSONUtil.parseObj(approval.getDbInfo());
                Map map = dbInfo.toBean(Map.class);
                map.put("separator", param.get("separator"));
                map.put("sql", approval.getApproveSql());
                //表结构语句不受事务控制，放到最后执行
                DbSqlExecCommand dbSqlExecCommand = new DbSqlExecCommand();
                dbSqlExecCommand.exec(map);
            }
        }
        return R.ok(approvalService.update(approval, wrapper));
    }
}

