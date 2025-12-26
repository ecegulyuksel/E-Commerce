package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {
    public boolean addFavorite(int userId, int productId) {
        String sql = "INSERT INTO favorites(user_id, product_id) VALUES(?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean removeFavorite(int userId, int productId) {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND product_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Integer> getFavoriteProductIds(int userId) {
        String sql = "SELECT product_id FROM favorites WHERE user_id = ?";
        List<Integer> productIds = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                productIds.add(rs.getInt("product_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productIds;
    }
}