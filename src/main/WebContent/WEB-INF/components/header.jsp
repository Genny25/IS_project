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
            </ul>

            <c:if test="${not empty sessionScope.user}">
                <c:choose>
                    <%-- MENU PERFORMER --%>
                    <c:when test="${sessionScope.user.role == 'Performer'}">

                    </c:when>

                    <%-- MENU CASTING DIRECTOR --%>
                    <c:when test="${sessionScope.user.role == 'CastingDirector'}">

                    </c:when>

                    <%-- MENU PRODUCTION MANAGER --%>
                    <c:when test="${sessionScope.user.role == 'ProductionManager'}">

                    </c:when>
                </c:choose>
            </c:if>
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

                        <a href="profilo.jsp" class="action-icon" aria-label="Profilo">
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