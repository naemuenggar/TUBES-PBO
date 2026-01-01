<%@ page import="java.sql.*,util.JDBC" %>
    <% try { Connection c=JDBC.getConnection(); Statement s=c.createStatement(); String
        T="DELETE FROM transaksi WHERE user_id IN " ; String U="DELETE FROM user WHERE " ; String
        E="email IN ('a@m.c','u@m.c')" ; String I="id IN ('u1','u2')" ; String S="(SELECT id FROM user WHERE " +E+")";
        s.executeUpdate(T+S); s.executeUpdate(T+"('u1','u2')"); int n=s.executeUpdate(U+E+" OR "+I);
if(n>0) out.print(" SUCCESS-DELETED:"+n+"<br><br><a href='index.jsp'>Kembali ke Dashboard</a>"); else
        out.print("INFO:ALREADY-CLEAN<br><br><a href='index.jsp'>Kembali ke Dashboard</a>"); c.close(); }catch(Exception
        e){
        out.print("ERR:"+e.getMessage()); } %>