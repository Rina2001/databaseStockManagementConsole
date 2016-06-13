package manipulate;
import static java.lang.System.out;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.sound.midi.ControllerEventListener;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;
import javax.swing.plaf.synth.SynthListUI;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import com.mysql.fabric.xmlrpc.base.Array;

import KeboardReder.ScannerRead;
import data.DataTransaction;
import data.Product;
import dbOperation.dbObjectDriver;
/**
 * 
 * @author chen rina
 * @Class Commander  is used for control attitude of program
 */
public  class Commander implements Pagination {
	public ArrayList<Product> pro;
	public ArrayList<Product> tmpSaving=new ArrayList<Product>(); // this method will auto save file of current Writting
	DataTransaction dt;
	Properties prop; // get properties'\
	int rowPos;
	int proID;
	private boolean isRecovery;
	
	public Commander() {
		pro=new ArrayList<Product>();
		getPropery();// getProperty Method	
	}

	/**
	 * @Method getPropety
	 */
	  private void getPropery(){
		  prop=new Properties();
			try {
				prop.load(new FileInputStream("Storage/rowSeter.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
	  }
	  /**
	   * @Method readTmpFile Get File Last recorded
	   */
	  public void readTmpFile(){
		  
		  if(getLastWrting()!=null){
				out.print("\nSave last record press \"Y\" or anykey to release : ");
				if (ScannerRead.ReadString().equals("y")) {
					if(pro==null){
						pro=new ArrayList<Product>();
					}
					pro.addAll(0,getLastWrting());
					Collections.reverse(pro);
					
					System.out.println("Product number : " + pro.size());
					Product.setLastId(pro.size());
					new dbObjectDriver().insertSingleRecord(getLastWrting());
					showList();
					
					
					tmpSaving=null;
					new AutoFile().clear("autoSaving.bak");
					this.isRecovery=true;
					
					
				}else{
					new AutoFile().clear("autoSaving.bak");
//					Product.setLastId(pro.size()+1);
					this.isRecovery=false;
				}
			}
	  }
	/**
	 * @author chen rina
	 * @exportData_Method block code testing for writing colection to file
	 */
	
	public void exportData() {
			
			dt=new DataTransaction();
		//String date=DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.UK).format(new Date());
			long start=System.currentTimeMillis();
			for(int i=1 ;i<=1E4;i++){
				dt.writeAll(pro, new Product(i,"Fanta",11,10,"2015"));
			}
			//call method write Object
			Collections.reverse(pro);
			new dbObjectDriver().insertData(pro);
			long stop=System.currentTimeMillis();
			System.out.println("Write Object to file"+(stop-start));
	}
	/**
	 * @Method ImportData
	 * @throws Exception
	 */

	public void importData(){
		pro=null;
		dt=new DataTransaction();
			long start=System.currentTimeMillis();
			pro=new dbObjectDriver().retrieveRecord();
			long stop=System.currentTimeMillis();
			System.out.println("Current Time Loading"+(stop-start));
		if(pro!=null){
			Collections.reverse(pro);
//			Product.setLastId(pro.size());
			if(this.isRecovery){
				
			}else{
				Product.setLastId(new dbObjectDriver().getLastRecord_Id());
			}
		}		
	}
	/**
	 * @Method ImportData
	 * @throws Exception
	 */
	static int id;
	public void menu(){
		
		Table t=new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
		t.addCell("    *)Display | W)rite | R|ead | U)pdate | D)elete | F)irst | P)revious | N)ext | L)ast   ");
		t.addCell("     S)earch | G)oto | Se)t row | Sa)ve | Ba)ck up Re)store | H)elp | E)xit     ");
		out.println(t.render()+"\n");
		out.print("Command :--->  ");
		String cmd=ScannerRead.ReadString();
		//add shortcut cmd
		Scanner scan=new Scanner(cmd);
		StringTokenizer token=new StringTokenizer(cmd, "#");
		String newcmd=null;
		int newcmdval=0;
		if(token.countTokens()>1){
			while(token.hasMoreTokens()){
				newcmd =token.nextToken();
				newcmdval=Integer.parseInt(token.nextToken());
			}
		}
		if(newcmd!=null){
			cmd=newcmd;
		}	
		switch (cmd.toLowerCase()) {
//					
			case "*":  showList(); //display product
				break;
				
			case "w":	
						int conId=Product.getLastId()+1;
						Product newPro=new Product();
						newPro=newPro.productInsert(conId);	
					
						tmpSaving.add(newPro);
						new AutoFile().autoExportFile(tmpSaving,"autoSaving.bak" );
					
						out.print("Are you sure want to add this record? [y/n]>");
						
						if (ScannerRead.ReadString().equals("y")) {
							if(pro!=null){
								dt.write(pro,newPro);
							
							}else{
								pro= new ArrayList<Product>();
								dt.write(pro, newPro);
							}
							
							out.println("Product added!");
							showList();
						}else {
							AutoFile au=new AutoFile();
							au.removeElement(newPro, "autoSaving.bak");
							Product.setLastId(conId-1);//remove from ram
							out.println("Product adding cancelled!");
						}
					
					
						this.menu();
				break;
			case "r": 	

				if(this.isShortcut(scan,"#")){
					Product product=null;
					int Id=newcmdval;
					product=dt.read(pro, Id);
					if(product!=null){
						Table t1=new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
						t1.addCell("ID ");
						t1.addCell(String.valueOf(": " + product.getId()));
						t1.addCell("Name ");
						t1.addCell(": " + product.getName());
						t1.addCell("Unit price ");
						t1.addCell(String.valueOf(": " + product.getUnitprice()));
						t1.addCell("Quantity ");
						t1.addCell(String.valueOf(": " + product.getQty()));
						t1.addCell("Imported Date ");
						t1.addCell(": " + product.getImportedDate());
						out.println(t1.render());
					}else{
						System.out.println("Product not found.Please read again.");
					}
				}else{
					
					Product product=null;
					int Id=0;
					do{
						out.print("Product ID >");
						Id=ScannerRead.ReadInt();
						if(dt.read(pro,Id)==null){
							System.out.println("product not found...");
						}
					}while(dt.read(pro,Id)==null);
					
					product=dt.read(pro, Id);
					Table t1=new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
					t1.addCell("ID ");
					t1.addCell(String.valueOf(": " + product.getId()));
					t1.addCell("Name ");
					t1.addCell(": " + product.getName());
					t1.addCell("Unit price ");
					t1.addCell(String.valueOf(": " + product.getUnitprice()));
					t1.addCell("Quantity ");
					t1.addCell(String.valueOf(": " + product.getQty()));
					t1.addCell("Imported Date ");
					t1.addCell(": " + product.getImportedDate());
					out.println(t1.render());
				}
				this.menu();
				break;
			case "u":  	out.print("Product Id>");
						 proID=ScannerRead.ReadInt();
						 update(proID); //update product
				break;	
			case "d":	
				if(this.isShortcut(scan,"#")){
					Product pr=dt.read(pro, newcmdval);
					//confirm delete
					if(pr!=null){
						Table t1=new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
						t1.addCell("ID ");
						t1.addCell(String.valueOf(": " + pr.getId()));
						t1.addCell("Name ");
						t1.addCell(": " + pr.getName());
						t1.addCell("Unit price ");
						t1.addCell(String.valueOf(": " + pr.getUnitprice()));
						t1.addCell("Quantity ");
						t1.addCell(String.valueOf(": " + pr.getQty()));
						t1.addCell("Imported Date ");
						t1.addCell(": " + pr.getImportedDate());
						out.println(t1.render());
						System.out.print("Are you sure want to delete this product?[y/n]: ");
						String con=ScannerRead.ReadString();
						if(con.equalsIgnoreCase("y")){
							dt.delete(pro, newcmdval);
							
							System.out.println("Delete Successfully!");
						}else{
							System.out.println("you have been cancel.");
						}
					}else{
						System.out.println("Invalid Product Id.");
					}
				}else{
					int i=0;
					do{
						System.out.print("Input ID of Product to delete: ");
						i=ScannerRead.ReadInt();
						if(dt.read(pro, i)==null){
							System.out.println("ID of product is invalid!");
						}
					}while(dt.read(pro, i)==null);
				
					//confirm delete
					System.out.print("Are you sure want to delete this product?[y/n]: ");
					String con=ScannerRead.ReadString();
				
					if(con.equalsIgnoreCase("y")){
						dt.delete(pro, i);
						new dbObjectDriver().removeRecord(i);//remove form database
						System.out.println("Delete Successfully!");
					}else{
						System.out.println("you have been cancel.");
					}							
				}
				this.menu();		
			
				break;	
			case "f":	first(); //navigate to first page
				break;	 
			case "p":	previous(); //navigate to previous page
				break;
			case "n":   next(); //navigate to next page
				break;	 
			case "l":	last(); //navigate to last page
				break;	
				
			case "s":	
						out.print("Search---|");
//						dt.search(pro, ScannerRead.ReadString());
						String name= ScannerRead.ReadString();
						dt.display(dt.search(pro, name), this.getPagePosition(),this.getNumRow());
//						if (list ==null){
//							System.out.println("Product not found for '"+name+"!");
//						}
						this.menu();
				break;	//search record;
				
			case "g":	goTo(); //go to specific page
					
				break;
			case "se":	out.print("Set display row>");
						this.setNumRow(ScannerRead.ReadInt());
						this.menu();
				break;
			case "sa":
						new dbObjectDriver().insertSingleRecord(tmpSaving);
						tmpSaving=null;
						new AutoFile().clear("autoSaving.bak");
						Collections.reverse(pro);
						out.println("Product save successfully.");
						this.menu();
				break; 
			case "ba": backup(); // back up 
				break;
			case "re": restore(); //restore
				break;
			case "h":	 Helps();
				break;
			case "e":System.exit(0);
			
			default:out.println("Invalid Command\n");
			this.menu();
		}
	}

	private void read(int proId) {
		Product product=dt.read(pro, proId);
			if (product!=null) {
				Table t=new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.SURROUND);
				t.addCell("ID ");
				t.addCell(String.valueOf(": " + product.getId()));
				t.addCell("Name ");
				t.addCell(": " + product.getName());
				t.addCell("Unit price ");
				t.addCell(String.valueOf(": " + product.getUnitprice()));
				t.addCell("Quantity ");
				t.addCell(String.valueOf(": " + product.getQty()));
				t.addCell("Imported Date ");
				t.addCell(": " + product.getImportedDate());
				out.println(t.render());
			} else {
				System.out.println("Product not found!");
				this.menu();
			}
	}

	private void update(int proId) {
		try {
			read(proId);
			out.print("What do you want to update?\n"
					+ "A)ll		N)ame		U)nit Price		Q)uantity		E)xit"
					+ "\n>");
			
				switch (ScannerRead.ReadString().toLowerCase()) {
//				update whole of a object
				case "a":
//					Update All Content
							Product upPro=new Product();
							upPro=upPro.productInsert(proId);	
							out.print("Are you sure want to update this record? [y/n]>");
							if (ScannerRead.ReadString().equals("y")) {
								 dt.update(pro,proId,upPro);
									
								    new dbObjectDriver().updateAll(upPro, proId);
									
									System.out.println("Product update succesfully");
									this.update(proID);
								}
							else {
								out.println("Product update cancelled!");
								this.update(proID);
							}
							this.update(proID);
					break;
//					update only name
				case "n":
							out.print("Product name>");
							String newName=ScannerRead.ReadString();
							out.print("Are you sure want to update this product name? [y/n]>");
								if (ScannerRead.ReadString().equals("y")) {
									dt.updateName(pro, proId,newName);
									 new dbObjectDriver().updateName(newName, proId);
									this.update(proID);
								System.out.println("Product update succesfully");
								}else {
									out.println("Product name update cancelled!");
								}
								System.out.println("lla");
								this.update(proID);
								
					break;
//					Update only unit price
				case "u":
							out.print("Product Unit Price>");
								int up=ScannerRead.ReadInt();
								out.print("Are you sure want to update this product unit price? [y/n]>");
									if (ScannerRead.ReadString().equals("y")) {
										dt.updateUP(pro, proId,up);
										new dbObjectDriver().updateUnitPrice(up, proId);
									System.out.println("Product update succesfully");
									this.update(proID);
									}else {
										out.println("Product unit price update cancelled!");
									}
									this.update(proID);
									this.menu();
					break;
				case "q":
							out.print("Product quantity>");
							int qty=ScannerRead.ReadInt();
							out.print("Are you sure want to update this product quantity? [y/n]>");
							if (ScannerRead.ReadString().equals("y")) {
								dt.updateQty(pro, proId,qty);
								new dbObjectDriver().updateQty(qty, proId);
							System.out.println("Product update succesfully");
							this.update(proID);
							}else {
									out.println("Product quantity update cancelled!");
								}
								this.update(proID);
								this.menu();
					break;
				case "h": Helps();
					break;
				case "e":
					this.menu();
					break;
				default:
					System.err.println("Invalid command!");
					this.update(proID);
			
				}
			this.menu();
		} catch (Exception e) {
			e.getMessage();
		}
		
	}
	
	/**
	 * @Method restore  to restore File from backUpdate
	 */
	@SuppressWarnings("resource")
	private void restore() {
		
		String fileName;
		File folder=new File("Storage/backup/");
		File[] files=folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			System.out.println((i+1)+ ":" + files[i].getName());
		}
		System.out.print("Select number to restore>");
		int num = ScannerRead.ReadInt();
		fileName=files[num-1].getName();
		try {
//			this is database restore
				this.trun();
			 	new dbObjectDriver().insertData(new ObjectFileMapping().objectReader("Storage/backup/"+fileName));
			 	
			   this.importData();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Restore completed");
			this.menu();
		
	}
	/**
	 * methdo delete record database
	 */
	public void trun(){
		Connection con=null;
		PreparedStatement stm=null;
		con=new dbObjectDriver().getCon();
		try {
			stm=con.prepareStatement("truncate tblproduct");
			stm.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * @Method Backup for back up file
	 */
	private void backup() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd-HH-mm");
		Date date = new Date();
		String fileName="backup-"+ dateFormat.format(date) + ".bak";
		try {
			new ObjectFileMapping().objectWriter(pro, "Storage/backup/"+fileName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Backup completed: " + fileName);
		this.menu();
	}
	public int getPagePosition(){
		return Integer.parseInt(prop.getProperty("rowPosition"));			
	}

	public void setPagePosition(int positionNumber){
		 
			try {
				FileOutputStream f=new FileOutputStream("Storage/rowSeter.properties");
			
				prop.setProperty("rowPosition", String.valueOf(positionNumber));
				prop.store(f, null);
			} catch (IOException e) {
				out.println("invalid");
				this.menu();
			}
	}
	
	public int getNumRow(){
		return Integer.parseInt(prop.getProperty("numRow"));	
		
	}

	public void setNumRow(int numberRow){
		
		try {
			FileOutputStream f=new FileOutputStream("Storage/rowSeter.properties");
			prop.setProperty("numRow", String.valueOf(numberRow));
			prop.store(f, null);
		} catch (IOException e) {
			out.println("invalid");
			this.menu();
		}
	}
	@Override
	public void first() {
		if(pro!=null){
			rowPos=1;
			dt.display(pro, rowPos, getNumRow());
		}
		{
			System.out.println("Current Data not found");
		}
		this.menu();
	}
	@Override
	public void last() {
		rowPos=getTotalPage();
		dt.display(pro, rowPos, getNumRow());
		this.menu();
	}
	
	/**
	 * @Method Next
	 */
	@Override
	public void next() {
		if(pro!=null){
			 rowPos++;
				if(rowPos> getTotalPage()){
					rowPos=1;
					dt.display(pro, rowPos, getNumRow());
					this.menu();
				}else{
					dt.display(pro, rowPos, getNumRow());
					this.menu();
				}
		}else{
			System.out.println("Current is not existing,sir!!\n");
		}
		this.menu();
	}
	
	/**
	 * @Method Previous
	 */
	@Override
	public void previous() {
		if(pro!=null){
			if(rowPos>1){
				rowPos--;
				dt.display(pro, rowPos, getNumRow());
			}else{
				rowPos=getTotalPage(); 
				dt.display(pro, rowPos, getNumRow());
			}
		}
		else{
			System.out.println("Current is not existring!!!!!!\n");
		}
		this.menu();
	}
	/**
	 * @Method showlist
	 */
	@Override
	public void showList() {
		if(pro!=null){
			rowPos=getPagePosition();
			dt.display(pro,rowPos ,getNumRow());
		}
		else{
			out.println("Data is Empty or no file found!!,sir\n");
		}
		this.menu();
	}
	/**
	 * @Method goto
	 */
	@Override
	public void goTo() {
		if(pro!=null){
			out.println("Page: ");
			int goTo=ScannerRead.ReadInt();
			if (goTo>0 && goTo<=(getTotalPage())) {
				this.setPagePosition(goTo);
				dt.display(pro, getPagePosition(), getNumRow());
			}else {
				out.println("Page not found");
			}
		}else{
			out.print("Data is Empty or no file found!!,sir");
		}
		this.menu();
	}
	public int getTotalPage(){
			float devperPage= (float)pro.size()/getNumRow();
			return (int) Math.ceil(devperPage); 		
	}
	
	/***
	 * @method getLast Recorded
	 */
		ArrayList<Product> getLastWrting(){
			ArrayList<Product> tmp=new AutoFile().autoImportFile("autoSaving.bak");
			if(tmp!=null)
				Collections.reverse(tmp);
			return tmp;
		}
		public void Helps(){
			Table t=new Table(1, BorderStyle.UNICODE_DOUBLE_BOX_WIDE, ShownBorders.SURROUND);
			
			t.addCell("1.    press * : Display all record of products");
			t.addCell("2.    press w: Add new product");
			t.addCell("      press w ->#proname-unitprice-qty: shortcut for add new product");
			t.addCell("3.    press r: read Content any content");
			t.addCell("      press r#proId: shortcut for read product by Id");
			t.addCell("4.    press u: Update Data");
			t.addCell("5.    press d: Delete Data");
			t.addCell("      press d#proId: shortcut for delete product by Id");
			t.addCell("6.    press f: Display Fist Page");
			t.addCell("7.    press p: Display Previous Page");
			t.addCell("8.    press n: Display Next page");
			t.addCell("9.    press l: Display last page");
			t.addCell("10.   press s: Search product by name");
			t.addCell("11.   press sa: Save record to file");
			t.addCell("12.   press ba: Backup data");
			t.addCell("13.   press re: Restore data");
			t.addCell("14.   press h: Help ");
			System.out.println(t.render());
		}
		public boolean isShortcut(Scanner scan,String ptt){
			boolean ishortcut=false;
			String parttern=scan.findInLine(ptt);
			if(parttern==null){
				ishortcut=false;
			}else if(parttern.equals(ptt)){
				ishortcut=true;
			}
			return ishortcut;
		}

	
}
