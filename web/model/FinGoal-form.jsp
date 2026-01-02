<%@ page import="model.FinGoal" %>
    <jsp:useBean id="goal" class="model.FinGoal" scope="request" />
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

        <head>
            <meta charset="UTF-8">
            <title>${goal.id == null ? "Tambah" : "Edit"} FinGoal</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
        </head>

        <body>

            <nav class="navbar">
                <a href="${pageContext.request.contextPath}/" class="brand">&#128176; MoneyMate</a>
                <div class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Dashboard</a>
                    <a href="../FinGoalServlet">Financial Goals</a>
                </div>
            </nav>

            <div class="container" style="max-width: 600px;">
                <div class="card">
                    <h2 style="margin-top: 0; margin-bottom: 1.5rem;">${goal.id == null ? "Tambah" : "Edit"} Progress
                    </h2>
                    <form action="${pageContext.request.contextPath}/FinGoalServlet" method="post">
                        <input type="hidden" name="id" value="${goal.id}">
                        <div class="form-group">
                            <label>Target Tabungan</label>
                            <select name="targetId" required>
                                <option value="">-- Pilih Target --</option>
                                <c:forEach var="t" items="${targets}">
                                    <option value="${t.id}" ${goal.targetId==t.id ? 'selected' : '' }>${t.nama} (Target:
                                        <fmt:formatNumber value="${t.jumlahTarget}" pattern="#,###" />)
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Jumlah Progress (Rp)</label>
                            <c:set var="formattedProgress" value="" />
                            <c:if test="${goal.progress > 0}">
                                <c:set var="formattedProgress">
                                    <fmt:formatNumber value="${goal.progress}" pattern="#,##0" />
                                </c:set>
                            </c:if>
                            <input type="text" data-type="currency" name="progress" value="${formattedProgress}"
                                required>
                        </div>
                        <div class="form-group">
                            <label>Status</label>
                            <select name="status">
                                <option value="Belum Tercapai" ${goal.status=="Belum Tercapai" ? "selected" : "" }>Belum
                                    Tercapai</option>
                                <option value="Tercapai" ${goal.status=="Tercapai" ? "selected" : "" }>Tercapai</option>
                            </select>
                        </div>

                        <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                            <input type="submit" value="Simpan" class="btn btn-primary" style="flex: 1;">
                            <a href="${pageContext.request.contextPath}/FinGoalServlet" class="btn btn-danger"
                                style="background-color: #6c757d; color: white; flex: 1; text-align: center; text-decoration: none;">Kembali</a>
                        </div>
                    </form>
                </div>
            </div>
            </div>
            <script src="${pageContext.request.contextPath}/js/currency.js"></script>
        </body>

        </html>