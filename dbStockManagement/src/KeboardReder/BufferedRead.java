package KeboardReder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BufferedRead {

	private static BufferedReader buff;
	
	public static String ReadString(){
		String text=null;
		buff=new BufferedReader(new InputStreamReader(System.in));
		try {
			text= buff.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return text;
	}
	
}
