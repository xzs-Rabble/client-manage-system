<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>通用后台管理模板系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${request.contextPath}/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${request.contextPath}/layuiadmin/style/admin.css" media="all">
    <link rel="stylesheet" href="${request.contextPath}/layuiadmin/style/common.css" media="all">
    <link rel="stylesheet" href="${request.contextPath}/layuiadmin/style/popup.css" media="all">
</head>
<body>

<div class="layui-card layui-content">
    <div class="layui-card-body">
        <form class="layui-form" action="" lay-filter="component-form-element">
            <div class="layui-row layui-col-space10 layui-form-item">

                <#--<div class="layui-col-lg6">
                        <label class="layui-form-label">唯一id</label>
                    <div class="layui-input-block">
                        <input type="text"  name="id"  placeholder="请输入"  autocomplete="off" class="layui-input">
                    </div>
                </div>-->
                <div class="layui-col-lg6">
                        <label class="layui-form-label">客户id</label>
                    <div class="layui-input-block">
                        <select name="custId" lay-filter="customerSelect">
                            <option value="">请选择</option>
                            <#list custs  as cust>
                                <option value="${cust.id}">${cust.customerName}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="layui-col-lg6">
                        <label class="layui-form-label">联系人id</label>
                    <div class="layui-input-block">
                        <select name="linkmanId" id="linkman">
                            <option value="">请选择</option>
                            <#--<#list linkmanIds  as linkmanId>
                                <option value="${linkmanId.id}">${linkmanId.linkman}</option>
                            </#list>-->
                        </select>
                    </div>
                </div>
                <div class="layui-col-lg6">
                        <label class="layui-form-label">拜访方式</label>
                    <div class="layui-input-block">
                        <select name="visitType">
                            <option value="1" selected>上门走访</option>
                            <option value="2">电话拜访</option>
                        </select>
                    </div>
                </div>
                <div class="layui-col-lg6">
                        <label class="layui-form-label">拜访原因</label>
                    <div class="layui-input-block">
                        <input type="text"  name="visitReason"  placeholder="请输入"  autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-col-lg6">
                        <label class="layui-form-label">交流内容</label>
                    <div class="layui-input-block">
                        <input type="text"  name="content"  placeholder="请输入"  autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-col-lg6">
                        <label class="layui-form-label">拜访时间</label>
                    <div class="layui-input-block">
                        <input type="text"  id="visitDate" name="visitDate"  placeholder="请输入"  autocomplete="off" class="layui-input">
                    </div>
                </div>
                <#--<div class="layui-col-lg6">
                        <label class="layui-form-label">录入人</label>
                    <div class="layui-input-block">
                        <input type="text"  name="inputUser"  placeholder="请输入"  autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-col-lg6">
                        <label class="layui-form-label">录入时间</label>
                    <div class="layui-input-block">
                        <input type="text"  name="inputTime"  placeholder="请输入"  autocomplete="off" class="layui-input">
                    </div>
                </div>-->

            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn layui-btn-normal" lay-submit lay-filter="Add-filter">新增</button>
                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="${request.contextPath}/layuiadmin/layui/layui.js"></script>
<script src="${request.contextPath}/layui-extend.js"></script>
<script type="text/javascript" src="${request.contextPath}/scripts/visit/visitinfo/add.js?_=${randomNum}"></script>
</body>