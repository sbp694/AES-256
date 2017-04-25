import java.util.*;
import java.io.*;

public class convertHex{

	public static void main (String [] args){
		String line = "a6bb015c";
		byte [][] key = formatKey(line);
		System.out.println(key[0][0]);
		System.out.println(key[0][1]);
		System.out.println(key[1][0]);
		System.out.println(key[1][1]);
	}

	//Converts keyString of hexadecimal to a 4x8 array of bytes
	private static byte [][] formatKey(String keyString){

		byte [][] key = new byte[2][2];
		int x_int = 0;
		int y_int = 0;
		for (int a = 0; a < keyString.length(); a += 2){
			char x = keyString.charAt(a);
			char y = keyString.charAt(a + 1);

			//convert base 16 to base 10
			if ((int)x > 57){
				String x_str = x + "";
				x_str.toLowerCase();
				x = x_str.charAt(0);
				x_int = (int)x - 87;
			} else {
				x_int = Integer.parseInt(x + "");
			}

			if ((int)y > 57){
				//System.out.println("Exception");
				String y_str = y + "";
				
				y_str.toLowerCase();
				y = y_str.charAt(0);
				y_int = (int)y - 87;
			} else {
				y_int = Integer.parseInt(y + "");
			}

			// System.out.println((a / 2) % 2);
			// System.out.println((a / 2) / 2);

			key[(a / 2) % 2][(a / 2) / 2] = (byte)(16 * x_int + y_int);
		}
		return key;
	}
}
