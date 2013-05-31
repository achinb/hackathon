package hack.db;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.tweak.ConnectionFactory;
import org.skife.jdbi.v2.tweak.HandleCallback;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBExecutionHelper implements  DBConstants {
    public Object execute(HandleCallback handleCallback) {
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
        return new DBI(cf).withHandle(handleCallback);
    }

    private static Connection getConnection()
            throws URISyntaxException, SQLException, ClassNotFoundException {
        Class.forName(dbClass);
        Connection connection = DriverManager.getConnection(dbUrl,
                username, password);

        return connection;
    }
}
