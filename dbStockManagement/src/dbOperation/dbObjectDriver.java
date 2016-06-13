package dbOperation;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import data.Product;

public class dbObjectDriver {
	
	
/**
 * @method Insert data
 * @param pro arraylist
 */
	public Connection getCon(){
		try {
			Class.forName("org.postgresql.Driver");
			return DriverManager.getConnection("jdbc:postgresql://localhost:5434/dbStock?","rina","rina");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void insertData(ArrayList<Product> pro) {
		String sql="INSERT INTO tblproduct(pro_id,pro_name,pro_unitprice,pro_qty,pro_import_date) VALUES(?,?,?,?,?)";
		Connection con=null;
		PreparedStatement pstm=null;//=con.prepareStatement(sql);
		final int batchSize = 1000;
		int count = 0;
		try{
			con=getCon();
			//con.setAutoCommit(false);
			con.setAutoCommit(false);
			pstm=con.prepareStatement(sql);
		for(Product p:pro){
			
			pstm.setInt(1, p.getId());
			pstm.setString(2, p.getName());
			pstm.setInt(3, p.getUnitprice());
			pstm.setInt(4, p.getQty());
			pstm.setString(5, p.getImportedDate());
			pstm.addBatch();
			count++;
			if(count % batchSize == 0 || count==pro.size()) {
		        pstm.executeBatch();
		        con.commit();
		    }
		}
			con.commit();
		}
		catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally {
			try {
				if(pstm!=null)pstm.close();
				if(con!=null)con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @Method insertSingle record
	 * @desciption performance insertings
	 */
	public void insertSingleRecord(ArrayList<Product> pro){
		String sql="INSERT INTO tblproduct(pro_id,pro_name,pro_unitprice,pro_qty,pro_import_date) VALUES(?,?,?,?,?)";
		Connection con=null;
		PreparedStatement pstm=null;//=con.prepareStatement(sql);

		try{
			con=getCon();
			pstm=con.prepareStatement(sql);
		for(Product p:pro){
			
			pstm.setInt(1, p.getId());
			pstm.setString(2, p.getName());
			pstm.setInt(3, p.getUnitprice());
			pstm.setInt(4, p.getQty());
			pstm.setString(5, p.getImportedDate());
			pstm.executeUpdate();   
		}
		}
		catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally {
			try {
				if(pstm!=null)pstm.close();
				if(con!=null)con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
/**
 * @Method retrive data
 * @return array list
 */
	public ArrayList<Product> retrieveRecord(){
		
		Connection  con=null;
		ArrayList<Product>po=new ArrayList<Product>();
		
		try {
			con=getCon();
			Statement stm=con.createStatement();
			ResultSet rs=stm.executeQuery("select * from tblproduct order by pro_id ");
			while(rs.next()){
				po.add(new Product(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getString(5)));
			}
			return po;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(con!=null ){
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return po;
	}
	/**
	 * Method for delete
	 */
	public void removeRecord(int id){
		Connection con=null;
		PreparedStatement stm=null;
		String sql="delete from tblproduct where pro_id=?";
		try {
			con=getCon();
			stm=con.prepareStatement(sql);
			stm.setInt(1, id);
			stm.executeUpdate();
			System.out.println("Affect One row");
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(con!=null){
					con.close();
				}
				if(stm!=null){
					stm.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	
	}
	/**
	 * @method Update single record in database 
	 * @param pro is use for get recent record to update
	 * @param id for searching content that has id like bellow
	 */
	public boolean updateAll(Product pro,int id){
		String sql="UPDATE tblproduct set pro_name=?,pro_unitprice=?,pro_qty=?,pro_import_date=? where pro_id=?";
		Connection  con=null;
		PreparedStatement pstm=null;//=con.prepareStatement(sql);
		try{
			con=getCon();
			pstm=con.prepareStatement(sql);
			
			pstm.setString(1, pro.getName());
			pstm.setInt(2, pro.getUnitprice());
			pstm.setInt(3, pro.getQty());
			pstm.setString(4, pro.getImportedDate());
			
			pstm.setInt(5,pro.getId());
			pstm.executeUpdate();
			
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally {
			try {
				if(pstm!=null)pstm.close();
				if(con!=null)con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	public boolean updateName(String name,int id){
		String sql="UPDATE tblproduct set pro_name=? where pro_id=?";
		Connection  con=null;
		PreparedStatement pstm=null;//=con.prepareStatement(sql);
		try{
			con=getCon();
			pstm=con.prepareStatement(sql);
			
			pstm.setString(1,name);
			pstm.setInt(2,id);
			pstm.executeUpdate();
			System.out.println("Update affect "+pstm.executeUpdate()+" rows");
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally {
			try {
				if(pstm!=null)pstm.close();
				if(con!=null)con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	public boolean updateUnitPrice(int unitPrice,int id){
		String sql="UPDATE tblproduct set pro_unitprice=? where pro_id=?";
		Connection  con=null;
		PreparedStatement pstm=null;//=con.prepareStatement(sql);
		try{
			con=getCon();
			pstm=con.prepareStatement(sql);
			
			pstm.setInt(1,unitPrice);
			pstm.setInt(2,id);
			
			System.out.println("Update affect "+pstm.executeUpdate()+" rows");
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally {
			try {
				if(pstm!=null)pstm.close();
				if(con!=null)con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	public boolean updateQty(int qty,int id){
		String sql="UPDATE tblproduct set pro_qty=? where pro_id=?";
		Connection  con=null;
		PreparedStatement pstm=null;//=con.prepareStatement(sql);
		try{
			con=getCon();
			pstm=con.prepareStatement(sql);
			
			pstm.setInt(1, qty);
			pstm.setInt(2,id);
			
			System.out.println("Update affect "+pstm.executeUpdate()+" rows");
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally {
			try {
				if(pstm!=null)pstm.close();
				if(con!=null)con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	public boolean updateImport_Date(Product pro,int id){
		String sql="UPDATE tblproduct set pro_import_date=? where pro_id=?";
		Connection  con=null;
		PreparedStatement pstm=null;//=con.prepareStatement(sql);
		try{
			con=getCon();
			pstm=con.prepareStatement(sql);
			
			pstm.setString(1, pro.getImportedDate());
			pstm.setInt(2,pro.getId());
			
			System.out.println("Update affect "+pstm.executeUpdate()+" rows");
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally {
			try {
				if(pstm!=null)pstm.close();
				if(con!=null)con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * @Method GetLastID
	 */
	public int getLastRecord_Id(){
		ResultSet rs=null;
				String sql="select pro_id from tblproduct  order by pro_id DESC limit 1";
				Connection  con=null;
				PreparedStatement pstm=null;//=con.prepareStatement(sql);
				try{
					con=getCon();
					pstm=con.prepareStatement(sql);
					rs=pstm.executeQuery();
					rs.next();
					return rs.getInt("pro_id");
				}
				catch(Exception e){
					e.printStackTrace();
					try {
						con.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				finally {
					try {
						if(pstm!=null)pstm.close();
						if(con!=null)con.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return 0;
	}
}
