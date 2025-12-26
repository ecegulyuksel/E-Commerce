package model;

import java.util.Date;
import model.User;
import java.text.SimpleDateFormat;

public class CreditCard {
    private int cardId;
    private String cardNumber;
    private User user;
    private String securityCode;
    private Date expDate;

    public CreditCard(String cardNumber, User user, String securityCode, Date expDate) {
        setCardNumber(cardNumber);
        this.user = user;
        setSecurityCode(securityCode);
        this.expDate = expDate;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        if (cardNumber != null && cardNumber.matches("\\d{16}")) {
            this.cardNumber = cardNumber;
        } else {
            System.out.println("Error: Credit card number must be a 16-digit number..");
            this.cardNumber = null;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        if (securityCode != null && securityCode.matches("\\d{3}")) {
            this.securityCode = securityCode;
        } else {
            System.out.println("Error: Security code must be a 3-digit number.");
            this.securityCode = null;
        }
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }
    
    @Override
public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
    return "Card: **** **** **** " + cardNumber.substring(cardNumber.length() - 4)
         + " | Expiration: " + sdf.format(expDate);
}
}