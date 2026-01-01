<%@ page import="model.Kategori" %>
    <jsp:useBean id="kategori" class="model.Kategori" scope="request" />

    <head>
        <meta charset="UTF-8">
        <title>${kategori.id == null ? "Tambah" : "Edit"} Kategori</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
    </head>

    <body>

        <nav class="navbar">
            <a href="${pageContext.request.contextPath}/" class="brand">&#128176; MoneyMate</a>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/">Dashboard</a>
                <a href="../KategoriServlet">Kategori</a>
            </div>
        </nav>

        <div class="container" style="max-width: 600px;">
            <div class="card">
                <h2 style="margin-top: 0; margin-bottom: 1.5rem;">${kategori.id == null ? "Tambah" : "Edit"} Kategori
                </h2>
                <form action="${pageContext.request.contextPath}/KategoriServlet" method="post">
                    <!-- ID hidden for update, empty for create -->
                    <c:if test="${not empty kategori.id}">
                        <input type="hidden" name="id" value="${kategori.id}">
                    </c:if>
                    <div class="form-group">
                        <label>Nama Kategori</label>
                        <input type="text" name="nama" value="${kategori.nama}" required
                            placeholder="Misal: Makanan, Transport">
                    </div>

                    <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                        <input type="submit" value="Simpan" class="btn btn-primary" style="flex: 1;">
                        <a href="${pageContext.request.contextPath}/KategoriServlet" class="btn btn-danger"
                            style="background-color: #6c757d; color: white; flex: 1; text-align: center; text-decoration: none;">Kembali</a>
                    </div>
                </form>
            </div>
        </div>
    </body>

    </html>