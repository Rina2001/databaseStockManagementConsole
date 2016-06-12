package dbOperation;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;

import data.Product;

public class dbObjectMapping {
		
	static Connection getCon()throws Exception{
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection("jdbc:postgresql://localhost:5433/dbStock?","rina","2001");
	}
	void insertData(ArrayList<Product> pro)throws Exception{
		String sql="INSERT INTO tblProduct(pro_id,pro_name,pro_unitprice,pro_qty,pro_importDate) VALUES(?,?,?,?,?)";
		Connection con=getCon();
		con.setAutoCommit(false);
	PreparedStatement pstm=con.prepareStatement(sql);
//		final int batchSize = 10000;
//		int count = 1;
//		for(Product p:pro){
//			pstm.setInt(1, p.getId());
//			pstm.setString(2, p.getName());
//			pstm.setInt(3, p.getUnitprice());
//			pstm.setInt(4, p.getQty());
//			pstm.setString(5, p.getImportedDate());
//			pstm.addBatch();
//			if(++count % batchSize == 0) {
//		        pstm.executeBatch();
//		    }
//		}
		Product p=new Product(2,"coca",12,12,"2014");
		pstm.setInt(1, p.getId());
		pstm.setString(2, p.getName());
		pstm.setInt(3, p.getUnitprice());
		pstm.setInt(4, p.getQty());
		pstm.setString(5, p.getImportedDate());
		pstm.addBatch();
		pstm.executeBatch(); // insert remaining records
		
		con.commit();
		pstm.close();
		con.close();
	}
	void select(){
		try {
			Connection con=getCon();
			Statement stm=con.createStatement();
			ResultSet rs=stm.executeQuery("select * from tblProduct");
			while(rs.next()){
				System.out.println(rs.getString(5));
			}
			
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	public static void main(String[] args)throws Exception {
			ArrayList<Product> pro=new ArrayList<Product>();
			for(int i=1;i<1E3;i++){
				pro.add(new Product(i, "coca", 12, 12, "2016"));
			}
				new dbObjectMapping().select();
		
	}
}












