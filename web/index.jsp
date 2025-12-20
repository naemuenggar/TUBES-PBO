<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="model.User" %>
        <% User loggedInUser=(User) session.getAttribute("user"); String userRole=loggedInUser !=null ?
            loggedInUser.getRole() : "user" ; boolean isAdmin="admin" .equals(userRole); %>
            <!DOCTYPE html>
            <html>

            <head>
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
                        <h1 style="font-size: 3rem; margin-bottom: 0.5rem; text-align: center;">Welcome Back!</h1>
                        <p
                            style="color: var(--text-muted); font-size: 1.1rem; max-width: 600px; margin: 0 auto; text-align: center;">
                            Kelola keuangan masa depanmu dengan lebih cerdas. Pantau pemasukan, tagihan, dan tabunganmu
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

                    <div style="margin-top: 4rem; text-align: center; color: var(--text-muted); font-size: 0.9rem;">
                        &copy; 2025 MoneyMate Financial System
                    </div>
                </div>
            </body>

            </html>