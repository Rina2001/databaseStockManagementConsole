package manipulate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import data.Product;

public class AutoFile {
	/**
	 * 
	 * @param product for Arraylist of product object
	 * @param fileName File Name in storage
	 */
	public void autoExportFile(ArrayList<Product> product, String fileName){
		 ObjectOutputStream objWrite=null;	
		try{
				objWrite=new ObjectOutputStream(new FileOutputStream("Storage/"+fileName));
				objWrite.writeObject(product);
			}catch(Exception me){
				me.getStackTrace();
			}
			finally {
				try {
					if(objWrite!=null){
						objWrite.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
	}
	public ArrayList<Product>  autoImportFile(String fileName){
		ArrayList<Product> product=null;
		ObjectInputStream obj=null;
		File f=new File("Storage/"+fileName);
		try{
			if(f.exists() ) { 
				obj=new ObjectInputStream(new FileInputStream(f.getAbsolutePath()));
				 	product=(ArrayList<Product>)obj.readObject();
				obj.close();
				return product;
			}
			else{
				System.out.println("***no file found,don't worry.");
				return null;
			}
		}catch(Exception me){
			me.getStackTrace();
		}
		finally {
			try {
				if(obj!=null){
					obj.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return product;
	}
	/**
	 * @Method Clear File content
	 * @param fileName your fileName
	 */
	public void clear(String fileName){
		FileOutputStream out=null;
		try {
			 out=new FileOutputStream("Storage/"+fileName);
			out.write(("").getBytes());
			
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @method remove element 
	 */
	public boolean removeElement(Product obj ,String fileName){
		
		try {
			ArrayList<Product> pro=this.autoImportFile(fileName);
			
			for(Product p: pro){
				if(p.getId()==obj.getId()){
					pro.remove(p);
					this.clear(fileName);
					
					if(!pro.isEmpty()){
						this.autoExportFile(pro, fileName);
						System.out.println("Done");
						return true;
					}
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
