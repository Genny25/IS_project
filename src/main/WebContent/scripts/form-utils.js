/**
 * form-utils.js
 * Utility library for form validation, error handling, and UI manipulation.
 */
const FormUtils = {

    /**
     * Displays a list of error messages within a specific container.
     * Clears previous errors, makes the container visible, and scrolls it into view.
     *
     * @param {HTMLElement} container - The div element where errors will be displayed.
     * @param {Array<string>} errors - An array of error message strings.
     */
    showErrors: function(container, errors) {
        if (!container) return;

        container.innerHTML = '';
        container.style.display = 'block';

        errors.forEach(err => {
            const p = document.createElement('p');
            p.textContent = err;
            p.style.margin = '0.2rem 0';
            container.appendChild(p);
        });

        container.scrollIntoView({ behavior: 'smooth', block: 'center' });
    },

    /**
     * Clears the content of the error container and hides it.
     *
     * @param {HTMLElement} container - The div element to clear and hide.
     */
    clearErrors: function(container) {
        if (!container) return;
        container.innerHTML = '';
        container.style.display = 'none';
    },

    /**
     * Initializes all password toggle buttons (eye icons).
     * Searches for elements with the class `.toggle-password`.
     * Replaces the element to remove old listeners (preventing duplicates) and adds click logic
     * to switch input type between 'password' and 'text'.
     */
    initPasswordToggles: function() {
        document.querySelectorAll('.toggle-password').forEach(icon => {
            // Remove previous listeners by cloning the node to avoid duplicates if called multiple times
            const newIcon = icon.cloneNode(true);
            icon.parentNode.replaceChild(newIcon, icon);

            newIcon.addEventListener('click', function() {
                const targetId = this.getAttribute('data-target');
                const targetInput = document.getElementById(targetId);
                if (targetInput) {
                    const type = targetInput.getAttribute('type') === 'password' ? 'text' : 'password';
                    targetInput.setAttribute('type', type);
                    this.classList.toggle('fa-eye');
                    this.classList.toggle('fa-eye-slash');
                }
            });
        });
    },

    /**
     * Initializes a phone input field to accept only numeric characters
     * and enforces a maximum length of 10 digits.
     *
     * @param {string|HTMLElement} inputOrId - The ID string or the DOM element of the input.
     */
    initPhoneInput: function(inputOrId) {
        const input = typeof inputOrId === 'string' ? document.getElementById(inputOrId) : inputOrId;
        if (!input) return;

        input.addEventListener('input', function() {
            this.value = this.value.replace(/\D/g, '').substring(0, 10);
            FormUtils.validateField(this); // Updates visual state
        });
    },

    /**
     * Sets the button to a loading state (disabled with spinner) or restores it.
     *
     * @param {HTMLButtonElement} btn - The submit button element.
     * @param {boolean} isLoading - True to set loading state, false to reset.
     * @param {string} [loadingText='Attendi...'] - The text to display while loading.
     */
    setLoadingButton: function(btn, isLoading, loadingText = 'Attendi...') {
        if (!btn) return;
        if (isLoading) {
            btn.disabled = true;
            btn.dataset.originalText = btn.innerHTML; // Save original text
            btn.innerHTML = `<i class="fas fa-spinner fa-spin"></i> ${loadingText}`;
        } else {
            btn.disabled = false;
            if (btn.dataset.originalText) {
                btn.innerHTML = btn.dataset.originalText;
            }
        }
    },

    /**
     * Updates the visual state (green/red border) of a form field based on validity.
     *
     * @param {HTMLElement} input - The input element to validate.
     * @param {Function} [customCheckFn=null] - Optional function returning true/false for custom logic.
     * @returns {boolean} True if valid, false otherwise.
     */
    validateField: function(input, customCheckFn = null) {
        // If empty and not required, reset classes
        if (input.value.trim() === '' && !input.required) {
            input.classList.remove('valid-field', 'invalid-field');
            return true;
        }

        let isValid = input.checkValidity();

        // Check custom logic if provided (e.g., password match)
        if (customCheckFn && typeof customCheckFn === 'function') {
            if (!customCheckFn(input)) isValid = false;
        }

        if (isValid) {
            input.classList.remove('invalid-field');
            input.classList.add('valid-field');
        } else {
            input.classList.remove('valid-field');
            // Show red border only if the user has typed something
            if (input.value.length > 0) input.classList.add('invalid-field');
        }
        return isValid;
    },

    /**
     * Attaches 'input' and 'blur' event listeners to all form fields for real-time validation.
     *
     * @param {HTMLFormElement} form - The form element containing the inputs.
     * @param {Function} [fieldSpecificCheckFn=null] - Optional function for ID-specific checks.
     */
    bindLiveValidation: function(form, fieldSpecificCheckFn = null) {
        const inputs = form.querySelectorAll('input, select, textarea');
        inputs.forEach(input => {
            const handler = () => FormUtils.validateField(input, fieldSpecificCheckFn);
            input.addEventListener('blur', handler);
            input.addEventListener('input', handler);
        });
    }
};