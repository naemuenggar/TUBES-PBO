<%@ page import="model.Pemasukan" %>
<jsp:useBean id="pemasukan" class="model.Pemasukan" scope="request" />
<html>
<head><title>Form Pemasukan</title></head>
<body>
<h2>${pemasukan.id == null ? "Tambah" : "Edit"} Pemasukan</h2>
<form action="../PemasukanServlet" method="post">
    ID: <input type="text" name="id" value="${pemasukan.id}" /><br/>
    User ID: <input type="text" name="userId" value="${pemasukan.userId}" /><br/>
    Jumlah: <input type="number" name="jumlah" value="${pemasukan.jumlah}" /><br/>
    Deskripsi: <input type="text" name="deskripsi" value="${pemasukan.deskripsi}" /><br/>
    Tanggal: <input type="date" name="tanggal" value="${pemasukan.tanggal}" /><br/>
    Kategori ID: <input type="text" name="kategoriId" value="${pemasukan.kategoriId}" /><br/>
    <input type="submit" value="Simpan" />
</form>
</body>
</html>