package data;

import static java.lang.System.out;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

import KeboardReder.ScannerRead;
import java.text.DateFormat;
public class Product implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Fields For Product
	
	private int id;
	private static int lastId=0;
	private String name;
	private int unitprice;
	private int qty;
	private String importedDate;
	
	//Default Constructor
	public Product() {
		id++;
		name="";
		unitprice=0;
		qty=0;
		importedDate="";
	}
	public Product(String name, int unitprice, int qty,String date){
	
		this.name=name;
		this.unitprice=unitprice;
		this.qty=qty;
		this.importedDate=date;
	}
	//Constructor with parameters
	public Product(int id,String name, int unitprice, int qty,String date){
		this.id=id;
		this.name=name;
		this.unitprice=unitprice;
		this.qty=qty;
		this.importedDate=date;
		setLastId(getLastId() + 1);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(int unitprice) {
		this.unitprice = unitprice;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getImportedDate() {
		return importedDate;
	}
	public void setImportedDate(String importedDate) {
		this.importedDate = importedDate;
	}
/*
 * @return Product object
 * 
 */
	public Product productInsert(int conID){
		setLastId(getLastId() + 1);
		Product p=new Product();
		p.setId(conID);
//		System.out.println("Id:" + conID);
//		System.out.print("Product name> ");
//		p.setName(ScannerRead.ReadString());
//		System.out.print("Unit Price> ");
//		p.setUnitprice(ScannerRead.ReadInt());
//		System.out.print("Quantity> ");
//		p.setQty(ScannerRead.ReadInt());
//		p.setImportedDate(p.GetDate());
//	
//		System.out.println("Id: " + conID);
//		System.out.println("Product name: " + p.getName());
//		System.out.println("Unit Price: " + p.getUnitprice());
//		System.out.println("Quantity: " + p.getQty());
//		System.out.println("Imported date: " + p.getImportedDate());
		
		System.out.println("Id:" + conID);
		
		//check user use shortcut
		
		String prname=null;
		StringTokenizer token=null;
		boolean isShortcut=false;
		do{
			System.out.print("Product name> ");
			prname=ScannerRead.ReadString();
			Scanner scan=new Scanner(prname);
			token=new StringTokenizer(prname.substring(1), "-");
			//check case user use simple add product
			//else use shortcut to add product
			if(scan.findInLine("#")==null){
				isShortcut=false;
				break;
			}else{
				if(token.countTokens()!=3){
					System.out.println("Enter wrong format.shortcut fromat should be \"#proname-unitprice-qty\"");
				}
				isShortcut=true;
			}
		}while(token.countTokens()!=3);
		
		
		if(isShortcut){
			p.setName(token.nextToken());
			p.setUnitprice(Integer.parseInt(token.nextToken()));
			p.setQty(Integer.parseInt(token.nextToken()));
			p.setImportedDate(p.GetDate());
		}else{
			p.setName(prname);
			System.out.print("Unit Price> ");
			p.setUnitprice(ScannerRead.ReadInt());
			System.out.print("Quantity> ");
			p.setQty(ScannerRead.ReadInt());
			p.setImportedDate(p.GetDate());
		}
		
		System.out.println("Id: " + conID);
		System.out.println("Product name: " + p.getName());
		System.out.println("Unit Price: " + p.getUnitprice());
		System.out.println("Quantity: " + p.getQty());
		System.out.println("Imported date: " + p.getImportedDate());

		return p;
	}
	
	public static int ProductRead(){
		System.out.print("Enter Product Id : ");
		return ScannerRead.ReadInt();
	}
	

	public String GetDate(){
		return DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.UK).format(new Date());
	}

	public static int getLastId() {
		return lastId;
	}

	public static void setLastId(int lastId) {
		Product.lastId = lastId;
	}
}
