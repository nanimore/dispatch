package com.dailycodebuffer.mq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dailycodebuffer.mq.model.Instance;
import com.dailycodebuffer.mq.model.Job;
import com.dailycodebuffer.mq.model.JobRel;
import com.dailycodebuffer.mq.service.InstanceService;
import com.dailycodebuffer.mq.service.JobRelService;
import com.dailycodebuffer.mq.service.JobService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MessageListener {

    @Resource
    private InstanceService instanceService;

    @Resource
    private JobRelService jobRelService;

    @Resource
    private JobService jobService;

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(CustomMessage message) {

        System.out.println(message.toString());
        // 查询该任务的父任务
        Long jobId = message.getJobId();
        Job currentJob = jobService.getById(jobId);
        // 找到当前节点的父亲
        QueryWrapper<JobRel> jobRelQW = new QueryWrapper<>();
        jobRelQW.eq("rel_tail", jobId);
        List<JobRel> fatherJobRels = jobRelService.list(jobRelQW);

        if (CollectionUtils.isEmpty(fatherJobRels)) {
            System.out.println("当前任务没有父任务, 执行当前节点：" + currentJob.getJobName());
            QueryWrapper<Instance> instanceQW = new QueryWrapper<>();
            instanceQW.eq("job_id", jobId);
            instanceQW.eq("batch", message.getBatch());
            instanceQW.eq("status", 0);
            Instance instance = new Instance();
            instance.setStatus(1);
            instanceService.update(instance, instanceQW);
            return;
        }

        // 查询该批次中该任务的父任务有没有执行，没有则跳过
        for (JobRel fatherJobRel : fatherJobRels) {
            QueryWrapper<Instance> instanceQW = new QueryWrapper<>();
            instanceQW.eq("job_id", fatherJobRel.getRelHead());
            instanceQW.eq("batch", message.getBatch());
            instanceQW.eq("status", 0);
            List<Instance> list = instanceService.list(instanceQW);

            if (!CollectionUtils.isEmpty(list)) {
                System.out.println("父任务未执行" + list.toString());
                return;
            }

        }

        System.out.println("当前任务父任务已执行, 执行当前节点：" + currentJob.getJobName());
        QueryWrapper<Instance> instanceQW = new QueryWrapper<>();
        instanceQW.eq("job_id", jobId);
        instanceQW.eq("batch", message.getBatch());
        instanceQW.eq("status", 0);
        Instance instance = new Instance();
        instance.setStatus(1);
        instanceService.update(instance, instanceQW);


    }

}
