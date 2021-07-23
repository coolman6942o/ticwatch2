package com.mobvoi.ticwatch.framework.domain.entitys.ticwatch;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * @since 2021-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_motion_records")
@ApiModel(value="TbMotionRecordsEntity对象", description="")
public class TbMotionRecordsEntity extends Model<TbMotionRecordsEntity> {

    private static final long serialVersionUID = 1L;

    @TableField("account_id")
    private String accountId;

    private String type;

    @TableField("start_at")
    private Long startAt;

    @TableField("end_at")
    private Long endAt;

    @TableField("total_distance")
    private Integer totalDistance;

    @TableField("total_calorie")
    private Integer totalCalorie;

    @TableField("total_steps")
    private Integer totalSteps;

    @TableField("total_motion_time")
    private Integer totalMotionTime;

    @TableField("avg_heart_rate")
    private Integer avgHeartRate;

    @ApiModelProperty(value = "手表端数据产生时间")
    @TableField("recorded_at")
    private Long recordedAt;

    @ApiModelProperty(value = "数据上传保存时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
