<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Accedi" scope="request"/>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle}</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<jsp:include page="/WEB-INF/components/header.jsp" />

<main class="container mt-3 mb-3">
    <div class="card registration-card login-card">
        <h1 class="text-center mb-2">Accedi</h1>

        <c:if test="${not empty errors}">
            <div class="alert-danger mb-2">
                <c:forEach var="error" items="${errors}">
                    <p>${error}</p>
                </c:forEach>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm" novalidate>

            <div class="mb-2 registration-section">

                <div class="mb-2">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email"
                           value="${param.email}" required>
                </div>

                <div class="mb-2">
                    <label for="password">Password</label>
                    <div class="password-wrapper">
                        <input type="password" id="password" name="password" required>
                        <i class="fas fa-eye toggle-password" data-target="password"></i>
                    </div>
                </div>

            </div>

            <button type="submit" class="btn btn-primary btn-block btn-lg">
                <i class="fas fa-sign-in-alt"></i> Accedi
            </button>

            <p class="text-center mt-2 login-link-text">
                Non hai un account?
                <a href="${pageContext.request.contextPath}/registration" class="login-link">
                    Registrati qui
                </a>
            </p>
        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/scripts/login-validation.js"></script>
<script src="${pageContext.request.contextPath}/scripts/form-utils.js"></script>
</body>
</html>