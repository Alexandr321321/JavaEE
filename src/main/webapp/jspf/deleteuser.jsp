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
        <h3>Удаление пользователя</h3>
        <table class="table table-sm" id="table-info">
            <thead>
            <tr>
                <th scope="col">Код</th>
                <th scope="col">Имя</th>
                <th scope="col">Фамилия</th>
                <th scope="col">Почта</th>
                <th scope="col">Телефон</th>
                <th scope="col">Статус</th>
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
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </aside>
    <section>
        <article>
            <h3>Удаление пользователя</h3>
            <div class="text-article">
                <form method="post" action="">
                    <div class="mb-3 row">
                        <%--@declare id="iduser"--%><label for="iduser" class="col-sm-3 col-form-label">Код пользователя</label>
                        <div class="col-sm-7">
                            <input type="number" name="iduser" class="form-control" id="staticIduser" readonly value="${usersDelete[0].getId()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="firstname"--%><label for="firstname" class="col-sm-3 col-form-label">Имя</label>
                        <div class="col-sm-7">
                            <input type="text" name="firstname" class="form-control" id="staticFirstname" readonly value="${usersDelete[0].getFirstName()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="lastname"--%><label for="lastname" class="col-sm-3 col-form-label">Фамилия</label>
                        <div class="col-sm-7">
                            <input type="text" name="lastname" class="form-control" id="staticLastname" readonly value="${usersDelete[0].getLastName()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="phone"--%><label for="phone" class="col-sm-3 col-form-label">Телефон</label>
                        <div class="col-sm-7">
                            <input type="number" name="phone" class="form-control" id="staticPhone" readonly value="${usersDelete[0].getPhone()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="email"--%><label for="email" class="col-sm-3 col-form-label">Почта</label>
                        <div class="col-sm-7">
                            <input type="text" name="email" class="form-control" id="staticEmail" readonly value="${usersDelete[0].getEmail()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="status"--%><label for="status" class="col-sm-3 col-form-label">Статус</label>
                        <div class="col-sm-7">
                            <div class="col-sm-7">
                                <input type="text" name="status" class="form-control" id="staticStatus" readonly value="${usersDelete[0].getStatus()}"/>
                            </div>
                        </div>
                    </div>
                    <button type="submit"
                            class="btn btn-primary">Удалить</button>
                    <a href='<c:url value="/users" />'
                       role="button"
                       class="btn btn-secondary">Отменить/Возврат</a>
                    </p>

                </form>
            </div>
        </article>
    </section>
</div>
</body>
<jsp:include page="footer.jsp" />
</html>

