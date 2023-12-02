package com.example.dispatch.controller;

import com.example.dispatch.common.BaseResponse;
import com.example.dispatch.common.ErrorCode;
import com.example.dispatch.common.ResultUtils;
import com.example.dispatch.exception.BusinessException;
import com.example.dispatch.service.JobRelService;
import com.example.dispatch.service.JobService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
//@ApiSupport(author = "nanimo")
public class JobController {

    @Resource
    private JobService jobService;

    @Resource
    private JobRelService jobRelService;

    @GetMapping("/exception")
    public BaseResponse<?> getException() {
        int a = 1 / 0;
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }

    @GetMapping("/exception2")
//    @ApiImplicitParam(name = "name",value = "姓名",required = true)
//    @ApiOperation("测试接口")
    @ApiOperationSupport(author = "nanimo")
    public BaseResponse<?> getException2() {
        throw new BusinessException(ErrorCode.SYSTEM_ERROR,"throw BusinessException");
    }

    @PostMapping("/addjob")
    public BaseResponse<Long> createJob(@RequestParam String jobName, @RequestParam String jobDetail, @RequestParam String jobExecutor) {

        long jobId = jobService.createJob(jobName, jobDetail, jobExecutor);

        if (jobId <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"创建失败");
        }

        return ResultUtils.success(jobId);
    }

    @PostMapping("/deleteJob")
    public BaseResponse<Boolean> deleteJobById(@RequestParam Long jobId) {
        Boolean result = jobService.deleteJobById(jobId);

        if (result) {
            return ResultUtils.success(result);
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"删除失败");
        }
    }

    @PostMapping("/addRel")
    public BaseResponse<Boolean> createRelation(@RequestParam String relName, @RequestParam long relHeadId, @RequestParam long relTailId) {

        Boolean result = jobRelService.createRelation(relName, relHeadId, relTailId);

        if (result) {
            return ResultUtils.success(result);
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"添加关系失败");
        }
    }

    @PostMapping("deleteRel")
    public BaseResponse<Boolean> deleteRelationById(@RequestParam long relId) {

        Boolean result = jobRelService.deleteRelationById(relId);

        if (result) {
            return ResultUtils.success(result);
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除关系失败");
        }
    }

    @PostMapping("/start")
    public BaseResponse<Boolean> startJobById(@RequestParam Long jobId) {

        return ResultUtils.success(jobService.startJob(jobId));
    }

}
