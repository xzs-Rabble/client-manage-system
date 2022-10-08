package cn.wolfcode.web.modules.visitinfo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 拜访信息表
 * </p>
 *
 * @author HUOXINWL
 * @since 2022-06-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbVisit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一id
     */
    @Excel(name = "id")
    private String id;

    /**
     * 客户id
     */
    @NotBlank(message = "请填写客户",groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "客户id")
    private String custId;

    @TableField(exist = false)
    //@Excel(name = "custName")
    private String custName;
    /**
     * 联系人id
     */
    @NotBlank(message = "请填写联系人",groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "联系人id")
    private String linkmanId;

    @TableField(exist = false)
    //@Excel(name = "linkmanName")
    private String linkmanName;
    /**
     * 拜访方式, 1 上门走访, 2 电话拜访
     */
    //@NotBlank(message = "请填写拜访方式",groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "拜访方式")
    @Size(min = 1,max = 2,message = "请填写拜访方式")
    private int visitType;

    /**
     * 拜访原因
     */
    @NotBlank(message = "请填写拜访原因",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100,message = "拜访原因不能超过100个字",groups = {AddGroup.class,UpdateGroup.class})
    @Excel(name = "拜访原因")
    private String visitReason;

    /**
     * 交流内容
     */
    @NotBlank(message = "请填写交流内容",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100,message = "交流内容不能超过100个字",groups = {AddGroup.class,UpdateGroup.class})
    @Excel(name = "交流内容")
    private String content;

    /**
     * 拜访时间
     */
    @Past(message = "请填写拜访时间,并且不能超过今天",groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "拜访时间")
    private LocalDate visitDate;

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
