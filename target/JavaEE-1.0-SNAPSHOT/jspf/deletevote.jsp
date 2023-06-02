<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page import="com.example.javaee.Vote"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
<title>Голосование</title>
<head>
    <meta charset="UTF-8">
    <title>Votes</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="header.jsp" />
<div id="main">
    <aside class="leftAside">
        <h3>Удаление голосования</h3>
        <table class="table table-sm" id="table-info">
            <thead>
            <tr>
                <th scope="col">Код</th>
                <th scope="col">Тайтл</th>
                <th scope="col">Дата начала</th>
                <th scope="col">Дата окончания</th>
                <th scope="col">Статус</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="vote" items="${votes}">
                <tr>
                    <td>${vote.getId()}</td>
                    <td>${vote.getTitle()}</td>
                    <td>${vote.getDateStart()}</td>
                    <td>${vote.getDateFinish()}</td>
                    <td>${vote.getStatus()}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </aside>
    <section>
        <article>
            <h3>Удалить голосование</h3>
            <div class="text-article">
                <form method="post" action="">
                    <div class="mb-3 row">
                        <%--@declare id="idvote"--%><label for="idvote" class="col-sm-3 col-form-label">Код голоса</label>
                        <div class="col-sm-7">
                            <input type="number" name="idvote" class="form-control" id="staticIdvote" readonly value="${votesDelete[0].getId()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="title"--%><label for="title" class="col-sm-3 col-form-label">Тайтл</label>
                        <div class="col-sm-7">
                            <input type="text" name="title" class="form-control" id="staticTitle" readonly value="${votesDelete[0].getTitle()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="dateStart"--%><label for="dateStart" class="col-sm-3 col-form-label">Дата начала</label>
                        <div class="col-sm-7">
                            <input type="date" name="dateStart" class="form-control" id="staticDateStart" readonly value="${votesDelete[0].getDateStart()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="dateFinish"--%><label for="dateFinish" class="col-sm-3 col-form-label">Дата окончания</label>
                        <div class="col-sm-7">
                            <input type="date" name="dateFinish" class="form-control" id="staticDateFinish" readonly value="${votesDelete[0].getDateFinish()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="status"--%><label for="status" class="col-sm-3 col-form-label">Статус</label>
                        <div class="col-sm-7">
                            <input type="text" name="status" class="form-control" id="staticStatus" readonly value="${votesDelete[0].getStatus()}"/>
                        </div>
                    </div>
                    <button type="submit"
                            class="btn btn-primary">Удалить</button>
                    <a href='<c:url value="/votes" />'
                       role="button"
                       class="btn btn-secondary">Отменить/Возврат</a>
                </form>
            </div>
        </article>
    </section>
</div>
</body>
<jsp:include page="footer.jsp" />
</html>

