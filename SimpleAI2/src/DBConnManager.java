
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//DB 연결 설정 및 해제
public class DBConnManager {
	static String dbServerAddr = "jdbc:mysql://220.67.115.33:3306/";
	static String dbName = "dhkwon_1512"; // 여러분 DB 이름으로 수정
	static String user = "dhkwon_1512"; // 여러분 계정 이름으로 수정
	static String pswd = "stdt069"; // 여러분 비밀번호로 수정
		
	// static block (클래스가 JVM에 로딩될 때 실행됨)
	static {
		// JDBC 드라이버 로딩
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// DB 연결 설정
	public static Connection GetConnection() throws SQLException {
		return DriverManager.getConnection(dbServerAddr + dbName, user, pswd);
	}
	
	public static Connection GetConnection(String dbName) throws SQLException {
		return DriverManager.getConnection(dbServerAddr + dbName, user, pswd);
	}
	
	// DB 연결 해제
	public static void CloseConnection(Connection conn) throws SQLException {
		if (conn != null)
			conn.close();
	}
}
