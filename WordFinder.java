import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Thread;

public class WordFinder extends Thread{

	public static String[] word;
	public static final int WORD_COUNT = 10;
	public static final int MIN_LETTERS = 6;
	public boolean valid;

	private static final int DICTIONARY_LENGTH = 61504;
	private static final int WORKER_COUNT = 8; // reasonable assumption for # of logical processors
	private static final int WORKLOAD = WordFinder.DICTIONARY_LENGTH / WordFinder.WORKER_COUNT;

	public static String[] foundWords;
	public static int foundWordsIndex;

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
		WordFinder.word = new String[DICTIONARY_LENGTH]; // file "american_english" contains around 86,000 words
		WordFinder.foundWords = new String[WordFinder.WORD_COUNT];
		WordFinder.foundWordsIndex = 0;

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
			this.word[i] = new String("");
		}

		wordsFile.close();
		/*for(int i = 0; i < 20; ++i)
			System.out.println(this.word[i]);*/
	}

	// pass a string, returns true if WORD_COUNT unique words of at least MIN_LETTERS are found.
	// reliable enough for checking if something decrypted properly
	// search from this.word[lower] to this.word[upper]
	public boolean isEnglish(String text){
		if(!this.valid)
			return false;

		for(int i = 0; i < WordFinder.WORD_COUNT; ++i)
			this.foundWords[i] = new String("");

		WordFinderWorker[] worker = new WordFinderWorker[WordFinder.WORKER_COUNT];

		// spawn the workers
		for(int i = 0; i < WordFinder.WORKER_COUNT; ++i){
			worker[i] = new WordFinderWorker(text, i * WordFinder.WORKLOAD, (i * WordFinder.WORKLOAD) + WORKLOAD, i);
			worker[i].start();
		}

		// wait for workers to finish
		for(int i = 0; i < WordFinder.WORKER_COUNT; ++i){
			try{
				worker[i].join();
			}catch(Exception e){
			}
		}

		boolean success = WordFinder.foundWordsIndex == WordFinder.WORD_COUNT;

		// print found words
		if(success)
			System.out.print("\033[32;1m");
		else
			System.out.print("\033[31;1m");

		for(int i = 0; i < WordFinder.foundWords.length; ++i){
			System.out.println("WordFinder.foundWords[" + i + "] == \"" + WordFinder.foundWords[i] + "\";");
		}
		System.out.println("\033[0m");
		return success;
	}
}

