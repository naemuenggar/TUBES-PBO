<%@ page import="java.util.*, model.TargetTabungan" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <jsp:useBean id="targets" scope="request" type="java.util.List" />
            <html>

            <head>
                <title>Daftar Target Tabungan</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap"
                    rel="stylesheet">
                <style>
                    .progress-container {
                        width: 100%;
                        background: rgba(226, 232, 240, 0.3);
                        border-radius: 50px;
                        height: 24px;
                        overflow: hidden;
                        position: relative;
                        backdrop-filter: blur(10px);
                        box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
                    }

                    .progress-bar {
                        height: 100%;
                        background: linear-gradient(135deg, var(--accent), var(--primary));
                        border-radius: 50px;
                        transition: width 0.6s ease;
                        display: flex;
                        align-items: center;
                        justify-content: flex-end;
                        padding-right: 8px;
                        position: relative;
                        overflow: hidden;
                    }

                    .progress-bar::before {
                        content: '';
                        position: absolute;
                        top: 0;
                        left: 0;
                        right: 0;
                        bottom: 0;
                        background: linear-gradient(90deg,
                                transparent,
                                rgba(255, 255, 255, 0.3),
                                transparent);
                        animation: shimmer 2s infinite;
                    }

                    @keyframes shimmer {
                        0% {
                            transform: translateX(-100%);
                        }

                        100% {
                            transform: translateX(100%);
                        }
                    }

                    .progress-text {
                        font-size: 0.75rem;
                        font-weight: 600;
                        color: white;
                        text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
                        position: relative;
                        z-index: 1;
                    }

                    .progress-info {
                        display: flex;
                        justify-content: space-between;
                        margin-top: 0.5rem;
                        font-size: 0.75rem;
                        color: var(--text-muted);
                    }

                    .progress-complete {
                        background: linear-gradient(135deg, #10b981, #059669);
                    }

                    .progress-high {
                        background: linear-gradient(135deg, var(--accent), var(--primary));
                    }

                    .progress-medium {
                        background: linear-gradient(135deg, #f59e0b, #d97706);
                    }

                    .progress-low {
                        background: linear-gradient(135deg, #ef4444, #dc2626);
                    }
                </style>
            </head>

            <body>

                <nav class="navbar">
                    <a href="${pageContext.request.contextPath}/" class="brand">💰 MoneyMate</a>
                    <div class="nav-links">
                        <a href="${pageContext.request.contextPath}/">Dashboard</a>
                        <a href="TargetTabunganServlet">Target Tabungan</a>
                    </div>
                </nav>

                <div class="container">
                    <div
                        style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                        <h2>Daftar Target Tabungan</h2>
                        <a href="TargetTabunganServlet?action=form" class="btn btn-primary">+ Tambah Target</a>
                    </div>

                    <div class="card">
                        <div class="table-responsive">
                            <table>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>User ID</th>
                                        <th>Nama Target</th>
                                        <th>Jumlah Target</th>
                                        <th style="width: 300px;">Progress</th>
                                        <th>Aksi</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="t" items="${targets}">
                                        <tr>
                                            <td>${t.id}</td>
                                            <td>${t.userId}</td>
                                            <td><strong>${t.nama}</strong></td>
                                            <td>Rp
                                                <fmt:formatNumber value="${t.jumlahTarget}" pattern="#,###" />
                                            </td>
                                            <td>
                                                <div class="progress-container">
                                                    <c:set var="percentage"
                                                        value="${t.progressPercentage > 100 ? 100 : t.progressPercentage}" />
                                                    <c:set var="progressClass" value="progress-low" />
                                                    <c:if test="${t.progressPercentage >= 100}">
                                                        <c:set var="progressClass" value="progress-complete" />
                                                    </c:if>
                                                    <c:if
                                                        test="${t.progressPercentage >= 70 && t.progressPercentage < 100}">
                                                        <c:set var="progressClass" value="progress-high" />
                                                    </c:if>
                                                    <c:if
                                                        test="${t.progressPercentage >= 30 && t.progressPercentage < 70}">
                                                        <c:set var="progressClass" value="progress-medium" />
                                                    </c:if>
                                                    <div class="progress-bar ${progressClass}"
                                                        style="width: ${percentage}%">
                                                        <span class="progress-text">
                                                            <fmt:formatNumber value="${t.progressPercentage}"
                                                                pattern="#.#" />%
                                                        </span>
                                                    </div>
                                                </div>
                                                <div class="progress-info">
                                                    <span>Rp
                                                        <fmt:formatNumber value="${t.currentProgress}"
                                                            pattern="#,###" />
                                                    </span>
                                                    <span>dari Rp
                                                        <fmt:formatNumber value="${t.jumlahTarget}" pattern="#,###" />
                                                    </span>
                                                </div>
                                            </td>
                                            <td>
                                                <a href="TargetTabunganServlet?action=edit&id=${t.id}"
                                                    class="btn btn-sm btn-primary"
                                                    style="background-color: var(--primary);">Edit</a>
                                                <a href="TargetTabunganServlet?action=delete&id=${t.id}"
                                                    class="btn btn-sm btn-danger"
                                                    onclick="return confirm('Yakin hapus?');">Hapus</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty targets}">
                                        <tr>
                                            <td colspan="7"
                                                style="text-align: center; padding: 2rem; color: var(--text-muted);">
                                                Belum ada target tabungan.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </body>

            </html>