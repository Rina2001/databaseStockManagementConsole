package dbOperation;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
/**
 * 
 * @author chen rina
 * @Class Database Connection
 */
public class DatabaseConnection  {
	private  Connection con;
	private static DatabaseConnection obj;
	/**
	 * Constructor
	 * 
	 */
	private DatabaseConnection() {
		makeConnection();
	}
	/**
	 * @static method Connection
	 * @return database Connection Object
	 */
	public static DatabaseConnection getInstance(){
		if(obj==null){
			obj=new DatabaseConnection();
			return obj;
		}
		return obj;
	}
	/**
	 * 
	 * @return Connection Object
	 */
	public Connection getConnection(){
		return con;
	}
	/**
	 * Connection to specific Object
	 */
	private void makeConnection(){
		Properties prop=new Properties();
		FileInputStream file=null;
		try {
			file=new FileInputStream("dbDriverMapping/dbEngine.properties");
			prop.load(file);
			System.out.println(prop.getProperty("mySql.Name"));
			Class.forName(prop.getProperty("mySql.Name"));//get type of Engine
			con=DriverManager.getConnection(prop.getProperty("mySql.dbMap"));//get Object in specific data base engine
			
		} catch (Exception e) {
			System.out.println(e);
		}
		finally {
			try {
				file.close();
			} catch (Exception  e2) {
				// TODO: handle exception
			}
			
			
		}
	}

}
