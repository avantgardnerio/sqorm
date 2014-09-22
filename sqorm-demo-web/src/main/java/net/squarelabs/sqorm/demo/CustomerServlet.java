package net.squarelabs.sqorm.demo;

import net.squarelabs.sqorm.codegen.model.Table;
import net.squarelabs.sqorm.dataset.Dataset;
import net.squarelabs.sqorm.dataset.Recordset;
import net.squarelabs.sqorm.driver.DbDriver;
import net.squarelabs.sqorm.driver.DriverFactory;
import net.squarelabs.sqorm.fluent.Query;
import net.squarelabs.sqorm.schema.DbSchema;
import net.squarelabs.sqorm.sql.QueryCache;
import org.apache.commons.dbcp.BasicDataSource;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // TODO: Connection pool, properties file, etc
            BasicDataSource pool = new BasicDataSource();
            pool.setDriverClassName("com.mysql.jdbc.Driver");
            pool.setUrl("jdbc:mysql://127.0.0.1/sqorm?allowMultiQueries=true&user=sqorm&password=sqorm");

            // TODO: Wrap this mess up in a single helper class
            try(Connection con = pool.getConnection()) {
                DbDriver driver = DriverFactory.getDriver(con);
                DbSchema db = new DbSchema(driver, "net.squarelabs.sqorm.demo");
                QueryCache cache = new QueryCache(driver);
                Map<String,Object> parms = new HashMap<>();
                String customerId = req.getParameter("customerId");
                parms.put("CustomerId", customerId);
                Dataset ds = new Dataset(db);
                ds.fill(cache, con, "GetOrdersByCustomer", parms);
                Collection<customer> customers = ds.ensureRecordset(customer.class).all();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(resp.getOutputStream(), customers);
            }
        } catch (Exception ex) {
            throw new ServletException("Error gettting customers!", ex);
        }
    }

}
