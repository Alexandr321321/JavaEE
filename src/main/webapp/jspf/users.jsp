<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page import="com.example.javaee.User"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
<title>Пользователи</title>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="header.jsp" />
<div id="main">
        <aside class="leftAside">
            <h3>Список пользователей</h3>
            <table class="table table-sm" id="table-info">
                <thead>
                <tr>
                    <th scope="col">Код</th>
                    <th scope="col">Имя</th>
                    <th scope="col">Фамилия</th>
                    <th scope="col">Почта</th>
                    <th scope="col">Телефон</th>
                    <th scope="col">Статус</th>
                    <th scope="col">Редактировать</th>
                    <th scope="col">Удалить</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.getId()}</td>
                        <td>${user.getFirstName()}</td>
                        <td>${user.getLastName()}</td>
                        <td>${user.getPhone()}</td>
                        <td>${user.getEmail()}</td>
                        <td>${user.getStatus()}</td>
                        <td><a
                                href='<c:url value="/edituser?id=${user.getId()}" />'
                                role="button" class="btn btn-outline-primary">
                            <img alt="Редактировать" src="${pageContext.request.contextPath}/images/edit.webp" style="width: 30px; height: 30px"></a>
                        </td>
                        <td><a
                            href='<c:url value="/deleteuser?id=${user.getId()}"/>'
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
            <h3>Добавить пользователя</h3>
            <div class="text-article">
                <form method="post" action="">
                    <div class="mb-3 row">
                        <%--@declare id="firstname"--%><label for="firstname" class="col-sm-3 col-form-label">Имя</label>
                            <div class="col-sm-7">
                                <input type="text" name="firstname" class="form-control" id="staticFirstname" />
                            </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="lastname"--%><label for="lastname" class="col-sm-3 col-form-label">Фамилия</label>
                            <div class="col-sm-7">
                                <input type="text" name="lastname" class="form-control" id="staticLastname"/>
                            </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="phone"--%><label for="phone" class="col-sm-3 col-form-label">Телефон</label>
                            <div class="col-sm-7">
                                <input type="number" name="phone" class="form-control" id="staticPhone" />
                            </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="email"--%><label for="email" class="col-sm-3 col-form-label">Почта</label>
                            <div class="col-sm-7">
                                <input type="text" name="email" class="form-control" id="staticEmail" />
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
