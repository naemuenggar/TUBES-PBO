<%@ page import="java.util.*, model.FinGoal" %>
    <jsp:useBean id="fingoals" scope="request" type="java.util.List" />
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <head>
                <title>Daftar FinGoal</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap"
                    rel="stylesheet">
            </head>

            <body>

                <nav class="navbar">
                    <a href="${pageContext.request.contextPath}/" class="brand">💰 MoneyMate</a>
                    <div class="nav-links">
                        <a href="${pageContext.request.contextPath}/">Dashboard</a>
                        <a href="FinGoalServlet">Financial Goals</a>
                    </div>
                </nav>

                <div class="container">
                    <div
                        style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                        <h2>Financial Goals Progress</h2>
                        <a href="model/FinGoal-form.jsp" class="btn btn-primary">+ Update Progress</a>
                    </div>

                    <div class="card">
                        <div class="table-responsive">
                            <table>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Target ID</th>
                                        <th>Progress Saat Ini</th>
                                        <th>Status</th>
                                        <th>Aksi</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="f" items="${fingoals}">
                                        <tr>
                                            <td>${f.id}</td>
                                            <td>${f.targetId}</td>
                                            <td>Rp
                                                <fmt:formatNumber value="${f.progress}" pattern="#,###" />
                                            </td>
                                            <td>
                                                <span
                                                    class="tag ${f.status == 'Tercapai' ? 'tag-pemasukan' : 'tag-pengeluaran'}">
                                                    ${f.status}
                                                </span>
                                            </td>
                                            <td>
                                                <a href="FinGoalServlet?action=edit&id=${f.id}"
                                                    class="btn btn-sm btn-primary"
                                                    style="background-color: var(--primary);">Edit</a>
                                                <a href="FinGoalServlet?action=delete&id=${f.id}"
                                                    class="btn btn-sm btn-danger"
                                                    onclick="return confirm('Yakin hapus?');">Hapus</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty fingoals}">
                                        <tr>
                                            <td colspan="5"
                                                style="text-align: center; padding: 2rem; color: var(--text-muted);">
                                                Belum
                                                ada
                                                data FinGoal.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </body>

            </html>