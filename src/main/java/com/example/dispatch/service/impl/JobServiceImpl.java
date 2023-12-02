package com.example.dispatch.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dispatch.common.ErrorCode;
import com.example.dispatch.common.JobExecutor;
import com.example.dispatch.exception.BusinessException;
import com.example.dispatch.model.Job;
import com.example.dispatch.model.JobRel;
import com.example.dispatch.service.JobRelService;
import com.example.dispatch.service.JobService;
import com.example.dispatch.mapper.JobMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author nanimo
* @description 针对表【job】的数据库操作Service实现
* @createDate 2023-11-27 15:14:41
*/
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job>
    implements JobService{

//    private final JobRelService jobRelService;
//
//    public JobServiceImpl(JobRelService jobRelService) {
//        this.jobRelService = jobRelService;
//    }
    @Resource
    @Lazy
    private JobRelService jobRelService;
    @Override
    public long createJob(String jobName, String jobDetail, String jobExecutor) {

        Job job = new Job();
        job.setJobName(jobName);
        job.setJobDetail(jobDetail);

        boolean save = false;

        // 执行器类型不存在则抛异常
        try {
            JobExecutor.valueOf(jobExecutor);
            job.setJobExecutor(jobExecutor);
            save = this.save(job);
        } catch ( IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (save) {
            return job.getId();
        } else {
            return -1;
        }

    }

    @Override
    public Boolean deleteJobById(Long jobId) {

        if (jobId == null) {
            return false;
        }

        QueryWrapper<JobRel> jobRelQW = new QueryWrapper<>();

        jobRelQW.eq("rel_head", jobId).or().eq("rel_tail", jobId);

        boolean relResult = jobRelService.remove(jobRelQW);
        boolean jobResult = this.removeById(jobId);

        return jobResult;
    }

    @Override
    public Boolean startJob(Long jobId) {
        if (jobId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // a.判断任务是否存在
        Job job = this.getById(jobId);
        if (job == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "任务不存在");
        }
        // b.判断任务是否为根节点
        QueryWrapper<JobRel> jobRelQW = new QueryWrapper<>();
        jobRelQW.eq("rel_tail", jobId);
        if (jobRelService.count(jobRelQW) > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "任务不是根节点");
        }
        // c.判断是否有环

        // d.执行所有关联任务
        // 特殊处理多父亲的任务
        ArrayList<String> result = new ArrayList<>();
        startJob4(jobId,  result);

        System.out.println(result);

        return true;
    }

    private void startJob4(Long node, ArrayList<String> result) {

        Job currentRel = this.getById(node);

        // 找到当前节点的父亲
        QueryWrapper<JobRel> jobRelQW = new QueryWrapper<>();
        jobRelQW.eq("rel_tail", node);
        List<JobRel> jobRels = jobRelService.list(jobRelQW);

        for (JobRel jobRel : jobRels) {
            if (!result.contains(this.getById(jobRel.getRelHead()).getJobName())) {
                startJob4(jobRel.getRelHead(), result);
            }
        }

        if (!result.contains(currentRel.getJobName())){
            result.add(currentRel.getJobName());
        }

        // 找到当前节点的孩子
        QueryWrapper<JobRel> jobRelQW1 = new QueryWrapper<>();
        jobRelQW1.eq("rel_head", node);
        jobRels = jobRelService.list(jobRelQW1);
        for (JobRel jobRel : jobRels) {
            if (!result.contains(this.getById(jobRel.getRelTail()).getJobName())) {
                startJob4(jobRel.getRelTail(), result);
            }
        }

        return;
    }
}




