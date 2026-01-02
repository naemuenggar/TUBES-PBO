<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <fmt:setLocale value="id_ID" />
        <jsp:useBean id="laporan" scope="request" type="model.Laporan" />

        <head>
            <meta charset="UTF-8">
            <title>Laporan Keuangan</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
        </head>

        <body>

            <nav class="navbar">
                <a href="${pageContext.request.contextPath}/" class="brand">&#128176; MoneyMate</a>
                <div class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Dashboard</a>
                    <a href="LaporanServlet">Laporan</a>
                </div>
            </nav>

            <div class="container" style="max-width: 800px;">
                <h2>Laporan Keuangan</h2>

                <div class="card-grid"
                    style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1.5rem; margin-top: 2rem;">
                    <div class="card" style="text-align: center;">
                        <h3>Total Pemasukan</h3>
                        <p class="report-value" style="color: #2ecc71;">
                            <fmt:formatNumber value="${laporan.totalPemasukan}" type="currency" currencySymbol="Rp" />
                        </p>
                    </div>

                    <div class="card" style="text-align: center;">
                        <h3>Total Pengeluaran</h3>
                        <p class="report-value" style="color: #e74c3c;">
                            <fmt:formatNumber value="${laporan.totalPengeluaran}" type="currency" currencySymbol="Rp" />
                        </p>
                    </div>

                    <div class="card" style="text-align: center; border: 2px solid var(--accent);">
                        <h3>Sisa Saldo</h3>
                        <p class="report-value" style="color: var(--accent);">
                            <fmt:formatNumber value="${laporan.saldo}" type="currency" currencySymbol="Rp" />
                        </p>
                    </div>
                </div>
            </div>
            <div style="text-align: center; margin-bottom: 2rem;">
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary"
                    style="background-color: #6c757d; color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; display: inline-block;">Kembali
                    ke Dashboard</a>
            </div>
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    const values = document.querySelectorAll('.report-value');
                    values.forEach(el => {
                        let currentSize = 2.0; // Start at 2rem
                        // While content is wider than container and font is readable (> 0.8rem)
                        while (el.scrollWidth > el.clientWidth && currentSize > 0.8) {
                            currentSize -= 0.1;
                            el.style.fontSize = currentSize + 'rem';
                        }
                    });
                });
            </script>
        </body>

        </html>