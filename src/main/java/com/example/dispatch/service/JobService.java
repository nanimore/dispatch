package com.example.dispatch.service;

import com.example.dispatch.model.Job;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author nanimo
* @description 针对表【job】的数据库操作Service
* @createDate 2023-11-27 15:14:41
*/
public interface JobService extends IService<Job> {

    long createJob(String jobName, String jobDetail, String jobExecutor);

    Boolean deleteJobById(Long jobId);

    Boolean startJob(Long jobId);

}
