package com.dailycodebuffer.mq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.dailycodebuffer.mq.mapper.JobMapper;
import com.dailycodebuffer.mq.model.Instance;
import com.dailycodebuffer.mq.model.Job;
import com.dailycodebuffer.mq.model.JobRel;

import com.dailycodebuffer.mq.service.InstanceService;
import com.dailycodebuffer.mq.service.JobRelService;
import com.dailycodebuffer.mq.service.JobService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
* @author nanimo
* @description 针对表【job】的数据库操作Service实现
* @createDate 2023-11-27 15:14:41
*/
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job>
    implements JobService {

}


