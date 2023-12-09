package com.dailycodebuffer.mq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.dailycodebuffer.mq.mapper.JobRelMapper;
import com.dailycodebuffer.mq.model.Job;
import com.dailycodebuffer.mq.model.JobRel;
import com.dailycodebuffer.mq.service.JobRelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
* @author nanimo
* @description 针对表【job_rel】的数据库操作Service实现
* @createDate 2023-11-27 15:18:04
*/
@Service
public class JobRelServiceImpl extends ServiceImpl<JobRelMapper, JobRel>
    implements JobRelService {

}




