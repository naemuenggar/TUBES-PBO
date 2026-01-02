<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="model.User" %>
        <% User loggedInUser=(User) session.getAttribute("user"); User profileUser=(User)
            request.getAttribute("profileUser"); if (profileUser==null) profileUser=loggedInUser; String
            userRole=profileUser !=null ? profileUser.getRole() : "user" ; %>
            <!DOCTYPE html>
            <html>

            <head>
                <title>Profile - MoneyMate</title>
                <link rel="stylesheet" href="css/style.css">
                <style>
                    .profile-container {
                        max-width: 600px;
                        margin: 2rem auto;
                        padding: 0 1rem;
                    }

                    .profile-card {
                        background: rgba(255, 255, 255, 0.95);
                        backdrop-filter: blur(16px);
                        border: 1px solid rgba(255, 255, 255, 0.7);
                        border-radius: var(--radius-lg);
                        padding: 2.5rem;
                        box-shadow: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1);
                        animation: fadeUp 0.4s ease-out;
                    }

                    .profile-header {
                        text-align: center;
                        margin-bottom: 2rem;
                        padding-bottom: 2rem;
                        border-bottom: 2px solid #f1f5f9;
                    }

                    .profile-avatar {
                        width: 100px;
                        height: 100px;
                        background: linear-gradient(135deg, var(--primary), var(--accent));
                        border-radius: 50%;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        font-size: 3rem;
                        margin: 0 auto 1rem;
                        box-shadow: 0 10px 15px -3px rgba(79, 70, 229, 0.4);
                    }

                    .profile-name {
                        font-size: 1.75rem;
                        font-weight: 700;
                        margin-bottom: 0.25rem;
                        background: linear-gradient(135deg, var(--primary), var(--accent));
                        -webkit-background-clip: text;
                        -webkit-text-fill-color: transparent;
                    }

                    .profile-role {
                        display: inline-block;
                        font-size: 0.8rem;
                        text-transform: uppercase;
                        background: var(--primary);
                        color: white;
                        padding: 0.25rem 0.75rem;
                        border-radius: 50px;
                        font-weight: 700;
                        letter-spacing: 0.05em;
                    }

                    .info-section {
                        margin-bottom: 2rem;
                    }

                    .info-section h3 {
                        font-size: 1rem;
                        color: var(--text-muted);
                        text-transform: uppercase;
                        letter-spacing: 0.05em;
                        margin-bottom: 1rem;
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                    }

                    .info-item {
                        display: flex;
                        align-items: center;
                        padding: 1rem;
                        background: #f8fafc;
                        border-radius: var(--radius-md);
                        margin-bottom: 0.75rem;
                    }

                    .info-icon {
                        font-size: 1.25rem;
                        margin-right: 1rem;
                        width: 40px;
                        height: 40px;
                        background: white;
                        border-radius: var(--radius-sm);
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        box-shadow: var(--shadow-sm);
                    }

                    .info-content {
                        flex: 1;
                    }

                    .info-label {
                        font-size: 0.8rem;
                        color: var(--text-muted);
                        margin-bottom: 0.15rem;
                    }

                    .info-value {
                        font-weight: 600;
                        color: var(--text);
                    }

                    .password-section {
                        border-top: 2px solid #f1f5f9;
                        padding-top: 2rem;
                    }

                    .password-section h3 {
                        font-size: 1rem;
                        color: var(--text-muted);
                        text-transform: uppercase;
                        letter-spacing: 0.05em;
                        margin-bottom: 1.5rem;
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                    }

                    .form-group {
                        margin-bottom: 1.25rem;
                    }

                    .form-group label {
                        display: block;
                        margin-bottom: 0.5rem;
                        font-weight: 600;
                        color: var(--text);
                        font-size: 0.9rem;
                    }

                    .input-wrapper {
                        position: relative;
                    }

                    .input-icon {
                        position: absolute;
                        left: 1rem;
                        top: 50%;
                        transform: translateY(-50%);
                        font-size: 1.1rem;
                        color: var(--text-muted);
                    }

                    input[type="password"] {
                        width: 100%;
                        padding: 0.875rem 1rem 0.875rem 3rem;
                        background: white;
                        border: 2px solid #e2e8f0;
                        border-radius: var(--radius-md);
                        font-size: 1rem;
                        color: var(--text);
                        transition: all 0.2s;
                        font-family: inherit;
                        box-sizing: border-box;
                        /* Fix for overflow */
                    }

                    input:focus {
                        outline: none;
                        border-color: var(--primary);
                        box-shadow: 0 0 0 4px rgba(79, 70, 229, 0.1);
                    }

                    .btn-submit {
                        width: 100%;
                        padding: 1rem;
                        background: linear-gradient(135deg, var(--primary), var(--primary-hover));
                        color: white;
                        border: none;
                        border-radius: var(--radius-md);
                        font-size: 1rem;
                        font-weight: 600;
                        cursor: pointer;
                        transition: all 0.2s;
                        box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1);
                        margin-top: 0.5rem;
                    }

                    .btn-submit:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.2);
                        filter: brightness(110%);
                    }

                    .btn-submit:active {
                        transform: translateY(0);
                    }

                    .error-message {
                        background: linear-gradient(135deg, #fee2e2, #fecaca);
                        border: 1px solid #fca5a5;
                        color: #991b1b;
                        padding: 1rem;
                        border-radius: var(--radius-md);
                        margin-bottom: 1.5rem;
                        font-size: 0.9rem;
                        font-weight: 600;
                        text-align: center;
                        animation: shake 0.4s;
                    }

                    .success-message {
                        background: linear-gradient(135deg, #dcfce7, #bbf7d0);
                        border: 1px solid #86efac;
                        color: #166534;
                        padding: 1rem;
                        border-radius: var(--radius-md);
                        margin-bottom: 1.5rem;
                        font-size: 0.9rem;
                        font-weight: 600;
                        text-align: center;
                        animation: fadeUp 0.4s;
                    }

                    @keyframes shake {

                        0%,
                        100% {
                            transform: translateX(0);
                        }

                        25% {
                            transform: translateX(-10px);
                        }

                        75% {
                            transform: translateX(10px);
                        }
                    }

                    .back-link {
                        display: inline-flex;
                        align-items: center;
                        gap: 0.5rem;
                        color: var(--primary);
                        text-decoration: none;
                        font-weight: 600;
                        margin-bottom: 1rem;
                        transition: all 0.2s;
                    }

                    .back-link:hover {
                        transform: translateX(-4px);
                    }

                    /* Navbar styles */
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

                    .user-role-badge {
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
                </style>
            </head>

            <body>
                <nav class="navbar">
                    <a href="index.jsp" class="brand">💰 MoneyMate</a>
                    <div class="user-info">
                        <div class="user-badge">
                            <span>👤</span>
                            <span class="user-name">
                                <%= profileUser.getNama() %>
                            </span>
                            <span class="user-role-badge">
                                <%= userRole %>
                            </span>
                        </div>
                        <a href="LogoutServlet" class="btn-logout">Logout</a>
                    </div>
                </nav>

                <div class="profile-container">
                    <a href="index.jsp" class="back-link">← Kembali ke Dashboard</a>

                    <div class="profile-card">
                        <div class="profile-header">
                            <div class="profile-avatar">👤</div>
                            <div class="profile-name">
                                <%= profileUser.getNama() %>
                            </div>
                            <span class="profile-role">
                                <%= userRole %>
                            </span>
                        </div>

                        <% if (request.getAttribute("error") !=null) { %>
                            <div class="error-message">
                                ⚠️ <%= request.getAttribute("error") %>
                            </div>
                            <% } %>

                                <% if (request.getAttribute("success") !=null) { %>
                                    <div class="success-message">
                                        ✅ <%= request.getAttribute("success") %>
                                    </div>
                                    <% } %>

                                        <div class="info-section">
                                            <h3>📋 Informasi Akun</h3>
                                            <div class="info-item">
                                                <div class="info-icon">🆔</div>
                                                <div class="info-content">
                                                    <div class="info-label">User ID</div>
                                                    <div class="info-value">
                                                        <%= profileUser.getId() %>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="info-item">
                                                <div class="info-icon">👤</div>
                                                <div class="info-content">
                                                    <div class="info-label">Nama</div>
                                                    <div class="info-value">
                                                        <%= profileUser.getNama() %>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="info-item">
                                                <div class="info-icon">📧</div>
                                                <div class="info-content">
                                                    <div class="info-label">Email</div>
                                                    <div class="info-value">
                                                        <%= profileUser.getEmail() %>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="info-item">
                                                <div class="info-icon">🛡️</div>
                                                <div class="info-content">
                                                    <div class="info-label">Role</div>
                                                    <div class="info-value" style="text-transform: capitalize;">
                                                        <%= userRole %>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="password-section">
                                            <h3>🔐 Ganti Password</h3>
                                            <form method="post" action="ProfileServlet">
                                                <div class="form-group">
                                                    <label for="currentPassword">Password Saat Ini</label>
                                                    <div class="input-wrapper">
                                                        <span class="input-icon">🔒</span>
                                                        <input type="password" id="currentPassword"
                                                            name="currentPassword"
                                                            placeholder="Masukkan password saat ini" required>
                                                    </div>
                                                </div>

                                                <div class="form-group">
                                                    <label for="newPassword">Password Baru</label>
                                                    <div class="input-wrapper">
                                                        <span class="input-icon">🔑</span>
                                                        <input type="password" id="newPassword" name="newPassword"
                                                            placeholder="Masukkan password baru" required>
                                                    </div>
                                                </div>

                                                <div class="form-group">
                                                    <label for="confirmPassword">Konfirmasi Password Baru</label>
                                                    <div class="input-wrapper">
                                                        <span class="input-icon">🔑</span>
                                                        <input type="password" id="confirmPassword"
                                                            name="confirmPassword" placeholder="Ulangi password baru"
                                                            required>
                                                    </div>
                                                </div>

                                                <button type="submit" class="btn-submit">
                                                    Ganti Password 🔄
                                                </button>
                                            </form>
                                        </div>
                    </div>

                    <div style="margin-top: 2rem; text-align: center; color: var(--text-muted); font-size: 0.85rem;">
                        &copy; 2025 MoneyMate Financial System
                    </div>
                </div>
            </body>

            </html>