package cn.wolfcode.web.modules.linkmane.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 客户联系人
 * </p>
 *
 * @author HUOXINWL
 * @since 2022-06-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbCustLinkmane implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "id")
    private String id;

    /**
     * 客户id
     */
    @Excel(name = "客户id")
    private String custId;

    @TableField(exist = false)
    @Excel(name = "客户Name")
    private String custName;
    /**
     * 联系人名字
     */
    @Excel(name = "联系人名字")
    private String linkman;

    /**
     * 性别 1 男 0 女
     */
    @Excel(name = "性别")
    private Integer sex;

    /**
     * 年龄
     */
    @Excel(name = "年龄")
    private Integer age;

    /**
     * 联系人电话
     */
    @Excel(name = "联系人电话")
    private String phone;

    /**
     * 职位
     */
    @Excel(name = "职位")
    private String position;

    /**
     * 部门
     */
    @Excel(name = "部门")
    private String department;

    /**
     * 备注信息
     */
    @Excel(name = "备注信息")
    private String remark;

    /**
     * 录入人
     */
    @Excel(name = "录入人")
    private String inputUser;

    /**
     * 录入时间
     */
    @Excel(name = "录入时间")
    private LocalDateTime inputTime;
}
