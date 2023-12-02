package com.example.dispatch;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dispatch.model.Job;
import com.example.dispatch.model.JobRel;
import com.example.dispatch.service.JobRelService;
import com.example.dispatch.service.JobService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class JobServiceTests {

    @Resource
    private JobService jobService;

    @Resource
    private JobRelService jobRelService;

    @Test
    void test() {
        long jobId = jobService.createJob("testjob", "delete data", "SQL");
        System.out.println("jobId: " + jobId);
    }

    @Test
    void test2() {
        Job job = jobService.getById(123123L);
        System.out.println(job);
    }

    @Test
    void testCreateJob() {
        long jobId = 0;

        jobId = jobService.createJob("A", "create table dispatch", "SQL");
        System.out.println("jobIdA: " + jobId);

        jobId = jobService.createJob("B", "insert B", "SQL");
        System.out.println("jobIdB: " + jobId);
        jobId = jobService.createJob("C", "insert C", "SQL");
        System.out.println("jobIdC: " + jobId);
        jobId = jobService.createJob("D", "insert D", "SQL");
        System.out.println("jobIdD: " + jobId);
        jobId = jobService.createJob("E", "select E", "SQL");
        System.out.println("jobIdE: " + jobId);
        jobId = jobService.createJob("F", "select F", "SQL");
        System.out.println("jobIdF: " + jobId);
    }

    @Test
    void testCreateRelation() {

        Boolean r = false;
        r = jobRelService.createRelation("R1", 10L, 11L);
        System.out.println("r: " + r);

        r = jobRelService.createRelation("R2", 10L, 12L);
        System.out.println("r: " + r);

        r = jobRelService.createRelation("R3", 12L, 13L);
        System.out.println("r: " + r);

        r = jobRelService.createRelation("R4", 13L, 14L);
        System.out.println("r: " + r);

        r = jobRelService.createRelation("R5", 11L, 14L);
        System.out.println("r: " + r);

        r = jobRelService.createRelation("R6", 14L, 15L);
        System.out.println("r: " + r);

    }

    @Test
    void testCreateJob2() {
        // 有环
        Boolean r7 = jobRelService.createRelation("R7", 14L, 10L);
        System.out.println(r7);
    }

    @Test
    void removeJob() {
        Boolean b = jobService.deleteJobById(11L);
        Assertions.assertTrue(b);
    }

    @Test
    void removeRel() {
        Boolean b = jobRelService.deleteRelationById(28L);
        Assertions.assertTrue(b);
    }

    @Test
    void testException(){
        int i = 1/0;
    }
}
