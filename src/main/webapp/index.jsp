<%@ page language="java" pageEncoding="UTF-8" session="true"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

    <title>TODO List</title>
</head>
<body>
<div class="container pt-3">
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                TODO List
            </div>
            <div class="card-body">
                <div class="row">
                    <div>
                        <a class="nav-link" href='<c:url value="/addtask.jsp"/>'>Добавить новое задание</a>
                    </div>
                    <div>
                        <form action="<c:url value='/status'/>" method="get">
                            <label>
                                <input type="checkbox" name="checkbox" value="true">
                            </label>
                            <input type="submit" value="Показать все">
                        </form>
                    </div>
                    <div>
                        <c:choose>
                            <c:when test = "${sessionScope.checkbox == true}">
                                <div style="color:green; font-weight: bold; margin: 5px 0;">
                                    <c:out value="Вкл"/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div style="color:red; font-weight: bold; margin: 5px 0;">
                                    <c:out value="Выкл"/>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Номер</th>
                        <th scope="col">Описание</th>
                        <th scope="col">Дата Создания</th>
                        <th scope="col">Статус задания</th>
                        <th scope="col">Смена статуса</th>
                    </tr>
                    <tbody>
                    <c:forEach items="${requestScope.items}" var="item">
                        <tr>
                            <td>
                                <c:out value="${item.id}"/>
                            </td>
                            <td>
                                <c:out value="${item.description}"/>
                            </td>
                            <td>
                                <c:out value="${item.created}"/>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test = "${item.done == true}">
                                        <div style="color:green; font-weight: bold; margin: 30px 0;">
                                            <c:out value="Задание выполнено"/>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div style="color:red; font-weight: bold; margin: 30px 0;">
                                            <c:out value="Задание не выполнено"/>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <form action="<c:url value='/status?id=${item.id}&done=${item.done}'/>" method="post" enctype="multipart/form-data">
                                    <button type="submit" class="btn btn-primary">Закрыть/Открыть задание</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>