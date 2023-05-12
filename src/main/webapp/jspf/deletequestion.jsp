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
        <h3>Редакирование вопросов</h3>
        <table>
            <thead>
            <tr>
                <th scope="col">Код</th>
                <th scope="col">Голосвание</th>
                <th scope="col">Содержание вопроса</th>
                <th scope="col">Дата голосования</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="question" items="${questions}">
                <tr>
                    <td>${question.getId()}</td>
                    <td>${question.getVoteId()}</td>
                    <td>${question.getContent()}</td>
                    <td>${question.getDateVote()}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </aside>
    <section>
        <article>
            <h3>Редактировать вопрос</h3>
            <div class="text-article">
                <form method="post" action="">
                    <div class="mb-3 row">
                        <%--@declare id="idquestion"--%><label for="idquestion" class="col-sm-3 col-form-label">Код вопроса</label>
                        <div class="col-sm-7">
                            <input type="number" name="idquestion" class="form-control" id="staticIdquestion" readonly value="${questionsDelete[0].getId()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="voteid"--%><label for="voteid" class="col-sm-3 col-form-label" >Пользователь</label>
                        <div class="col-sm-7">
                            <input type="number" name="voteid" class="form-control" id="staticvoteid" readonly value="${questionsDelete[0].getVoteId()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="content"--%><label for="content" class="col-sm-3 col-form-label">Содержание вопроса</label>
                        <div class="col-sm-7">
                            <input type="text" name="content" class="form-control" id="staticContent" readonly value="${questionsDelete[0].getContent()}"/>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <%--@declare id="dateVote"--%><label for="dateVote" class="col-sm-3 col-form-label">Дата голосования</label>
                        <div class="col-sm-7">
                            <input type="date" name="dateVote" class="form-control" id="staticDatevote" readonly value="${questionsDelete[0].getDateVote()}"/>
                        </div>
                    </div>
                    <button type="submit"
                            class="btn btn-primary">Удалить</button>
                    <a href='<c:url value="/questions" />'
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

