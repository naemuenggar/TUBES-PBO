<%@ page import="java.util.*, model.Transaksi" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <jsp:useBean id="transaksis" scope="request" type="java.util.List" />

            <head>
                <title>Daftar Transaksi</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap"
                    rel="stylesheet">
            </head>

            <body>

                <nav class="navbar">
                    <a href="${pageContext.request.contextPath}/" class="brand">💰 MoneyMate</a>
                    <div class="nav-links">
                        <a href="${pageContext.request.contextPath}/">Dashboard</a>
                        <a href="TransaksiServlet">Transaksi</a>
                    </div>
                </nav>

                <div class="container">
                    <div
                        style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                        <h2>Daftar Transaksi</h2>
                        <a href="TransaksiServlet?action=form" class="btn btn-primary">+ Tambah Transaksi</a>
                    </div>

                    <div class="card">
                        <div class="table-responsive">
                            <table>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>User</th>
                                        <th>Jumlah</th>
                                        <th>Deskripsi</th>
                                        <th>Tanggal</th>
                                        <th>Kategori</th>
                                        <th>Jenis</th>
                                        <th>Aksi</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="t" items="${transaksis}">
                                        <tr>
                                            <td>${t.id}</td>
                                            <td>${t.userId}</td>
                                            <td>Rp
                                                <fmt:formatNumber value="${t.jumlah}" pattern="#,###" />
                                            </td>
                                            <td>${t.deskripsi}</td>
                                            <td>${t.tanggal}</td>
                                            <td>${t.kategoriId}</td>
                                            <td>
                                                <span
                                                    class="tag ${t.jenis == 'pemasukan' ? 'tag-pemasukan' : 'tag-pengeluaran'}">
                                                    ${t.jenis}
                                                </span>
                                            </td>
                                            <td>
                                                <a href="TransaksiServlet?action=edit&id=${t.id}"
                                                    class="btn btn-sm btn-primary"
                                                    style="background-color: var(--primary);">Edit</a>
                                                <a href="TransaksiServlet?action=delete&id=${t.id}"
                                                    class="btn btn-sm btn-danger"
                                                    onclick="return confirm('Yakin hapus?');">Hapus</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty transaksis}">
                                        <tr>
                                            <td colspan="8"
                                                style="text-align: center; padding: 2rem; color: var(--text-muted);">
                                                Belum ada data transaksi.
                                            </td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </body>

            </html>