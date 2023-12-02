package com.example.dispatch.service;

import com.example.dispatch.model.JobRel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author nanimo
* @description 针对表【job_rel】的数据库操作Service
* @createDate 2023-11-27 15:18:04
*/
public interface JobRelService extends IService<JobRel> {

    Boolean createRelation(String relName, long relHeadId, long relTailId);

    Boolean deleteRelationById(long relId);

}
