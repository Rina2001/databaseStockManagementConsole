import java.io.FileReader;

import animations.Animations;
import manipulate.Commander;
class StockDemo {
	public static void main(String[] args)throws Exception {
		//welcome text
		FileReader r=new FileReader("Storage/welcome.txt");
		int character;
		String s="";
        while ((character = r.read()) != -1) {
        	s=s+(char)character;
        }
        new Animations(s,3).join();

        //Loading();
        System.out.println();
        
        Commander cmd=new Commander();
       //cmd.trun();//delete record
    	//cmd.exportData();
     
        cmd.importData();
        cmd.readTmpFile();
        
        
       cmd.menu();

     	
	}
	static void Loading(){
		String str="";
		System.out.println();
		for(int i=0;i<("Please Wait Loading....").length();i++){
			str=""+("Please Wait Loading....").charAt(i);
			System.out.print(str);
		    try {
		    	Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
	}
}
