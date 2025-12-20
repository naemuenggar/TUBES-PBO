<%@ page import="java.util.*, model.Pemasukan" %>
<jsp:useBean id="pemasukanList" scope="request" type="java.util.List" />
<html>
<head><title>Daftar Pemasukan</title></head>
<body>
<h2>Daftar Pemasukan</h2>
<a href="pemasukan/form.jsp">+ Tambah Pemasukan</a>
<table border="1">
    <tr><th>ID</th><th>User ID</th><th>Jumlah</th><th>Deskripsi</th><th>Tanggal</th><th>Aksi</th></tr>
    <c:forEach var="p" items="${pemasukanList}">
        <tr>
            <td>${p.id}</td>
            <td>${p.userId}</td>
            <td>${p.jumlah}</td>
            <td>${p.deskripsi}</td>
            <td>${p.tanggal}</td>
            <td>
                <a href="PemasukanServlet?action=edit&id=${p.id}">Edit</a>
                <a href="PemasukanServlet?action=delete&id=${p.id}">Hapus</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
