package model;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private int userId;
    private String username;
    private String name;
    private String surname;
    private Date birthDate;
    private String password;
    private String email;
    private String homeAdress;
    private String workAdress;
    private ArrayList<Product> orderedProducts;
    private ArrayList<Product> favorites;
    private ArrayList<CreditCard> creditCards;

    public User(String username, String name, String surname, Date birthDate, String password, String email, String homeAdress, String workAdress) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.password = password;
        this.email = email;
        this.homeAdress = homeAdress;
        this.workAdress = workAdress;
        this.orderedProducts = new ArrayList<>();
        this.favorites = new ArrayList<>();
        this.creditCards = new ArrayList<>();
    }

    public User() {
        orderedProducts = new ArrayList<>();
        favorites = new ArrayList<>();
        creditCards = new ArrayList<>();
    }

    public void addFavoriteProduct(Product product) {
        if (!favorites.contains(product)) {
            favorites.add(product);
        }
    }

    public void orderProduct(Product product, int quantity) {
        for (int i = 0; i < quantity; i++) {
            orderedProducts.add(product);
        }
    }

    public void addCreditCard(CreditCard card) {
        creditCards.add(card);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomeAdress() {
        return homeAdress;
    }

    public void setHomeAdress(String homeAdress) {
        this.homeAdress = homeAdress;
    }

    public String getWorkAdress() {
        return workAdress;
    }

    public void setWorkAdress(String workAdress) {
        this.workAdress = workAdress;
    }

    public ArrayList<Product> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(ArrayList<Product> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public ArrayList<Product> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<Product> favorites) {
        this.favorites = favorites;
    }

    public ArrayList<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(ArrayList<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }
}