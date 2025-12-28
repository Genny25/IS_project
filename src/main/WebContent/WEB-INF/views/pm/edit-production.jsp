<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Modifica Produzione" scope="request"/>

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
            <i class="fas fa-edit"></i> Modifica Produzione
        </h2>
        <p class="form-subtitle text-center mb-2">
            Aggiorna i dettagli del progetto <strong>${production.title}</strong>.
        </p>

        <c:if test="${not empty errors}">
            <div class="alert-danger mb-2">
                <c:forEach var="error" items="${errors}">
                    <p>${error}</p>
                </c:forEach>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/pm/edit-production" method="post" id="productionForm" novalidate>

            <input type="hidden" name="id" value="${production.productionID}">

            <div class="mb-2 registration-section">

                <div class="mb-2">
                    <label for="title">Titolo del Progetto <span class="required-asterisk">*</span></label>
                    <input type="text" id="title" name="title"
                           value="${production.title}" required
                           placeholder="Es. Il Gladiatore 2">
                </div>

                <div class="mb-2">
                    <label for="type">Tipologia <span class="required-asterisk">*</span></label>
                    <select id="type" name="type" required>
                        <option value="">-- Seleziona --</option>
                        <option value="Film" ${production.type == 'Film' ? 'selected' : ''}>Film</option>
                        <option value="Serie_TV" ${production.type == 'Serie_TV' ? 'selected' : ''}>Serie TV</option>
                        <option value="Teatro" ${production.type == 'Teatro' ? 'selected' : ''}>Teatro</option>
                        <option value="Musical" ${production.type == 'Musical' ? 'selected' : ''}>Musical</option>
                        <option value="Cortometraggio" ${production.type == 'Cortometraggio' ? 'selected' : ''}>Cortometraggio</option>
                        <option value="Documentario" ${production.type == 'Documentario' ? 'selected' : ''}>Documentario</option>
                        <option value="Pubblicità" ${production.type == 'Pubblicità' ? 'selected' : ''}>Pubblicità</option>
                        <option value="Web_Series" ${production.type == 'Web_Series' ? 'selected' : ''}>Web Series</option>
                        <option value="Altro" ${production.type == 'Altro' ? 'selected' : ''}>Altro</option>
                    </select>
                </div>

            </div>

            <button type="submit" class="btn btn-primary btn-block btn-lg">
                <i class="fas fa-save"></i> Salva Modifiche
            </button>

            <div class="text-center mt-2">
                <a href="${pageContext.request.contextPath}/pm/productions" class="login-link">Annulla</a>
            </div>

        </form>
    </div>
</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/scripts/form-utils.js"></script>
<script src="${pageContext.request.contextPath}/scripts/production-validation.js"></script>

</body>
</html>