layui.use(['form', 'layer','laydate'], function () {
    var form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate,
        $ = layui.jquery;

    //执行一个laydate实例
    laydate.render({
        elem: '#visitDate' //指定元素
    });

    //监听企业客户选择事件
    form.on('select(customerSelect)',function (data){
        $.ajax({
            url: web.rootPath()+"linkmane/listByCustomerId?custId="+data.value,
            type: 'post',
            contentType: "application/json",
            dataType: 'json',
            traditional: true,
            success: function (data) {
                //清空原来的内容
                $('#linkman').empty();

                var optionHtml = '<option value="">请选择</option>'
                if(data.data.length > 0){
                    data.data.forEach(item => {
                        optionHtml += `<option value="${item.id}">${item.linkman}</option>`
                    })
                }

                //设置选择信息
                $("#linkman").html(optionHtml)
                //渲染表单
                form.render("select","component-form-element")
            },
            error: function (e) {
                layer.msg(e.responseJSON.message, {icon: 2});
            }
        })
    });

    form.on('submit(Add-filter)', function (data) {
        $.ajax({
            url: web.rootPath() + "visitinfo/update",
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
                layer.msg(e.responseJSON.message, {icon: 2});
            }

        })
        return false;
    });

});
