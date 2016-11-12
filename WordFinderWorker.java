import java.lang.Thread;

public class WordFinderWorker extends Thread{
	private String text;
	private int lower;
	private int upper;
	private int workerID;

	public WordFinderWorker(String text, int lower, int upper, int workerID){
		this.text = text;
		this.lower = lower;
		this.upper = upper;
		this.workerID = workerID;
	}

	// add words to WordFinder.foundWords
	// return true if WordFinder.foundWordsIndex >= WordFinder.WORD_COUNT
	private synchronized boolean addFoundWords(String fw){
		if(WordFinder.foundWordsIndex >= WordFinder.WORD_COUNT)
			return true;

		WordFinder.foundWords[WordFinder.foundWordsIndex] = new String(fw + " (worker " + this.workerID + ")");
		WordFinder.foundWordsIndex++;
		return WordFinder.foundWordsIndex >= WordFinder.WORD_COUNT;
	}

	public void run(){
		// loop through all the words in the words list
		for(int i = this.lower; i < this.upper; ++i){

			if(WordFinder.word[i].length() == 0)
				return;

			// look for any occurrence of <WordFinder.word[i]> in <text>
			if(text.contains(WordFinder.word[i])){
				if(this.addFoundWords(WordFinder.word[i])){
					return;
				}
			}
		}
	}

}

