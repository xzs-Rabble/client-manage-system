package cn.wolfcode.web.modules.contractinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 合同信息
 * </p>
 *
 * @author HUOXINWL
 * @since 2022-06-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TbContract implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 客户id
     */
    private String custId;

    @TableField(exist = false)
    private String custName;

    /**
     * 合同名称
     */
    @NotNull(message = "合同名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String contractName;

    /**
     * 合同编码
     */
    @NotNull(message = "合同编码不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String contractCode;

    /**
     * 合同金额
     */
    @Min(value = 0,message = "合同金额必须为数字",groups = {AddGroup.class, UpdateGroup.class})
    @NotNull(message = "合同金额不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private Integer amounts;

    /**
     * 合同生效开始时间
     */
    private LocalDate startDate;

    /**
     * 合同生效结束时间
     */
    private LocalDate endDate;

    /**
     * 合同内容
     */
    private String content;

    /**
     * 是否盖章确认 0 否 1 是
     */
    private int affixSealStatus;

    /**
     * 审核状态 0 未审核 1 审核通过 -1 审核不通过
     */
    private int auditStatus;

    /**
     * 是否作废 1 作废 0 在用
     */
    private int nullifyStatus;

    /**
     * 录入人
     */
    private String inputUser;

    /**
     * 录入时间
     */
    private LocalDateTime inputTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}
