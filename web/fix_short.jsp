<%@ page import="java.sql.*,util.JDBC" %>
    <% try { Connection c=JDBC.getConnection(); Statement s=c.createStatement(); String I="INSERT IGNORE INTO " ; String
        U="user(id,nama,email,password,role)VALUES" ; String K="kategori(id,nama,tipe)VALUES" ;
        s.executeUpdate(I+U+"('u1','A','a@m.c','x','admin')"); s.executeUpdate(I+U+"('u2','U','u@m.c','x','user')");
        s.executeUpdate(I+K+"('k1','Gaji','pemasukan')"); s.executeUpdate(I+K+"('k2','Hadiah','pemasukan')");
        s.executeUpdate(I+K+"('k3','Invest','pemasukan')"); s.executeUpdate(I+K+"('k6','Makan','pengeluaran')");
        s.executeUpdate(I+K+"('k7','Trans','pengeluaran')"); s.executeUpdate(I+K+"('k8','Shop','pengeluaran')");
        out.print("SUCCESS-DONE<br><br><a href='index.jsp'>Kembali ke Dashboard</a>"); c.close(); } catch (Exception e)
        { out.print("ERR:"+e.getMessage()); } %>