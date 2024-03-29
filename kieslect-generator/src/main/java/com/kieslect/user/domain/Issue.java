package com.kieslect.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-03-29
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_issue")
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 问题编号
     */
      @TableField("issue_no")
    private String issueNo;

      /**
     * 问题类型，0：功能异常，1：意见与建议，2：其他
     */
      @TableField("type")
    private Integer type;

      /**
     * 问题描述
     */
      @TableField("description")
    private String description;

      /**
     * 存储图片的文件路径，可以使用逗号分隔多个路径
     */
      @TableField("image_paths")
    private String imagePaths;

      /**
     * 联系邮箱
     */
      @TableField("contact_email")
    private String contactEmail;

      /**
     * 存储共享日志的文件路径，可以使用逗号分隔多个路径
     */
      @TableField("share_log")
    private String shareLog;

      /**
     * 创建时间
     */
      @TableField("create_time")
    private Long createTime;

      /**
     * 更新时间
     */
      @TableField("update_time")
    private Long updateTime;

      /**
     * 问题状态，0：未处理，1：处理中，2：已处理
     */
      @TableField("issue_status")
    private Integer issueStatus;

      /**
     * 问题解决人，0：admin
     */
      @TableField("issue_deal_user")
    private Integer issueDealUser;
}
