package hack.db;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.tweak.ConnectionFactory;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfiguration {

    static final String dbUrl = "jdbc:mysql://localhost/hack";
    static final String dbClass = "com.mysql.jdbc.Driver";
    static final String username = "dev";
    static final String password = "dev";


    public static void main(String args[]) {
        new DBConfiguration().getDbConfiguration();
    }

    public void getDbConfiguration() {
        DBI dbi = null;

        ConnectionFactory cf = new ConnectionFactory() {
            @Override
            public Connection openConnection()
                    throws SQLException {
                try {
                    return getConnection();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException cnfe) {
                    cnfe.printStackTrace();
                }
                return null;
            }
        };
        dbi = new DBI(cf);
        Handle handle = dbi.open();
        handle.execute("create table temp (id int primary key, name varchar(100))");
        handle.close();
    }

    private static Connection getConnection()
            throws URISyntaxException, SQLException, ClassNotFoundException {
        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl,
                username, password);

        return connection;
    }
}
