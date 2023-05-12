<%@page language="java" contentType="text/html"
        pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
    <title>Главная страница</title>
</head>
<body>
<jsp:include page="jspf/header.jsp" />
<div id="main">
    <h2>Функции системы</h2>
    <ul>
        <li><a href="users">Пользователи</a>
        <li><a href="choices">Выборы</a>
        <li><a href="questions">Вопросы</a>
        <li><a href="votes">Голосования</a>
    </ul>
</div>
</body>
<jsp:include page="jspf/footer.jsp" />
</html>
