{#- .name: Java class demonstrating connecting to PostgreSQL database over JDBC -#}
{#- .default(class_name): Main -#}
{#- .default(item_name): name -#}
{#- .default(package_path): org.mycompany -#}
package {{package_path}};

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public final class {{class_name}} {

    private final static Random s_rand = new Random();
    private final static String DB_NAME = "postgres";
    private final static String HEALTH_CHECK_MESSAGE = "server health check";
    private final static int SOCKET_TIMEOUT_SECONDS = 2;

    public static void main(String[] args) {
        final String hostName = "some-server";
        final int port = 7483;
        final String userName = "some-user";
        final String password = "some-password";
        System.out.println("isAlive=" + isAlive(
                hostName,
                port,
                DB_NAME,
                userName,
                password,
                SOCKET_TIMEOUT_SECONDS));
    }

    private static boolean isAlive(
            final String hostName,
            final int port,
            final String dbName,
            final String userName,
            final String password,
            final int socketTimeout) {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            logMessage("Could not register JDBC PostgreSQL driver");
            return false;
        }

        final String url = String.format(
                "jdbc:postgresql://%s:%d/%s?socketTimeout=%d",
                hostName,
                port,
                dbName,
                socketTimeout);

        logMessage(String.format("Attempting to connect to %s", url));
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            final Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(socketTimeout);

            // Random cookie to defeat cache and guarantee that database isn't cheating
            final int expectedCookie = s_rand.nextInt();

            final String sql = String.format("SELECT '%s', %d", HEALTH_CHECK_MESSAGE, expectedCookie);
            final ResultSet rs = stmt.executeQuery(sql);

            // Result set should consist of at least one row
            if (!rs.next()) {
                logMessage("Unexpected result from health check query: no rows returned");
                return false;
            }

            final String actualMessage = rs.getString(1);
            final int actualCookie = rs.getInt(2);

            // Result set should not have any more rows
            if (rs.next()) {
                logMessage("Unexpected result from health check query: too many rows returned");
                return false;
            }

            return actualMessage.equals(HEALTH_CHECK_MESSAGE) && actualCookie == expectedCookie;
        } catch (SQLException e) {
            logMessage("Unhandled exception: " + e.toString());
            return false;
        }
    }

    private static void logMessage(final String message) {
        System.out.println(message);
    }
}
