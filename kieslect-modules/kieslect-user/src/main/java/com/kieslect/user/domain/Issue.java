package com.kieslect.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
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
 * @since 2024-03-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_issue")
@Builder
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

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

    /**
     * app名字，0：Kieslect Fashion
     */
    @TableField("app_name")
    private Integer appName;

    /**
     * app系统，0：android，1：ios，2：harmony
     */
    @TableField("app_system")
    private Integer appSystem;

    /**
     * 手机型号
     */
    @TableField("app_type")
    private String appType;

    /**
     * app渠道，0：google，1：ios，2：huawei
     */
    @TableField("app_channel")
    private Integer appChannel;

    /**
     * app包状态，0：内测，1：公测，2：正式
     */
    @TableField("app_status")
    private Integer appStatus;

    /**
     * app版本
     */
    @TableField("app_version")
    private String appVersion;

    /**
     * 系统版本
     */
    @TableField("system_version")
    private String systemVersion;
}
