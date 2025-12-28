<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Le mie produzioni" scope="request"/>

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

    <div class="page-header">
        <h2 class="page-title"><i class="fas fa-video"></i> Le mie Produzioni</h2>
        <a href="${pageContext.request.contextPath}/pm/add-production" class="btn btn-primary">
            <i class="fas fa-plus"></i> Nuova
        </a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert-danger mb-2">${error}</div>
    </c:if>

    <div class="productions-card">
        <div class="table-responsive">
            <table class="production-table">
                <thead>
                <tr>
                    <th width="5%">ID</th>
                    <th width="35%">Titolo</th>
                    <th width="20%">Tipologia</th>
                    <th width="20%">Data Creazione</th>
                    <th width="20%" class="text-center">Azioni</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty productions}">
                        <c:forEach var="prod" items="${productions}">
                            <tr>
                                <td class="col-id">#${prod.productionID}</td>
                                <td><strong class="col-title">${prod.title}</strong></td>
                                <td>
                                    <span class="badge-type">${prod.type}</span>
                                </td>
                                <td>
                                    <i class="far fa-calendar-alt calendar-icon"></i>
                                        ${prod.creationDate.toLocalDate()}
                                </td>
                                <td class="actions-cell">
                                    <div class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/pm/edit-production?id=${prod.productionID}"
                                           class="btn-circle btn-edit" title="Modifica">
                                            <i class="fas fa-pen"></i>
                                        </a>

                                        <a href="${pageContext.request.contextPath}/pm/delete-production?id=${prod.productionID}"
                                           class="btn-circle btn-delete"
                                           title="Elimina"
                                           onclick="return confirm('Sei sicuro di voler eliminare la produzione \'${prod.title}\'? Questa azione è irreversibile.');">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5" class="empty-state">
                                <div class="empty-state-icon">
                                    <i class="fas fa-film"></i>
                                </div>
                                <p class="empty-state-text">Non hai ancora creato nessuna produzione.</p>
                                <a href="${pageContext.request.contextPath}/pm/add-production" class="btn btn-sm btn-primary">
                                    Inizia ora
                                </a>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>

</main>

<jsp:include page="/WEB-INF/components/footer.jsp" />
<script src="${pageContext.request.contextPath}/scripts/mobile-menu.js"></script>

</body>
</html>