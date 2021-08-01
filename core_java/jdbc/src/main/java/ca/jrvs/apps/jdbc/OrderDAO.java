package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataAccessObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderDAO extends DataAccessObject<Order> {
  final Logger logger = LoggerFactory.getLogger(DataAccessObject.class);

  private final String GET = "SELECT c.first_name, c.last_name, c.email, o.order_id, o.creation_date, o.total_due, o.status, " +
      "s.first_name, s.last_name, s.email, ol.quantity, p.code, p.name, p.size, p.variety, p.price FROM orders o " +
      "join customer c on o.customer_id=c.customer_id join salesperson s on o.salesperson_id = s.salesperson_id " +
      "join order_item ol on ol.order_id = o.order_id join product p on ol.product_id = p.product_id WHERE o.order_id = ?";

  public OrderDAO(Connection connection) {
    super(connection);
  }

  @Override
  public Order findById(long id) {
    Order order = new Order();
    try (PreparedStatement statement = this.connection.prepareStatement(GET);) {
      statement.setLong(1, id);
      ResultSet rs = statement.executeQuery();
      List<OrderLine> orderItems = new ArrayList<OrderLine>();
      while(rs.next()){
        order.setId(rs.getLong("order_id"));
        order.setCustomerFirstName(rs.getString(1));
        order.setCustomerLastName(rs.getString(2));
        order.setCustomerEmail(rs.getString(3));
        order.setCreationDate(rs.getTimestamp("creation_date"));
        order.setTotalDue(rs.getFloat("total_due"));
        order.setStatus(rs.getString("status"));
        order.setSalespersonFirstName(rs.getString(8));
        order.setSalespersonLastName(rs.getString(9));
        order.setSalespersonEmail(rs.getString(10));
        OrderLine orderLine = new OrderLine();
        orderLine.setProductCode(rs.getString("code"));
        orderLine.setProductName(rs.getString("name"));
        orderLine.setProductSize(rs.getInt("size"));
        orderLine.setProductVariety(rs.getString("variety"));
        orderLine.setProductPrice(rs.getFloat("price"));
        orderLine.setQuantity(rs.getInt("quantity"));
        orderItems.add(orderLine);
      }
      order.setOrderItems(orderItems);
    } catch (SQLException e) {
      logger.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
    return order;
  }

  @Override
  public List<Order> findAll() {
    return null;
  }

  @Override
  public Order update(Order dto) {
    return null;
  }

  @Override
  public Order create(Order dto) {
    return null;
  }

  @Override
  public void delete(long id) {

  }
}
