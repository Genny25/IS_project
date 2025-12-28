document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('productionForm');
    const errorContainer = document.getElementById('errorContainer');

    // Enable live validation (green/red borders)
    FormUtils.bindLiveValidation(form);

    form.addEventListener('submit', function(e) {
        FormUtils.clearErrors(errorContainer);
        let errorMessages = [];
        let formIsValid = true;

        const title = document.getElementById('title');
        const type = document.getElementById('type');

        // Check Title
        if (!title.value.trim()) {
            formIsValid = false;
            title.classList.add('invalid-field');
            errorMessages.push("Il titolo è obbligatorio.");
        }

        // Check Type
        if (!type.value) {
            formIsValid = false;
            type.classList.add('invalid-field');
            errorMessages.push("Devi selezionare una tipologia.");
        }

        if (!formIsValid) {
            e.preventDefault();
            FormUtils.showErrors(errorContainer, errorMessages);
        } else {
            const btn = form.querySelector('button[type="submit"]');
            FormUtils.setLoadingButton(btn, true, "Creazione...");
        }
    });
});