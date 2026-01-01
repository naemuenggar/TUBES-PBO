<%@ page import="model.Anggaran" %>
    <jsp:useBean id="anggaran" class="model.Anggaran" scope="request" />
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

        <head>
            <meta charset="UTF-8">
            <title>${anggaran.id == null ? "Tambah" : "Edit"} Anggaran</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
        </head>

        <body>

            <nav class="navbar">
                <a href="${pageContext.request.contextPath}/" class="brand">&#128176; MoneyMate</a>
                <div class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Dashboard</a>
                    <a href="../AnggaranServlet">Anggaran</a>
                </div>
            </nav>

            <div class="container" style="max-width: 600px;">
                <div class="card">
                    <h2 style="margin-top: 0; margin-bottom: 1.5rem;">${anggaran.id == null ? "Tambah" : "Edit"}
                        Anggaran
                    </h2>
                    <form action="../AnggaranServlet" method="post">
                        <div class="form-group">
                            <label>ID Anggaran</label>
                            <input type="text" name="id" value="${anggaran.id}" required placeholder="Contoh: A001">
                        </div>
                        <div class="form-group">
                            <label>User ID</label>
                            <input type="text" name="userId" value="${anggaran.userId}" required
                                placeholder="ID User Pemilik">
                        </div>
                        <div class="form-group">
                            <label>Nama Anggaran</label>
                            <input type="text" name="nama" value="${anggaran.nama}" required
                                placeholder="Misal: Belanja Bulanan">
                        </div>
                        <div class="form-group">
                            <label>Jumlah (Rp)</label>
                            <input type="text" data-type="currency" name="jumlah" value="<c:if test=" ${anggaran.jumlah>
                            0}">
                            <fmt:formatNumber value="${anggaran.jumlah}" pattern="#,##0" />
                            </c:if>"
                            required>
                        </div>

                        <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                            <input type="submit" value="Simpan" class="btn btn-primary" style="flex: 1;">
                            <a href="${pageContext.request.contextPath}/AnggaranServlet" class="btn btn-danger"
                                style="background-color: #6c757d; color: white; flex: 1; text-align: center; text-decoration: none;">Kembali</a>
                        </div>
                    </form>
                </div>
            </div>
            <script src="${pageContext.request.contextPath}/js/currency.js"></script>
        </body>

        </html>