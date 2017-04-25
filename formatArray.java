import java.util.*;
import java.io.*;

public class formatArray{
	public static void main(String [] args){
		Scanner scan = null;
		String array = "{";
		try{
			scan = new Scanner(new File("inverse.txt"));
		} catch(FileNotFoundException e){
			//Quit
		}

		

		while(scan.hasNextLine()){
			String hexLine = scan.nextLine();
			String hex = "0x";
			for (int a = 0; a < hexLine.length(); a++){
				char num = hexLine.charAt(a);
				if (a == hexLine.length() - 1){
					hex += num;
					hex += "},{";
					array += hex;
					hex = "0x";
				} else if (num == ' '){
					hex += ",";
					array += hex;
					hex = "0x";
				} else {
					hex += num;
				}
			}

		}

		System.out.println(array);
	}
}