package hack;

import de.neuland.jade4j.Jade4J;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 5/30/13
 */
public class ViewAppointments extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        DBExecutionHelper executionHelper = new DBExecutionHelper();
        List<ShoppingItem> shoppingItems = ((List<ShoppingItem>)executionHelper.execute(getAllShoppingItems()));

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("pageName", "Appointments Scheduled");
        model.put("shoppingItems", shoppingItems);
        String html =  Jade4J.render("./src/main/resources/html/index.jade", model);
        resp.getWriter().print(html);
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
