import java.util.*;
import java.io.*;

public class AES{

	private static int [] rconTable = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab};
	private static int [][] sBox = {{0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
									{0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
									{0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
									{0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
									{0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
									{0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
									{0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
									{0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
									{0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
									{0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
									{0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
									{0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
									{0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
									{0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
									{0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
									{0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}};

	private static int [][] sBoxInverse = {{0x52,0x09,0x6a,0xd5,0x30,0x36,0xa5,0x38,0xbf,0x40,0xa3,0x9e,0x81,0xf3,0xd7,0xfb},
											{0x7c,0xe3,0x39,0x82,0x9b,0x2f,0xff,0x87,0x34,0x8e,0x43,0x44,0xc4,0xde,0xe9,0xcb},
											{0x54,0x7b,0x94,0x32,0xa6,0xc2,0x23,0x3d,0xee,0x4c,0x95,0x0b,0x42,0xfa,0xc3,0x4e},
											{0x08,0x2e,0xa1,0x66,0x28,0xd9,0x24,0xb2,0x76,0x5b,0xa2,0x49,0x6d,0x8b,0xd1,0x25},
											{0x72,0xf8,0xf6,0x64,0x86,0x68,0x98,0x16,0xd4,0xa4,0x5c,0xcc,0x5d,0x65,0xb6,0x92},
											{0x6c,0x70,0x48,0x50,0xfd,0xed,0xb9,0xda,0x5e,0x15,0x46,0x57,0xa7,0x8d,0x9d,0x84},
											{0x90,0xd8,0xab,0x00,0x8c,0xbc,0xd3,0x0a,0xf7,0xe4,0x58,0x05,0xb8,0xb3,0x45,0x06},
											{0xd0,0x2c,0x1e,0x8f,0xca,0x3f,0x0f,0x02,0xc1,0xaf,0xbd,0x03,0x01,0x13,0x8a,0x6b},
											{0x3a,0x91,0x11,0x41,0x4f,0x67,0xdc,0xea,0x97,0xf2,0xcf,0xce,0xf0,0xb4,0xe6,0x73},
											{0x96,0xac,0x74,0x22,0xe7,0xad,0x35,0x85,0xe2,0xf9,0x37,0xe8,0x1c,0x75,0xdf,0x6e},
											{0x47,0xf1,0x1a,0x71,0x1d,0x29,0xc5,0x89,0x6f,0xb7,0x62,0x0e,0xaa,0x18,0xbe,0x1b},
											{0xfc,0x56,0x3e,0x4b,0xc6,0xd2,0x79,0x20,0x9a,0xdb,0xc0,0xfe,0x78,0xcd,0x5a,0xf4},
											{0x1f,0xdd,0xa8,0x33,0x88,0x07,0xc7,0x31,0xb1,0x12,0x10,0x59,0x27,0x80,0xec,0x5f},
											{0x60,0x51,0x7f,0xa9,0x19,0xb5,0x4a,0x0d,0x2d,0xe5,0x7a,0x9f,0x93,0xc9,0x9c,0xef},
											{0xa0,0xe0,0x3b,0x4d,0xae,0x2a,0xf5,0xb0,0xc8,0xeb,0xbb,0x3c,0x83,0x53,0x99,0x61},
											{0x17,0x2b,0x04,0x7e,0xba,0x77,0xd6,0x26,0xe1,0x69,0x14,0x63,0x55,0x21,0x0c,0x7d}};

	final static int[] LogTable = {
	0,   0,  25,   1,  50,   2,  26, 198,  75, 199,  27, 104,  51, 238, 223,   3, 
	100,   4, 224,  14,  52, 141, 129, 239,  76, 113,   8, 200, 248, 105,  28, 193, 
	125, 194,  29, 181, 249, 185,  39, 106,  77, 228, 166, 114, 154, 201,   9, 120, 
	101,  47, 138,   5,  33,  15, 225,  36,  18, 240, 130,  69,  53, 147, 218, 142, 
	150, 143, 219, 189,  54, 208, 206, 148,  19,  92, 210, 241,  64,  70, 131,  56, 
	102, 221, 253,  48, 191,   6, 139,  98, 179,  37, 226, 152,  34, 136, 145,  16, 
	126, 110,  72, 195, 163, 182,  30,  66,  58, 107,  40,  84, 250, 133,  61, 186, 
	43, 121,  10,  21, 155, 159,  94, 202,  78, 212, 172, 229, 243, 115, 167,  87, 
	175,  88, 168,  80, 244, 234, 214, 116,  79, 174, 233, 213, 231, 230, 173, 232, 
	44, 215, 117, 122, 235,  22,  11, 245,  89, 203,  95, 176, 156, 169,  81, 160, 
	127,  12, 246, 111,  23, 196,  73, 236, 216,  67,  31,  45, 164, 118, 123, 183, 
	204, 187,  62,  90, 251,  96, 177, 134,  59,  82, 161, 108, 170,  85,  41, 157, 
	151, 178, 135, 144,  97, 190, 220, 252, 188, 149, 207, 205,  55,  63,  91, 209, 
	83,  57, 132,  60,  65, 162, 109,  71,  20,  42, 158,  93,  86, 242, 211, 171, 
	68,  17, 146, 217,  35,  32,  46, 137, 180, 124, 184,  38, 119, 153, 227, 165, 
	103,  74, 237, 222, 197,  49, 254,  24,  13,  99, 140, 128, 192, 247, 112,   7};

    final static int[] AlogTable = {
	1,   3,   5,  15,  17,  51,  85, 255,  26,  46, 114, 150, 161, 248,  19,  53, 
	95, 225,  56,  72, 216, 115, 149, 164, 247,   2,   6,  10,  30,  34, 102, 170, 
	229,  52,  92, 228,  55,  89, 235,  38, 106, 190, 217, 112, 144, 171, 230,  49, 
	83, 245,   4,  12,  20,  60,  68, 204,  79, 209, 104, 184, 211, 110, 178, 205, 
	76, 212, 103, 169, 224,  59,  77, 215,  98, 166, 241,   8,  24,  40, 120, 136, 
	131, 158, 185, 208, 107, 189, 220, 127, 129, 152, 179, 206,  73, 219, 118, 154, 
	181, 196,  87, 249,  16,  48,  80, 240,  11,  29,  39, 105, 187, 214,  97, 163, 
	254,  25,  43, 125, 135, 146, 173, 236,  47, 113, 147, 174, 233,  32,  96, 160, 
	251,  22,  58,  78, 210, 109, 183, 194,  93, 231,  50,  86, 250,  21,  63,  65, 
	195,  94, 226,  61,  71, 201,  64, 192,  91, 237,  44, 116, 156, 191, 218, 117, 
	159, 186, 213, 100, 172, 239,  42, 126, 130, 157, 188, 223, 122, 142, 137, 128, 
	155, 182, 193,  88, 232,  35, 101, 175, 234,  37, 111, 177, 200,  67, 197,  84, 
	252,  31,  33,  99, 165, 244,   7,   9,  27,  45, 119, 153, 176, 203,  70, 202, 
	69, 207,  74, 222, 121, 139, 134, 145, 168, 227,  62,  66, 198,  81, 243,  14, 
	18,  54,  90, 238,  41, 123, 141, 140, 143, 138, 133, 148, 167, 242,  13,  23, 
	57,  75, 221, 124, 132, 151, 162, 253,  28,  36, 108, 180, 199,  82, 246,   1};									

	private static String acceptable = "0123456789abcdefABCDEF";

	public static void main (String [] args){

		String option = args[0];
		String keyFileName = args[1];
		String inputFile = args[2];

		Scanner scan = null;
		FileInputStream fileIn = null;
		FileInputStream keyFile = null;
		BufferedWriter writer = null;

		try{
			//fileIn = new FileInputStream(new File(inputFile));
			scan = new Scanner(new File(inputFile + ".txt"));
			keyFile = new FileInputStream(new File(keyFileName + ".txt"));
		} catch(FileNotFoundException e){
			System.out.println("File not found");
		}

		String keyString = readInKey(keyFile);
		int [][] key = formatKey(keyString);
		int [][] expandedKey = keyExpansion(key);

		// for (int x = 0; x < 4; x++){
		// 	for (int y = 0; y < 64; y++){
		// 		String hex = Integer.toHexString(expandedKey[x][y]);
		// 		if (hex.length() == 1){
		// 			String newHex = "0" + hex;
		// 			hex = newHex;
		// 		}
		// 		for (int z = 0; z < 5 - hex.length(); z++){
		// 			hex += " ";
		// 		}
		// 		System.out.print(hex);
		// 	}
		// 	System.out.println();
		// }

		double total_bytes = 0.0;
		long startTime = System.nanoTime();
		//Encrypting or decrypting?
		if (option.equals("e")){
			try{
				writer = new BufferedWriter(new FileWriter(new File(inputFile + "enc.txt")));
			} catch (IOException e){
				//Quit
			}
			while(scan.hasNextLine()){
				String text = scan.nextLine();
				String newText = checkLine(text);
				if (!(newText.equals(""))){
					int [][] state = formatState(newText);
					// System.out.println("Returned from format state...");
					// printState(state);
					int [][] roundKey = getRoundKey(expandedKey, 0);
					// System.out.println("Printing roundKey...");
					// printState(roundKey);

					//Apply the initial round of XORing with the round key
					// System.out.println("Printing state before addRoundKey...");
					// printState(state);
					state = addRoundKey(state, roundKey);
					// System.out.println("Printing result of addRoundKey...");
					// printState(state);

					//apply 13 rounds to state
					for (int a = 1; a < 14; a++){
						roundKey = getRoundKey(expandedKey, a);
						// System.out.println("Printing roundKey...");
						// printState(roundKey);
						state = subBytes(state, false);
						// System.out.println("Printing result of subBytes...");
						// printState(state);
						state = shiftRows(state, false);
						// System.out.println("Printing result of shiftRows...");
						// printState(state);
						state = mixColumns(state, false);
						// System.out.println("Printing result of mixColumns...");
						// printState(state);
						state = addRoundKey(state, roundKey);
						// System.out.println("Printing result of addRoundKey...");
						// printState(state);
					}
					
					//apply the final round to state (excluded mixColumns)
					roundKey = getRoundKey(expandedKey, 14);
					state = subBytes(state, false);
					state = shiftRows(state, false);
					state = addRoundKey(state, roundKey);
					String stateString = stateToString(state);
					try {
						writer.write(stateString, 0, stateString.length());
					} catch (IOException e){
						//Quit
					}
					
					// System.out.println("PRINTING FINAL RESULT...");
					// printState(state);

					//export ciphertext to encryption file
					total_bytes++;
				}
			}
		} else {
			try{
				writer = new BufferedWriter(new FileWriter(new File(inputFile + ".dec")));
			} catch (IOException e){
				//Quit
			}

			while(scan.hasNextLine()){
				String text = scan.nextLine();
				String newText = checkLine(text);
				if (!(newText.equals(""))){
					int [][] state = formatState(newText);
					int [][] roundKey = getRoundKey(expandedKey, 14);

					//invert the final round
					state = addRoundKey(state, roundKey);
					state = shiftRows(state, true);
					state = subBytes(state, true);

					for (int b = 13; b > 0; b--){
						roundKey = getRoundKey(expandedKey, b);
						state = addRoundKey(state, roundKey);
						state = mixColumns(state, true);
						state = shiftRows(state, true);
						state = subBytes(state, true);
					}

					roundKey = getRoundKey(expandedKey, 0);
					state = addRoundKey(state, roundKey);
					String stateString = stateToString(state);
					try {
						writer.write(stateString, 0, stateString.length());
					} catch (IOException e){
						//Quit
					}

					// System.out.println("PRINTING FINAL RESULT...");
					// printState(state);
					total_bytes++;
				}
			}
		}
		long endTime = System.nanoTime();
		System.out.println("Duration: " + (endTime - startTime));
		System.out.println("Total Bytes: " + total_bytes);
		double megabytes = (total_bytes * 16.0) / 1000000.0;
		double duration = ( megabytes / ((endTime - startTime) / 1000000000.0));
		System.out.println("Throughput in MB/sec: " + duration);
		try{
			writer.close();
		} catch (IOException e){
			//Quit
		}
		
	}

	private static String stateToString(int [][] state){
		String stateString = "";
		for (int a = 0; a < 4; a++){
			for (int b = 0; b < 4 ; b++){
				String hex = Integer.toHexString(state[b][a]);
				if (hex.length() == 1){
					String newHex = "0" + hex;
					hex = newHex;
				}
				stateString += hex;
			}
		}
		return stateString + '\n';
	}

	//Reads in the given key that is in hex
	private static String readInKey(FileInputStream in){
		//System.out.println("Reading in key...");
		try {
			String key = "";

			boolean end = false;
			while (!end){
				int input = in.read();
				if (input == -1){
					end = true;
				} else {
					key += (char)input;
				}
			}
			return key;
		} catch (IOException e){
			//Bad read
			//quit
		}
		
		return "";
	}

	//Arranges the hex characters into a 4x4 array
	private static int [][] formatState(String text){
		//System.out.println("Formating state...");
		int [][] state = new int [4][4];
		for (int a = 0; a < text.length(); a += 2){
			char x = text.charAt(a);
			char y = text.charAt(a + 1);

			int x_int = 0;
			int y_int = 0;
			//convert x base 16 to base 10
			if ((int)x > 57){
				String x_str = x + "";
				x_str.toLowerCase();
				x = x_str.charAt(0);
				x_int = (int)x - 87;
			} else {
				x_int = Integer.parseInt(x + "");
			}

			//convert x base 16 to base 10
			if ((int)y > 57){
				String y_str = y + "";
				y_str.toLowerCase();
				y = y_str.charAt(0);
				y_int = (int)y - 87;
			} else {
				y_int = Integer.parseInt(y + "");
			}

			state[(a / 2) % 4][(a / 2) / 4] = 16 * x_int + y_int;
		}
		return state;
	}

	//Converts keyString of hexadecimal to a 4x8 array of bytes
	private static int [][] formatKey(String keyString){
		//System.out.println("Formating key...");
		int [][] key = new int[4][8];
		for (int a = 0; a < keyString.length(); a += 2){
			char x = keyString.charAt(a);
			char y = keyString.charAt(a + 1);

			int x_int = 0;
			int y_int = 0;
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
				String y_str = y + "";
				y_str.toLowerCase();
				y = y_str.charAt(0);
				y_int = (int)y - 87;
			} else {
				y_int = Integer.parseInt(y + "");
			}

			key[(a / 2) % 4][(a / 2) / 4] = 16 * x_int + y_int;

		}

		return key;
	}

	private static int [][] keyExpansion(int [][] key){
		//System.out.println("Applying key expansion algorithm...");
		int [][] expandedKey = fillExpandedKey(key);
		//System.out.println("Printing key");
		//printKey(key);
		for (int a = 0; a < 14 ; a += 2){
			int [] base = new int[4];
			//4 bytes that the key expansion is based on for the next twelve bytes
			for (int b = 0; b < 4; b++){
				base [b] = expandedKey[b][((a * 4) + 8) - 1];
			}

			//4 bytes to be XORed with the rcon
			int [] toXOR = new int[4];
			for (int c = 0; c < 4; c++){
				toXOR [c] = expandedKey[c][((a * 4) + 8) - 8];
			}

			int [] root = getRoot(base, toXOR, a / 2, true);
			
			// System.out.println("Printing root...");
			// for (int b = 0; b < 4; b++){
			// 	System.out.println(root[b]);
			// }
			expandedKey = addTwelve(root, expandedKey, a);

			// System.out.println("Printing added 12...");
			// for (int d = 0; d < 4; d++){
			// 	for(int e = 1; e < 4; e++){
			// 		System.out.print(expandedKey[d][8 + e] + " ");
			// 	}
			// 	System.out.println();
			// }

			//get base for the root of the next twelve bytes
			for (int d = 0; d < 4; d++){
				base [d] = expandedKey[d][(((a + 1) * 4) + 8) - 1];
			}
			
			for (int e = 0; e < 4; e++){
				toXOR [e] = expandedKey[e][(((a + 1) * 4) + 8) - 8];
			}

			root = getRoot(base, toXOR, a, false);

			// System.out.println("Printing root...");
			// for (int b = 0; b < 4; b++){
			// 	System.out.println(root[b]);
			// }

			expandedKey = addTwelve(root, expandedKey, (a + 1));
			
			// System.out.println("Printing added 12...");
			// for (int d = 0; d < 4; d++){
			// 	for(int e = 1; e < 4; e++){
			// 		System.out.print(expandedKey[d][12 + e] + " ");
			// 	}
			// 	System.out.println();
			// }
		}

		return expandedKey;
	}

	//fill first 32 bytes of expand key array with given encryption key
	private static int [][]fillExpandedKey(int [][] key){
		int [][] expandedKey = new int[4][64];

		for (int a = 0; a < 8; a++){
			for (int b = 0; b < 4; b++){
				expandedKey[b][a] = key[b][a];
			}
		}

		return expandedKey;
	}

	private static int [] getRoot(int [] base, int [] toXOR, int rcon_iter, boolean secondHalf){
		int [] newBase = base;

		//Only apply the rotation for the root of the first 16 bytes
		if(secondHalf){
			//Rotate the four bytes
			int index = newBase[0];
			for (int a = 0; a < 3; a++){
				newBase[a] = newBase[a + 1];
			}
			newBase[3] = index;
		}

		//Apply the S-Box to the four bytes
		for(int b = 0; b < 4; b++){
			int x = newBase[b];
			int row = (x & 0xf0) >>> 4;
			int col = x & 0x0f;
			newBase[b] = sBox[row][col];
		}

		int [] rcon = {rconTable[rcon_iter], 0x00, 0x00, 0x00};

		//XOR with first column(4 bytes) of the previous 32 bytes and the rcon value
		for(int c = 0; c < 4; c++){
			newBase[c] ^= toXOR[c];
		}

		if (secondHalf){
			for(int d = 0; d < 4; d++){
				newBase[d] ^= rcon[d];
			}
		}

		return newBase;
	}

	private static int [][] addTwelve(int [] root, int [][] expandedKey, int iteration){
		int [][] key = expandedKey;
		//Adding root to expanded tree
		for (int a = 0; a < 4; a++){
			key[a][((iteration * 4) + 8)] = root[a];
		}

		//XOR root with the twelve bytes that come before the root 
		//to get the next twelve that go after the root
		for (int b = 7; b > 4; b--){
			for (int c = 0; c < 4; c++){
				key [c][(iteration * 4) + 8 + ((b - 8) * -1)] = key[c][((iteration * 4) + 8) - b] ^ key[c][((iteration * 4) + 8) + ((b - 7) * -1)];
			}
		}

		return key;
	}

	private static void printState(int [][]state){
		for (int a = 0; a < 4; a++){
			for (int b = 0; b < 4; b++){
				//System.out.print(state[a][b] + " ");
				String hex = Integer.toHexString(state[a][b]);
				if (hex.length() == 1){
					String newHex = "0" + hex;
					hex = newHex;
				}
				for (int z = 0; z < 5 - hex.length(); z++){
					hex += " ";
				}
				System.out.print(hex);
			}
			System.out.println();
		}
	}

	private static String checkLine(String text){
		//System.out.println("Checking line...");
		String newText = text;
		if (text.length() > 32){
			newText = text.substring(0, 32);
		} else if (text.length() < 32){
			for (int b = 0; b < (32 - text.length()); b++){
				text += "0";
			}
		}
		for (int a = 0; a < newText.length(); a++){
			//System.out.println("Char " + a + "= " + newText.charAt(a));
			int present = acceptable.indexOf(newText.charAt(a));
			if (present == -1){
				return "";
			}
		}

		return newText;
	}

	//returns the key needed for the specified round
	private static int [][] getRoundKey(int [][] expandedKey, int round){
		int [][] roundKey = new int [4][4];
		for (int a = round * 4; a < round * 4 + 4; a++){
			for (int b = 0; b < 4; b++){
				roundKey[b][a % 4] = expandedKey[b][a];
			}
		}
		return roundKey;
	}

	//substitute bytes in state with appropriate byte from given matrix
	private static int [][] subBytes(int [][] state, boolean inverse){
		int [][] newState = new int [4][4];
		//Apply the S-Box to the four bytes
		for (int a = 0; a < 4; a++){
			for(int b = 0; b < 4; b++){
				int x = state[b][a];
				int row = (x & 0xf0) >>> 4;
				int col = x & 0x0f;
				if (inverse){
					newState[b][a] = sBoxInverse[row][col];
				} else {
					newState[b][a] = sBox[row][col];
				}
			}
		}
		
		return newState;
	}

	//XOR the state with the roundKey
	private static int [][] addRoundKey(int [][] state, int [][] roundKey){
		int [][] newState = new int[4][4];
		for (int a = 0; a < 4; a++){
			for (int b = 0; b < 4; b++){
				newState[b][a] = state[b][a] ^ roundKey[b][a];
			}
		}
		
		return newState;
	}

	private static int [][] shiftRows(int [][] state, boolean inverse){
		int [][] newState = state;
		if (inverse){
			for (int a = 0; a < 4; a++){
				for (int b = 3; b >= a; b--){
					//perform shift right
					int x = newState[a][0];
					for (int c = 1; c < 4; c++){
						//shift left 3
						newState[a][c - 1] = newState[a][c]; 
					}
					newState[a][3] = x;
				}
			}
		} else {
			for (int a = 0; a < 4; a++){
				for (int b = 0; b < a; b++){
					//perform shift right
					int x = newState[a][0];
					for (int c = 1; c < 4; c++){
						//shift left 3
						newState[a][c - 1] = newState[a][c]; 
					}
					newState[a][3] = x;
				}
			}
		}
		
		return newState;
	}

	private static int [][] mixColumns(int [][] state, boolean inverse){
		int [][] newState = state;
		if (inverse){
			for (int c = 0; c < 4; c++){
				int a[] = new int[4];
		
				// note that a is just a copy of newState[.][c]
				for (int i = 0; i < 4; i++) 
				    a[i] = newState[i][c];
				
				newState[0][c] = (mul(0xE,a[0]) ^ mul(0xB,a[1]) ^ mul(0xD, a[2]) ^ mul(0x9,a[3]));
				newState[1][c] = (mul(0xE,a[1]) ^ mul(0xB,a[2]) ^ mul(0xD, a[3]) ^ mul(0x9,a[0]));
				newState[2][c] = (mul(0xE,a[2]) ^ mul(0xB,a[3]) ^ mul(0xD, a[0]) ^ mul(0x9,a[1]));
				newState[3][c] = (mul(0xE,a[3]) ^ mul(0xB,a[0]) ^ mul(0xD, a[1]) ^ mul(0x9,a[2]));
			}
		} else {
			for (int d = 0; d < 4; d++){
				// This is another alternate version of mixColumn, using the 
				// logtables to do the computation.
				
				int a[] = new int[4];
				
				// note that a is just a copy of st[.][c]
				for (int i = 0; i < 4; i++) 
				    a[i] = newState[i][d];
				
				// This is exactly the same as mixColumns1, if 
				// the mul columns somehow match the b columns there.
				newState[0][d] = (mul(2,a[0]) ^ a[2] ^ a[3] ^ mul(3,a[1]));
				newState[1][d] = (mul(2,a[1]) ^ a[3] ^ a[0] ^ mul(3,a[2]));
				newState[2][d] = (mul(2,a[2]) ^ a[0] ^ a[1] ^ mul(3,a[3]));
				newState[3][d] = (mul(2,a[3]) ^ a[1] ^ a[2] ^ mul(3,a[0]));
			}
		}
		return newState;
	}

    private static int mul (int a, int b) {
		int inda = (a < 0) ? (a + 256) : a;
		int indb = (b < 0) ? (b + 256) : b;

		if ( (a != 0) && (b != 0) ) {
		    int index = (LogTable[inda] + LogTable[indb]);
		    int val = (AlogTable[ index % 255 ] );
		    return val;
		} else {
		    return 0;
	    } // mul
	}

}