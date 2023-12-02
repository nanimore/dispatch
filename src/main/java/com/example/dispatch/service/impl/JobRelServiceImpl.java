package com.example.dispatch.service.impl;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dispatch.common.ErrorCode;
import com.example.dispatch.exception.BusinessException;
import com.example.dispatch.mapper.JobMapper;
import com.example.dispatch.model.Job;
import com.example.dispatch.model.JobRel;
import com.example.dispatch.service.JobRelService;
import com.example.dispatch.mapper.JobRelMapper;
import com.example.dispatch.service.JobService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author nanimo
* @description 针对表【job_rel】的数据库操作Service实现
* @createDate 2023-11-27 15:18:04
*/
@Service
public class JobRelServiceImpl extends ServiceImpl<JobRelMapper, JobRel>
    implements JobRelService{

    @Resource
    private JobService jobService;

    @Override
    public Boolean createRelation(String relName, long relHeadId, long relTailId) {

        // 1. 判断relHeadId是否存在
        Job job = jobService.getById(relHeadId);
        if (job == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"关系头节点不存在");
        }

        // 2. 判断relTailId是否存在
        job = jobService.getById(relTailId);
        if (job == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"关系尾节点不存在");
        }
        // 3. 判断如果关系创建后是否存在闭环
        // 如何存在tail -> head 的路径，再添加一个head -> tail 的路径，则存在闭环
        if (isConnected(relTailId, relHeadId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"该关系存在闭环");
        }

        JobRel jobRel = new JobRel();

        jobRel.setRelName(relName);
        jobRel.setRelHead(relHeadId);
        jobRel.setRelTail(relTailId);

        boolean result = this.save(jobRel);

        return result;
    }

    @Override
    public Boolean deleteRelationById(long relId) {

        return this.removeById(relId);
    }

    /**
     * 判断两个节点之间是否连通
     *node1-->node2
     */
    private Boolean isConnected(long node1, long node2) {
        if (node1 == node2) {
            return false;
        }

        QueryWrapper<JobRel> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("rel_head", node1);
        List<JobRel> jobRels = this.list(queryWrapper);
        LinkedList<Long> relTails = new LinkedList<>();


        while (!jobRels.isEmpty()) {

//            List<Long> relTails = new ArrayList<>();

            for (JobRel jobRel : jobRels) {
                if (jobRel.getRelTail() == node2) {
                    return true;
                }
                relTails.add(jobRel.getRelTail());
            }

            QueryWrapper<JobRel> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.in("rel_head", relTails);
            relTails.clear();

            jobRels = this.list(queryWrapper1);
        }

        return false;
    }
}




