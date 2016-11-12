import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class WordFinder{

	private String[] word;
	public static final int WORD_COUNT = 10;
	public static final int MIN_LETTERS = 6;
	public boolean valid;

	private static final int DICTIONARY_LENGTH = 61500;

	public WordFinder(){
		// load the file
		Scanner wordsFile;
		try{
		   wordsFile = new Scanner(new File("american-english"));
		}catch(FileNotFoundException fnf){
			System.err.println("\033[31merror: could not find words file \"american_english\"\033[0m");
			this.valid = false;
			return;
		}
		this.valid = true;

		// allocate the arrays
		this.word = new String[DICTIONARY_LENGTH]; // file "american_english" contains around 86,000 words

		// fill the this.word list
		int index = 0;
		while(wordsFile.hasNextLine()){
			String s = wordsFile.nextLine().toLowerCase();
			if(s.length() >= WordFinder.MIN_LETTERS){
				this.word[index++] = new String(s);
			}
		}

		// fill the rest of 'em up with empties
		for(int i = index; i < DICTIONARY_LENGTH; ++i){
			this.word[i] = "";
		}

		wordsFile.close();
		for(int i = 0; i < 20; ++i)
			System.out.println(this.word[i]);
	}

	// pass a string, returns true if WORD_COUNT unique words of at least MIN_LETTERS are found.
	// reliable enough for checking if something decrypted properly
	public boolean isEnglish(String text){
		String[] foundWords = new String[WordFinder.WORD_COUNT];

		for(int i = 0; i < WordFinder.WORD_COUNT; ++i)
			foundWords[i] = "";

		if(!this.valid)
			return false;

		// loop through all the words in the words list
		int foundWordsIndex = 0;
		for(int i = 0; i < this.word.length; ++i){
			// don't find words that have already been founj
			boolean conflict = false;
			for(int j = 0; j < WordFinder.WORD_COUNT; ++j){
				if(this.word[i].equals(foundWords[j])){
					conflict = true;
					break;
				}
			}
			if(conflict)
				continue;

			// look for any occurrence of <this.word[i]> in <text>
			if(text.contains(this.word[i])){
				foundWords[foundWordsIndex++] = this.word[i];

				if(foundWordsIndex == WORD_COUNT){
					// print the found words list
					for(int x = 0; x < foundWords.length; ++x){
						System.out.println("\033[32mthis.foundWords[" + x + "] == \"" + foundWords[x] + "\";\033[0m");
					}

					return true;
				}
			}
			/*System.out.print("\r\033[32;1m[" + (int)(((float)i/this.word.length)*100) + "% done] (found " + foundWordsIndex + " so far)\033[0m");*/
		}

		//System.out.println("foundWordsIndex == " + foundWordsIndex);
		// print the found words list
		/*for(int i = 0; i < foundWords.length; ++i){
			System.out.println("\033[31mthis.foundWords[" + i + "] == \"" + foundWords[i] + "\";\033[0m");
		}*/
		return false;
	}
}

