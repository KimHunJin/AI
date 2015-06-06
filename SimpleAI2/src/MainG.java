import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ysg01_000 on 2015-05-01.
 */
public class MainG extends JFrame implements ActionListener, KeyListener {

    private TextArea txtDisplay;
    private JLabel lblFat, lblStandardWeight, lblBMI;
    private JLabel lblFatS, lblStandardWeightS, lblBMIS;
    private JTextField txtsand;
    private JButton btnInputH, btnInputW, btnExit;
    private Font f = new Font("함초롬바탕", Font.BOLD, 13);
    private int H = 0, W = 0, No, Cache;
    private String height, weight, ID, Name, Sex;
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
    
	private String[] match = {"안녕|반갑|하이|반가", "시러|아니|안괞|싫어|안좋", "응|그래|괜찮|좋아|어",  "고마워|감사|고맙|땡큐"};
	private String[] hello = {"안녕하세요. 몸은 괜찮으신가요?", "반갑습니다. 몸은 어떠신가요?"};  // 안녕|반갑|하이
	private String[][] no = {{"병원에 가보는게 어떠세요?", "병원에는 가보셨나요?"}, {"다이어트 중이신가요?", "살 빼는 중이신가요?"},
            {"병원 부터 가세요1!", "병원을 가셔야죠! (｀Д´)"}, {"밥부터 드세요!", "밥은 거르면 안되요! (｀Д´)"}};  // 아니|안괜
	private String[][] ok = {{"다행이네요. 밥은 드셨나요?", "괜찮으셔서 다행이에요. 밥은 드셨나요?"},
            {"무엇을 드셨나요?", "무엇을 먹으셨나요?"}, {"힘내세요!", "화이팅! o(-\"-)o" },
            {"어떤 운동을 하실건가요?", "어떤 운동을 원하시나요?"}};  // 괜찮
	private String[] thank = {"별 말씀을..", "어멋 > <", "제가 해야할 일입니다. *^ㅡ^*", "아니에요. ☜(^^*)☞"};  // 고마워|감사|고맙
	private String[] dont = {"잘 모르겠네요.", "모르는 단어입니다.", "모르겠어요.", "뭐에요?", "뭐야?"};

    static int num_no = -1;
    static int num_ok = -1;
    static boolean no_action = false;


