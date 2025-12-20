<%@ page import="java.util.*, model.Pengingat" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <jsp:useBean id="pengingatList" scope="request" type="java.util.List" />
        <html>

        <head>
            <title>Daftar Pengingat</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
        </head>

        <body>

            <nav class="navbar">
                <a href="${pageContext.request.contextPath}/" class="brand">💰 MoneyMate</a>
                <div class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Dashboard</a>
                    <a href="PengingatServlet">Pengingat</a>
                </div>
            </nav>

            <div class="container">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                    <h2>Daftar Pengingat</h2>
                    <a href="PengingatServlet?action=form" class="btn btn-primary">+ Tambah Pengingat</a>
                </div>

                <div class="card">
                    <div class="table-responsive">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>User</th>
                                    <th>Pesan</th>
                                    <th>Tanggal</th>
                                    <th>Aksi</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="p" items="${pengingatList}">
                                    <tr>
                                        <td>${p.id}</td>
                                        <td>${p.userId}</td>
                                        <td><strong>${p.pesan}</strong></td>
                                        <td>${p.tanggal}</td>
                                        <td>
                                            <a href="PengingatServlet?action=edit&id=${p.id}"
                                                class="btn btn-sm btn-primary"
                                                style="background-color: var(--primary);">Edit</a>
                                            <a href="PengingatServlet?action=delete&id=${p.id}"
                                                class="btn btn-sm btn-danger"
                                                onclick="return confirm('Yakin hapus?');">Hapus</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty pengingatList}">
                                    <tr>
                                        <td colspan="5"
                                            style="text-align: center; padding: 2rem; color: var(--text-muted);">
                                            Belum ada pengingat.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </body>

        </html>