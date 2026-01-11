<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="java.util.*, model.Anggaran" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <jsp:useBean id="anggarans" scope="request" type="java.util.List" />

                <head>
                    <meta charset="UTF-8">
                    <title>Daftar Anggaran</title>
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;700&display=swap"
                        rel="stylesheet">
                </head>

                <body>

                    <nav class="navbar">
                        <a href="${pageContext.request.contextPath}/" class="brand">&#128176; MoneyMate</a>
                        <div class="nav-links">
                            <a href="${pageContext.request.contextPath}/">Dashboard</a>
                            <a href="AnggaranServlet">Anggaran</a>
                        </div>
                    </nav>

                    <div class="container">
                        <div
                            style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                            <h2>Daftar Anggaran</h2>
                            <a href="AnggaranServlet?action=form" class="btn btn-primary">+ Tambah Anggaran</a>
                        </div>

                        <div class="card">
                            <div class="table-responsive">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>User ID</th>
                                            <th>Nama Anggaran</th>
                                            <th>Jumlah</th>
                                            <th>Aksi</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="a" items="${anggarans}">
                                            <tr>
                                                <td>${a.id}</td>
                                                <td>${a.userId}</td>
                                                <td><strong>${a.nama}</strong></td>
                                                <td>Rp
                                                    <fmt:formatNumber value="${a.jumlah}" pattern="#,###" />
                                                </td>
                                                <td>
                                                    <a href="AnggaranServlet?action=edit&id=${a.id}"
                                                        class="btn btn-sm btn-primary"
                                                        style="background-color: var(--primary);">Edit</a>
                                                    <a href="AnggaranServlet?action=delete&id=${a.id}"
                                                        class="btn btn-sm btn-danger"
                                                        onclick="return confirm('Yakin hapus?');">Hapus</a>
                                                    <button onclick="addFunds('${a.id}', '${a.nama}')"
                                                        class="btn btn-sm"
                                                        style="background-color: #28a745; color: white;">+ Dana</button>
                                                    <button onclick="withdrawFunds('${a.id}', '${a.nama}')"
                                                        class="btn btn-sm"
                                                        style="background-color: #ffc107; color: black;">-
                                                        Tarik</button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty anggarans}">
                                            <tr>
                                                <td colspan="5"
                                                    style="text-align: center; padding: 2rem; color: var(--text-muted);">
                                                    Belum ada data anggaran.</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <!-- Modal Styles -->
                    <style>
                        .modal-overlay {
                            display: none;
                            position: fixed;
                            top: 0;
                            left: 0;
                            width: 100%;
                            height: 100%;
                            background: rgba(0, 0, 0, 0.5);
                            backdrop-filter: blur(4px);
                            z-index: 1000;
                            justify-content: center;
                            align-items: center;
                            opacity: 0;
                            transition: opacity 0.3s ease;
                        }

                        .modal-overlay.show {
                            display: flex;
                            opacity: 1;
                        }

                        .modal-content {
                            background: white;
                            padding: 2rem;
                            border-radius: var(--radius-lg);
                            width: 90%;
                            max-width: 400px;
                            box-shadow: var(--shadow-lg);
                            transform: translateY(20px);
                            transition: transform 0.3s cubic-bezier(0.18, 0.89, 0.32, 1.28);
                            text-align: center;
                        }

                        .modal-overlay.show .modal-content {
                            transform: translateY(0);
                        }

                        .modal-title {
                            font-size: 1.5rem;
                            margin-bottom: 1rem;
                            color: var(--text);
                        }

                        .modal-actions {
                            display: flex;
                            gap: 1rem;
                            justify-content: center;
                            margin-top: 1.5rem;
                        }
                    </style>

                    <!-- Add/Withdraw Modal -->
                    <div id="fundModal" class="modal-overlay">
                        <div class="modal-content">
                            <h3 id="modalTitle" class="modal-title">Atur Dana</h3>
                            <p id="modalSubtitle" style="color: var(--text-muted); margin-bottom: 1.5rem;">...</p>

                            <form id="fundForm" method="POST" action="AnggaranServlet"
                                onsubmit="return prepareSubmit()">
                                <input type="hidden" name="action" id="modalAction">
                                <input type="hidden" name="id" id="modalId">

                                <div class="form-group">
                                    <label for="displayAmount" style="text-align: left;">Jumlah Dana (Rp)</label>
                                    <input type="text" id="displayAmount" placeholder="Contoh: 50.000" required
                                        autofocus oninput="formatInput(this)"
                                        style="font-family: monospace; letter-spacing: 1px;">
                                    <input type="hidden" name="amount" id="realAmount">
                                </div>

                                <div class="modal-actions">
                                    <button type="button" class="btn btn-secondary" onclick="closeModal()"
                                        style="background: #e2e8f0; color: var(--text);">Batal</button>
                                    <button type="submit" class="btn btn-primary" id="modalSubmitBtn">Simpan</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div style="text-align: center; margin-bottom: 2rem;">
                        <a href="${pageContext.request.contextPath}/" class="btn btn-secondary"
                            style="background-color: #6c757d; color: white; text-decoration: none; padding: 0.5rem 1rem; border-radius: 4px; display: inline-block;">Kembali
                            ke Dashboard</a>
                    </div>

                    <script>
                        const modal = document.getElementById('fundModal');
                        const form = document.getElementById('fundForm');
                        const title = document.getElementById('modalTitle');
                        const subtitle = document.getElementById('modalSubtitle');
                        const actionInput = document.getElementById('modalAction');
                        const idInput = document.getElementById('modalId');
                        const displayInput = document.getElementById('displayAmount');
                        const realAmountInput = document.getElementById('realAmount');
                        const submitBtn = document.getElementById('modalSubmitBtn');

                        function formatInput(input) {
                            // Remove non-digits
                            let value = input.value.replace(/\D/g, '');

                            // Update real hidden input
                            realAmountInput.value = value;

                            // Add dots for visual formatting
                            input.value = new Intl.NumberFormat('id-ID').format(value);
                        }

                        function prepareSubmit() {
                            // Ensure real value is set before submit
                            if (!realAmountInput.value) {
                                alert("Mohon masukkan jumlah dana.");
                                return false;
                            }
                            return true;
                        }

                        function openModal(action, id, nama) {
                            modal.classList.add('show');
                            idInput.value = id;
                            actionInput.value = action;
                            displayInput.value = '';
                            realAmountInput.value = '';
                            setTimeout(() => displayInput.focus(), 100);

                            if (action === 'addFunds') {
                                title.innerText = "Tambah Dana";
                                subtitle.innerText = "Menambahkan dana ke budget: " + nama;
                                submitBtn.innerText = "Tambah (+)";
                                submitBtn.style.background = "var(--success)";
                            } else {
                                title.innerText = "Tarik Dana";
                                subtitle.innerText = "Menarik dana dari budget: " + nama;
                                submitBtn.innerText = "Tarik (-)";
                                submitBtn.style.background = "var(--warning)";
                            }
                        }

                        function closeModal() {
                            modal.classList.remove('show');
                        }

                        // Close on outside click
                        modal.addEventListener('click', (e) => {
                            if (e.target === modal) closeModal();
                        });

                        // Update existing buttons to use openModal
                        function addFunds(id, nama) {
                            openModal('addFunds', id, nama);
                        }

                        function withdrawFunds(id, nama) {
                            openModal('withdrawFunds', id, nama);
                        }
                    </script>
                </body>

                </html>