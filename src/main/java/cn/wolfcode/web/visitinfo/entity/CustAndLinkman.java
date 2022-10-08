package cn.wolfcode.web.modules.visitinfo.entity;

import cn.wolfcode.web.modules.custinfo.entity.TbCustomer;
import cn.wolfcode.web.modules.linkmane.entity.TbCustLinkmane;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustAndLinkman {
    private TbCustLinkmane tbCustLinkmane;
    private TbCustomer tbCustomer;
}
