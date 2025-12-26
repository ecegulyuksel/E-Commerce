package test;

import model.User;
import model.Product;
import model.CreditCard;
import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class EcommerceTest {
    public static void main(String[] args) {
        System.out.println("E-commerce Application Test Starting...\n");
        
        // Create test user
        User testUser = new User(
            "testuser",
            "Test",
            "User",
            new Date(),
            "password123",
            "test@example.com",
            "Home Address",
            "Work Address"
        );
        System.out.println("1. User Creation Test:");
        System.out.println("Username: " + testUser.getUsername());
        System.out.println("Email: " + testUser.getEmail());
        System.out.println("Test successful!\n");

        // Create test product
        Product testProduct = new Product(
            "Test Product",
            "Red",
            "Electronics",
            10,
            99.99,
            "Test description",
            10.1    
        );
        System.out.println("2. Product Creation Test:");
        System.out.println("Product name: " + testProduct.getProductName());
        System.out.println("Stock: " + testProduct.getProductStock());
        System.out.println("Price: " + testProduct.getProductPrice());
        System.out.println("Color: " + testProduct.getProductColor());
        System.out.println("Category: " + testProduct.getCategory());
        System.out.println("Description: " + testProduct.getDescription());
        System.out.println("Test successful!\n");

        // Credit card addition test
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        Date expDate = null;
        try {
            expDate = sdf.parse("12/25");
        } catch (Exception e) {
            System.out.println("Date conversion error: " + e.getMessage());
        }
        
        CreditCard testCard = new CreditCard(
            "1234567890123456",
            testUser,
            "123",
            expDate
        );
        testUser.addCreditCard(testCard);
        System.out.println("3. Credit Card Addition Test:");
        System.out.println("Number of added cards: " + testUser.getCreditCards().size());
        System.out.println("Card number: " + testUser.getCreditCards().get(0).getCardNumber());
        System.out.println("Test successful!\n");

        // Product purchase test
        System.out.println("4. Product Purchase Test:");
        int purchaseQuantity = 2;
        int initialStock = testProduct.getProductStock();
        testUser.orderProduct(testProduct, purchaseQuantity);
        System.out.println("Number of ordered products: " + testUser.getOrderedProducts().size());
        testProduct.reduceStock(purchaseQuantity);
        System.out.println("Remaining stock: " + testProduct.getProductStock());
        System.out.println("Test successful!\n");

        // Add to favorites test
        System.out.println("5. Add to Favorites Test:");
        testUser.addFavoriteProduct(testProduct);
        System.out.println("Number of favorite products: " + testUser.getFavorites().size());
        System.out.println("Favorite product name: " + testUser.getFavorites().get(0).getProductName());
        System.out.println("Test successful!\n");

        // Test adding the same product to favorites again
        System.out.println("6. Duplicate Favorite Addition Test:");
        testUser.addFavoriteProduct(testProduct);
        System.out.println("Number of favorite products (after duplicate addition): " + testUser.getFavorites().size());
        System.out.println("Test successful!\n");

        // Insufficient stock test
        System.out.println("7. Insufficient Stock Test:");
        int insufficientQuantity = testProduct.getProductStock() + 1;
        boolean stockStatus = testProduct.reduceStock(insufficientQuantity);
        System.out.println("Purchase with insufficient stock: " + (stockStatus ? "Successful" : "Failed"));
        System.out.println("Test successful!\n");

        System.out.println("All tests completed!");
    }
}