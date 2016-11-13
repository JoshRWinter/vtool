import java.lang.Thread;

public class WordFinder extends Thread{
	public static final int WORD_COUNT = 12; // how many words to look for
	public static final int MIN_LETTERS = 7; // only count words with at least this many letters

	private int worker_count; // number of threads to use
	private int workload;

	public String[] foundWords;
	public int foundWordsIndex;
	public Dictionary dict;

	public WordFinder(Dictionary dict){
		// allocate the arrays
		this.foundWords = new String[WordFinder.WORD_COUNT];
		this.foundWordsIndex = 0;
		this.dict = dict;

		int logicals = Runtime.getRuntime().availableProcessors();
		if(logicals == 4 || logicals == 8 || logicals == 12 || logicals == 16 || logicals == 32)
			this.worker_count = logicals;
		else
			this.worker_count = 8;
		this.workload = Dictionary.DICTIONARY_LENGTH / this.worker_count;
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

		WordFinderWorker[] worker = new WordFinderWorker[this.worker_count];

		// spawn the workers
		for(int i = 0; i < this.worker_count; ++i){
			worker[i] = new WordFinderWorker(this, text, i * this.workload, (i * this.workload) + this.workload, i);
			worker[i].start();
		}

		// wait for workers to finish
		for(int i = 0; i < this.worker_count; ++i){
			try{
				worker[i].join();
			}catch(Exception e){
				System.out.println("\033[31;1mAn exception occurred in worker[" + i + "].join()\033[0m");
			}
		}

		return this.foundWordsIndex == WordFinder.WORD_COUNT;
	}
}

