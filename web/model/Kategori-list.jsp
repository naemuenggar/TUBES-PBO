<%@ page import="java.util.*, model.Kategori" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <jsp:useBean id="kategoris" scope="request" type="java.util.List" />

        <head>
            <title>Daftar Kategori</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
        </head>

        <body>

            <nav class="navbar">
                <a href="${pageContext.request.contextPath}/" class="brand">💰 MoneyMate</a>
                <div class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Dashboard</a>
                    <a href="KategoriServlet">Kategori</a>
                </div>
            </nav>

            <div class="container">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                    <h2>Daftar Kategori</h2>
                    <a href="KategoriServlet?action=form" class="btn btn-primary">+ Tambah Kategori</a>
                </div>

                <div class="card">
                    <div class="table-responsive">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Nama Kategori</th>
                                    <th>Aksi</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="k" items="${kategoris}">
                                    <tr>
                                        <td>${k.id}</td>
                                        <td><strong>${k.nama}</strong></td>
                                        <td>
                                            <a href="KategoriServlet?action=edit&id=${k.id}"
                                                class="btn btn-sm btn-primary"
                                                style="background-color: var(--primary);">Edit</a>
                                            <a href="KategoriServlet?action=delete&id=${k.id}"
                                                class="btn btn-sm btn-danger"
                                                onclick="return confirm('Yakin hapus?');">Hapus</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty kategoris}">
                                    <tr>
                                        <td colspan="4"
                                            style="text-align: center; padding: 2rem; color: var(--text-muted);">
                                            Belum ada data kategori.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </body>

        </html>