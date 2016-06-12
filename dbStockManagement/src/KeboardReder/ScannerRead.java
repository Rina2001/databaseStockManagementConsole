package KeboardReder;

import java.util.Scanner;

public class ScannerRead {

	private static Scanner scanner;
	
	public static String ReadString(){
		String text;
		scanner=new Scanner(System.in);
		return scanner.nextLine();
	}
		
	public static int ReadInt(){
		int num = -1;
		scanner=new Scanner(System.in);
		while(scanner.hasNextInt()){
				num=scanner.nextInt();
				break;
			
		}
		return num;
	}
}
