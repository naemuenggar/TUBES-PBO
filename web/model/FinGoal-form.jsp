<%@ page import="model.FinGoal" %>
    <jsp:useBean id="goal" class="model.FinGoal" scope="request" />

    <head>
        <title>${goal.id == null ? "Tambah" : "Edit"} FinGoal</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap" rel="stylesheet">
    </head>

    <body>

        <nav class="navbar">
            <a href="${pageContext.request.contextPath}/" class="brand">💰 MoneyMate</a>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/">Dashboard</a>
                <a href="../FinGoalServlet">Financial Goals</a>
            </div>
        </nav>

        <div class="container" style="max-width: 600px;">
            <div class="card">
                <h2 style="margin-top: 0; margin-bottom: 1.5rem;">${goal.id == null ? "Tambah" : "Edit"} Progress</h2>
                <form action="${pageContext.request.contextPath}/FinGoalServlet" method="post">
                    <div class="form-group">
                        <label>ID Progress</label>
                        <input type="text" name="id" value="${goal.id}" required placeholder="Contoh: G001">
                    </div>
                    <div class="form-group">
                        <label>Target ID</label>
                        <input type="text" name="targetId" value="${goal.targetId}" required
                            placeholder="Contoh: TGT001">
                    </div>
                    <div class="form-group">
                        <label>Jumlah Progress (Rp)</label>
                        <input type="number" step="0.01" name="progress" value="${goal.progress}" required>
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
                        <a href="../FinGoalServlet" class="btn btn-danger"
                            style="background-color: #e2e8f0; color: #333; flex: 1;">Batal</a>
                    </div>
                </form>
            </div>
        </div>
    </body>

    </html>