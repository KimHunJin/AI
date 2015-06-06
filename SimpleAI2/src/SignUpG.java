import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by ysg01_000 on 2015-05-01.
 */
//DB명 : AImember/AIMemData
public class SignUpG extends JFrame implements ActionListener {

    private JLabel lblName, lblID, lblPassword, lblPassword2, lblsex;
    private JTextField txtName, txtID;
    private JPasswordField ptxtPassword, ptxtPassword2;
    private JRadioButton rbtnman, rbtnwoman;
    private ButtonGroup btngGroup;
    private JButton btnOK, btnExit;
    private String temp;

	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
    
    public SignUpG() {
        super("회원가입");
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(245, 270);
        this.setLocation(300, 250);

        lblName = new JLabel("이름 : ");
        lblID = new JLabel("아이디 : ");
        lblPassword = new JLabel("비밀번호 : ");
        lblPassword2 = new JLabel("비밀번호 확인 : ");
        lblsex = new JLabel("성별 : ");
        lblName.setBounds(65, 20, 60, 40);
        lblID.setBounds(52, 50, 80, 40);
        lblPassword.setBounds(39, 80, 80, 40);
        lblPassword2.setBounds(10, 110, 100, 40);
        lblsex.setBounds(60, 140, 60, 40);

        txtName = new JTextField();
        txtID = new JTextField();
        ptxtPassword = new JPasswordField();
        ptxtPassword2 = new JPasswordField();
        txtName.setBounds(110, 30, 100, 20);
        txtID.setBounds(110, 60, 100, 20);
        ptxtPassword.setBounds(110, 90, 100, 20);
        ptxtPassword2.setBounds(110, 120, 100, 20);

        rbtnman = new JRadioButton("남");
        rbtnwoman = new JRadioButton("여");
        rbtnman.setBounds(105, 150, 40, 20);
        rbtnwoman.setBounds(150, 150, 40, 20);

        btngGroup = new ButtonGroup();
        btngGroup.add(rbtnman);
        btngGroup.add(rbtnwoman);

        btnOK = new JButton("확인");
        btnExit = new JButton("취소");
        btnOK.setBounds(30, 190, 75, 30);
        btnExit.setBounds(135, 190, 75, 30);
        btnOK.addActionListener(this);
        btnExit.addActionListener(this);

        add(lblName);
        add(lblID);
        add(lblPassword);
        add(lblPassword2);
        add(lblsex);
        add(txtName);
        add(txtID);
        add(ptxtPassword);
        add(ptxtPassword2);
        add(rbtnman);
        add(rbtnwoman);
        add(btnOK);
        add(btnExit);

        this.setVisible(true);
    }
    private boolean initPlayer() {
		try {
			conn = DBConnManager.GetConnection();
			String sql = "SELECT * FROM `AIMember` WHERE ID=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, txtID.getText());
			rs = stmt.executeQuery();
			if(rs.next()){
				return false;
			}//아이디 중복확인
			
			String sex;
			if(rbtnman.isSelected()){
				sex = "man";
			}else {
				sex = "woman";
			}
			sql = "INSERT INTO `AIMember` (`No`, `Name`, `ID`,`Password`, `sex`, `cache`) VALUES (NULL, ?, ?, ?, ?, ?);";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, txtName.getText());
			stmt.setString(2, txtID.getText());
			stmt.setString(3, temp);
			stmt.setString(4, sex);
			stmt.setInt(5, 0);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			showError("오류!");
		} finally {
			// 리소스 반환
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
				showError("오류!");
			}
		}
		return true;
	}

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnOK) {
                temp = new String(ptxtPassword.getPassword());
                String temp2 = new String(ptxtPassword2.getPassword());
                
                if(txtName.getText().equals("")){
               	 	showError("이름을 입력하세요.");
                }else if(txtID.getText().equals("")) {
                    showError("아이디를 입력하세요.");
                }else if(ptxtPassword.getText().equals("")) {
                    showError("비밀번호를 입력하세요.");
                }else if(ptxtPassword2.getText().equals("")){
                	showError("비밀번호를 재입력하세요.");
                }else if(!rbtnman.isSelected() && !rbtnwoman.isSelected()){
                	showError("성별을 체크하세요.");
                }else if(!temp.equals(temp2)){
                	showError("비밀번호가 서로 다릅니다.");
                }else{
                	if(initPlayer()){
                		JOptionPane.showMessageDialog(this.getParent(), "회원가입이 정상적으로 되었습니다.");
                		this.dispose();
                		new HomeG();
                	}else {
                		showError("아이디가 중복되었습니다.");
                	}
                }
            }
        
        if (e.getSource() == btnExit) {
            this.dispose();
            new HomeG();
        }
    }
    
    private void showError(String msg) {
		JOptionPane.showMessageDialog(this.getParent(), msg, "오류",
				JOptionPane.ERROR_MESSAGE);
    }
}
