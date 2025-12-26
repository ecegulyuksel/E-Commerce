package dao;

import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products(product_name, product_color, category, product_stock, product_price, description) " +
                     "VALUES(?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getProductColor());
            pstmt.setString(3, product.getCategory());
            pstmt.setInt(4, product.getProductStock());
            pstmt.setDouble(5, product.getProductPrice());
            pstmt.setString(6, product.getDescription());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        product.setProductId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
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
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    public List<Product> getProductsByCategory(String category) {
        String sql = "SELECT * FROM products WHERE category = ?";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
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
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    public Product getProductById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
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
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateProductStock(int productId, int quantity) {
        String sql = "UPDATE products SET product_stock = product_stock - ? WHERE product_id = ? AND product_stock >= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}