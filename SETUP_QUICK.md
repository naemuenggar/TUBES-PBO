# Quick setup

## Database
1. Buat DB + tabel:
   - Jalankan file: `database/MoneyMate.sql`
2. Pastikan setting koneksi di `src/java/util/JDBC.java` sesuai:
   - URL: `jdbc:mysql://localhost:3306/MoneyMate`
   - user: `root`
   - password: `""`

## Server
- Disarankan Tomcat 9 (karena masih pakai `javax.servlet.*`)
