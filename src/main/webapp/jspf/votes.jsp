<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page import="com.example.javaee.Vote"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
<title>Голосования</title>
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
            <h3>Список голосований</h3>
            <table class="table table-sm" id="table-info">
                <thead>
                <tr>
                    <th scope="col">Код</th>
                    <th scope="col">Тайтл</th>
                    <th scope="col">Дата начала</th>
                    <th scope="col">Дата окончания</th>
                    <th scope="col">Статус</th>
                    <th scope="col">Редактировать</th>
                    <th scope="col">Удалить</th>
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
                        <td><a
                                href='<c:url value="/editvote?id=${vote.getId()}" />'
                                role="button" class="btn btn-outline-primary">
                            <img alt="Редактировать" src="${pageContext.request.contextPath}/images/edit.webp" style="width: 30px; height: 30px"></a>
                        </td>
                        <td><a
                                href='<c:url value="/deletevote?id=${vote.getId()}"/>'
                                role="button" class="btn btn-outline-primary"> <img
                                alt="Удалить" src="${pageContext.request.contextPath}/images/delete.webp" style="width: 30px; height: 30px"></a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </aside>
    <section>
        <article>
            <h3>Добавить голосование</h3>
            <div class="text-article">
                <form method="post" action="">
                    <div class="mb-3 row">
                        <%--@declare id="title"--%><label for="title" class="col-sm-3 col-form-label">Тайтл</label>
                            <div class="col-sm-7">
                                <input type="text" name="title" class="form-control" id="staticTitle"/>
                            </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="dateStart"--%><label for="dateStart" class="col-sm-3 col-form-label">Дата начала</label>
                            <div class="col-sm-7">
                                <input type="date" name="dateStart" class="form-control" id="staticDateStart"/>
                            </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="dateFinish"--%><label for="dateFinish" class="col-sm-3 col-form-label">Дата окончания</label>
                            <div class="col-sm-7">
                                <input type="date" name="dateFinish" class="form-control" id="staticDateFinish"/>
                            </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="status"--%><label for="status" class="col-sm-3 col-form-label">Статус</label>
                            <div class="col-sm-7">
                                <select name="status" class="form-control" id="staticStatus">
                                    <option disabled>Выберите статус</option>
                                    <option value="true">true</option>
                                    <option value="false">false</option>
                                </select>
                            </div>
                    </div>
                    <p>
                        <button class="btn btn-primary" type="submit">Добавить</button>
                    </p>
                </form>
            </div>
        </article>
    </section>
</div>
</body>
<jsp:include page="footer.jsp" />
</html>
