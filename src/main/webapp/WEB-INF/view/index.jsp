<%--
  Created by IntelliJ IDEA.
  User: liuang
  Date: 2017/5/26
  Time: 19:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>FOR FREEDOM</title>
    <link rel="stylesheet" href="${ctx}/resources/bootstrapv3.3/css/bootstrap.min.css">
    <style>
        .bg {
            background: url(${ctx}/resources/freedom.png);
            filter: "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale')";
            -moz-background-size: 100% 100%;
            background-size: 100% 100%;
        }

        table th {
            font-size: 12px;
        }
    </style>
</head>
<body class="bg">
<div class="container">
    <div class="row">
        <div style="float: right" id="time"></div>
    </div>
    <button class="btn btn-primary" onclick="show()">添加</button>
    <table class="table">
        <tr>
            <th>id</th>
            <th>email</th>
            <th>code</th>
            <th>上班打卡时间段</th>
            <th>下班打卡时间段</th>
            <th>今日上班时间</th>
            <th>今日下班时间</th>
            <th></th>
        </tr>
        <c:forEach items="${applicationScope.jobs}" var="job">
            <tr>
                <td>${job.id}</td>
                <td>${job.email}</td>
                <td>${job.code}</td>
                <td>${job.checkInTimeFrom}-${job.checkInTimeTo}</td>
                <td>${job.checkOutTimeFrom}-${job.checkOutTimeTo}</td>
                <td>${job.checkInTime}</td>
                <td>${job.checkOutTime}</td>
                <td>
                    <a href="#" class="edit btn btn-primary btn-sm">修改</a>
                    <a href="#" class="del btn btn-danger btn-sm" data-id="${job.id}">删除</a>
                    <a href="#" class="check btn btn-default btn-sm" data-id="${job.id}" data-type="checkin">上班</a>
                    <a href="#" class="check btn btn-default btn-sm" data-id="${job.id}" data-type="checkout">下班</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<div class="modal fade bs-example-modal-lg" id="modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="exampleModalLabel">悄悄滴打枪地不要</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="id" class="col-sm-2 control-label">id</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="id" placeholder="id">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="email" class="col-sm-2 control-label">email</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="email" placeholder="email">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="code" class="col-sm-2 control-label">code</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="code" placeholder="code">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="checkInTime" class="col-sm-2 control-label">上班打卡时间段</label>
                        <div class="col-sm-10">
                            <select id="checkInTime">
                                <option value="8:00-8:30">8:00-8:30</option>
                                <option value="8:30-9:00">8:30-9:00</option>
                                <option value="9:00-9:30">9:00-9:30</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="checkOutTime" class="col-sm-2 control-label">下班打卡时间段</label>
                        <div class="col-sm-10">
                            <select id="checkOutTime">
                                <option value="17:00-19:00">17点以后</option>
                                <option value="17:30-19:30">17:30以后</option>
                                <option value="18:00-20:00">18点以后</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="button" class="btn btn-default" onclick="add()">确定</button>
                        </div>
                    </div>
                </form>
            </div>

        </div>
    </div>
</div>
<script src="${ctx}/resources/jquery.min.js"></script>
<script src="${ctx}/resources/bootstrapv3.3/js/bootstrap.js"></script>
<script>
    $(function () {
        $(".edit").click(function (e) {
            e.preventDefault();
            var tds = $(this).parent().siblings();
            $("#id").val($(tds[0]).html());
            $("#email").val($(tds[1]).html());
            $("#code").val($(tds[2]).html());
            $("#checkInTime").val($(tds[3]).html());
            $("#checkOutTime").val($(tds[4]).html());
            show();

        });
        $(".del").click(function (e) {
            e.preventDefault();
            $.ajax({
                url: "${ctx}/del",
                type: "get",
                data: {id: $(this).attr("data-id")},
                success: function (r) {
                    if (r) {
                        window.location.reload(true);
                    }
                }
            });
        });
        $(".check").click(function (e) {
            e.preventDefault();
            $.ajax({
                url: "${ctx}/check",
                type: "get",
                data: {id: $(this).attr("data-id"), type: $(this).attr("data-type")},
                success: function (r) {
                    if (r && r == 'true') {
                        alert("打卡成功");
                    } else {
                        alert("打卡失败");
                    }
                },
                error: function () {
                    alert("打卡失败");
                }
            });
        });
        window.setInterval(function () {
            $.ajax({
                url: "${ctx}/time",
                type: "get",
                success: function (result) {
                    $("#time").html(result);
                }
            });
        }, 1000);

    });
    function show() {
        $("#modal").modal('show');
    }
    function add() {
        $.ajax({
            url: "${ctx}/add",
            type: "post",
            data: {
                id: $("#id").val(),
                email: $("#email").val(),
                checkInTimeFrom: $("#checkInTime").val().split('-')[0],
                checkInTimeTo: $("#checkInTime").val().split('-')[1],
                checkOutTimeFrom: $("#checkOutTime").val().split('-')[0],
                checkOutTimeTo: $("#checkOutTime").val().split('-')[1],
                code: $("#code").val()
            },
            success: function (result) {
                if (result) {
                    window.location.reload(true);
                }
                $("#modal").modal('hide');
            },
            error: function (e) {
                alert("出错了");
            }
        });
    }
</script>
</body>
</html>