    public MainG(final String height, final String weight, final String ID) {
        super("건강도우미");
        this.height = height;
        this.weight = weight;
        this.ID = ID;

        this.setLayout(null);
        this.setResizable(false);
        this.setSize(470, 400);
        this.setLocation(300, 250);

        txtDisplay = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
        txtDisplay.setBounds(20, 20, 300, 300);
        txtDisplay.setEditable(false);

        lblFat = new JLabel("< 체지방량 >");
        lblStandardWeight = new JLabel("< 표준체중 >");
        lblBMI = new JLabel("< B M I >");
        lblFat.setFont(f);
        lblStandardWeight.setFont(f);
        lblBMI.setFont(f);
        lblFat.setBounds(340, 20, 100, 30);
        lblStandardWeight.setBounds(340, 90, 100, 30);
        lblBMI.setBounds(340, 160, 100, 30);

        lblFatS = new JLabel("사용자 체지방량");
        lblStandardWeightS = new JLabel("사용자 표준체중");
        lblBMIS = new JLabel("사용자 BMI");
        lblFatS.setFont(f);
        lblStandardWeightS.setFont(f);
        lblBMIS.setFont(f);
        lblFatS.setBounds(340, 50, 100, 30);
        lblStandardWeightS.setBounds(340, 120, 100, 30);
        lblBMIS.setBounds(340, 190, 100, 30);

        txtsand = new JTextField();
        txtsand.setBounds(20, 330, 300, 20);
        txtsand.addKeyListener(this);

        btnInputH = new JButton("키 설정");
        btnInputW = new JButton("체중 설정");
        btnExit = new JButton("종료");
        btnInputH.setBounds(340, 240, 100, 30);
        btnInputW.setBounds(340, 280, 100, 30);
        btnExit.setBounds(340, 320, 100, 30);
        btnInputH.addActionListener(this);
        btnInputW.addActionListener(this);
        btnExit.addActionListener(this);

        add(txtDisplay); add(lblFat); add(lblStandardWeight);
        add(lblBMI); add(lblFatS); add(lblStandardWeightS);
        add(lblBMIS); add(txtsand); add(btnInputH);
        add(btnInputW); add(btnExit);
        this.setVisible(true);

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            	try { //회원정보를 가져온다.
        			conn = DBConnManager.GetConnection();
        			String sql = "SELECT * FROM `AIMember` WHERE ID=?";
        			stmt = conn.prepareStatement(sql);
        			stmt.setString(1, ID);
        			rs = stmt.executeQuery();
        			while(rs.next()) {
        				No = rs.getInt("No"); //넘버
        				Name = rs.getString("Name"); //이름
        				Sex = rs.getString("sex"); //성별
        				Cache = rs.getInt("cache"); //캐시
        			}
            	}catch(Exception ea){
            		showError("오류!!!");
            	}
            	
                measure(height, weight);
                txtDisplay.append("건강 도우미 : 안녕하세요. 건강도우미 입니다. \r\n 당신은 현재 " + lblStandardWeightS.getText() + " 입니다.\r\n\n");
            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    public void measure(String height, String weight) {
        DecimalFormat bmi_format = new DecimalFormat();
        bmi_format.applyLocalizedPattern("0.##");
        lblBMIS.setText(bmi_format.format(measure_bmi(height, weight)));
        lblFatS.setText(bmi_format.format(measure_fat(measure_bmi(height, weight), weight)) + "kg");
        lblStandardWeightS.setText(standard(measure_bmi(height, weight)));
        MemData();
    }

    public double measure_bmi(String height, String weight) {
    	
    	double bmi = 0;
    	double a = 0;
    	double b = 0;
    	if(Sex.equals("man")) {
    		a = 1.1 * Double.parseDouble(weight);
    		b = 128 * Double.parseDouble(weight) / Double.parseDouble(height);
    		bmi = a - b;
        }
    	else {
    		a = 1.07 * Double.parseDouble(weight);
    		b = 128 * Double.parseDouble(weight) / Double.parseDouble(height);
    		bmi = a - b;
    	}
        return bmi;

    }

    public double measure_fat(double bmi, String weight) {
        return bmi * Double.parseDouble(weight) / 100;
    }

    public String standard(double mea_fat) {

        if (mea_fat > 35) {
            return "고도비만";
        } else if (mea_fat > 30 && mea_fat < 35) {
            return "비만";
        } else if (mea_fat > 25 && mea_fat < 30) {
            return "과체중";
        } else if (mea_fat > 20 && mea_fat < 25) {
            return "정상체중";
        } else {
            return "저체중";
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnInputH) {
            while (true) {
                try {
                    String h = JOptionPane.showInputDialog(this.getParent(), "키를 입력하세요.", "키 입력", JOptionPane.INFORMATION_MESSAGE);
                    if (h == null)
                        break;
                    H = Integer.parseInt(h);
                    if (H >= 250 || H < 20) {
                        showError("키를 제대로 입력하세요.");
                    } else {
                        height = H + "";
                        measure(height, weight);
                        txtDisplay.append("건강 도우미 : 체중이 변하셨군요. \r\n 당신은 현재 " + lblStandardWeightS.getText() + " 입니다.\r\n\n");
                        break;
                    }
                } catch (NumberFormatException ne) {
                    showError("숫자만 입력하세요.");
                } catch (Exception e1) {
                    showError("오류!");
                }
            }
            //바뀌었다고 알려줌
        }

        if (e.getSource() == btnInputW) {
            while (true) {
                try {
                    String w = JOptionPane.showInputDialog(this.getParent(), "몸무게를 입력하세요.", "몸무게 입력", JOptionPane.INFORMATION_MESSAGE);
                    if (w == null)
                        break;
                    W = Integer.parseInt(w);
                    if (W >= 250 || W < 20) {
                        showError("몸무게를 제대로 입력하세요.");
                    } else {
                        weight = W + "";
                        measure(height, weight);
                        txtDisplay.append("건강 도우미 : 체중이 변하셨군요. \r\n 당신은 현재 " + lblStandardWeightS.getText() + " 입니다.\r\n\n");
                        break;
                    }
                } catch (NumberFormatException ne) {
                    showError("숫자만 입력하세요.");
                } catch (Exception e1) {
                    showError("오류!");
                }
            }
            //바뀌었다고 알려줌
        }

        if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this.getParent(), msg, "오류", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource().equals(txtsand)) {
            if (e.getKeyChar() == KeyEvent.VK_ENTER) {  // enter 입력 시 이벤트Ʈ
                txtDisplay.append(txtsand.getText().toString() + "\r\n");
                AI(txtsand.getText());
                txtsand.setText("");
            }
        }
    }

