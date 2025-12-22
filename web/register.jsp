<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Register - MoneyMate</title>
        <link rel="stylesheet" href="css/style.css">
        <style>
            body {
                display: flex;
                align-items: center;
                justify-content: center;
                min-height: 100vh;
                padding: 2rem;
            }

            .register-container {
                width: 100%;
                max-width: 450px;
                animation: fadeUp 0.6s ease-out;
            }

            .register-card {
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(16px);
                border: 1px solid rgba(255, 255, 255, 0.7);
                border-radius: var(--radius-lg);
                padding: 3rem;
                box-shadow: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1);
            }

            .register-header {
                text-align: center;
                margin-bottom: 2.5rem;
            }

            .register-icon {
                font-size: 4rem;
                margin-bottom: 1rem;
                display: block;
                filter: drop-shadow(0 4px 8px rgba(79, 70, 229, 0.3));
            }

            .register-title {
                font-size: 2rem;
                margin-bottom: 0.5rem;
                background: linear-gradient(135deg, var(--primary), var(--accent));
                -webkit-background-clip: text;
                background-clip: text;
                -webkit-text-fill-color: transparent;
            }

            .register-subtitle {
                color: var(--text-muted);
                font-size: 0.95rem;
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

            .form-group {
                margin-bottom: 1.5rem;
            }

            .form-group label {
                display: block;
                margin-bottom: 0.5rem;
                font-weight: 600;
                color: var(--text);
            }

            .input-wrapper {
                position: relative;
            }

            .input-icon {
                position: absolute;
                left: 1rem;
                top: 50%;
                transform: translateY(-50%);
                font-size: 1.2rem;
                color: var(--text-muted);
            }

            input[type="text"],
            input[type="email"],
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
            }

            input:focus {
                outline: none;
                border-color: var(--primary);
                box-shadow: 0 0 0 4px rgba(79, 70, 229, 0.1);
            }

            .btn-register {
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

            .btn-register:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.2);
                filter: brightness(110%);
            }

            .btn-register:active {
                transform: translateY(0);
            }

            .login-link {
                text-align: center;
                margin-top: 1.5rem;
                color: var(--text-muted);
                font-size: 0.9rem;
            }

            .login-link a {
                color: var(--primary);
                font-weight: 600;
                text-decoration: none;
                transition: all 0.2s;
            }

            .login-link a:hover {
                text-decoration: underline;
            }
        </style>
    </head>

    <body>
        <div class="register-container">
            <div class="register-card">
                <div class="register-header">
                    <span class="register-icon">✨</span>
                    <h1 class="register-title">Daftar Akun</h1>
                    <p class="register-subtitle">Mulai kelola keuangan Anda hari ini</p>
                </div>

                <% if (request.getAttribute("error") !=null) { %>
                    <div class="error-message">
                        ⚠️ <%= request.getAttribute("error") %>
                    </div>
                    <% } %>

                        <form method="post" action="RegisterServlet">
                            <div class="form-group">
                                <label for="nama">Nama Lengkap</label>
                                <div class="input-wrapper">
                                    <span class="input-icon">👤</span>
                                    <input type="text" id="nama" name="nama" placeholder="" required autofocus
                                        value="<%= request.getParameter(" nama") !=null ? request.getParameter("nama")
                                        : "" %>"
                                    >
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="email">Email</label>
                                <div class="input-wrapper">
                                    <span class="input-icon">📧</span>
                                    <input type="email" id="email" name="email" placeholder="" required
                                        value="<%= request.getParameter(" email") !=null ? request.getParameter("email")
                                        : "" %>"
                                    >
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="password">Password</label>
                                <div class="input-wrapper">
                                    <span class="input-icon">🔒</span>
                                    <input type="password" id="password" name="password"
                                        placeholder="Minimal 6 karakter" required minlength="6">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="confirmPassword">Konfirmasi Password</label>
                                <div class="input-wrapper">
                                    <span class="input-icon">🔐</span>
                                    <input type="password" id="confirmPassword" name="confirmPassword"
                                        placeholder="Ketik ulang password" required minlength="6">
                                </div>
                            </div>

                            <button type="submit" class="btn-register">
                                Daftar Sekarang 🚀
                            </button>
                        </form>

                        <div class="login-link">
                            Sudah punya akun? <a href="LoginServlet">Login di sini</a>
                        </div>
            </div>

            <div style="margin-top: 2rem; text-align: center; color: var(--text-muted); font-size: 0.85rem;">
                &copy; 2025 MoneyMate Financial System
            </div>
        </div>
    </body>

    </html>