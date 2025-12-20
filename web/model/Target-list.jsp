<%@ page import="java.util.*, model.TargetTabungan" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <jsp:useBean id="targets" scope="request" type="java.util.List" />
        <html>

        <head>
            <title>Daftar Target Tabungan</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
        </head>

        <body>

            <nav class="navbar">
                <a href="${pageContext.request.contextPath}/" class="brand">💰 MoneyMate</a>
                <div class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Dashboard</a>
                    <a href="TargetTabunganServlet">Target Tabungan</a>
                </div>
            </nav>

            <div class="container">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                    <h2>Daftar Target Tabungan</h2>
                    <a href="TargetTabunganServlet?action=form" class="btn btn-primary">+ Tambah Target</a>
                </div>

                <div class="card">
                    <div class="table-responsive">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>User ID</th>
                                    <th>Nama Target</th>
                                    <th>Jumlah Target</th>
                                    <th>Aksi</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="t" items="${targets}">
                                    <tr>
                                        <td>${t.id}</td>
                                        <td>${t.userId}</td>
                                        <td><strong>${t.nama}</strong></td>
                                        <td>Rp ${t.jumlahTarget}</td>
                                        <td>
                                            <a href="TargetTabunganServlet?action=edit&id=${t.id}"
                                                class="btn btn-sm btn-primary"
                                                style="background-color: var(--primary);">Edit</a>
                                            <a href="TargetTabunganServlet?action=delete&id=${t.id}"
                                                class="btn btn-sm btn-danger"
                                                onclick="return confirm('Yakin hapus?');">Hapus</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty targets}">
                                    <tr>
                                        <td colspan="5"
                                            style="text-align: center; padding: 2rem; color: var(--text-muted);">
                                            Belum ada target tabungan.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </body>

        </html>