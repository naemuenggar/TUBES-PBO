<%@ page import="java.util.*, model.Tagihan" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <jsp:useBean id="tagihanList" scope="request" type="java.util.List" />
            <html>

            <head>
                <title>Daftar Tagihan</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap"
                    rel="stylesheet">
            </head>

            <body>

                <nav class="navbar">
                    <a href="${pageContext.request.contextPath}/" class="brand">💰 MoneyMate</a>
                    <div class="nav-links">
                        <a href="${pageContext.request.contextPath}/">Dashboard</a>
                        <a href="TagihanServlet">Tagihan</a>
                    </div>
                </nav>

                <div class="container">
                    <div
                        style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                        <h2>Daftar Tagihan</h2>
                        <a href="TagihanServlet?action=form" class="btn btn-primary">+ Tambah Tagihan</a>
                    </div>

                    <div class="card">
                        <div class="table-responsive">
                            <table>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>User ID</th>
                                        <th>Nama Tagihan</th>
                                        <th>Jumlah</th>
                                        <th>Jatuh Tempo</th>
                                        <th>Aksi</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="t" items="${tagihanList}">
                                        <tr>
                                            <td>${t.id}</td>
                                            <td>${t.userId}</td>
                                            <td><strong>${t.nama}</strong></td>
                                            <td>Rp
                                                <fmt:formatNumber value="${t.jumlah}" pattern="#,###" />
                                            </td>
                                            <td>${t.tanggalJatuhTempo}</td>
                                            <td>
                                                <a href="../TransaksiServlet?action=payBill&id=${t.id}&nama=${t.nama}&jumlah=${t.jumlah}&userId=${t.userId}"
                                                    class="btn btn-sm btn-success"
                                                    style="background-color: var(--success); margin-right: 5px;">Bayar</a>
                                                <a href="TagihanServlet?action=edit&id=${t.id}"
                                                    class="btn btn-sm btn-primary"
                                                    style="background-color: var(--primary);">Edit</a>
                                                <a href="TagihanServlet?action=delete&id=${t.id}"
                                                    class="btn btn-sm btn-danger"
                                                    onclick="return confirm('Yakin hapus?');">Hapus</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty tagihanList}">
                                        <tr>
                                            <td colspan="6"
                                                style="text-align: center; padding: 2rem; color: var(--text-muted);">
                                                Belum ada tagihan.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </body>

            </html>