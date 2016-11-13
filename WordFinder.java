import java.lang.Thread;

public class WordFinder extends Thread{
	public static final int WORD_COUNT = 12; // how many words to look for
	public static final int MIN_LETTERS = 7; // only count words with at least this many letters

	private static final int WORKER_COUNT = 8; // reasonable assumption for # of logical processors
	private static final int WORKLOAD = Dictionary.DICTIONARY_LENGTH / WordFinder.WORKER_COUNT; // workload for each worker

	public String[] foundWords;
	public int foundWordsIndex;
	public Dictionary dict;

	public WordFinder(Dictionary dict){
		// allocate the arrays
		this.foundWords = new String[WordFinder.WORD_COUNT];
		this.foundWordsIndex = 0;
		this.dict = dict;
	}

	public String[] getFoundWords(){
		String[] copy = new String[WordFinder.WORD_COUNT];
		for(int i = 0; i < WordFinder.WORD_COUNT; ++i)
			copy[i] = new String(this.foundWords[i]);

		return copy;
	}

	// add words to WordFinder.foundWords
	// return true if WordFinder.foundWordsIndex >= WordFinder.WORD_COUNT
	public synchronized boolean addFoundWord(String fw, int wid){
		if(this.foundWordsIndex >= WordFinder.WORD_COUNT)
			return true;

		this.foundWords[this.foundWordsIndex] = fw + " [via worker " + wid + "]";
		this.foundWordsIndex++;
		return this.foundWordsIndex >= WordFinder.WORD_COUNT;
	}

	// pass a string, returns true if WORD_COUNT unique words of at least MIN_LETTERS are found.
	// reliable enough for checking if something decrypted properly
	// search from Dictionary.word[lower] to Dictionary.word[upper]
	public boolean isEnglish(String text){
		for(int i = 0; i < WordFinder.WORD_COUNT; ++i)
			this.foundWords[i] = new String("");

		WordFinderWorker[] worker = new WordFinderWorker[WordFinder.WORKER_COUNT];

		// spawn the workers
		for(int i = 0; i < WordFinder.WORKER_COUNT; ++i){
			worker[i] = new WordFinderWorker(this, text, i * WordFinder.WORKLOAD, (i * WordFinder.WORKLOAD) + WORKLOAD, i);
			worker[i].start();
		}

		// wait for workers to finish
		for(int i = 0; i < WordFinder.WORKER_COUNT; ++i){
			try{
				worker[i].join();
			}catch(Exception e){
				System.out.println("\033[31;1mAn exception occurred in worker[" + i + "].join()\033[0m");
			}
		}

		return this.foundWordsIndex == WordFinder.WORD_COUNT;
	}
}

