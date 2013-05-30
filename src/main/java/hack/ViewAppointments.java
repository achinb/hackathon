package hack;

import hack.db.DBExecutionHelper;
import hack.form.ShoppingItem;
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
import java.util.List;

/**
 * Date: 5/30/13
 */
public class ViewAppointments extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        DBExecutionHelper executionHelper = new DBExecutionHelper();
        List<ShoppingItem> shoppingItems = ((List<ShoppingItem>)executionHelper.execute(getAllShoppingItems()));

        for(ShoppingItem shoppingItem: shoppingItems) {
            String displayString = "User Name: %s, User email: %s, Shopping Item: %s";
            resp.getWriter().println(String.format(displayString, shoppingItem.getUserName(), shoppingItem.getUserEmail(), shoppingItem.getUrl()));
        }
    }

    private HandleCallback getAllShoppingItems() {
        return new HandleCallback<List<ShoppingItem>>() {
            @Override
            public List<ShoppingItem> withHandle(Handle handle)
                    throws Exception {
                return handle.createQuery("select u.name, u.email, sc.url from shopping_cart sc, user u where sc.user_id = u.id").map(new ShoppingItemMapper()).list();
            }
        };
    }

    static class ShoppingItemMapper implements ResultSetMapper<ShoppingItem>
    {
        public ShoppingItem map(int rowIndex, ResultSet rs, StatementContext ctxt) throws SQLException {
            return new ShoppingItem(rs.getString(1), rs.getString(2), rs.getString(3));
        }
    }

}
