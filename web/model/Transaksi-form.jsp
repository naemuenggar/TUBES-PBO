<%@ page import="model.Transaksi" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <jsp:useBean id="transaksi" class="model.Transaksi" scope="request" />

            <head>
                <meta charset="UTF-8">
                <title>${transaksi.id == null ? "Tambah" : "Edit"} Transaksi</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap"
                    rel="stylesheet">
            </head>

            <body>

                <nav class="navbar">
                    <a href="${pageContext.request.contextPath}/" class="brand">&#128176; MoneyMate</a>
                    <div class="nav-links">
                        <a href="${pageContext.request.contextPath}/">Dashboard</a>
                        <a href="../TransaksiServlet">Transaksi</a>
                    </div>
                </nav>

                <div class="container" style="max-width: 600px;">
                    <div class="card">
                        <h2 style="margin-top: 0; margin-bottom: 1.5rem;">${transaksi.id == null ? "Tambah" : "Edit"}
                            Transaksi
                        </h2>

                        <form action="${pageContext.request.contextPath}/TransaksiServlet" method="post">
                            <!-- Hidden field for Pay Bill logic -->
                            <c:if test="${not empty sourceTagihanId}">
                                <input type="hidden" name="sourceTagihanId" value="${sourceTagihanId}">
                            </c:if>

                            <!-- ID hidden for update -->
                            <c:if test="${not empty transaksi.id}">
                                <input type="hidden" name="id" value="${transaksi.id}">
                            </c:if>

                            <div class="form-group">
                                <label>User</label>
                                <select name="userId" required>
                                    <option value="">-- Pilih User --</option>
                                    <c:forEach var="u" items="${users}">
                                        <option value="${u.id}" ${transaksi.userId==u.id ? 'selected' : '' }>${u.nama}
                                            (${u.email})</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Jumlah (Rp)</label>
                                <input type="text" data-type="currency" name="jumlah" value="<c:if test="
                                    ${transaksi.jumlah> 0}">
                                <fmt:formatNumber value="${transaksi.jumlah}" pattern="#,##0" />
                                </c:if>"
                                required>
                            </div>

                            <div class="form-group">
                                <label>Deskripsi</label>
                                <input type="text" name="deskripsi" value="${transaksi.deskripsi}"
                                    placeholder="Keterangan transaksi">
                            </div>

                            <div class="form-group">
                                <label>Tanggal</label>
                                <input type="date" name="tanggal" value="${transaksi.tanggal}" required>
                            </div>

                            <div class="form-group">
                                <label>Kategori</label>
                                <select name="kategoriId" required>
                                    <option value="">-- Pilih Kategori --</option>
                                    <c:forEach var="k" items="${kategoris}">
                                        <option value="${k.id}" ${transaksi.kategoriId==k.id ? 'selected' : '' }>
                                            ${k.nama}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Jenis Transaksi</label>
                                <select name="jenis">
                                    <option value="pemasukan" ${transaksi.jenis=="pemasukan" ? "selected" : "" }>
                                        Pemasukan
                                    </option>
                                    <option value="pengeluaran" ${transaksi.jenis=="pengeluaran" ? "selected" : "" }>
                                        Pengeluaran
                                    </option>
                                </select>
                            </div>

                            <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                                <input type="submit" value="Simpan" class="btn btn-primary" style="flex: 1;">
                                <a href="${pageContext.request.contextPath}/TransaksiServlet" class="btn btn-danger"
                                    style="background-color: #6c757d; color: white; flex: 1; text-align: center; text-decoration: none;">Kembali</a>
                            </div>
                        </form>
                    </div>
                </div>

                <script src="${pageContext.request.contextPath}/js/currency.js"></script>
            </body>

            </html>