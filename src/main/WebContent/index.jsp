<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Audire - Piattaforma Casting</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@600;700&family=Open+Sans:wght@400;600&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include Header -->
<jsp:include page="/WEB-INF/components/header.jsp" />

<!-- Main Content -->
<main>
    <div class="container">
        <!-- Hero Section
        <section class="hero">
            <div class="hero-content">
                <h1>Benvenuto su Audire</h1>
                <p>La piattaforma professionale per casting e produzioni cinematografiche</p>
                <a href="casting.jsp" class="btn btn-primary btn-lg">Esplora i Casting</a>
            </div>
        </section>

        Content Sections
        <section class="product-showcase mt-3">
            <h2 class="section-title">In Evidenza</h2>
            <div class="text-center">
                <p>Contenuto in arrivo...</p>
            </div>
        </section>-->
    </div>
</main>

<!-- Include Footer -->
<jsp:include page="/WEB-INF/components/footer.jsp" />

<script src="${pageContext.request.contextPath}/scripts/mobile-menu.js"></script>
</body>
</html>