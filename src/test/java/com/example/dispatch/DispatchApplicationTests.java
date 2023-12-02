package com.example.dispatch;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dispatch.model.Job;
import com.example.dispatch.service.JobService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DispatchApplicationTests {

    @Resource
    private JobService jobService;

    @Test
    void contextLoads() {

    }

    @Test
//    @Transactional(rollbackFor = RuntimeException.class)
    void testJobService() {
        Job job = new Job();
        job.setId(0L);
        job.setJobName("job21");
        job.setJobDetail("creat table");
        job.setJobExecutor("sql");
        job.setCreateTime(new Date());
        job.setUpdateTime(new Date());
        job.setIsDeleted(0);


        boolean save = jobService.save(job);
        Assertions.assertTrue(save);

        Job getJob = jobService.getById(job.getId());
        System.out.println(getJob);

        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", job.getId());

        boolean remove = jobService.remove(queryWrapper);
        Assertions.assertTrue(remove);
    }

}
