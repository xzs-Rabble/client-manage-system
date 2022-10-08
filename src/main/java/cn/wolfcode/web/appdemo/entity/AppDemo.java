package cn.wolfcode.web.modules.appdemo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author HUOXINWL
 * @since 2022-06-19
 */
public class AppDemo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    /**
     * 名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 描述
     */
    @TableField("INFO")
    private String info;

    /**
     * 年龄
     */
    @TableField("AGE")
    private Integer age;

    @TableField("DESCS")
    private String descs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

    @Override
    public String toString() {
        return "AppDemo{" +
            "id=" + id +
            ", name=" + name +
            ", createTime=" + createTime +
            ", info=" + info +
            ", age=" + age +
            ", descs=" + descs +
        "}";
    }
}
