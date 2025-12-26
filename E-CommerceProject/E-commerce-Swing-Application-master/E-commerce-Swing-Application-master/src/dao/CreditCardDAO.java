package dao;

import model.CreditCard;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreditCardDAO {
    public boolean addCreditCard(CreditCard card) {
        String sql = "INSERT INTO credit_cards(card_number, user_id, security_code, exp_date) " +
                     "VALUES(?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, card.getCardNumber());
            pstmt.setInt(2, card.getUser().getUserId());
            pstmt.setString(3, card.getSecurityCode());
            pstmt.setDate(4, new java.sql.Date(card.getExpDate().getTime()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        card.setCardId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<CreditCard> getCreditCardsByUser(int userId) {
        String sql = "SELECT * FROM credit_cards WHERE user_id = ?";
        List<CreditCard> cards = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(userId);
                
                CreditCard card = new CreditCard(
                    rs.getString("card_number"),
                    user,
                    rs.getString("security_code"),
                    rs.getDate("exp_date")
                );
                card.setCardId(rs.getInt("card_id"));
                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }
    
    public CreditCard getCreditCardById(int cardId) {
        String sql = "SELECT * FROM credit_cards WHERE card_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cardId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                
                CreditCard card = new CreditCard(
                    rs.getString("card_number"),
                    user,
                    rs.getString("security_code"),
                    rs.getDate("exp_date")
                );
                card.setCardId(rs.getInt("card_id"));
                return card;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}