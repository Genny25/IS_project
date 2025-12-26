document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('loginForm');
    const errorContainer = document.getElementById('errorContainer');

    FormUtils.initPasswordToggles();
    FormUtils.bindLiveValidation(form);

    form.addEventListener('submit', function(e) {
        FormUtils.clearErrors(errorContainer);

        if (!form.checkValidity()) {
            e.preventDefault();

            form.querySelectorAll('input').forEach(i => FormUtils.validateField(i));
            FormUtils.showErrors(errorContainer, ["Inserisci email e password."]);
        } else {
            const btn = form.querySelector('button[type="submit"]');
            FormUtils.setLoadingButton(btn, true, "Accesso...");
        }
    });
});