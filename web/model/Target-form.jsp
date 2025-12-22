<%@ page import="model.TargetTabungan" %>
    <jsp:useBean id="target" class="model.TargetTabungan" scope="request" />
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <head>
            <title>${target.id == null ? "Tambah" : "Edit"} Target Tabungan</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
        </head>

        <body>

            <nav class="navbar">
                <a href="${pageContext.request.contextPath}/" class="brand">💰 MoneyMate</a>
                <div class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Dashboard</a>
                    <a href="../TargetTabunganServlet">Target Tabungan</a>
                </div>
            </nav>

            <div class="container" style="max-width: 600px;">
                <div class="card">
                    <h2 style="margin-top: 0; margin-bottom: 1.5rem;">${target.id == null ? "Tambah" : "Edit"} Target
                        Tabungan</h2>
                    <form action="${pageContext.request.contextPath}/TargetTabunganServlet" method="post">
                        <!-- ID hidden for update -->
                        <c:if test="${not empty target.id}">
                            <input type="hidden" name="id" value="${target.id}">
                        </c:if>
                        <div class="form-group">
                            <label>User</label>
                            <select name="userId" required>
                                <option value="">-- Pilih User --</option>
                                <c:forEach var="u" items="${users}">
                                    <option value="${u.id}" ${target.userId==u.id ? 'selected' : '' }>${u.nama}
                                        (${u.email})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Nama Target</label>
                            <input type="text" name="nama" value="${target.nama}" required
                                placeholder="Misal: Beli Laptop">
                        </div>
                        <div class="form-group">
                            <label>Jumlah Target (Rp)</label>
                            <input type="number" step="0.01" name="jumlahTarget" value="${target.jumlahTarget}"
                                required>
                        </div>

                        <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                            <input type="submit" value="Simpan" class="btn btn-primary" style="flex: 1;">
                            <a href="../TargetTabunganServlet" class="btn btn-danger"
                                style="background-color: #e2e8f0; color: #333; flex: 1;">Batal</a>
                        </div>
                    </form>
                </div>
            </div>
        </body>

        </html>