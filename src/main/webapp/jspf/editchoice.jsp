<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page import="com.example.javaee.Choice"%>
<%@ page import="com.example.javaee.User"%>
<%@ page import="com.example.javaee.Question"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
<title>Выборы</title>
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
        <h3>Редактирование выборов</h3>
        <table>
            <thead>
            <tr>
                <th scope="col">Код</th>
                <th scope="col">Вопрос</th>
                <th scope="col">Пользователь</th>
                <th scope="col">Ответ</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="choice" items="${choices}">
                <tr>
                    <td>${choice.getId()}</td>
                    <td>${choice.getQuestionId()}</td>
                    <td>${choice.getUserId()}</td>
                    <td>${choice.getChoiceUser()}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </aside>
    <section>
        <article>
            <h3>Редактировать выбор</h3>
            <div class="text-article">
                <form method="POST" action="">
                    <div class="mb-3 row">
                        <%--@declare id="idchoice"--%><label for="idchoice" class="col-sm-3 col-form-label">Код выбора</label>
                        <div class="col-sm-7">
                            <input type="number" name="idchoice" class="form-control" id="staticIdchoice" readonly value="${choicesEdit[0].getId()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="questionid"--%><label for="questionid" class="col-sm-3 col-form-label">Вопрос</label>
                        <div class="col-sm-7">
                            <select name="questionId" class="form-control">
                                <option disabled>Выберите вопрос</option>
                                <c:forEach var="question" items="${questions}">
                                    <option value="${question}">
                                        <c:out value="${question.getContent()}"></c:out>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="userid"--%><label for="userid" class="col-sm-3 col-form-label">Пользователь</label>
                        <div class="col-sm-7">
                            <select name="userId" class="form-control">
                                <option disabled>Выберите пользователя</option>
                                <c:forEach var="user" items="${users}">
                                    <option value="${user}">
                                        <c:out value="${user.getFirstName()}"></c:out> <c:out value="${user.getLastName()}"></c:out>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="choiceuser"--%><label for="choiceuser" class="col-sm-3 col-form-label">Выбор пользователя</label>
                        <div class="col-sm-7">
                            <input type="number" name="choiceUser"  class="form-control" id="staticChoiceUser" value="${choicesEdit[0].getChoiceUser()}"/>
                        </div>
                    </div>
                    <button type="submit"
                            class="btn btn-primary">Редактировать</button>
                    <a href='<c:url value="/choices" />'
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
