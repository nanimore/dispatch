package com.dailycodebuffer.mq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dailycodebuffer.mq.model.Instance;
import com.dailycodebuffer.mq.model.Job;
import com.dailycodebuffer.mq.model.JobRel;
import com.dailycodebuffer.mq.service.InstanceService;
import com.dailycodebuffer.mq.service.JobRelService;
import com.dailycodebuffer.mq.service.JobService;
import com.dailycodebuffer.mq.utils.SmallTool;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessageListener {

    @Resource
    private InstanceService instanceService;

    @Resource
    private JobRelService jobRelService;

    @Resource
    private JobService jobService;

    @Resource
    private RabbitTemplate template;

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(CustomMessage message) {

        System.out.println("当前消息" + message.toString());

        Long jobId = message.getJobId();
        Job currentJob = jobService.getById(jobId);

        // 查询当前节点是否执行 true 为未执行
        Boolean currentState = true;

        synchronized (this) {
            QueryWrapper<Instance> instanceQW = new QueryWrapper<>();
            instanceQW.eq("job_id", jobId);
            instanceQW.eq("batch", message.getBatch());
            instanceQW.eq("status", 0);
            int count = instanceService.count(instanceQW);

            if (count == 0) {
                currentState = false;
            }
        }
        if (!currentState) {
            System.out.println("当前任务已经执行过, 跳过当前任务：" +currentJob.getJobName());
        }

        // 查询该任务的父任务



        QueryWrapper<JobRel> jobRelQW = new QueryWrapper<>();
        jobRelQW.eq("rel_tail", jobId);
        List<JobRel> fatherJobRels = jobRelService.list(jobRelQW);

        if (CollectionUtils.isEmpty(fatherJobRels) && currentState) {
            System.out.println("当前任务没有父任务, 执行当前节点：" + currentJob.getJobName());
            QueryWrapper<Instance> instanceQW = new QueryWrapper<>();
            instanceQW.eq("job_id", jobId);
            instanceQW.eq("batch", message.getBatch());
            instanceQW.eq("status", 0);
            Instance instance = new Instance();
            instance.setStatus(1);
            instanceService.update(instance, instanceQW);
        } else if (currentState) {
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


        // 投递子任务
        // 遍历当前任务的子任务
        LinkedList<Job> linkedList = new LinkedList<>();
        QueryWrapper<JobRel> jobRelQW1 = new QueryWrapper<>();
        jobRelQW1.eq("rel_head", jobId);
        List<JobRel> jobRels1 = jobRelService.list(jobRelQW1);
        for (JobRel jobRel : jobRels1) {
            linkedList.add(jobService.getById(jobRel.getRelTail()));
        }


        ArrayList<CompletableFuture<Void>> tasks = new ArrayList<>();

        for (Job job : linkedList) {
            CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
                CustomMessage messageSon = new CustomMessage();
                messageSon.setJobId(job.getId());
                messageSon.setBatch(message.getBatch());
                messageSon.setMessage(job.getJobDetail());

                template.convertAndSend(MQConfig.EXCHANGE,
                        MQConfig.ROUTING_KEY, messageSon);
                SmallTool.printTimeAndThread(job.getJobName());
            });


            tasks.add(task);
        }

        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();


    }

}
