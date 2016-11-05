import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class WordFinder{

	private String[] foundWords;
	private String[] word;
	public static final int WORD_COUNT = 10;
	public static final int MIN_LETTERS = 3;

	public WordFinder(){
		Scanner wordsFile;
		try{
		   wordsFile = new Scanner(new File("americoan_english"));
		}catch(FileNotFoundException fnf){
			System.err.println("\033[31merror: could not find words file \"american_english\"\033[0m");
			return;
		}

		this.word = new String[78000]; // file "american_english" contains around 77,120 words
		this.foundWords = new String[WordFinder.WORD_COUNT];

		int index = 0;
		while(wordsFile.hasNextLine()){
			this.word[index++] = wordsFile.nextLine();
		}
		// fill the rest of 'em up with empties
		for(int i = index; i < 78000; ++i){
			this.word[i] = "";
		}
		wordsFile.close();
	}

	// pass a string, returns true if WORD_COUNT unique words of at least MIN_LETTERS are found.
	// reliable enough for checking if something decrypted properly
	public boolean isEnglish(String text){
		for(int i = 0; i < this.word.length; ++i){
			if(this.word[i].length() < WordFinder.MIN_LETTERS)
				continue;
			boolean conflict = false;
			for(int j = 0; j < WordFinder.WORD_COUNT; ++j){
				if(this.word[i].equals(this.foundWords[j])){
					conflict = true;
					break;
				}
			}
			if(conflict)
				continue;

			if(this.word[i].matches(text))
				return true;
		}
		return false;
	}
}

