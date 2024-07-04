package com.kieslect.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-06-29
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_app_help_info")
public class AppHelpInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 帮助标题
     */
      @TableField("help_title")
    private String helpTitle;

      /**
     * 帮助详情
     */
      @TableField("help_detail")
    private String helpDetail;

      /**
     * 语言
     */
      @TableField("language")
    private String language;

      /**
     * 问题归属类型
     */
      @TableField("help_type_id")
    private Integer helpTypeId;

      /**
     * appid
     */
      @TableField("app_id")
    private Integer appId;

      /**
     * 创建时间
     */
      @TableField("create_time")
    private LocalDateTime createTime;

      /**
     * 更新时间
     */
      @TableField("update_time")
    private LocalDateTime updateTime;

      /**
     * 创建者
     */
      @TableField("create_user")
    private String createUser;

      /**
     * 更新者
     */
      @TableField("update_user")
    private String updateUser;
}
