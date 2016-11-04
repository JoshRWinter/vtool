import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class Main{
	public static void main(String[] args){
		Scanner consolein = new Scanner(System.in);
		Scanner filename;
		System.out.print("enter the filename for the ciphertext: ");
		String filepath = consolein.nextLine();
		if(filepath.length() == 0)
			filepath = "cipher1.txt";

		try{
			filename = new Scanner(new File(filepath));
		}catch(FileNotFoundException fnf){
			System.err.println("that file doesn't exist or is not readable.\nExiting...\n");
			return;
		}

		Vigenere v;
		String s = filename.nextLine();
		v = new Vigenere(s);
		System.out.println("Encrypted text:\n\033[1m" + s + "\033[0m\n\n");
		boolean correct = false;
		while(!correct){
			System.out.println("Decrypted text:\n\033[1m" + v.decrypt() + "\033[0m\n\n");
			System.out.println("\nThe key is \"\033[32m" + v.getKey() + "\033[0m\"");
			System.out.println("Is this correct? y/N ");
			String response = consolein.nextLine();
			correct = response.equals("y") || response.equals("Y");
		}
	}
}

