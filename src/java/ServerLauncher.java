
import java.io.File;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class ServerLauncher {

    public static void main(String[] args) throws Exception {
        String webappDirLocation = "web";
        Tomcat tomcat = new Tomcat();

        // Portal defined via argument or default to 8080
        String operationPort = System.getProperty("PORT");
        if (operationPort == null || operationPort.isEmpty()) {
            operationPort = "8080";
        }
        
        tomcat.setPort(Integer.valueOf(operationPort));
        tomcat.getConnector(); // Initialize connector

        File webAppDir = new File(webappDirLocation);
        System.out.println("Configuring app with basedir: " + webAppDir.getAbsolutePath());

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", webAppDir.getAbsolutePath());
        
        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        File additionWebInfClasses = new File("build/web/WEB-INF/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        System.out.println("Starting embedded Tomcat server...");
        tomcat.start();
        System.out.println("Server started at http://localhost:" + operationPort);
        tomcat.getServer().await();
    }
}
