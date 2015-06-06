import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by ysg01_000 on 2015-05-01.
 */

public class HomeG extends JFrame implements ActionListener {

	private JLabel lblID, lblPassword;
	private JTextField txtID;
	private JPasswordField ptxtPassword;
	private JButton btnLogin, btnSignup, btnExit;
	private String temp, sex;
	private int Cache, No, height, weight;

	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;

	public HomeG() {
		super("로그인");
		this.setLayout(null);
		this.setResizable(false);
		this.setSize(210, 230);
		this.setLocation(300, 250);
		lblID = new JLabel("아이디 : ");
		lblPassword = new JLabel("비밀번호 : ");
		lblID.setBounds(33, 20, 60, 40);
		lblPassword.setBounds(20, 55, 80, 40);

		txtID = new JTextField();
		ptxtPassword = new JPasswordField();
		txtID.setBounds(90, 30, 100, 20);
		ptxtPassword.setBounds(90, 65, 100, 20);

		btnLogin = new JButton("로그인");
		btnSignup = new JButton("회원가입");
		btnExit = new JButton("종료");
		btnLogin.setBounds(20, 100, 170, 30);
		btnSignup.setBounds(20, 150, 90, 30);
		btnExit.setBounds(120, 150, 70, 30);
		btnLogin.addActionListener(this);
		btnSignup.addActionListener(this);
		btnExit.addActionListener(this);

		add(lblID);
		add(lblPassword);
		add(txtID);
		add(ptxtPassword);
		add(btnLogin);
		add(btnSignup);
		add(btnExit);

		this.setVisible(true);
	}

	// public String getID() {
	// return txtID.getText();
	// }
	//
	// public String getPassword() {
	// String temp = new String(ptxtPassword.getPassword());
	// return temp;
	// }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLogin) {
			temp = new String(ptxtPassword.getPassword());

			if (txtID.getText().equals("")) {
				showError("아이디를 입력하세요.");
			} else if (ptxtPassword.getText().equals("")) {
				showError("비밀번호를 입력하세요.");
			} else if (LoginPlayer()) {
				if(Cache == 0){
				InputG ip = new InputG(txtID.getText(), temp, sex.toString());
				ip.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				this.dispose();
				}else{
					cacheCheck();
					MainG mg = new MainG(Integer.toString(height), Integer.toString(weight), txtID.getText());
					mg.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
					this.dispose();
				}
			} else {
				showError("아이디 또는 비밀번호가 틀렸습니다.");
				txtID.setText(null);
				ptxtPassword.setText(null);
			}
		}
		if (e.getSource() == btnSignup) {
			this.dispose();
			SignUpG sg = new SignUpG();
			sg.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
		if (e.getSource() == btnExit) {
			System.exit(0);
		}
	}

	private boolean LoginPlayer() {
		try {
			conn = DBConnManager.GetConnection();
			String sql = "SELECT * FROM `AIMember` WHERE ID=? AND Password=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, txtID.getText());
			stmt.setString(2, temp);
			
			rs = stmt.executeQuery();
			if(rs.next()){
				No = rs.getInt("No");
				Cache = rs.getInt("cache");
				return true;
			}
		}catch(Exception e){
			showError("오류!");
		} finally {
			// 리소스 반환
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			}catch (Exception e) {
				showError("오류!");
			}
		}
		return false;
	}
	
	private void cacheCheck() {
		try {
			conn = DBConnManager.GetConnection();
			String sql = "SELECT * FROM `AIMemData` WHERE No=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, No);
			rs = stmt.executeQuery();
			if(rs.next()){
				height = rs.getInt("Height");
				weight = rs.getInt("Weight");
			}
		}catch(Exception e){
			showError("오류!");
		} finally {
			// 리소스 반환
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			}catch (Exception e) {
				showError("오류!");
			}
		}
	}

	private void showError(String msg) {
		JOptionPane.showMessageDialog(this.getParent(), msg, "오류",
				JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {
		HomeG Home = new HomeG();
		Home.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
