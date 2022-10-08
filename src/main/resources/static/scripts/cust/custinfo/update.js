layui.use(['form', 'layer','laydate'], function () {
    var form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate,
        $ = layui.jquery;

    //执行一个laydate实例
    laydate.render({
        elem: '#registerDate' //指定元素
    });

    form.on('submit(Add-filter)', function (data) {
        $.ajax({
            url: web.rootPath() + "custinfo/update",
            contentType: "application/json",
            type: "put",
            data: JSON.stringify(data.field),
            dataType: 'json',
            success: function (data) {
                layer.msg("操作成功", {
                    icon: 1,
                    success: function () {
                        reloadTb("Update-frame", "#SearchBtn");
                    }
                });
            },
            error: function (e) {
                if(e.responseJSON.errorCode == 1003){
                    layer.msg(e.responseJSON.data, {icon: 2});
                }else{
                    layer.msg(e.responseJSON.message, {icon: 2});
                }
            }

        })
        return false;
    });

});
