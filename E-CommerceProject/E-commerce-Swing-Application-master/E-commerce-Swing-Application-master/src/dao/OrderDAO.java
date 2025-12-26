package dao;

import model.Order;
import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public boolean addOrder(Order order) {
        String sql = "INSERT INTO orders(user_id, product_id, card_id, quantity) VALUES(?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, order.getUser().getUserId());
            pstmt.setInt(2, order.getProduct().getProductId());
            pstmt.setInt(3, order.getCreditCard().getCardId());
            pstmt.setInt(4, order.getQuantity());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Order> getOrdersByUser(int userId) {
        String sql = "SELECT o.*, p.* FROM orders o " +
                     "JOIN products p ON o.product_id = p.product_id " +
                     "WHERE o.user_id = ? ORDER BY o.order_date DESC";
        List<Order> orders = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("product_name"),
                    rs.getString("product_color"),
                    rs.getString("category"),
                    rs.getInt("product_stock"),
                    rs.getDouble("product_price"),
                    rs.getString("description"),
                    rs.getDouble("product_weight")
                );
                product.setProductId(rs.getInt("product_id"));
                
                Order order = new Order(null, product, null, rs.getInt("quantity"));
                order.setOrderId(rs.getInt("order_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}