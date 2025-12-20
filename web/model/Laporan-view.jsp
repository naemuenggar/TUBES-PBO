<%@ page import="model.Laporan" %>
    <jsp:useBean id="laporan" scope="request" type="model.Laporan" />

    <head>
        <title>Laporan Keuangan</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
    </head>

    <body>

        <nav class="navbar">
            <a href="${pageContext.request.contextPath}/" class="brand">💰 MoneyMate</a>
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
                    <p style="font-size: 2rem; color: #2ecc71; font-weight: bold; margin: 1rem 0;">Rp
                        ${laporan.totalPemasukan}</p>
                </div>

                <div class="card" style="text-align: center;">
                    <h3>Total Pengeluaran</h3>
                    <p style="font-size: 2rem; color: #e74c3c; font-weight: bold; margin: 1rem 0;">Rp
                        ${laporan.totalPengeluaran}</p>
                </div>

                <div class="card" style="text-align: center; border: 2px solid var(--accent);">
                    <h3>Sisa Saldo</h3>
                    <p style="font-size: 2rem; color: var(--accent); font-weight: bold; margin: 1rem 0;">Rp
                        ${laporan.saldo}</p>
                </div>
            </div>
        </div>
    </body>

    </html>