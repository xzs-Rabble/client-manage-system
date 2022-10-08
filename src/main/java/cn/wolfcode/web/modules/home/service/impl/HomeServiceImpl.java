package cn.wolfcode.web.modules.home.service.impl;

import cn.wolfcode.web.modules.custinfo.service.ITbCustomerService;
import cn.wolfcode.web.modules.home.service.IHomeService;
import cn.wolfcode.web.modules.sys.service.SysUserLoginLogService;
import cn.wolfcode.web.modules.sys.service.SysUserOperationLogService;
import cn.wolfcode.web.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户信息 服务实现类
 * </p>
 *
 * @author 陈天狼
 * @since 2022-06-20
 */
@Service
public class HomeServiceImpl implements IHomeService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserLoginLogService sysUserLoginLogService;
    @Autowired
    private SysUserOperationLogService sysUserOperationLogService;
    @Autowired
    private ITbCustomerService iTbCustomerService;


    @Override
    public int getLoginCount() {
        return sysUserLoginLogService.count();
    }

    @Override
    public int getUserCount() {
        return sysUserService.count();
    }

    @Override
    public int getOperationLogCount() {
        return sysUserOperationLogService.count();
    }

    @Override
    public int getCustInfoCount() {
        return iTbCustomerService.count();
    }
}
