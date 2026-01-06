<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Footer -->
<footer>
    <div class="container">
        <div class="footer-columns">
            <!-- Logo Column -->
            <div class="footer-column">
                <div class="footer-logo">
                    <img src="images/logo.png" alt="Audire Logo" style="max-width: 150px;">
                </div>
            </div>

            <!-- Contatti e Assistenza -->
            <div class="footer-column">
                <h4>Contatti e Assistenza</h4>
                <ul>
                    <li><a href="faq.jsp">FAQ</a></li>
                    <li><a href="contattaci.jsp">Contattaci</a></li>
                    <li><a href="supporto-tecnico.jsp">Supporto tecnico</a></li>
                </ul>
            </div>

            <!-- Informazioni -->
            <div class="footer-column">
                <h4>Informazioni</h4>
                <ul>
                    <li><a href="privacy-policy.jsp">Privacy policy</a></li>
                    <li><a href="termini-condizioni.jsp">Termini e condizioni d'uso</a></li>
                    <li><a href="gdpr.jsp">Informativa GDPR</a></li>
                    <li><a href="cookie-policy.jsp">Cookie policy</a></li>
                </ul>
            </div>

            <!-- Social -->
            <div class="footer-column">
                <h4>Social</h4>
                <div class="social-icons">
                    <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" aria-label="Facebook">
                        <i class="fab fa-facebook"></i>
                    </a>
                    <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" aria-label="Instagram">
                        <i class="fab fa-instagram"></i>
                    </a>
                    <a href="https://linkedin.com" target="_blank" rel="noopener noreferrer" aria-label="LinkedIn">
                        <i class="fab fa-linkedin"></i>
                    </a>
                </div>
            </div>
        </div>

        <!-- Copyright -->
        <div class="text-center mt-2" style="padding-top: 1rem; border-top: 1px solid rgba(255,255,255,0.1); color: var(--color-text-secondary);">
            <p>&copy; <%= java.time.Year.now().getValue() %> Audire. Tutti i diritti riservati.</p>
        </div>
    </div>
</footer>