    private void AI(String text) {
        int count = -1;
        boolean found = false;
        for (int i = 0; i < match.length; i++) {
            Pattern pattern = Pattern.compile(match[i], Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            found = matcher.find();
            if (found == true) {
                count = i;
                break;
            }
        }
        switch (count) {
            case 0: {
                txtDisplay.append("건강 도우미 : " + hello[((int) (Math.random() * hello.length))] + "\r\n\n");
                num_no = 0;
                num_ok = 0;
                break;
            }
            case 1: {
                if(num_no == 2 && no_action == true) {
                    num_no = 3;
                    no_action = false;
                }
                txtDisplay.append("건강 도우미 : " + no[num_no][((int) (Math.random() * no[num_no].length))] + "\r\n\n");
                num_no = 2;
                num_ok = 0;
                break;
            }
            case 2: {

                txtDisplay.append("건강 도우미 : " + ok[num_ok][((int) (Math.random() * ok[num_ok].length))] + "\r\n\n");
                num_no = 1;
                num_ok = 1;
                no_action = true;
                break;
            }
            case 3: {
                txtDisplay.append("건강 도우미 : " + thank[((int) (Math.random() * thank.length))] + "\r\n\n");
                break;
            }
        }
        if(found == false) {
            txtDisplay.append("건강 도우미 : " + dont[((int) (Math.random() * dont.length))] + "\r\n\n");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    private void MemUpdate(){
    	
    }
    
    private void MemData() {
    	try {
			conn = DBConnManager.GetConnection();
			String sql;
			if(Cache == 0){
			sql = "INSERT INTO `AIMemData` (`No`, `Bmi`, `Fat`, `Standard`, `Height`, `Weight`) VALUES (?, ?, ?, ?, ? ,?);";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, No);
			double temp1 = measure_bmi(height, weight);
			String temp2 = String.format("%.2f", temp1);
			double temp3 = Double.parseDouble(temp2);
			stmt.setDouble(2, temp3);
			temp1 = measure_fat(measure_bmi(height, weight), weight);
			temp2 = String.format("%.2f", temp1);
			temp3 = Double.parseDouble(temp2);
			stmt.setDouble(3, temp3);
			stmt.setString(4, lblStandardWeightS.getText());
			stmt.setInt(5, Integer.parseInt(height));
			stmt.setInt(6, Integer.parseInt(weight));
			stmt.executeUpdate();
			
			Cache = 1;
			sql = "UPDATE `AIMember` SET `cache`=? WHERE `No`=?;";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Cache);
			stmt.setInt(2, No);
			stmt.executeUpdate();
			
			}else{ //업데이트
				sql = "UPDATE `AIMemData` SET `Bmi`=?, `Fat`=?, `Standard`=?, `Height`=?, `Weight`=? WHERE `No`=?;";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(6, No);
				double temp1 = measure_bmi(height, weight);
				String temp2 = String.format("%.2f", temp1);
				double temp3 = Double.parseDouble(temp2);
				stmt.setDouble(1, temp3);
				temp1 = measure_fat(measure_bmi(height, weight), weight);
				temp2 = String.format("%.2f", temp1);
				temp3 = Double.parseDouble(temp2);
				stmt.setDouble(2, temp3);
				stmt.setString(3, lblStandardWeightS.getText());
				stmt.setInt(4, Integer.parseInt(height));
				stmt.setInt(5, Integer.parseInt(weight));
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
	}
}