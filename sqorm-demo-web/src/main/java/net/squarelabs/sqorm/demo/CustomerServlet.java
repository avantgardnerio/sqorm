package net.squarelabs.sqorm.demo;

import net.squarelabs.sqorm.dataset.Dataset;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerServlet extends HttpServlet {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // TODO: Wrap this mess up in a single helper class
            try(Connection con = AppContext.getPool().getConnection()) {
                Dataset ds = new Dataset(AppContext.getSchema());

                Map<String,Object> parms = new HashMap<>();
                int customerId = Integer.parseInt(req.getParameter("customerId"));
                parms.put("CustomerId", customerId);
                ds.fill(AppContext.getQueryCache(), con, "GetOrdersByCustomer", parms);

                customer customer = ds.from(customer.class).top();
                mapper.writeValue(resp.getOutputStream(), customer);
            }
        } catch (Exception ex) {
            throw new ServletException("Error gettting customers!", ex);
        }
    }

}
