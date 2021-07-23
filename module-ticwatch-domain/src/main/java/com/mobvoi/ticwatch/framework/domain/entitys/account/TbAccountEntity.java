package com.mobvoi.ticwatch.framework.domain.entitys.account;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiekun
 * @since 2021-07-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_account")
@ApiModel(value="TbAccountEntity对象", description="")
public class TbAccountEntity extends Model<TbAccountEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户wwid")
    private String wwid;

    @ApiModelProperty(value = "付费时长")
    @TableField("payment_time")
    private Long paymentTime;

    @ApiModelProperty(value = "可用存储空间（用时长代表空间）")
    @TableField("free_space")
    private Long freeSpace;

    @ApiModelProperty(value = "总共的存储空间（用时长代表空间）")
    @TableField("total_space")
    private Long totalSpace;

    @TableField("nick_name")
    private String nickName;

    private String email;

    private String phone;

    @ApiModelProperty(value = "会员购买时长卡时的折扣")
    @TableField("vip_duration_discount")
    private BigDecimal vipDurationDiscount;

    @ApiModelProperty(value = "会员卡到期时间")
    @TableField("vip_expired_at")
    private LocalDateTime vipExpiredAt;

    @ApiModelProperty(value = "创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
