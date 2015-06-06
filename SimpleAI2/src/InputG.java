import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ysg01_000 on 2015-05-01.
 */
public class InputG extends JFrame implements ActionListener {

	private JLabel lblheight, lblweight;
	private JTextField txtheight, txtweight;
	private JButton btnOK;
	private String ID, Password, sex;

	public InputG(final String ID, final String Password, final String sex) {
		super("입력");
		this.ID = ID;
		this.Password = Password;
		this.sex = sex;
		this.setLayout(null);
		this.setResizable(false);
		this.setSize(210, 170);
		this.setLocation(300, 250);

		lblheight = new JLabel("키 : ");
		lblweight = new JLabel("몸무게 : ");
		lblheight.setBounds(46, 20, 60, 40);
		lblweight.setBounds(20, 55, 80, 40);

		txtheight = new JTextField();
		txtweight = new JTextField();
		txtheight.setBounds(80, 30, 100, 20);
		txtweight.setBounds(80, 65, 100, 20);

		btnOK = new JButton("확인");
		btnOK.setBounds(65, 100, 75, 30);
		btnOK.addActionListener(this);

		add(lblheight);
		add(lblweight);
		add(txtheight);
		add(txtweight);
		add(btnOK);

		this.setVisible(true);
	}

	public String getheight() {
		return txtheight.getText();
	}

	public String getweight() {
		return txtweight.getText();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOK) {
			try {
				if (txtheight.getText().equals("")) {
					showError("키를 입력하세요.");
				} else if (txtweight.getText().equals("")) {
					showError("몸무게를 입력하세요.");
				} else if (Integer.parseInt(txtheight.getText()) >= 250
						|| Integer.parseInt(txtheight.getText()) < 50) {
					showError("키를 제대로 입력하세요.");
					txtheight.setText("");
				} else if (Integer.parseInt(txtweight.getText()) >= 250
						|| Integer.parseInt(txtweight.getText()) < 20) {
					showError("몸무게를 제대로 입력하세요.");
					txtweight.setText("");
				} else {
					MainG mg = new MainG(getheight(), getweight(), ID);
					mg.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
					this.dispose();
				}
			} catch (NumberFormatException ne) {
				showError("키 또는 몸무게는 숫자로만 입력하세요.");
				txtheight.setText(null);
				txtweight.setText(null);
			} catch (Exception e1) {
				showError("오류!");
			}
		}
	}

	private void showError(String msg) {
		JOptionPane.showMessageDialog(this.getParent(), msg, "오류",
				JOptionPane.ERROR_MESSAGE);
	}
}
