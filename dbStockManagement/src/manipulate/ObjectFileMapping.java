package manipulate;
import java.io.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import static java.lang.System.out;

import data.DataTransaction;
import data.Product;
/**
 * @author chen rina
 * @ObjectFileMappingis used for drive with Storage 
 */
public final class ObjectFileMapping {
		
	/**
	 * @Method ObjectWriter to write Object to file
	 * @pro mean product reference
	 * @file Name
	 */
	public void objectWriter(ArrayList<Product> pro,String fileName){
		try{
		BufferedOutputStream bfOut;
		ObjectOutputStream objectOutputStream;
		bfOut=new BufferedOutputStream(new FileOutputStream(fileName));
			objectOutputStream=new ObjectOutputStream(bfOut);
				Collections.reverse(pro);
				objectOutputStream.writeObject(pro);
				out.println("Done\n"); 
				objectOutputStream.close();
		}
		catch(Exception e){
			e.getLocalizedMessage();
		}
	}
	/**
	 * @author chen rina
	 * @param fileName file Name
	 * @return {@link ArrayList}
	 * @throws Exception super class exception to catch error
	 */
//	public ArrayList<Product> objectReader(String fileName)throws Exception{
//		ArrayList<Product>pro=null;
//		BufferedInputStream bfInput;
//			bfInput=new BufferedInputStream(new FileInputStream(fileName));
//			ObjectInputStream obj=new ObjectInputStream(bfInput);
//			
//			long start=System.currentTimeMillis();
//				pro=((ArrayList<Product>)obj.readObject());
//				
//			long stop=System.currentTimeMillis();
//				System.out.println(stop-start);
//				
//				obj.close();
//				return pro;
//		
//	}
	public ArrayList<Product> objectReader(String fileName){
        ArrayList<Product>pro=new ArrayList<>();
        
        File f=new File(fileName);
        try{
        	if(f.exists() && !f.isDirectory()) { 
        		BufferedInputStream bfInput;
    			bfInput=new BufferedInputStream(new FileInputStream(f));
    			ObjectInputStream obj=new ObjectInputStream(bfInput);
    			return pro=(ArrayList<Product>)obj.readObject();
			}
        	else{
        			out.println("File not found,Cause list null,sir");
        			return null;
        		}
        }
        catch(Exception ex){
        		ex.getMessage();
        	}
    	return null;
	}
	
}

