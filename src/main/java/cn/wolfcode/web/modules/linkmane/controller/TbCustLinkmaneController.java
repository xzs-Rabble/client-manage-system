package cn.wolfcode.web.modules.linkmane.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.wolfcode.web.commons.entity.ExcelExportEntityWrapper;
import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.CityUtils;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.PoiExportHelper;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.custinfo.entity.TbCustomer;
import cn.wolfcode.web.modules.custinfo.service.ITbCustomerService;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysDict;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.wolfcode.web.modules.linkmane.entity.TbCustLinkmane;
import cn.wolfcode.web.modules.linkmane.service.ITbCustLinkmaneService;

import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.SameUrlData;
import link.ahsj.core.annotations.SysLog;
import link.ahsj.core.annotations.UpdateGroup;
import link.ahsj.core.entitys.ApiModel;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author HUOXINWL
 * @since 2022-06-21
 */
@Log4j2
@Controller
@RequestMapping("linkmane")
public class TbCustLinkmaneController extends BaseController {

    @Autowired
    private ITbCustLinkmaneService entityService;

    @Autowired
    private ITbCustomerService customerService;


    private static final String LogModule = "TbCustLinkmane";

    @GetMapping("/list.html")
    /*public String list() {
        return "user/linkmane/list";
    }*/
    public ModelAndView list(ModelAndView mv){
        List<TbCustomer> custlist = customerService.list();
        mv.addObject("custs",custlist);
        mv.setViewName("user/linkmane/list");
        return mv;
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('user:linkmane:add')")
    public ModelAndView toAdd(ModelAndView mv) {
        List<TbCustomer> listCustomer = customerService.list();
        mv.addObject("custs",listCustomer);
        mv.setViewName("user/linkmane/add");
        return mv;
    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('user:linkmane:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {
        List<TbCustomer> listCustomer = customerService.list();
        mv.addObject("custs",listCustomer);
        mv.setViewName("user/linkmane/update");
        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('user:linkmane:list')")
    public ResponseEntity page(LayuiPage layuiPage,String parameterName,String custId) {
        log.debug("-----数据："+parameterName+"\t"+custId);
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        IPage page1 = entityService.lambdaQuery()
                .eq(StringUtils.isNotBlank(custId),TbCustLinkmane::getCustId,custId)
                .like(StringUtils.isNotBlank(parameterName),TbCustLinkmane::getLinkman,parameterName)
                .or()
                .like(StringUtils.isNotBlank(parameterName),TbCustLinkmane::getPhone,parameterName)
                .page(page);
        List<TbCustLinkmane> records = page1.getRecords();
        for (TbCustLinkmane record:records){
            String  custName = customerService.getById(record.getCustId()).getCustomerName();
            record.setCustName(custName);
        }
        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page1));
        //return ResponseEntity.ok(LayuiTools.toLayuiTableModel(entityService.page(page)));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('user:linkmane:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbCustLinkmane entity, HttpServletRequest request) {
        SysUser user = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        entity.setInputUser(user.getUsername());
        entity.setInputTime(LocalDateTime.now());
        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('user:linkmane:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbCustLinkmane entity) {
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('user:linkmane:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.EXPORT, module = LogModule)
    @PostMapping("export")
    public void export(HttpServletResponse response,String parameterName,String custId) throws IOException {
        List<TbCustLinkmane> list = entityService.lambdaQuery()
                .eq(StringUtils.isNotBlank(custId), TbCustLinkmane::getCustId, custId)
                .and(StringUtils.isNotBlank(parameterName), q ->
                        q.like(TbCustLinkmane::getLinkman, parameterName)
                                .or()
                                .like(TbCustLinkmane::getPhone, parameterName)
                )
                .list();

        //执行文件到处
        ExportParams exportParams = new ExportParams();
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, TbCustLinkmane.class,list);
        PoiExportHelper.exportExcel(response, "客户联系人列表", workbook);
    }

    @RequestMapping("listByCustomerId")
    public ResponseEntity<ApiModel> listByCustomerId(String custId){
        if (StringUtils.isBlank(custId)) {
            return ResponseEntity.ok(ApiModel.ok());
        }

        List<TbCustLinkmane> list = entityService.lambdaQuery().eq(TbCustLinkmane::getCustId, custId).list();
        return ResponseEntity.ok(ApiModel.data(list));
    }

}
