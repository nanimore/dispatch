package com.example.dispatch.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName job_rel
 */
@TableName(value ="job_rel")
@Data
public class JobRel implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关系名
     */
    private String relName;

    /**
     * 关系头
     */
    private Long relHead;

    /**
     * 关系尾
     */
    private Long relTail;

    /**
     * 任务创建时间
     */
    private Date createTime;

    /**
     * 任务更新时间
     */
    private Date updateTime;

    /**
     * 是否删除，0-否，1-是
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        JobRel other = (JobRel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRelName() == null ? other.getRelName() == null : this.getRelName().equals(other.getRelName()))
            && (this.getRelHead() == null ? other.getRelHead() == null : this.getRelHead().equals(other.getRelHead()))
            && (this.getRelTail() == null ? other.getRelTail() == null : this.getRelTail().equals(other.getRelTail()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()));
    }

//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
//        result = prime * result + ((getRelName() == null) ? 0 : getRelName().hashCode());
//        result = prime * result + ((getRelHead() == null) ? 0 : getRelHead().hashCode());
//        result = prime * result + ((getRelTail() == null) ? 0 : getRelTail().hashCode());
//        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
//        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
//        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
//        return result;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", relName=").append(relName);
        sb.append(", relHead=").append(relHead);
        sb.append(", relTail=").append(relTail);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}