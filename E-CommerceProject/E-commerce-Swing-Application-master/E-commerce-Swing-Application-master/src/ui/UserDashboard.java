package ui;

import dao.*;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Date;

public class UserDashboard extends JFrame {
    private String username;
    private User currentUser;
    private JTabbedPane tabbedPane;
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private CreditCardDAO creditCardDAO = new CreditCardDAO();
    private FavoriteDAO favoriteDAO = new FavoriteDAO();
    
    public UserDashboard(String username) {
        this.username = username;
        this.currentUser = new UserDAO().getUserByUsername(username);
        
        setTitle("User Panel - " + username);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Menü çubuğu
        JMenuBar menuBar = new JMenuBar();
        JMenu userMenu = new JMenu("Account");
        JMenuItem logoutItem = new JMenuItem("Log off");
        logoutItem.addActionListener(e -> logout());
        userMenu.add(logoutItem);
        menuBar.add(userMenu);
        setJMenuBar(menuBar);
        
        tabbedPane = new JTabbedPane();
        
        // Ürünler sekmesi
        JPanel productsPanel = createProductsPanel();
        tabbedPane.addTab("Products", productsPanel);
        
        // Favoriler sekmesi
        JPanel favoritesPanel = createFavoritesPanel();
        tabbedPane.addTab("Favourites", favoritesPanel);
        
        // Alışveriş Sepeti sekmesi
        //JPanel cartPanel = createCartPanel();
        //tabbedPane.addTab("Cart", cartPanel);
        
        // Siparişler sekmesi
        JPanel ordersPanel = createOrdersPanel();
        tabbedPane.addTab("Orders", ordersPanel);
        
        // Kredi kartları sekmesi
        JPanel cardsPanel = createCreditCardsPanel();
        tabbedPane.addTab("Credit Cards", cardsPanel);
        
        add(tabbedPane);
    }
    
    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Filtreleme
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{"All", "Electronics", "Clothing", "Books", "Sports", "Home"});
        JButton filterButton = new JButton("Filter");
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryCombo);
        filterPanel.add(filterButton);
        panel.add(filterPanel, BorderLayout.NORTH);
        
        // Ürün listesi
        DefaultListModel<Product> productListModel = new DefaultListModel<>();
        JList<Product> productList = new JList<>(productListModel);
        productList.setFixedCellHeight(60);
        productList.setCellRenderer(new ProductListRenderer());
        JScrollPane scrollPane = new JScrollPane(productList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton orderButton = new JButton("Order");
        JButton addFavoriteButton = new JButton("Add to favourites");
        JButton logoutButton = new JButton("Log off");
        
        buttonPanel.add(orderButton);
        buttonPanel.add(addFavoriteButton);
        buttonPanel.add(logoutButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Filtreleme işlevi
        filterButton.addActionListener(e -> {
            String category = (String) categoryCombo.getSelectedItem();
            List<Product> products = category.equals("All") ? 
                productDAO.getAllProducts() : productDAO.getProductsByCategory(category);
            
            productListModel.clear();
            products.forEach(productListModel::addElement);
        });
        
        // Sipariş işlevi
        orderButton.addActionListener(e -> {
            Product selected = productList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a product");
                return;
            }
            
            // Kredi kartı seçimi
            List<CreditCard> cards = creditCardDAO.getCreditCardsByUser(currentUser.getUserId());
            if (cards.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must add a credit card before ordering.");
                return;
            }
            
            CreditCard selectedCard = (CreditCard) JOptionPane.showInputDialog(
                this, "Select Credit Card:", "Card Selection",
                JOptionPane.QUESTION_MESSAGE, null, 
                cards.toArray(), cards.get(0));
            
            if (selectedCard != null) {
                Order order = new Order(currentUser, selected, selectedCard, 1);
                if (orderDAO.addOrder(order) && productDAO.updateProductStock(selected.getProductId(), 1)) {
                    JOptionPane.showMessageDialog(this, "Order created successfully.");
                    filterButton.doClick(); // Listeyi yenile
                } else {
                    JOptionPane.showMessageDialog(this, "Order could not be created.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Favori ekleme butonu
        addFavoriteButton.addActionListener(e -> {
         Product selected = productList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a product.");
        return;
        }
    
        if (favoriteDAO.addFavorite(currentUser.getUserId(), selected.getProductId())) {
            JOptionPane.showMessageDialog(this, "Product added to favorites.");
            refreshFavoritesTab(); // Favoriler sekmesini yenile
        } else {
            JOptionPane.showMessageDialog(this, "This product is already in your favorites!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        });
        
        // Çıkış işlevi
        logoutButton.addActionListener(e -> logout());
        
        // Başlangıçta ürünleri yükle
        filterButton.doClick();
        
        return panel;
    }
    
    private JPanel createFavoritesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<Product> model = new DefaultListModel<>();
        JList<Product> favoritesList = new JList<>(model);
        favoritesList.setCellRenderer(new ProductListRenderer());
        
        // Favorileri yükle
        favoriteDAO.getFavoriteProductIds(currentUser.getUserId()).forEach(id -> {
            Product p = productDAO.getProductById(id);
            if (p != null) model.addElement(p);
        });
        
        JScrollPane scrollPane = new JScrollPane(favoritesList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton removeButton = new JButton("Remove from Favorites");
        JButton orderButton = new JButton("Order");
        
        removeButton.addActionListener(e -> {
            Product selected = favoritesList.getSelectedValue();
            if (selected != null && favoriteDAO.removeFavorite(currentUser.getUserId(), selected.getProductId())) {
                model.removeElement(selected);
                JOptionPane.showMessageDialog(this, "Product removed from favorites");
            }
        });
        
        orderButton.addActionListener(e -> {
            Product selected = favoritesList.getSelectedValue();
            if (selected != null) {
                List<CreditCard> cards = creditCardDAO.getCreditCardsByUser(currentUser.getUserId());
                if (cards.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "You must add a credit card before ordering.");
                    return;
                }
                
                CreditCard selectedCard = (CreditCard) JOptionPane.showInputDialog(
                    this, "Select Credit Card:", "Kart Seçimi",
                    JOptionPane.QUESTION_MESSAGE, null, 
                    cards.toArray(), cards.get(0));
                
                if (selectedCard != null) {
                    Order order = new Order(currentUser, selected, selectedCard, 1);
                    if (orderDAO.addOrder(order) && productDAO.updateProductStock(selected.getProductId(), 1)) {
                        JOptionPane.showMessageDialog(this, "Order created successfully");
                        // Favorileri yenile
                        model.clear();
                        favoriteDAO.getFavoriteProductIds(currentUser.getUserId()).forEach(id -> {
                            Product p = productDAO.getProductById(id);
                            if (p != null) model.addElement(p);
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "Order could not be created", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        buttonPanel.add(removeButton);
        buttonPanel.add(orderButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> ordersList = new JList<>(model);
        
        // Siparişleri yükle
        orderDAO.getOrdersByUser(currentUser.getUserId()).forEach(order -> {
            model.addElement(String.format("Order #%d - %s - %s", 
                order.getOrderId(), 
                order.getProduct().getProductName(),
                order.getOrderDate()));
        });
        
        JScrollPane scrollPane = new JScrollPane(ordersList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCreditCardsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<CreditCard> model = new DefaultListModel<>();
        JList<CreditCard> cardsList = new JList<>(model);
        
        // Kartları yükle
        creditCardDAO.getCreditCardsByUser(currentUser.getUserId()).forEach(model::addElement);
        
        cardsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                         boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof CreditCard) {
                    CreditCard card = (CreditCard) value;
                    setText(String.format("•••• •••• •••• %s - %s", 
                        card.getCardNumber().substring(12),
                        card.getExpDate()));
                }
                return this;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(cardsList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Credit Card");
        
        addButton.addActionListener(e -> {
            JTextField cardNumberField = new JTextField(16);
            JTextField securityCodeField = new JTextField(3);
            JTextField expMonthField = new JTextField(2);
            JTextField expYearField = new JTextField(4);
            
            JPanel inputPanel = new JPanel(new GridLayout(4, 2));
            inputPanel.add(new JLabel("Credit Card Number:"));
            inputPanel.add(cardNumberField);
            inputPanel.add(new JLabel("Security Code:"));
            inputPanel.add(securityCodeField);
            inputPanel.add(new JLabel("Expiration Month:"));
            inputPanel.add(expMonthField);
            inputPanel.add(new JLabel("Expiration Year:"));
            inputPanel.add(expYearField);
            
            int result = JOptionPane.showConfirmDialog(
                this, inputPanel, "New Credit Card", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String cardNumber = cardNumberField.getText().replaceAll("\\s+", "");
                    String securityCode = securityCodeField.getText();
                    int month = Integer.parseInt(expMonthField.getText());
                    int year = Integer.parseInt(expYearField.getText());
                    
                    if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
                        JOptionPane.showMessageDialog(this, "Invalid card number", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (securityCode.length() != 3 || !securityCode.matches("\\d+")) {
                        JOptionPane.showMessageDialog(this, "Invalid security code", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    @SuppressWarnings("deprecation")
                    Date expDate = new Date(year - 1900, month - 1, 1);
                    
                    CreditCard card = new CreditCard(cardNumber, currentUser, securityCode, expDate);
                    if (creditCardDAO.addCreditCard(card)) {
                        model.addElement(card);
                        JOptionPane.showMessageDialog(this, "Card added successfully");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        buttonPanel.add(addButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this, "Are you sure you want to log out?", 
            "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
    
    private class ProductListRenderer extends JPanel implements ListCellRenderer<Product> {
    private JLabel nameLabel;
    private JLabel detailsLabel;
    private JLabel priceLabel;
    private JLabel stockLabel;
    private JPanel infoPanel;
    
    public ProductListRenderer() {
        setLayout(new BorderLayout(10, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Create components
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        detailsLabel = new JLabel();
        detailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        detailsLabel.setForeground(Color.GRAY);
        
        priceLabel = new JLabel();
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0, 100, 0));
        
        stockLabel = new JLabel();
        stockLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        // Info panel for left-aligned items
        infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(nameLabel);
        infoPanel.add(detailsLabel);
        
        // Layout
        add(infoPanel, BorderLayout.WEST);
        add(priceLabel, BorderLayout.CENTER);
        add(stockLabel, BorderLayout.EAST);
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends Product> list, Product product, 
                                                int index, boolean isSelected, boolean cellHasFocus) {
        // Set values
        nameLabel.setText(product.getProductName());
        detailsLabel.setText(product.getCategory());
        priceLabel.setText(String.format("₺%.2f", product.getProductPrice()));
        
        // Stock status with color coding
        if (product.getProductStock() > 10) {
            stockLabel.setText("In Stock");
            stockLabel.setForeground(new Color(0, 120, 0));
        } else if (product.getProductStock() > 0) {
            stockLabel.setText("Low Stock (" + product.getProductStock() + ")");
            stockLabel.setForeground(new Color(200, 120, 0));
        } else {
            stockLabel.setText("Out of Stock");
            stockLabel.setForeground(Color.RED);
        }
        
        // Selection colors
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            nameLabel.setForeground(list.getSelectionForeground());
            priceLabel.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            nameLabel.setForeground(list.getForeground());
            priceLabel.setForeground(new Color(0, 100, 0));
        }
        
        // Add some visual padding and border
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        return this;
    }
}
    private void refreshFavoritesTab() {
    JPanel favoritesPanel = createFavoritesPanel();
    tabbedPane.remove(1); // Favoriler sekmesinin indeksi
    tabbedPane.insertTab("Favourites", null, favoritesPanel, null, 1);
    tabbedPane.setSelectedIndex(1); // Kullanıcıyı favoriler sekmesinde tut
}
    
       
}