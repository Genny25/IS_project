<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<header>
    <div class="container">
        <button class="mobile-menu-toggle" id="mobile-menu-toggle" aria-label="Toggle menu">
            <i class="fas fa-bars"></i>
        </button>

        <div class="logo">
            <a href="${pageContext.request.contextPath}/">Audire</a>
        </div>

        <nav class="main-nav" id="main-nav">
            <ul>
                <c:choose>
                    <c:when test="${sessionScope.user.role == 'ProductionManager'}">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/pm/add-production">Nuova Produzione</a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/pm/productions">Le Mie Produzioni</a>
                        </li>
                    </c:when>

                    <c:when test="${sessionScope.user.role == 'CastingDirector'}">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/cd/">Nuovo Casting</a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/cd/">I Miei Casting</a>
                        </li>
                    </c:when>

                    <c:otherwise>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/">Home</a>
                        </li>
                        <li class="nav-item">
                            <a href="casting.jsp">Casting</a>
                        </li>
                        <li class="nav-item">
                            <a href="produzioni.jsp">Produzioni</a>
                        </li>
                        <li class="nav-item">
                            <a href="chi-siamo.jsp">Chi siamo</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>

        <div class="header-actions">
            <div class="search-bar">
                <input type="search" id="search-input" placeholder="Cerca..." aria-label="Cerca">
                <span class="search-spinner" id="search-spinner">
                    <i class="fas fa-spinner fa-spin"></i>
                </span>
                <button type="submit" aria-label="Cerca">
                    <i class="fas fa-search"></i>
                </button>
                <div class="search-suggestions-container" id="search-suggestions"></div>
            </div>

            <div class="header-icons">
                <c:choose>
                    <%-- UTENTE LOGGATO --%>
                    <c:when test="${not empty sessionScope.user}">

                        <span class="user-welcome d-none-mobile">
                            Ciao, <strong>${sessionScope.user.firstName}</strong>
                        </span>

                        <c:if test="${sessionScope.user.role == 'ProductionManager'}">
                            <a href="${pageContext.request.contextPath}/pm/productions" class="action-icon" aria-label="Dashboard" title="Dashboard">
                                <i class="fas fa-tachometer-alt"></i> </a>
                        </c:if>
                        <c:if test="${sessionScope.user.role == 'CastingDirector'}">
                            <a href="${pageContext.request.contextPath}/cd/dashboard" class="action-icon" aria-label="Dashboard" title="Dashboard">
                                <i class="fas fa-tachometer-alt"></i>
                            </a>
                        </c:if>

                        <a href="profilo.jsp" class="action-icon" aria-label="Profilo" title="Il tuo profilo">
                            <i class="fas fa-user"></i>
                        </a>

                        <a href="${pageContext.request.contextPath}/logout" class="action-icon logout-icon" title="Esci">
                            <i class="fas fa-sign-out-alt"></i>
                        </a>
                    </c:when>

                    <%-- UTENTE NON LOGGATO --%>
                    <c:otherwise>
                        <a href="profilo.jsp" class="action-icon" aria-label="Profilo">
                            <i class="fas fa-user"></i>
                        </a>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-sm">Login</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</header>

<div class="nav-overlay" id="nav-overlay"></div>
<jsp:include page="/WEB-INF/components/notification.jsp"/>