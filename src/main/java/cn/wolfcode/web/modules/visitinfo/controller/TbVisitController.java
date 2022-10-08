package cn.wolfcode.web.modules.visitinfo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.PoiExportHelper;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.custinfo.entity.TbCustomer;
import cn.wolfcode.web.modules.custinfo.service.ITbCustomerService;
import cn.wolfcode.web.modules.linkmane.entity.TbCustLinkmane;
import cn.wolfcode.web.modules.linkmane.service.ITbCustLinkmaneService;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.wolfcode.web.modules.visitinfo.entity.TbVisit;
import cn.wolfcode.web.modules.visitinfo.service.ITbVisitService;

import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.SameUrlData;
import link.ahsj.core.annotations.SysLog;
import link.ahsj.core.annotations.UpdateGroup;
import link.ahsj.core.entitys.ApiModel;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * @author HUOXINWL
 * @since 2022-06-22
 */
@Controller
@RequestMapping("visitinfo")
public class TbVisitController extends BaseController {

    @Autowired
    private ITbVisitService entityService;
    @Autowired
    private ITbCustomerService customerService;
    @Autowired
    private ITbCustLinkmaneService linkmaneService;

    private static final String LogModule = "TbVisit";

    @GetMapping("/list.html")
    public String list() {
        return "visit/visitinfo/list";
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('visit:visitinfo:add')")
    public ModelAndView toAdd(ModelAndView mv) {
        List<TbCustomer> custs = customerService.list();
        mv.addObject("custs",custs);
        /*List<CustAndLinkman> custAndLinkmenList = new LinkedList<>();
        for (TbCustomer tbCustomer:listCustomer){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("cust_id",tbCustomer.getId());
            TbCustLinkmane tbCustLinkmane = linkmaneService.getOne(wrapper);
            CustAndLinkman custAndLinkman = new CustAndLinkman(tbCustLinkmane, tbCustomer);
            custAndLinkmenList.add(custAndLinkman);
        }*/
        List<TbCustLinkmane> linkmanId = linkmaneService.list();
        mv.addObject("linkmanIds",linkmanId);
        mv.setViewName("visit/visitinfo/add");
        return mv;
    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('visit:visitinfo:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {
        List<TbCustomer> listCustomer = customerService.list();
        mv.addObject("custs",listCustomer);
        List<TbCustLinkmane> linkmaneList = linkmaneService.list();
        mv.addObject("linkmanIds",linkmaneList);
        mv.setViewName("visit/visitinfo/update");
        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('visit:visitinfo:list')")
    public ResponseEntity page(LayuiPage layuiPage, String parameterName, String selectedVisitType, LocalDate Time) {
        System.out.println("这里啊"+Time);
        System.out.println(Time);
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        if (selectedVisitType == null) selectedVisitType = "0";
        IPage page1 = entityService.lambdaQuery()
                .eq(!selectedVisitType.equals("0"),TbVisit::getVisitType,Integer.parseInt(selectedVisitType))
                .like(StringUtils.isNotBlank(parameterName),TbVisit::getVisitReason,parameterName)
                .or()
                .like(StringUtils.isNotBlank(parameterName),TbVisit::getContent,parameterName)
                .page(page);

        //回显名字
        List<TbVisit> records = page1.getRecords();
        for (TbVisit record:records){
            String custName = customerService.getById(record.getCustId()).getCustomerName();
            record.setCustName(custName);
            String linkmanName = linkmaneService.getById(record.getLinkmanId()).getLinkman();
            record.setLinkmanName(linkmanName);
        }
        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page1));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('visit:visitinfo:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbVisit entity, HttpServletRequest request) {
        SysUser user = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        entity.setInputUser(user.getUsername());
        entity.setInputTime(LocalDateTime.now());
        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('visit:visitinfo:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbVisit entity) {
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('visit:visitinfo:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {
        entityService.removeById(id);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.EXPORT, module = LogModule)
    @PostMapping("export")
    public void export(HttpServletResponse response, String parameterName, String selectedVisitType) throws IOException {
        if (selectedVisitType == null) selectedVisitType = "0";
        List<TbVisit> list = entityService.lambdaQuery()
                .eq(!selectedVisitType.equals("0"),TbVisit::getVisitType,Integer.parseInt(selectedVisitType))
                .and(StringUtils.isNotBlank(parameterName), q ->
                        q.like(TbVisit::getVisitReason, parameterName)
                                .or()
                                .like(TbVisit::getContent, parameterName)
                )
                .list();

        //执行文件到处
        ExportParams exportParams = new ExportParams();
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, TbVisit.class,list);
        PoiExportHelper.exportExcel(response, "联系人走访管理", workbook);
    }

}
