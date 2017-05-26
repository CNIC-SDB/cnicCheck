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
</head>
<body>
<div class="container">
    <button class="btn btn-primary" onclick="show()">添加</button>
    <table class="table">
        <tr>
            <th>id</th>
            <th>email</th>
            <th>上班打卡时间段</th>
            <th>下班打卡时间段</th>
            <th></th>
        </tr>
        <c:forEach items="${applicationScope.jobs}" var="job">
            <tr>
                <td>${job.id}</td>
                <td>${job.email}</td>
                <td>${job.checkInTimeFrom}-${job.checkInTimeTo}</td>
                <td>${job.checkOutTimeFrom}-${job.checkOutTimeTo}</td>
                <td>
                    <a>修改</a>
                    <a>删除</a>
                    <a>日志</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<div class="modal fade bs-example-modal-lg" id="modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            ...
        </div>
    </div>
</div>
<script src="${ctx}/resources/jquery.min.js"></script>
<script src="${ctx}/resources/bootstrapv3.3/js/bootstrap.js"></script>
<script>
    $(function () {

    });
    function show() {
        $("#modal").modal('show');
    }
</script>
</body>
</html>
