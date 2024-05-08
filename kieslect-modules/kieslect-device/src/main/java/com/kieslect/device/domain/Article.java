package com.kieslect.device.domain;

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
 * @since 2024-04-29
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 见 t_app_manage 表 的 id
     */
    @TableField("app_id")
    private Integer appId;

    /**
     * 见 t_article_type 表 的 id
     */
    @TableField("article_type_id")
    private Integer articleTypeId;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 语言
     */
    @TableField("language")
    private Integer language;

    /**
     * 文章状态
     */
    @TableField("article_status")
    private Byte articleStatus = 1;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Integer createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Integer updateTime;

    @TableField("father_id")
    private Integer fatherId;
}
