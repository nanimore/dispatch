package com.example.dispatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dispatch.model.Instance;
import com.example.dispatch.service.InstanceService;
import com.example.dispatch.mapper.InstanceMapper;
import org.springframework.stereotype.Service;

/**
* @author nanimo
* @description 针对表【instance】的数据库操作Service实现
* @createDate 2023-12-08 21:19:18
*/
@Service
public class InstanceServiceImpl extends ServiceImpl<InstanceMapper, Instance>
    implements InstanceService{

}




