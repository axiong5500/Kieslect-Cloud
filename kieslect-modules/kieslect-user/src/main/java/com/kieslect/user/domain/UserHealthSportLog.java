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
 * @since 2024-04-15
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_user_health_sport_log")
public class UserHealthSportLog implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("upload_file")
    private String uploadFile;

    @TableField("upload_file_size")
    private Double uploadFileSize;

    @TableField("create_time")
    private long createTime;

    @TableField("update_time")
    private long updateTime;
}
