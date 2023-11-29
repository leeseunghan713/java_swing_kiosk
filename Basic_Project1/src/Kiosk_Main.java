import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Main extends JFrame implements ActionListener {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JPanel firstPanel;
    private JPanel secondPanel;
    private JPanel imagePanel;
    private JButton clickButton;

    public Main() {
        setTitle("키오스크 초기화면");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // 첫번째 패널
        firstPanel = new JPanel();
        firstPanel.setLayout(new GridLayout(4, 1)); // 3x1 그리드 설정

        textField1 = new JTextField("선문");
        textField1.setHorizontalAlignment(JTextField.CENTER);
        textField1.setEditable(false); // 수정 불가능하도록 설정
        firstPanel.add(textField1);

        textField2 = new JTextField("학생");
        textField2.setHorizontalAlignment(JTextField.CENTER);
        textField2.setEditable(false); // 수정 불가능하도록 설정
        firstPanel.add(textField2);

        textField3 = new JTextField("식당");
        textField3.setHorizontalAlignment(JTextField.CENTER);
        textField3.setEditable(false); // 수정 불가능하도록 설정
        firstPanel.add(textField3);

        textField4 = new JTextField("키오스크");
        textField4.setHorizontalAlignment(JTextField.CENTER);
        textField4.setEditable(false); // 수정 불가능하도록 설정
        firstPanel.add(textField4);

        container.add(firstPanel, BorderLayout.NORTH);

        // 두번째 패널 (이미지 랜덤으로 출력되는 패널)
        JPanel secondPanel = new JPanel();
        secondPanel.setPreferredSize(new Dimension(300, 300)); // 크기 설정
        secondPanel.setLayout(new GridBagLayout()); // GridBagLayout 설정

        imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);

        // 랜덤으로 이미지 파일 선택
        int randomImageIndex = (int) (Math.random() * 6) + 1;
        String imageName = randomImageIndex + ".jpg";

        // 이미지 파일 로드
        ImageIcon imageIcon = new ImageIcon(imageName);
        JLabel imageLabel = new JLabel(imageIcon);
        imagePanel.add(imageLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.CENTER;
        secondPanel.add(imagePanel, gbc);

        container.add(secondPanel, BorderLayout.CENTER);


        // 세번째 패널 (버튼 클릭시 Order 클래스가 실행되는 패널)
        JPanel thirdPanel = new JPanel();

        clickButton = new JButton("주문 하기");
        clickButton.addActionListener(this);
   
        thirdPanel.add(clickButton);

        container.add(thirdPanel, BorderLayout.SOUTH);

        setVisible(true);
        
    }
    // 세번째 패널에 버튼 클릭시 실행되는 코드
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clickButton) {
        	Order orderFrame = new Order();
        	orderFrame.main(null); // Order 클래스 실행
            dispose(); // 현재 프레임 닫기
        }
    }


}

public class Kiosk_Main {
    public static void main(String[] args) {
        new Main();
    }
}
