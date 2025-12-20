<%@ page import="model.Pengeluaran" %>
<jsp:useBean id="pengeluaran" class="model.Pengeluaran" scope="request" />
<html>
<head><title>Form Pengeluaran</title></head>
<body>
<h2>${pengeluaran.id == null ? "Tambah" : "Edit"} Pengeluaran</h2>
<form action="../PengeluaranServlet" method="post">
    ID: <input type="text" name="id" value="${pengeluaran.id}" /><br/>
    User ID: <input type="text" name="userId" value="${pengeluaran.userId}" /><br/>
    Jumlah: <input type="number" name="jumlah" value="${pengeluaran.jumlah}" /><br/>
    Deskripsi: <input type="text" name="deskripsi" value="${pengeluaran.deskripsi}" /><br/>
    Tanggal: <input type="date" name="tanggal" value="${pengeluaran.tanggal}" /><br/>
    Kategori ID: <input type="text" name="kategoriId" value="${pengeluaran.kategoriId}" /><br/>
    <input type="submit" value="Simpan" />
</form>
</body>
</html>
