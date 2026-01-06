<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Gestione Team - ${production.title}" scope="request"/>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/production.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<jsp:include page="/WEB-INF/components/header.jsp" />

<main class="container mt-3 mb-3">

    <div class="page-header">
        <div>
            <h2 class="page-title"><i class="fas fa-users"></i> Gestione Team</h2>
            <p class="form-subtitle">Produzione: <strong>${production.title}</strong></p>
        </div>
        <a href="${pageContext.request.contextPath}/pm/productions" class="btn btn-secondary btn-sm">
            <i class="fas fa-arrow-left"></i> Torna alla lista
        </a>
    </div>

    <div class="card team-card">
        <h4><i class="fas fa-user-plus"></i> Aggiungi Casting Director</h4>
        <p>Seleziona un professionista disponibile dalla lista (Nome e Email).</p>

        <form action="${pageContext.request.contextPath}/pm/team" method="post" class="team-form">
            <input type="hidden" name="action" value="add">
            <input type="hidden" name="productionId" value="${production.productionID}">

            <div class="team-select-group">
                <label for="userId" class="team-select-label">Seleziona Utente:</label>

                <select name="userId" id="userId" required class="team-select">
                    <option value="">-- Seleziona un Casting Director --</option>

                    <c:forEach var="cd" items="${availableCDs}">
                        <option value="${cd.userID}">
                                ${cd.firstName} ${cd.lastName} &nbsp; • &nbsp; ${cd.email}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <button type="submit" class="btn btn-primary" ${empty availableCDs ? 'disabled' : ''}>
                Assegna al Team
            </button>
        </form>

        <c:if test="${empty availableCDs}">
            <p class="text-danger mt-1" style="font-size: 0.9rem;">
                <i class="fas fa-exclamation-circle"></i> Non ci sono altri Casting Director disponibili nel sistema.
            </p>
        </c:if>
    </div>

    <div class="productions-card">
        <div class="table-responsive">
            <table class="production-table">
                <thead>
                <tr>
                    <th width="40%">Nome</th>
                    <th width="40%">Email</th>
                    <th width="20%" class="text-center">Azioni</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty currentTeam}">
                        <c:forEach var="member" items="${currentTeam}">
                            <tr>
                                <td>
                                    <div class="avatar-container">
                                        <div class="avatar-circle">
                                                ${member.firstName.charAt(0)}${member.lastName.charAt(0)}
                                        </div>
                                        <strong>${member.firstName} ${member.lastName}</strong>
                                    </div>
                                </td>
                                <td>
                                    <a href="mailto:${member.email}" class="table-link text-secondary">
                                            ${member.email}
                                    </a>
                                </td>
                                <td class="text-center">
                                    <form action="${pageContext.request.contextPath}/pm/remove-team-member" method="post" style="margin: 0;">

                                        <input type="hidden" name="productionId" value="${production.productionID}">
                                        <input type="hidden" name="userId" value="${member.userID}">

                                        <button type="submit" class="btn-circle btn-delete" title="Rimuovi dal team"
                                                onclick="return confirm('Sei sicuro di voler rimuovere ${member.firstName} ${member.lastName} dal team?');">
                                            <i class="fas fa-user-minus"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="3" class="empty-state">
                                <div class="empty-state-icon" style="opacity: 0.5;">
                                    <i class="fas fa-users-slash"></i>
                                </div>
                                <p class="empty-state-text">Il team è vuoto. Aggiungi il primo membro sopra.</p>
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