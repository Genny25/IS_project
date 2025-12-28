<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Nuova Produzione" scope="request"/>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle}</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/production.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<jsp:include page="/WEB-INF/components/header.jsp" />

<main class="container mt-3 mb-3">

    <div class="card login-card">
        <h2 class="form-title text-center mb-2">
            <i class="fas fa-clapperboard"></i> Nuova Produzione
        </h2>
        <p class="form-subtitle text-center mb-2">
            Crea un nuovo progetto per iniziare i casting.
        </p>

        <c:if test="${not empty errors}">
            <div class="alert-danger mb-2">
                <c:forEach var="error" items="${errors}">
                    <p>${error}</p>
                </c:forEach>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/pm/add-production" method="post" id="productionForm" novalidate>

            <div class="mb-2 registration-section">

                <div class="mb-2">
                    <label for="title">Titolo del Progetto <span class="required-asterisk">*</span></label>
                    <input type="text" id="title" name="title"
                           value="${param.title}" required
                           placeholder="Es. Il Gladiatore 2">
                </div>

                <div class="mb-2">
                    <label for="type">Tipologia <span class="required-asterisk">*</span></label>
                    <select id="type" name="type" required>
                        <option value="">-- Seleziona --</option>
                        <option value="Film" ${param.type == 'Film' ? 'selected' : ''}>Film</option>
                        <option value="Serie_TV" ${param.type == 'Serie_TV' ? 'selected' : ''}>Serie TV</option>
                        <option value="Teatro" ${param.type == 'Teatro' ? 'selected' : ''}>Teatro</option>
                        <option value="Musical" ${param.type == 'Musical' ? 'selected' : ''}>Musical</option>
                        <option value="Cortometraggio" ${param.type == 'Cortometraggio' ? 'selected' : ''}>Cortometraggio</option>
                        <option value="Documentario" ${param.type == 'Documentario' ? 'selected' : ''}>Documentario</option>
                        <option value="Pubblicità" ${param.type == 'Pubblicità' ? 'selected' : ''}>Pubblicità</option>
                        <option value="Web_Series" ${param.type == 'Web_Series' ? 'selected' : ''}>Web Series</option>
                        <option value="Altro" ${param.type == 'Altro' ? 'selected' : ''}>Altro</option>
                    </select>
                </div>

            </div>

            <button type="submit" class="btn btn-primary btn-block btn-lg">
                <i class="fas fa-plus-circle"></i> Crea Produzione
            </button>

            <div class="text-center mt-2">
                <a href="${pageContext.request.contextPath}/pm/productions" class="login-link">Annulla e torna alla lista</a>
            </div>

        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/scripts/form-utils.js"></script>
<script src="${pageContext.request.contextPath}/scripts/production-validation.js"></script>

</body>
</html>