<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="model.TargetTabungan" %>
        <%@ page import="model.User" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                    <jsp:useBean id="target" class="model.TargetTabungan" scope="request" />

                    <!DOCTYPE html>
                    <html>

                    <head>
                        <meta charset="UTF-8">
                        <title>Menabung untuk ${target.nama}</title>
                        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap"
                            rel="stylesheet">
                    </head>

                    <body>

                        <nav class="navbar">
                            <a href="${pageContext.request.contextPath}/" class="brand">&#128176; MoneyMate</a>
                            <div class="nav-links">
                                <a href="${pageContext.request.contextPath}/">Dashboard</a>
                                <a href="TargetTabunganServlet">Target Tabungan</a>
                            </div>
                        </nav>

                        <div class="container" style="max-width: 600px;">
                            <div class="card">
                                <h2 style="margin-top: 0; margin-bottom: 1.5rem;">Menabung: ${target.nama}</h2>

                                <form action="${pageContext.request.contextPath}/TargetTabunganServlet" method="post">
                                    <input type="hidden" name="action" value="processTabung">
                                    <input type="hidden" name="targetId" value="${target.id}">
                                    <input type="hidden" name="userId" value="${target.userId}">

                                    <div class="form-group">
                                        <label>Jumlah Setoran (Rp)</label>
                                        <input type="text" data-type="currency" name="jumlahSetor" required
                                            placeholder="Masukkan nominal yang ingin ditabung">
                                    </div>

                                    <div class="form-group">
                                        <label>Catatan (Opsional)</label>
                                        <input type="text" name="catatan" placeholder="Contoh: Sisa uang jajan">
                                    </div>

                                    <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                                        <input type="submit" value="Tabung Sekarang" class="btn btn-primary"
                                            style="flex: 1;">
                                        <a href="${pageContext.request.contextPath}/TargetTabunganServlet"
                                            class="btn btn-danger"
                                            style="background-color: #6c757d; color: white; flex: 1; text-align: center; text-decoration: none;">Kembali</a>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <script src="${pageContext.request.contextPath}/js/currency.js"></script>
                    </body>

                    </html>