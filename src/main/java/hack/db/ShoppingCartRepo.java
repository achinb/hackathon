package hack.db;

import hack.form.ShoppingItem;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.HandleCallback;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ShoppingCartRepo {
    public List<ShoppingItem> all() {
        DBExecutionHelper executionHelper = new DBExecutionHelper();
        return ((List<ShoppingItem>)executionHelper.execute(getAllShoppingItems()));
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
