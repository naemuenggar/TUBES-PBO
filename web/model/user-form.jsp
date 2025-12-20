<%@ page import="model.User" %>
    <jsp:useBean id="user" class="model.User" scope="request" />

    <head>
        <title>${user.id == null ? "Tambah" : "Edit"} User</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
    </head>

    <body>

        <nav class="navbar">
            <a href="${pageContext.request.contextPath}/" class="brand">💰 MoneyMate</a>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/">Dashboard</a>
                <a href="../UserServlet">Users</a>
            </div>
        </nav>

        <div class="container" style="max-width: 600px;">
            <div class="card">
                <h2 style="margin-top: 0; margin-bottom: 1.5rem;">${user.id == null ? "Tambah" : "Edit"} User</h2>
                <form action="../UserServlet" method="post">
                    <!-- ID hidden for update -->
                    <c:if test="${not empty user.id}">
                        <input type="hidden" name="id" value="${user.id}">
                    </c:if>
                    <div class="form-group">
                        <label>Nama Lengkap</label>
                        <input type="text" name="nama" value="${user.nama}" required placeholder="Nama User">
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" value="${user.email}" required placeholder="user@example.com">
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="password" value="${user.password}" required>
                    </div>

                    <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                        <input type="submit" value="Simpan" class="btn btn-primary" style="flex: 1;">
                        <a href="../UserServlet" class="btn btn-danger"
                            style="background-color: #e2e8f0; color: #333; flex: 1;">Batal</a>
                    </div>
                </form>
            </div>
        </div>
    </body>

    </html>