package hack;

import hack.db.DBExecutionHelper;
import hack.form.User;
import org.eclipse.jetty.http.HttpStatus;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.HandleCallback;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddShoppingCart extends HttpServlet {

    public static final String URL_KEY = "urls";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Credentials", "true ");
        resp.addHeader("Access-Control-Allow-Methods", "OPTIONS, POST");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Depth, User-Agent, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control");

        User aUser = findOrCreateUser(req);

        findOrCreateShoppingCardItem(aUser, req);

        resp.setStatus(HttpStatus.OK_200);
    }

    private void findOrCreateShoppingCardItem(User aUser, HttpServletRequest req) {

        DBExecutionHelper executionHelper = new DBExecutionHelper();
        for (String url : req.getParameterValues("urls[]")) {
            executionHelper.execute(putItem(aUser, url));
        }
    }

    private HandleCallback putItem(final User aUser, final String url) {
        return new HandleCallback<Integer>() {
            @Override
            public Integer withHandle(Handle handle)
                    throws Exception {
                return handle.update("insert into shopping_cart (user_id, url) values (" + aUser.getId() + ",'" + url + "')");
            }
        };
    }

    private User findOrCreateUser(HttpServletRequest req) {
        String email = req.getParameter("email").toLowerCase();
        String userName = req.getParameter("userName").toLowerCase();
        DBExecutionHelper executionHelper = new DBExecutionHelper();
        User aUser = ((User) executionHelper.execute(getUser(email)));
        if (aUser == null) {
            executionHelper.execute(putUser(email, userName));
            aUser = ((User) executionHelper.execute(getUser(email)));
        }
        return aUser;
    }

    private HandleCallback<User> getUser(final String email) {
        return new HandleCallback<User>() {
            @Override
            public User withHandle(Handle handle)
                    throws Exception {
                return handle.createQuery("select id, name, email from user where email = '" + email + "'").map(new UserMapper()).first();

            }
        };
    }

    private HandleCallback<Integer> putUser(final String email, final String name) {
        return new HandleCallback<Integer>() {
            @Override
            public Integer withHandle(Handle handle)
                    throws Exception {
                return handle.update("insert into user (name, email) values ('" + name + "','" + email + "')");
            }
        };
    }

    static class UserMapper implements ResultSetMapper<User> {
        public User map(int rowIndex, ResultSet rs, StatementContext ctxt) throws SQLException {
            return new User(rs.getInt(1), rs.getString(2), rs.getString(3));
        }
    }

}
