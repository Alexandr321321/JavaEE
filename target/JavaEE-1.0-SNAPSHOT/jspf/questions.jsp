<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page import="com.example.javaee.Choice"%>
<%@ page import="com.example.javaee.Vote"%>
<%@ page import="com.example.javaee.Question"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
<title>Вопросы</title>
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
            <h3>Список вопросов</h3>
            <table>
                <thead>
                <tr>
                    <th scope="col">Код</th>
                    <th scope="col">Голосвание</th>
                    <th scope="col">Содержание вопроса</th>
                    <th scope="col">Дата голосования</th>
                    <th scope="col">Редактировать</th>
                    <th scope="col">Удалить</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="question" items="${questions}">
                    <tr>
                        <td>${question.getId()}</td>
                        <td>${question.getVoteId()}</td>
                        <td>${question.getContent()}</td>
                        <td>${question.getDateVote()}</td>
                        <td><a
                                href='<c:url value="/editquestion?id=${question.getId()}" />'
                                role="button" class="btn btn-outline-primary">
                            <img alt="Редактировать" src="${pageContext.request.contextPath}/images/edit.webp" style="width: 30px; height: 30px"></a>
                        </td>
                        <td><a
                                href='<c:url value="/deletequestion?id=${question.getId()}"/>'
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
            <h3>Добавить вопрос</h3>
            <div class="text-article">
                <form method="post" action="">
                    <div class="mb-3 row">
                        <%--@declare id="voteId"--%><label for="voteId" class="col-sm-3 col-form-label" >Голос</label>
                            <div class="col-sm-7">
                                <select name="voteId" class="form-control">
                                    <option disabled>Выберите голос</option>
                                    <c:forEach var="vote" items="${votes}">
                                        <option value="${vote}">
                                                ${vote.getTitle()}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="content"--%><label for="content" class="col-sm-3 col-form-label">Содержание вопроса</label>
                            <div class="col-sm-7">
                                <input type="text" name="content" class="form-control" id="staticContent"/>
                            </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="dateVote"--%><label for="dateVote" class="col-sm-3 col-form-label">Дата голосования</label>
                            <div class="col-sm-7">
                                <input type="date" name="dateVote" class="form-control" id="staticDateVote"/>
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