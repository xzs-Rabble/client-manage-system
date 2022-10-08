package cn.wolfcode.web.modules.custinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 客户信息
 * </p>
 *
 * @author HUOXINWL
 * @since 2022-06-20
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 企业名称
     */
    @NotBlank(message = "请填写企业名称",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 199,message = "企业名称不能超过199个字",groups = {AddGroup.class,UpdateGroup.class})
    private String customerName;

    /**
     * 法定代表人
     */
    private String legalLeader;

    /**
     * 成立时间
     */
    private LocalDate registerDate;

    /**
     * 经营状态, 0 开业、1 注销、2 破产
     */
    private Integer openStatus;

    /**
     * 所属地区省份
     */
    private String province;

    @TableField(exist = false)
    private String provinceName;

    /**
     * 注册资本,(万元)
     */
    private String regCapital;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 经营范围
     */
    private String scope;

    /**
     * 注册地址
     */
    private String regAddr;

    /**
     * 录入时间
     */
    private LocalDateTime inputTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 录入人
     */
    private String inputUserId;

}
