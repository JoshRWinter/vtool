import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Dictionary{
	public static final int DICTIONARY_LENGTH = 61504; // divisible by 8
	public String[] word;

	public Dictionary(){
		// load the file
		Scanner wordsFile;
		try{
		   wordsFile = new Scanner(new File("american-english"));
		}catch(FileNotFoundException fnf){
			System.err.println("\033[31merror: could not find words file \"american_english\"\033[0m");
			return;
		}

		// allocate
		this.word = new String[Dictionary.DICTIONARY_LENGTH]; // file "american_english" contains around 61,000 words

		// fill the word list
		int index = 0;
		while(wordsFile.hasNextLine()){
			String s = wordsFile.nextLine().toLowerCase();
			if(s.length() >= WordFinder.MIN_LETTERS){
				this.word[index++] = s;
			}
		}

		// fill the rest of 'em up with empties
		for(int i = index; i < Dictionary.DICTIONARY_LENGTH; ++i){
			this.word[i] = new String("");
		}

		wordsFile.close();
	}

	public void print(){
		for(int i = 0; i < 300; ++i)
			System.out.print(this.word[i] + "; ");
	}
}

