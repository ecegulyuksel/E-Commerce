package model;

import java.util.Date;

public class Order {
    private int orderId;
    private User user;
    private Product product;
    private CreditCard creditCard;
    private int quantity;
    private Date orderDate;

    public Order(User user, Product product, CreditCard creditCard, int quantity) {
        this.user = user;
        this.product = product;
        this.creditCard = creditCard;
        this.quantity = quantity;
        this.orderDate = new Date();
    }

    public boolean processOrder() {
        if (creditCard.getExpDate() == null || creditCard.getExpDate().before(new Date())) {
            System.out.println("Order failed: Credit card is invalid or expired.");
            return false;
        }
        if (product.reduceStock(quantity)) {
            user.orderProduct(product, quantity);
            System.out.println("The order was created successfully.");
            return true;
        } else {
            System.out.println("Order failed: Insufficient stock.");
            return false;
        }
    }
    

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}