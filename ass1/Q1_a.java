package ass1;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

public class Q1_a {
	
	public final static String EMPTY = "";
	public final static String FILE_NAME = "is_text.txt";
	
	static String readFile(String path) throws IOException{
		Charset encoding = StandardCharsets.UTF_8;
		  byte[] encoded = Files.readAllBytes(Paths.get(path));
		  return encoding.decode(ByteBuffer.wrap(encoded)).toString();
		}
	
	
	public static byte[] initializationVector(){
		Random r = null;
		byte[] res = null;
		char c = 0;
		String str = "";
		
		for (int i = 0; i < 10; i++){
			r = new Random();
			c = (char) (r.nextInt(26) + 'a');
			str = str + c;
		}
		
		try {
			res = str.getBytes("UTF-8");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return res;
		
	}
	
	public static byte[] stringToBinary(String str){
		byte[] bin = null;
//		String num="";
//		try {
//			
//			bin = str.getBytes("UTF-8");			
//			for (byte i: bin){
//				num = num + Integer.toBinaryString(i);
//				System.out.println((char)i + " : " + i +" = "+ Integer.toBinaryString(i));
//			}
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
		try {
			bin = str.getBytes("UTF-8");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return bin;
	}
	
	public static byte[] convertByKey(byte[] block){
		
		for(int i = 0; i < block.length; i++){
			if (block[i] == (byte)97)
				block[i] = (byte)101; // -> e
			else if ((block[i] == (byte)98))
				block[i] = (byte)99; // -> c
			else if (block[i] == (byte) 99)
				block[i] = (byte)103; // -> g
			else if (block[i] == 100)
				block[i] = (byte)97; // -> a
			else if (block[i] == 101)
				block[i] = (byte)98; // -> b
			else if (block[i] == 102)
				block[i] = (byte)104; // -> h
			else if (block[i] == 103)
				block[i] = (byte)100; // -> d
			else if (block[i] == 104)
				block[i] = (byte)102; // -> f
		}
		return block;
	}
	
	private static void printString(byte[] arg){
		String num="";
		try {
					
			for (byte i: arg){
				num = num + Integer.toBinaryString(i);
				System.out.println((char)i + " : " + i );
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	private static byte[] xorBlocks(byte[] block1, byte[] block2){
		byte[] ansBlock = new byte[10];
		int tmp = 0;
		for (int i = 0; i < block1.length; i++){
			if (block2[i] == 0)
				ansBlock[i] = block1[i];
			else{
				tmp = (32 + ((block1[i] ^ block2[i])%94));
				ansBlock[i] = (byte) tmp;
			}
//			System.out.println((char)ansBlock[i]);
			
		}
		return ansBlock;
	}
	
	public static void main(String[] args) throws IOException {
			
		String input = readFile(FILE_NAME);
		byte[] fixedInput = null;
		byte[] block = null;
		byte[] initVector = initializationVector();
		byte[] codedText = new byte[input.length()];
		int index = 0;
		
		fixedInput = stringToBinary(input);

//		printString(fixedInput);
			
		if (fixedInput.length == 0){
			System.out.println("input was empty");
		}
		 
		while(fixedInput.length >= 10){
			
			block = Arrays.copyOfRange(fixedInput, 0, 10);

			initVector = xorBlocks(initVector, block);

			initVector = convertByKey(initVector);			

			//save the result of AES in codedText
			for(int i = 0; i < 10; i++)
				codedText[index * 10 + i] = initVector[i];
			index++;
			
			fixedInput = Arrays.copyOfRange(fixedInput, 10, fixedInput.length);
			
		}
		printString(codedText);
		// taking care of the left overs of the input
		if(fixedInput.length > 0){
			byte[] buffedInput = new byte[10];
			
			for( int i = 0; i < 10; i++){
				if (fixedInput.length > i)
					buffedInput[i] = fixedInput[i];
				else buffedInput[i] = 0;
			}
			initVector = xorBlocks(initVector, buffedInput);

			initVector = convertByKey(initVector);	

		}
			

	}
}
