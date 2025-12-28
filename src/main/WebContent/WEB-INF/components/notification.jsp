<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script defer src="${pageContext.request.contextPath}/scripts/notification.js"></script>

<%--@elvariable id="notificationType" type="java.lang.String"--%>
<%--@elvariable id="notificationMessage" type="java.lang.String"--%>
<c:if test="${not empty sessionScope.notificationMessage}">
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            // Escape delle virgolette per sicurezza JavaScript
            const message = '${fn:replace(sessionScope.notificationMessage, "'", "\\'")}';
            const type = '${sessionScope.notificationType}';

            if (window.notify && typeof window.notify[type] === 'function') {
                window.notify[type](message);
            } else {
                window.notify.info(message);
            }
        });
    </script>
    <c:remove var="notificationMessage" scope="session"/>
    <c:remove var="notificationType" scope="session"/>
</c:if>
<div id="notification-container"></div>