import java.util.Scanner;
import java.io.File;

public class CMDMain{

	public static void main(boolean noColors, boolean quiet, String file){
		String color_green;
		String color_yellow;
		String color_reset;

		if(!noColors){
			color_green = "\033[32m";
			color_yellow = "\033[33m";
			color_reset = "\033[0m";
		}
		else{
			color_green = "";
			color_yellow = "";
			color_reset = "";
		}

		Scanner filein = null;
		Scanner consolein = new Scanner(System.in);
		boolean invalidParameter = false;
		
		// try the file passed as parameter
		try{
			filein = new Scanner(new File(file));
		}catch(Exception e){
			invalidParameter = true;
		}

		while(invalidParameter){
			invalidParameter = false;
			System.out.print("enter filename: ");
			try{
				filein = new Scanner(new File(consolein.nextLine()));
			}catch(Exception e){
				invalidParameter = true;
			}
		}

		// read the file
		String ctext = "";
		while(filein.hasNextLine()){
			ctext = ctext + filein.nextLine();
		}
		filein.close();
		consolein.close();

		// print the ciphertext first
		if(!quiet){
			System.out.println(color_yellow + "Encrypted Text: " + color_reset);
			System.out.println(ctext);
			System.out.println();
		}

		// do the main thing
		Vtool vtool = new Vtool(ctext, 1, 12);
		vtool.decrypt();

		// wait till it's done
		VtoolStatus vs;
		do{
			vs = vtool.status(null);
			// print status bar
			if(!quiet){
				System.out.print("\r                                         \r");
				System.out.print(color_green + vs.status + color_reset);
			}

			try{
				Thread.sleep(200);
			}catch(Exception e){
			}
		}while(vs.decrypted == null);

		if(!quiet){
			System.out.println(color_yellow + "\nDecrypted Text:" + color_reset);
		}

		// if it didn't get it, quit
		if(vs.decrypted.equals("!"))
			return;

		// print the resulting decrypted text
		System.out.println(vs.decrypted);

		// print the key and other info
		if(!quiet){
			System.out.print("\nkey: \"" + color_green + vtool.getKey() + color_reset + "\"\nLogical Processors: " + WordFinder.processorCount() + "\nIOC suggested a key length of " + Vigenere.indexOfCoincedence(vtool.getCText()) + "\n");
		}

		// print the found words
		if(!quiet){
			String[] foundWords = vtool.getFoundWords();
			for(int i = 0; i < foundWords.length; ++i)
				System.out.println((i + 1) + ": " + foundWords[i]);
		}
	}
}
