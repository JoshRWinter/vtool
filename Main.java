import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class Main{
	public static void main(String[] args){
		Scanner consolein = new Scanner(System.in);
		Scanner filename;
		System.out.print("enter the filename for the ciphertext: ");
		try{
			filename = new Scanner(new File(/*consolein.nextLine()*/"cipher.txt"));
		}catch(FileNotFoundException fnf){
			System.err.println("that file doesn't exist or is not readable.\nExiting...\n");
			return;
		}

		Vigenere v;
		String s = filename.nextLine();
		v = new Vigenere(s);
		System.out.println("Encrypted text: \"" + s + "\"");
		System.out.println("Decrypted text: \"" + v.decrypt() + "\"");
	}
}

