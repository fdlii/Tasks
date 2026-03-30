package yourcompany;

import com.yourcompany.task_14.ApplicationConfig;
import com.yourcompany.task_5_1.MenuController;
import com.yourcompany.task_5_1.interfaces.IMenuController;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Program {
    public static void main(String[] args) throws Exception {
        runLiquibaseMigrations();

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        IMenuController menuController = context.getBean(MenuController.class);
        menuController.run();
    }

    private static void runLiquibaseMigrations() throws Exception {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/book_store";
        String username = "postgres";
        String password = "root";

        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;

        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
        System.setErr(new PrintStream(OutputStream.nullOutputStream()));

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            JdbcConnection jdbcConnection = new JdbcConnection(connection);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);

            String changelogPath = "db/changelog/db.changelog-master.yaml";
            Liquibase liquibase = new Liquibase(
                    changelogPath,
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update("");
        }
        finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }
}
