package hack;

import hack.db.DBExecutionHelper;
import hack.form.User;
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
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        //Find or create user by email address
        //Once user found/created, find or create item in shopping cart.
        /*
            SELECT ID from user where email = 'abc'
            INSERT into user values ('name', 'email')
            select ID from shopping_cart where user_id = <idfromabove> and url = 'xyz'
            insert into shopping_cart values (user_id, url)
         */
        User aUser = findOrCreateUser(req);

        findOrCreateShoppingCardItem(aUser, req);

        resp.getWriter().print("Hello from username: " + aUser.getName());
    }

    private void findOrCreateShoppingCardItem(User aUser, HttpServletRequest req) {
        String url = req.getParameter("url");

        DBExecutionHelper executionHelper = new DBExecutionHelper();
        executionHelper.execute(putItem(aUser, url));

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
        User aUser = ((User)executionHelper.execute(getUser(email)));
        if(aUser == null) {
            Integer retVal = (Integer)executionHelper.execute(putUser(email, userName));
            aUser = ((User)executionHelper.execute(getUser(email)));
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

    static class UserMapper implements ResultSetMapper<User>
    {
        public User map(int rowIndex, ResultSet rs, StatementContext ctxt) throws SQLException {
            return new User(rs.getInt(1), rs.getString(2), rs.getString(3));
        }
    }

}
