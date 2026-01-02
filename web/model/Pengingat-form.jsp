<%@ page import="model.Pengingat" %>
    <jsp:useBean id="pengingat" class="model.Pengingat" scope="request" />
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <head>
            <meta charset="UTF-8">
            <title>${pengingat.id == null ? "Tambah" : "Edit"} Pengingat</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
        </head>

        <body>

            <nav class="navbar">
                <a href="${pageContext.request.contextPath}/" class="brand">&#128176; MoneyMate</a>
                <div class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Dashboard</a>
                    <a href="../PengingatServlet">Pengingat</a>
                </div>
            </nav>

            <div class="container" style="max-width: 600px;">
                <div class="card">
                    <h2 style="margin-top: 0; margin-bottom: 1.5rem;">${pengingat.id == null ? "Tambah" : "Edit"}
                        Pengingat</h2>
                    <form action="${pageContext.request.contextPath}/PengingatServlet" method="post">
                        <input type="hidden" name="id" value="${pengingat.id}">
                        <div class="form-group">
                            <label>User</label>
                            <c:choose>
                                <c:when test="${sessionScope.user.role == 'admin'}">
                                    <select name="userId" required>
                                        <option value="">-- Pilih User --</option>
                                        <c:forEach var="u" items="${users}">
                                            <option value="${u.id}" ${pengingat.userId==u.id ? 'selected' : '' }>
                                                ${u.nama} (${u.email})</option>
                                        </c:forEach>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <input type="text" value="${sessionScope.user.nama}" readonly
                                        style="background-color: #f1f5f9; cursor: not-allowed;">
                                    <input type="hidden" name="userId" value="${sessionScope.user.id}">
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="form-group">
                            <label>Pesan</label>
                            <input type="text" name="pesan" value="${pengingat.pesan}" required
                                placeholder="Misal: Bayar Listrik">
                        </div>
                        <div class="form-group">
                            <label>Tanggal</label>
                            <input type="date" name="tanggal" value="${pengingat.tanggal}" required>
                        </div>

                        <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                            <input type="submit" value="Simpan" class="btn btn-primary" style="flex: 1;">
                            <a href="${pageContext.request.contextPath}/PengingatServlet" class="btn btn-danger"
                                style="background-color: #6c757d; color: white; flex: 1; text-align: center; text-decoration: none;">Kembali</a>
                        </div>
                    </form>
                </div>
            </div>
        </body>

        </html>