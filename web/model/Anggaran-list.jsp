<%@ page import="java.util.*, model.Anggaran" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <jsp:useBean id="anggarans" scope="request" type="java.util.List" />

        <head>
            <title>Daftar Anggaran</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
        </head>

        <body>

            <nav class="navbar">
                <a href="${pageContext.request.contextPath}/" class="brand">💰 MoneyMate</a>
                <div class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Dashboard</a>
                    <a href="AnggaranServlet">Anggaran</a>
                </div>
            </nav>

            <div class="container">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                    <h2>Daftar Anggaran</h2>
                    <a href="model/Anggaran-form.jsp" class="btn btn-primary">+ Tambah Anggaran</a>
                </div>

                <div class="card">
                    <div class="table-responsive">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>User ID</th>
                                    <th>Nama Anggaran</th>
                                    <th>Jumlah</th>
                                    <th>Aksi</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="a" items="${anggarans}">
                                    <tr>
                                        <td>${a.id}</td>
                                        <td>${a.userId}</td>
                                        <td><strong>${a.nama}</strong></td>
                                        <td>Rp ${a.jumlah}</td>
                                        <td>
                                            <a href="AnggaranServlet?action=edit&id=${a.id}"
                                                class="btn btn-sm btn-primary"
                                                style="background-color: var(--primary);">Edit</a>
                                            <a href="AnggaranServlet?action=delete&id=${a.id}"
                                                class="btn btn-sm btn-danger"
                                                onclick="return confirm('Yakin hapus?');">Hapus</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty anggarans}">
                                    <tr>
                                        <td colspan="5"
                                            style="text-align: center; padding: 2rem; color: var(--text-muted);">
                                            Belum ada data anggaran.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </body>

        </html>