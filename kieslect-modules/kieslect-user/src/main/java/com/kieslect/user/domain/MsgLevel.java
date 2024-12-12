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
 * @since 2024-11-26
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_msg_level")
public class MsgLevel implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * app内部标识，0：kstyleos，1：ksos，2：ckos，3：ckwearos，4：kieslect，5：xofit，6：gloryfit，7：aiwearos
     */
      @TableField("app_name")
    private Integer appName;

      /**
     * 消息类型，0：issue
     */
      @TableField("msg_type")
    private Integer msgType;

      /**
     * 消息等级：0-一般不显示，1-内部显示红点，2-外部显示红点
     */
      @TableField("msg_level")
    private Integer msgLevel;

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
}
