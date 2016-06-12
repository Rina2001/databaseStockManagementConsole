package data;

import static java.lang.System.out;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import manipulate.DataManipulation;

public class DataTransaction implements DataManipulation{

	/**
	 * @return product object
	 */
	@Override
	public Product write(ArrayList<Product> table, Product record) {
		if(table!=null){
			table.add(0,record);
		}else{
			table.add(record);
			System.out.println("not null");
		}
		
		return record;
	}
	public Product writeAll(ArrayList<Product> table, Product record) {
		table.add(record);
		return record;
	}

	@Override
	public ArrayList<Product> display(ArrayList<Product> table,int position,int perpage){
		ArrayList<Product> list=new ArrayList<>();
		float devperPage= (float)table.size()/perpage;
		int pageAmount= (int) Math.ceil(devperPage);  
//		System.out.println(pageAount);
		if(pageAmount<position){
			position=pageAmount;
		}
		int start=(position-1)*perpage;
		int end=perpage+start;
		if(position==pageAmount){
			end= (table.size()-((pageAmount-1)*perpage))+start;
		}
//		System.out.println(end);
		for(int i=start;i<end;i++){
			list.add(table.get(i));
		}
		
		CellStyle cs = new CellStyle(HorizontalAlign.center);
		Table tb = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
	    tb.setColumnWidth(0, 10, 14);
	    tb.setColumnWidth(1, 25, 30);
	    tb.setColumnWidth(2, 10, 16);
	    tb.setColumnWidth(3, 10, 16);
	    tb.setColumnWidth(4, 10, 16);
	    
		tb.addCell("ID", cs);
		tb.addCell("Name",cs); 
		tb.addCell("Unit Price",cs); 
		tb.addCell("Qty",cs);
		tb.addCell("Imported Date",cs);
		
		for(Product record : list){
			tb.addCell(String.valueOf(record.getId()),cs);
			tb.addCell(record.getName());
			tb.addCell(String.valueOf(record.getUnitprice()),cs);
			tb.addCell(String.valueOf(record.getQty()),cs);
			tb.addCell(record.getImportedDate(),cs);
		}
		out.println(tb.render());
		Table t2= new Table(2, BorderStyle.DESIGN_CURTAIN_WIDE, ShownBorders.SURROUND);
	    t2.setColumnWidth(0, 40, 40);
	    t2.setColumnWidth(1, 40, 40);
		t2.addCell("Page:" + position + " of " + pageAmount);
		t2.addCell("Total record: " + table.size(),new CellStyle(HorizontalAlign.right));
		out.println(t2.render());
	
		return list;
	}

	@Override
	public Product read(ArrayList<Product> table, int recordId) {
		boolean rt=false;
		int index=0;
		Product product=null;
		for(Product p:table){
			if(p.getId()==recordId){
				rt=true; break;
			}else{
				rt=false;
				index++;
			}
		}
		if(rt){
			product=table.get(index);
		}
		return product;
	}
	@Override
	public boolean update(ArrayList<Product> table, int proId, Product newRecord) {
		boolean rt=false;
		int index=0;
		ArrayList<Product> list=(ArrayList<Product>)table;
		for(Product p:list){
			if(p.getId()==proId){
				rt=true; break;
			}else{
				rt=false;
				index++;
			}
		}
		if(rt){
			list.set(index, newRecord);
		}
		return rt;
	}
	public void updateName(ArrayList<Product> table, int proId, String name) {
		ArrayList<Product> list=(ArrayList<Product>)table;
		for(Product p:list){
			if(p.getId()==proId){
				p.setName(name);
			}
		}
	}
	public void updateUP(ArrayList<Product> table, int proId, int unitPrice) {
		ArrayList<Product> list=(ArrayList<Product>)table;
		for(Product p:list){
			if(p.getId()==proId){
				p.setUnitprice(unitPrice);
			}
		}
	}
	public void updateQty(ArrayList<Product> table, int proId, int qty) {
		ArrayList<Product> list=(ArrayList<Product>)table;
		for(Product p:list){
			if(p.getId()==proId){
				p.setQty(qty);
			}
		}
	}
	@Override
	public boolean delete(ArrayList<Product> table, int proId) {
		boolean rt=false;
		int index=0;
		for(Product p: table){
			if(p.getId()==proId){
				rt=true; break;
			}else{
				rt=false;
				index++;
			}
		}
		if(rt){
			table.remove(index);
		}
		return rt;
	}

	@Override
	public void search(ArrayList<Product> table, String name) {
		CellStyle cs = new CellStyle(HorizontalAlign.center);
		Table tb = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
	    tb.setColumnWidth(0, 10, 14);
	    tb.setColumnWidth(1, 25, 30);
	    tb.setColumnWidth(2, 10, 16);
	    tb.setColumnWidth(3, 10, 16);
	    tb.setColumnWidth(4, 10, 16);
	    
		tb.addCell("ID", cs);
		tb.addCell("Name",cs); 
		tb.addCell("Unit Price",cs); 
		tb.addCell("Qty",cs);
		tb.addCell("Imported Date",cs);
		int i=0;
		for(Product pro : table){
			if(pro.getName().toLowerCase().contains(name.toLowerCase())){
				i++;
				//System.out.println("search:"+pro.getName());
				tb.addCell(String.valueOf(pro.getId()),cs);
				tb.addCell(pro.getName());
				tb.addCell(String.valueOf(pro.getUnitprice()),cs);
				tb.addCell(String.valueOf(pro.getQty()),cs);
				tb.addCell(pro.getImportedDate(),cs);
			}
		}
		if (i>0) {
			System.out.println("Product found for '"+name+"' : "+i);
			System.out.println(tb.render());
		} else {
			System.out.println("Product not found for '"+name+"!");
		}

		
	}


}
