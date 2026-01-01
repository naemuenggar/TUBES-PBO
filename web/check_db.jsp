<%@ page import="java.sql.*" %>
    <%@ page import="util.JDBC" %>
        <%@ page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html>

            <head>
                <title>Database Check</title>
                <meta charset="UTF-8">
            </head>

            <body style="font-family: monospace; padding: 20px;">
                <h1>Database Connection Diagnostic</h1>
                <hr />
                <% try { Connection conn=JDBC.getConnection(); out.println("<h2 style='color:green'>SUCCESS: Connected
                    to Database!</h2>");
                    out.println("URL: " + conn.getMetaData().getURL() + "<br />");
                    conn.close();
                    } catch (Throwable e) {
                    out.println("<h2 style='color:red'>FAILURE: Critical Error</h2>");
                    out.println("<h3>Error Type: " + e.getClass().getName() + "</h3>");
                    out.println("<h3>Message: " + e.getMessage() + "</h3>");
                    out.println("
                    <pre>");
                        e.printStackTrace(new java.io.PrintWriter(out));
                        out.println("</pre>");
                    }
                    %>
            </body>

            </html>