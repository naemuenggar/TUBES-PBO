<%@ page import="model.Anggaran" %>
    <jsp:useBean id="anggaran" class="model.Anggaran" scope="request" />
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

            <head>
                <meta charset="UTF-8">
                <title>${anggaran.id == null ? "Tambah" : "Edit"} Anggaran</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap"
                    rel="stylesheet">
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
                        <form action="${pageContext.request.contextPath}/AnggaranServlet" method="post">
                            <!-- ID hidden or auto-generated -->
                            <c:if test="${not empty anggaran.id}">
                                <input type="hidden" name="id" value="${anggaran.id}">
                            </c:if>

                            <div class="form-group">
                                <label>User</label>
                                <c:choose>
                                    <c:when test="${sessionScope.user.role == 'admin'}">
                                        <select name="userId" required>
                                            <option value="">-- Pilih User --</option>
                                            <c:forEach var="u" items="${users}">
                                                <option value="${u.id}" ${anggaran.userId==u.id ? 'selected' : '' }>
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
                                <label>Nama Anggaran</label>
                                <input type="text" name="nama" value="${anggaran.nama}" required
                                    placeholder="Misal: Belanja Bulanan">
                            </div>
                            <div class="form-group">
                                <label>Jumlah (Rp)</label>
                                <c:set var="formattedJumlah" value="" />
                                <c:if test="${anggaran.jumlah > 0}">
                                    <c:set var="formattedJumlah">
                                        <fmt:formatNumber value="${anggaran.jumlah}" pattern="#,##0" />
                                    </c:set>
                                </c:if>
                                <input type="text" data-type="currency" name="jumlah" value="${formattedJumlah}"
                                    placeholder="Contoh: 10,000,000" required>
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