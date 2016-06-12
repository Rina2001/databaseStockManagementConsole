package manipulate;

import java.util.ArrayList;

import data.Product;
/**
 * 
 * @author chen rina 
 * @interface Data for Operation
 *
 */
public interface DataManipulation {

	//
	public Product write(ArrayList<Product> table, Product record);
	/**
	 * 
	 * @param table contain data as one row
	 * @param position express position of of data
	 * @param perPage number of page
	 * @return as array list
	 */
	public ArrayList<Product> display(ArrayList<Product> table,int position,int perPage);
	public Object read(ArrayList<Product> table,int recordId);
	public boolean update(ArrayList<Product> table,int proId,Product newRecord);
	public boolean delete(ArrayList<Product> table,int proId);
	public void search(ArrayList<Product> table,String name);
	
}
