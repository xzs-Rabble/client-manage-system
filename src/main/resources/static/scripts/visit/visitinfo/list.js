layui.use(['form', 'layer', 'table', 'laytpl', 'laydate'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        laydate = layui.laydate,
        table = layui.table;

    laydate.render({
        elem: '#Time', //指定元素
        type: 'datetime',
        range: true,
        //max : getNowFormatDate()
    });

    //执行一个laydate实例
    laydate.render({
        elem: '#visitStartDate' //指定元素
    });

    //执行一个laydate实例
    laydate.render({
        elem: '#visitEndDate' //指定元素
    });

    //用户列表
    var tableIns = table.render({
        elem: '#List',
        url: web.rootPath() + 'visitinfo/list',
        cellMinWidth: 95,
        page: true,
        height: "full-" + Math.round(Number($('.layui-card-header').height()) + 44),
        limits: [10, 13, 15, 20, 30, 50, 100, 200],
        limit: 10,
        toolbar: '#List-toolbar',
        id: "ListTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
                    {field: 'id', title: '唯一id', minWidth: 100, align: "center"},
                    {field: 'custName', title: '客户id', minWidth: 100, align: "center"},
                    {field: 'linkmanName', title: '联系人id', minWidth: 100, align: "center"},
                    {field: 'visitType', title: '拜访方式', minWidth: 100, align: "center",templet: function (d) {
                            if (d.visitType == '1') {
                                return "上门走访";
                            } else if (d.visitType == '2') {
                                return "电话拜访";
                            }
                        }},
                    {field: 'visitReason', title: '拜访原因', minWidth: 100, align: "center"},
                    {field: 'content', title: '交流内容', minWidth: 100, align: "center"},
                    {field: 'visitDate', title: '拜访时间', minWidth: 100, align: "center"},
                    {field: 'inputUser', title: '录入人', minWidth: 100, align: "center"},
                    {field: 'inputTime', title: '录入时间', minWidth: 100, align: "center"},

            {title: '操作', width: 160, templet: '#List-editBar', fixed: "right", align: "center"}
        ]],

    });

    //头工具栏事件
    table.on('toolbar(List-toolbar)', function (obj) {
        switch (obj.event) {
            case 'add':
                layer.msg("add");
                break;
            case 'update':
                layer.msg("update");
                break;
            case 'delete':
                layer.msg("delete");
                break;
            case 'export':
                layer.msg("export");
                break;
        }
        ;
    });

    var $ = layui.$, active = {
        reload: function () {
            //获取搜索条件值
            var parameterName = $("#searchForm").find("input[name='parameterName']").val().trim();
            var selectedVisitType = $("#searchForm").find("select[name='selectedVisitType']").val().trim();
            //表格重载
            tableIns.reload({
                where: { //设定异步数据接口的额外参数，任意设
                    parameterName: parameterName,
                    selectedVisitType: selectedVisitType
                }
            });
        }
    };

    $('#SearchBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });


    //头工具栏事件
    table.on('toolbar(List-toolbar)', function (obj) {
        if (obj.event == 'add') {
            layer.open({
                id: "Save-frame",
                type: 2,
                area: ['600px', '501px'],
                title: '新增',
                fixed: false,
                maxmin: true,
                content: web.rootPath() + 'visitinfo/add.html'
            });
        }
        if (obj.event == 'export'){
            var eix;
            /*var diceName = $("#searchForm").find("input[name='diceName']").val().trim();
            var diceCodes = formSelects.value("diceCodes-select2", "val");*/
            var url = web.rootPath() + 'visitinfo/export'
            $.fileDownload(url, {
                httpMethod: 'POST',
                prepareCallback: function (url) {
                    eix = layer.load(2);
                },
                successCallback: function (url) {
                    layer.close(eix)
                },
                failCallback: function (html, url) {
                    layer.close(eix)
                    layer.msg("导出失败", {icon: 2});
                }
            });
        }
        ;
    });
    //监听工具条
    table.on('tool(List-toolbar)', function (obj) {
        var data = obj.data;
        switch (obj.event) {
            case 'update':
                layer.open({
                    id: "Update-frame",
                    type: 2,
                    resize: false,
                    area: ['550px', '500px'],
                    title: '修改',
                    fixed: false,
                    maxmin: true,
                    content: web.rootPath() + "visitinfo/" + data.id + ".html?_"
                });
                break;
            case 'delete':
                var index = layer.confirm('确定要删除?', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    $.ajax({
                        url: web.rootPath() + "visitinfo/delete/" + data.id,
                        type: "delete",
                        contentType: "application/json;charset=utf-8",
                        data: JSON.stringify(data.field),
                        dataType: 'json',
                        success: function (data) {
                            layer.msg("操作成功", {
                                icon: 1,
                                success: function () {
                                    $('#SearchBtn').trigger("click");
                                }
                            });
                        },
                        error: function (e) {
                            layer.msg(e.responseJSON.message, {icon: 2});
                        }
                    })
                }, function () {
                });
                break;
        };
    });

    $(window).resize(function () {
        $('div[lay-id="ListTable"]').height(document.body.offsetHeight - Math.round(Number($('.layui-card-header').height()) + 47));
    });
});
