<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="model.Tagihan" %>
        <%@ page import="model.Kategori" %>
            <%@ page import="java.util.List" %>
                <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                        <jsp:useBean id="tagihan" class="model.Tagihan" scope="request" />

                        <!DOCTYPE html>
                        <html>

                        <head>
                            <meta charset="UTF-8">
                            <title>Bayar Tagihan</title>
                            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap"
                                rel="stylesheet">
                        </head>

                        <body>

                            <nav class="navbar">
                                <a href="${pageContext.request.contextPath}/" class="brand">&#128176; MoneyMate</a>
                                <div class="nav-links">
                                    <a href="${pageContext.request.contextPath}/">Dashboard</a>
                                    <a href="TagihanServlet">Tagihan</a>
                                </div>
                            </nav>

                            <div class="container" style="max-width: 600px;">
                                <div class="card">
                                    <h2 style="margin-top: 0; margin-bottom: 1.5rem;">Pembayaran Tagihan</h2>

                                    <div
                                        style="background: #f8fafc; padding: 1rem; border-radius: 8px; margin-bottom: 1.5rem; border-left: 4px solid var(--primary);">
                                        <div style="font-size: 0.9rem; color: #666;">Tagihan yang akan dibayar:</div>
                                        <div style="font-size: 1.25rem; font-weight: bold; margin: 0.25rem 0;">
                                            ${tagihan.nama}</div>
                                        <div style="font-size: 1.1rem; color: var(--danger); font-weight: 600;">
                                            Rp
                                            <fmt:formatNumber value="${tagihan.jumlah}" pattern="#,###" />
                                        </div>
                                    </div>

                                    <form action="${pageContext.request.contextPath}/TagihanServlet" method="post">
                                        <input type="hidden" name="action" value="processPayBill">
                                        <input type="hidden" name="tagihanId" value="${tagihan.id}">
                                        <input type="hidden" name="userId" value="${tagihan.userId}">
                                        <input type="hidden" name="nama" value="${tagihan.nama}">

                                        <input type="hidden" name="jumlah" value="${tagihan.jumlah}">
                                        <div class="form-group">
                                            <label>Jumlah Pembayaran (Rp)</label>
                                            <div
                                                style="padding: 10px; background-color: #f1f5f9; border-radius: 8px; font-weight: 500; color: #4b5563;">
                                                <fmt:formatNumber value="${tagihan.jumlah}" pattern="#,##0" />
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label>Kategori Transaksi</label>
                                            <select name="kategoriId" required>
                                                <option value="">-- Pilih Kategori (Misal: Tagihan) --</option>
                                                <c:forEach var="k" items="${kategoris}">
                                                    <option value="${k.id}">${k.nama}</option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <div class="form-group">
                                            <label>Tanggal Pembayaran</label>
                                            <input type="date" name="tanggal"
                                                value="<%= new java.sql.Date(System.currentTimeMillis()) %>" required>
                                        </div>

                                        <div class="form-group">
                                            <label>Catatan</label>
                                            <input type="text" name="deskripsi" value="Bayar Tagihan: ${tagihan.nama}">
                                        </div>

                                        <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                                            <input type="submit" value="Konfirmasi Pembayaran" class="btn btn-success"
                                                style="flex: 1; background-color: var(--success);">
                                            <a href="${pageContext.request.contextPath}/TagihanServlet"
                                                class="btn btn-danger"
                                                style="background-color: #6c757d; color: white; flex: 1; text-align: center; text-decoration: none;">Batal</a>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            </div>
                            </div>
                            <script src="${pageContext.request.contextPath}/js/currency.js"></script>
                        </body>

                        </html>