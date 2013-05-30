package hack;

import hack.db.DBExecutionHelper;
import hack.form.User;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.tweak.HandleCallback;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Date: 5/30/13
 */
public class ViewAppointments extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        DBExecutionHelper executionHelper = new DBExecutionHelper();
//        ShoppingItem aUser = ((User)executionHelper.execute(getAllShoppingItems()));
//        if(aUser == null) {
//            Integer retVal = (Integer)executionHelper.execute(putUser(email, userName));
//            aUser = ((User)executionHelper.execute(getUser(email)));
//        }
//        return aUser;
        resp.getWriter().print("appointmenting!\n");
    }

    private HandleCallback getAllShoppingItems() {

        return new HandleCallback<User>() {
            @Override
            public User withHandle(Handle handle)
                    throws Exception {
                return null;//handle.createQuery("select * from user where email = '" + email + "'").map(new UserMapper()).first();

            }
        };
    }
}
