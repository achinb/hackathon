package hack;

import de.neuland.jade4j.Jade4J;
import hack.db.ShoppingCartRepo;
import hack.form.ShoppingItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        ShoppingCartRepo shoppingCartRepo = new ShoppingCartRepo();
        List<ShoppingItem> shoppingItems = shoppingCartRepo.all();

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("pageName", "Appointments Scheduled");
        model.put("shoppingItems", shoppingItems);
        String html =  Jade4J.render("./src/main/resources/html/index.jade", model);
        resp.getWriter().print(html);
    }


}
