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

		String s = filename.nextLine();
		Vtool v = new Vtool(s, 1, 12);

		// show the encrypted text
		System.out.println("\033[33mEncrypted text:\033[0m\n" + s + "\n");

		// decrypt it
		v.decrypt();

		// keep track of its progress
		VtoolStatus status;
		do{
			try{
				Thread.sleep(300);
			}catch(Exception e){};
			status = v.status(null);
			System.out.print("\r\033[32m" + status.status + "\033[0m");
		}while(status.decrypted == null);
		System.out.println();

		if(!status.decrypted.equals("!")){
			// show the decrypted text
			System.out.println("\033[33mDecrypted text:\033[0m\n\033[1m" + status.decrypted + "\033[0m\n\n");
			// show the key
			System.out.println("\nThe key is \"\033[1;32m" + v.getKey() + "\033[0m\"");
			// show the matched words that verified this plaintext
			System.out.println("These are the words that verified the plain text:");
			String[] foundWords = v.getFoundWords();
			for(int i = 0; i < foundWords.length; ++i)
				System.out.println(i + ": \"\033[32m" + foundWords[i] + "\033[0m\"");
		}
		else
			System.out.println("\033[1;31mvtool could not decrypt this message\033[0m");
	}
}

