import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Order extends JFrame {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3303/project1";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    private static final Map<String, Integer> cart = new HashMap<>();
    private static int totalAmount = 0;
    private static JTextArea cartTextArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    // 주문 화면을 생성하고 표시하는 메소드
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("키오스크 주문창");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        
        frame.setLocationRelativeTo(null);  // 화면 가운데로 설정

        JTabbedPane tabbedPane = new JTabbedPane();

        // 각 탭에 대한 패널을 생성하여 탭에 추가
        tabbedPane.addTab("한식", createMenuPanel("1"));
        tabbedPane.addTab("일식", createMenuPanel("2"));
        tabbedPane.addTab("중식", createMenuPanel("3"));
        tabbedPane.addTab("양식", createMenuPanel("4"));
        tabbedPane.addTab("분식", createMenuPanel("5"));

        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // 장바구니 패널 추가 (탭 밑에 상시 출력)
        JPanel cartPanel = createCartPanel();
        frame.getContentPane().add(cartPanel, BorderLayout.SOUTH);

        // 홈으로 가는 버튼 추가
        JButton homeButton = new JButton("홈으로");
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Kiosk_Main.main(null);  // Kiosk_Main 클래스 실행
                frame.dispose();  // 현재 프레임 닫기
            }
        });
        frame.getContentPane().add(homeButton, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    // 메뉴 탭에 대한 패널을 생성하는 메소드
    private static JPanel createMenuPanel(String REST_ID) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4));

        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();

            String query = "SELECT menu_name, menu_price FROM menu1 WHERE REST_ID = " + REST_ID;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String menuName = resultSet.getString("menu_name");
                int menuPrice = resultSet.getInt("menu_price");

                JButton menuButton = new JButton(menuName + " | " + menuPrice + "원");
                menuButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addToCart(menuName, menuPrice);
                        updateTotalAmount();
                        updateCartTextArea();
                    }
                });

                panel.add(menuButton);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return panel;
    }

    // 장바구니 패널을 생성하는 메소드
 // 장바구니 패널을 생성하는 메소드
    private static JPanel createCartPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton clearCartButton = new JButton("장바구니 비우기");
        clearCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearCart();
                updateTotalAmount();
                updateCartTextArea();
            }
        });
        panel.add(clearCartButton, BorderLayout.NORTH);

        cartTextArea = new JTextArea();
        cartTextArea.setPreferredSize(new Dimension(200, 150));
        JScrollPane scrollPane = new JScrollPane(cartTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton paymentButton = new JButton("결제하기");
        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(panel, "총 " + totalAmount + "원이 결제되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                clearCart();
                updateTotalAmount();
                updateCartTextArea();
            }
        });
        panel.add(paymentButton, BorderLayout.SOUTH);

        return panel;
    }


    // 장바구니에 메뉴를 추가하는 메소드
    private static void addToCart(String menuName, int menuPrice) {
        if (cart.containsKey(menuName)) {
            cart.put(menuName, cart.get(menuName) + 1);
        } else {
            cart.put(menuName, 1);
        }
    }

    // 장바구니를 비우는 메소드
    private static void clearCart() {
        cart.clear();
    }

    // 총 가격을 업데이트하는 메소드
    private static void updateTotalAmount() {
        totalAmount = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String menuName = entry.getKey();
            int menuPrice = getMenuPrice(menuName);
            int quantity = entry.getValue();
            totalAmount += menuPrice * quantity;
        }
    }

    // 메뉴의 가격을 가져오는 메소드
    private static int getMenuPrice(String menuName) {
        int menuPrice = 0;

        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();

            String query = "SELECT menu_price FROM menu1 WHERE menu_name = '" + menuName + "'";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                menuPrice = resultSet.getInt("menu_price");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuPrice;
    }

    // 장바구니 텍스트 영역을 업데이트하는 메소드
    private static void updateCartTextArea() {
        cartTextArea.setText("");

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String menuName = entry.getKey();
            int quantity = entry.getValue();
            int menuPrice = getMenuPrice(menuName);

            String cartItem = menuName + " x" + quantity + " | " + (menuPrice * quantity) + "원\n";
            cartTextArea.append(cartItem);
        }

        cartTextArea.append("총 가격: " + totalAmount + "원");
    }
}
