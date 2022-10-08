package cn.wolfcode.web.modules.contractinfo.controller;

import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.custinfo.entity.TbCustomer;
import cn.wolfcode.web.modules.custinfo.service.ITbCustomerService;
import cn.wolfcode.web.modules.linkmane.entity.TbCustLinkmane;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import cn.wolfcode.web.modules.visitinfo.entity.TbVisit;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.wolfcode.web.modules.contractinfo.entity.TbContract;
import cn.wolfcode.web.modules.contractinfo.service.ITbContractService;

import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.SameUrlData;
import link.ahsj.core.annotations.SysLog;
import link.ahsj.core.annotations.UpdateGroup;
import link.ahsj.core.entitys.ApiModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author HUOXINWL
 * @since 2022-06-22
 */
@Controller
@RequestMapping("contractinfo")
public class TbContractController extends BaseController {

    @Autowired
    private ITbContractService entityService;
    @Autowired
    private ITbCustomerService customerService;

    private static final String LogModule = "TbContract";

    @GetMapping("/list.html")
    public String list() {
        return "contract/contractinfo/list";
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('contract:contractinfo:add')")
    public ModelAndView toAdd(ModelAndView mv) {
        List<TbCustomer> custs = customerService.list();
        mv.addObject("custs",custs);
        mv.setViewName("contract/contractinfo/add");
        return mv;
    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('contract:contractinfo:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {
        List<TbCustomer> listCustomer = customerService.list();
        mv.addObject("custs",listCustomer);
        mv.setViewName("contract/contractinfo/update");
        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('contract:contractinfo:list')")
    public ResponseEntity page(LayuiPage layuiPage,String parameterName,String selectedAffixSealStatus,String selectedAuditStatus,String selectedNullifyStatus) {
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        System.out.println("这里"+selectedAffixSealStatus);
        System.out.println("这里"+selectedAuditStatus);
        System.out.println("这里"+selectedNullifyStatus);
        if (selectedAffixSealStatus == null) selectedAffixSealStatus = "-2";
        if (selectedAuditStatus == null) selectedAuditStatus = "-2";
        if (selectedNullifyStatus == null) selectedNullifyStatus = "-2";
        IPage page1 = entityService.lambdaQuery()
                .eq(!selectedAffixSealStatus.equals("-2"), TbContract::getAffixSealStatus,Integer.parseInt(selectedAffixSealStatus))
                .eq(!selectedAuditStatus.equals("-2"), TbContract::getAuditStatus,Integer.parseInt(selectedAuditStatus))
                .eq(!selectedNullifyStatus.equals("-2"), TbContract::getNullifyStatus,Integer.parseInt(selectedNullifyStatus))
                .like(StringUtils.isNotBlank(parameterName),TbContract::getContractName,parameterName)
                .or()
                .like(StringUtils.isNotBlank(parameterName),TbContract::getContractCode,parameterName)
                .page(page);
        List<TbContract> records = page1.getRecords();
        for (TbContract record:records){
            String  custName = customerService.getById(record.getCustId()).getCustomerName();
            record.setCustName(custName);
        }
        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page1));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('contract:contractinfo:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbContract entity,HttpServletRequest request) {
        SysUser user = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        entity.setInputUser(user.getUsername());
        entity.setInputTime(LocalDateTime.now());
        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('contract:contractinfo:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbContract entity,HttpServletRequest request) {
        entity.setUpdateTime(LocalDateTime.now());
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('contract:contractinfo:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);
        return ResponseEntity.ok(ApiModel.ok());
    }

}
