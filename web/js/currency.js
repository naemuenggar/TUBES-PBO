function formatCurrency(input) {
    // Get cursor position to restore later (simple version: just let it jump for now, or improve if needed)
    let value = input.value;

    // Remove non-numeric chars except dot
    value = value.replace(/[^0-9.]/g, '');

    // Split integer and decimal parts
    let parts = value.split('.');
    let integerPart = parts[0];
    let decimalPart = parts.length > 1 ? '.' + parts[1].substring(0, 2) : '';

    // Add commas to integer part
    integerPart = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ",");

    // If user deleted everything, leave empty
    if (value === "") {
        input.value = "";
    } else {
        input.value = integerPart + decimalPart;
    }
}

document.addEventListener("DOMContentLoaded", function () {
    let currencyInputs = document.querySelectorAll('input[data-type="currency"]');

    currencyInputs.forEach(function (input) {
        // Initial format if value exists
        if (input.value) {
            formatCurrency(input);
        }

        // Add listener
        input.addEventListener('input', function () {
            formatCurrency(this);
        });
    });

    // Handle form submission: Strip commas from currency inputs
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function () {
            let currencyInputs = this.querySelectorAll('input[data-type="currency"]');
            currencyInputs.forEach(input => {
                input.value = input.value.replace(/,/g, '');
            });
        });
    });
});
