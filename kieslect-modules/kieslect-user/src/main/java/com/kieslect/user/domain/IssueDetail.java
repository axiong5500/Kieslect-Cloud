package com.kieslect.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-11-25
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_issue_detail")
public class IssueDetail implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 问题ID，见 t_issue 表 ID
     */
      @TableField("issue_id")
    private Long issueId;

      /**
     * 人员，0：系统人员，1：用户
     */
      @TableField("issue_user_id")
    private Integer issueUserId;

      /**
     * 回复的消息
     */
      @TableField("issue_msg")
    private String issueMsg;

      /**
     * 回复的文件路径
     */
      @TableField("file_path")
    private String filePath;

      /**
     * 上传的文件类型，0：image，1：voice，2：video
     */
      @TableField("issue_file_type")
    private Integer issueFileType;

      /**
     * 创建时间，时间戳，秒
     */
      @TableField("create_time")
    private Long createTime;

      /**
     * 更新时间，时间戳，秒
     */
      @TableField("update_time")
    private Long updateTime;

      /**
     * 问题状态，0：已读，1：未读
     */
      @TableField("issue_status")
    private Integer issueStatus;
}
