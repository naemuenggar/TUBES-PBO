<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="model.User" %>
        <%@ page import="util.DashboardHelper" %>
            <%@ page import="model.Tagihan" %>
                <%@ page import="model.Pengingat" %>
                    <%@ page import="java.util.List" %>
                        <% User loggedInUser=(User) session.getAttribute("user"); String userRole=loggedInUser !=null ?
                            loggedInUser.getRole() : "user" ; boolean isAdmin="admin" .equals(userRole); List<Tagihan>
                            dueBills = null;
                            List<Pengingat> todayReminders = null;
                                boolean showModal = false;

                                if (loggedInUser != null) {
                                dueBills = DashboardHelper.getDueTagihan(loggedInUser.getId());
                                todayReminders = DashboardHelper.getTodayPengingat(loggedInUser.getId());
                                showModal = !dueBills.isEmpty() || !todayReminders.isEmpty();
                                }
                                %>
                                <!DOCTYPE html>
                                <html>

                                <head>
                                    <meta charset="UTF-8">
                                    <title>MoneyMate Dashboard</title>
                                    <link rel="stylesheet" href="css/style.css">
                                    <style>
                                        .user-info {
                                            display: flex;
                                            align-items: center;
                                            gap: 1rem;
                                        }

                                        .user-badge {
                                            display: flex;
                                            align-items: center;
                                            gap: 0.5rem;
                                            background: rgba(79, 70, 229, 0.1);
                                            padding: 0.5rem 1rem;
                                            border-radius: 50px;
                                            font-size: 0.9rem;
                                        }

                                        .user-name {
                                            font-weight: 600;
                                            color: var(--primary);
                                        }

                                        .user-role {
                                            font-size: 0.75rem;
                                            text-transform: uppercase;
                                            background: var(--primary);
                                            color: white;
                                            padding: 0.15rem 0.5rem;
                                            border-radius: 50px;
                                            font-weight: 700;
                                            letter-spacing: 0.05em;
                                        }

                                        .btn-logout {
                                            background: linear-gradient(135deg, var(--danger), #dc2626);
                                            color: white;
                                            padding: 0.5rem 1rem;
                                            border-radius: var(--radius-md);
                                            text-decoration: none;
                                            font-weight: 600;
                                            font-size: 0.9rem;
                                            transition: all 0.2s;
                                            box-shadow: var(--shadow-sm);
                                        }

                                        .btn-logout:hover {
                                            transform: translateY(-2px);
                                            box-shadow: var(--shadow-md);
                                            filter: brightness(110%);
                                        }

                                        .btn-profile {
                                            background: linear-gradient(135deg, var(--primary), var(--primary-hover));
                                            color: white;
                                            padding: 0.5rem 1rem;
                                            border-radius: var(--radius-md);
                                            text-decoration: none;
                                            font-weight: 600;
                                            font-size: 0.9rem;
                                            transition: all 0.2s;
                                            box-shadow: var(--shadow-sm);
                                        }

                                        .btn-profile:hover {
                                            transform: translateY(-2px);
                                            box-shadow: var(--shadow-md);
                                            filter: brightness(110%);
                                        }

                                        /* Modal Styles */
                                        .modal {
                                            display: none;
                                            position: fixed;
                                            z-index: 1000;
                                            left: 0;
                                            top: 0;
                                            width: 100%;
                                            height: 100%;
                                            overflow: auto;
                                            background-color: rgba(0, 0, 0, 0.5);
                                            backdrop-filter: blur(5px);
                                            animation: fadeIn 0.3s;
                                        }

                                        @keyframes fadeIn {
                                            from {
                                                opacity: 0;
                                            }

                                            to {
                                                opacity: 1;
                                            }
                                        }

                                        .modal-content {
                                            background-color: #fefefe;
                                            margin: 5% auto;
                                            padding: 0;
                                            border: 1px solid #888;
                                            width: 90%;
                                            max-width: 500px;
                                            border-radius: 12px;
                                            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
                                            animation: slideDown 0.4s ease;
                                        }

                                        @keyframes slideDown {
                                            from {
                                                top: -50px;
                                                opacity: 0;
                                            }

                                            to {
                                                top: 0;
                                                opacity: 1;
                                            }
                                        }

                                        .modal-header {
                                            background: linear-gradient(135deg, var(--primary), var(--primary-hover));
                                            color: white;
                                            padding: 1rem 1.5rem;
                                            border-radius: 12px 12px 0 0;
                                            display: flex;
                                            justify-content: space-between;
                                            align-items: center;
                                        }

                                        .modal-body {
                                            padding: 1.5rem;
                                            max-height: 60vh;
                                            overflow-y: auto;
                                        }

                                        .close {
                                            color: white;
                                            font-size: 28px;
                                            font-weight: bold;
                                            cursor: pointer;
                                            transition: 0.2s;
                                        }

                                        .close:hover {
                                            color: #e2e8f0;
                                            transform: scale(1.1);
                                        }

                                        .reminder-item {
                                            background: #f8fafc;
                                            border-left: 4px solid var(--accent);
                                            padding: 12px;
                                            margin-bottom: 12px;
                                            border-radius: 4px;
                                            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
                                        }

                                        .bill-item {
                                            background: #fff1f2;
                                            border-left: 4px solid var(--danger);
                                            padding: 12px;
                                            margin-bottom: 12px;
                                            border-radius: 4px;
                                            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
                                        }
                                    </style>
                                </head>

                                <body>
                                    <nav class="navbar">
                                        <a href="index.jsp" class="brand">💰 MoneyMate</a>
                                        <div class="user-info">
                                            <div class="user-badge">
                                                <span>👤</span>
                                                <span class="user-name">
                                                    <%= loggedInUser.getNama() %>
                                                </span>
                                                <span class="user-role">
                                                    <%= userRole %>
                                                </span>
                                            </div>
                                            <a href="ProfileServlet" class="btn-profile">👤 Profile</a>
                                            <a href="LogoutServlet" class="btn-logout">Logout</a>
                                        </div>
                                    </nav>

                                    <div class="container">
                                        <!-- Hero Section -->
                                        <div style="text-align: center; margin-bottom: 3rem; margin-top: 3rem;">
                                            <h1 style="font-size: 3rem; margin-bottom: 0.5rem; text-align: center;">
                                                Welcome Back!</h1>
                                            <p
                                                style="color: var(--text-muted); font-size: 1.1rem; max-width: 600px; margin: 0 auto; text-align: center;">
                                                Kelola keuangan masa depanmu dengan lebih cerdas. Pantau pemasukan,
                                                tagihan, dan tabunganmu
                                                di satu
                                                tempat.
                                            </p>
                                        </div>

                                        <!-- Dashboard Grid -->
                                        <div class="menu-grid">
                                            <a href="TransaksiServlet" class="card menu-item">
                                                <div class="icon">💸</div>
                                                <h3>Transaksi</h3>
                                                <small>Catat Pemasukan & Pengeluaran</small>
                                            </a>
                                            <a href="AnggaranServlet" class="card menu-item">
                                                <div class="icon">📊</div>
                                                <h3>Anggaran</h3>
                                                <small>Atur Budget Bulanan</small>
                                            </a>
                                            <a href="LaporanServlet" class="card menu-item">
                                                <div class="icon">📈</div>
                                                <h3>Laporan</h3>
                                                <small>Ringkasan Keuangan</small>
                                            </a>
                                            <a href="TargetTabunganServlet" class="card menu-item">
                                                <div class="icon">🎯</div>
                                                <h3>Target Tabungan</h3>
                                                <small>Goals Anda</small>
                                            </a>
                                            <a href="TagihanServlet" class="card menu-item">
                                                <div class="icon">🧾</div>
                                                <h3>Tagihan</h3>
                                                <small>Jatuh Tempo</small>
                                            </a>
                                            <a href="PengingatServlet" class="card menu-item">
                                                <div class="icon">🔔</div>
                                                <h3>Pengingat</h3>
                                                <small>Reminder & Notes</small>
                                            </a>
                                            <a href="FinGoalServlet" class="card menu-item">
                                                <div class="icon">🏁</div>
                                                <h3>FinGoal</h3>
                                                <small>Progress Tracking</small>
                                            </a>

                                            <!-- Admin Only Features -->
                                            <% if (isAdmin) { %>
                                                <a href="KategoriServlet" class="card menu-item">
                                                    <div class="icon">📂</div>
                                                    <h3>Kategori</h3>
                                                    <small>Master Data</small>
                                                </a>
                                                <a href="UserServlet" class="card menu-item">
                                                    <div class="icon">👥</div>
                                                    <h3>Users</h3>
                                                    <small>Kelola Pengguna</small>
                                                </a>
                                                <% } %>
                                        </div>

                                        <div
                                            style="margin-top: 4rem; text-align: center; color: var(--text-muted); font-size: 0.9rem;">
                                            &copy; 2025 MoneyMate Financial System
                                        </div>
                                    </div>

                                    <% if (showModal) { %>
                                        <div id="notificationModal" class="modal" style="display: block;">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h3 style="margin:0; font-size: 1.25rem;">🔔 Notifikasi Penting</h3>
                                                    <span class="close"
                                                        onclick="document.getElementById('notificationModal').style.display='none'">&times;</span>
                                                </div>
                                                <div class="modal-body">
                                                    <% if (!dueBills.isEmpty()) { %>
                                                        <h4
                                                            style="color: var(--danger); margin-top:0; margin-bottom: 10px;">
                                                            ⚠️ Tagihan Jatuh Tempo (3 Hari)</h4>
                                                        <% for (Tagihan t : dueBills) { %>
                                                            <div class="bill-item">
                                                                <div
                                                                    style="display:flex; justify-content:space-between; align-items:center;">
                                                                    <strong>
                                                                        <%= t.getNama() %>
                                                                    </strong>
                                                                    <span
                                                                        style="font-weight:bold; color:var(--danger);">Rp
                                                                        <%= String.format("%,.0f", t.getJumlah()) %>
                                                                    </span>
                                                                </div>
                                                                <div
                                                                    style="font-size:0.85rem; color:#666; margin-top:4px;">
                                                                    Jatuh Tempo: <%= t.getTanggalJatuhTempo() %>
                                                                </div>
                                                                <div style="text-align:right; margin-top:8px;">
                                                                    <a href="${pageContext.request.contextPath}/TagihanServlet?action=payBill&id=<%= t.getId() %>"
                                                                        class="btn-sm"
                                                                        style="background:var(--primary); color:white; padding:4px 10px; border-radius:4px; text-decoration:none; font-size:0.8rem;">Bayar
                                                                        Sekarang</a>
                                                                </div>
                                                            </div>
                                                            <% } %>
                                                                <% } %>

                                                                    <% if (!todayReminders.isEmpty()) { %>
                                                                        <h4
                                                                            style="color: var(--accent); margin-top: 1.5rem; margin-bottom: 10px;">
                                                                            📅 Pengingat Hari Ini</h4>
                                                                        <% for (Pengingat p : todayReminders) { %>
                                                                            <div class="reminder-item">
                                                                                <%= p.getPesan() %>
                                                                            </div>
                                                                            <% } %>
                                                                                <% } %>
                                                </div>
                                            </div>
                                        </div>
                                        <script>
                                            // Close modal when clicking outside
                                            window.onclick = function (event) {
                                                var modal = document.getElementById('notificationModal');
                                                if (event.target == modal) {
                                                    modal.style.display = "none";
                                                }
                                            }
                                        </script>
                                        <% } %>
                                </body>

                                </html>