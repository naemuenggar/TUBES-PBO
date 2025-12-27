<%@ page import="java.util.*, model.Pengeluaran" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <jsp:useBean id="pengeluaranList" scope="request" type="java.util.List" />
            <html>

            <head>
                <title>Daftar Pengeluaran</title>
            </head>

            <body>
                <h2>Daftar Pengeluaran</h2>
                <a href="pengeluaran/form.jsp">+ Tambah Pengeluaran</a>
                <table border="1">
                    <tr>
                        <th>ID</th>
                        <th>User ID</th>
                        <th>Jumlah</th>
                        <th>Deskripsi</th>
                        <th>Tanggal</th>
                        <th>Aksi</th>
                    </tr>
                    <c:forEach var="p" items="${pengeluaranList}">
                        <tr>
                            <td>${p.id}</td>
                            <td>${p.userId}</td>
                            <td>Rp
                                <fmt:formatNumber value="${p.jumlah}" pattern="#,###" />
                            </td>
                            <td>${p.deskripsi}</td>
                            <td>${p.tanggal}</td>
                            <td>
                                <a href="PengeluaranServlet?action=edit&id=${p.id}">Edit</a>
                                <a href="PengeluaranServlet?action=delete&id=${p.id}">Hapus</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </body>

            </html>