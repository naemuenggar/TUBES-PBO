<%@ page import="model.Tagihan" %>
    <jsp:useBean id="tagihan" class="model.Tagihan" scope="request" />
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <head>
                <meta charset="UTF-8">
                <title>${tagihan.id == null ? "Tambah" : "Edit"} Tagihan</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap"
                    rel="stylesheet">
            </head>

            <body>

                <nav class="navbar">
                    <a href="${pageContext.request.contextPath}/" class="brand">&#128176; MoneyMate</a>
                    <div class="nav-links">
                        <a href="${pageContext.request.contextPath}/">Dashboard</a>
                        <a href="../TagihanServlet">Tagihan</a>
                    </div>
                </nav>

                <div class="container" style="max-width: 600px;">
                    <div class="card">
                        <h2 style="margin-top: 0; margin-bottom: 1.5rem;">${tagihan.id == null ? "Tambah" : "Edit"}
                            Tagihan
                        </h2>
                        <form action="${pageContext.request.contextPath}/TagihanServlet" method="post">
                            <c:if test="${not empty tagihan.id}">
                                <input type="hidden" name="id" value="${tagihan.id}">
                            </c:if>
                            <div class="form-group">
                                <label>User</label>
                                <select name="userId" required>
                                    <option value="">-- Pilih User --</option>
                                    <c:forEach var="u" items="${users}">
                                        <option value="${u.id}" ${tagihan.userId==u.id ? 'selected' : '' }>${u.nama}
                                            (${u.email})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Nama Tagihan</label>
                                <input type="text" name="nama" value="${tagihan.nama}" required
                                    placeholder="Misal: Listrik PLN">
                            </div>
                            <div class="form-group">
                                <label>Jumlah Tagihan (Rp)</label>
                                <input type="text" data-type="currency" name="jumlah" value="<c:if test="
                                    ${tagihan.jumlah> 0}">
                                <fmt:formatNumber value="${tagihan.jumlah}" pattern="#,##0" />
                                </c:if>"
                                required>
                            </div>

                            <div class="form-group">
                                <label>Tangal Jatuh Tempo</label>
                                <input type="date" name="tanggalJatuhTempo" value="${tagihan.tanggalJatuhTempo}"
                                    required>
                            </div>

                            <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                                <input type="submit" value="Simpan" class="btn btn-primary" style="flex: 1;">
                                <a href="${pageContext.request.contextPath}/TagihanServlet" class="btn btn-danger"
                                    style="background-color: #6c757d; color: white; flex: 1; text-align: center; text-decoration: none;">Kembali</a>
                            </div>
                        </form>
                    </div>
                </div>
                <script src="${pageContext.request.contextPath}/js/currency.js"></script>
            </body>

            </html>