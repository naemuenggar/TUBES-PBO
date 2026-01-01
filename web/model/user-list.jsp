<%@ page import="java.util.*, model.User" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <jsp:useBean id="users" scope="request" type="java.util.List" />
        <html>

        <head>
            <title>Daftar User</title>
        </head>

        <head>
            <title>Daftar User</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
        </head>

        <body>

            <nav class="navbar">
                <a href="${pageContext.request.contextPath}/" class="brand">&#128176; MoneyMate</a>
                <div class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Dashboard</a>
                    <a href="UserServlet">Users</a>
                </div>
            </nav>

            <div class="container">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                    <h2>Daftar User</h2>
                    <a href="model/user-form.jsp" class="btn btn-primary">+ Tambah User</a>
                </div>

                <div class="card" style="padding: 0;">
                    <table border="0">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nama</th>
                                <th>Email</th>
                                <th>Aksi</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td>${u.id}</td>
                                    <td><strong>${u.nama}</strong></td>
                                    <td>${u.email}</td>
                                    <td>
                                        <a href="UserServlet?action=edit&id=${u.id}" class="btn btn-sm btn-primary"
                                            style="background-color: var(--accent);">Edit</a>
                                        <a href="UserServlet?action=delete&id=${u.id}" class="btn btn-sm btn-danger"
                                            onclick="return confirm('Yakin hapus?');">Hapus</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty users}">
                                <tr>
                                    <td colspan="4"
                                        style="text-align: center; padding: 2rem; color: var(--text-light);">
                                        Belum ada data user.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

            <div style="text-align: center; margin-bottom: 2rem;">
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary"
                    style="background-color: #6c757d; color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; display: inline-block;">Kembali
                    ke Dashboard</a>
            </div>
        </body>

        </html>