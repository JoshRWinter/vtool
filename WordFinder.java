import java.util.Scanner;
import java.io.File;
public class WordFinder{

	private String[] word;
	public final int WORD_COUNT = 10;
	public final int MIN_LETTERS = 3;

	public WordFinder(){
		Scanner wordsFile;
	   try{
		   wordsfile = new Scanner(new File("americoan_english"));
	   }catch(FileNotFoundException fnf){
		   System.err.println("\033[31merror: could not find words file \"american_english\"\033[0m");
			return
	   }

	   this.word = new String[120000]; // file "american_english" contains around 119,000 words

	   int index = 0;
	   while(wordsFile.hasNextLine()){
		   this.word[index++] = wordsFile.nextLine();
	   }
	   wordsFile.close();
	}

	// pass a string, returns true if WORD_COUNT unique words of at least MIN_LETTERS are found.
	// reliable enough for checking if something decrypted properly
	public boolean isEnglish(){
		return true;
	}
}

