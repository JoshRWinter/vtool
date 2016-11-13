import java.lang.Thread;

public class WordFinderWorker extends Thread{
	private String text;
	private int lower;
	private int upper;
	private int workerID;

	private WordFinder wf;

	public WordFinderWorker(WordFinder wf, String text, int lower, int upper, int workerID){
		this.text = text;
		this.lower = lower;
		this.upper = upper;
		this.workerID = workerID;

		this.wf = wf;
	}

	public void run(){
		// loop through all the words in the words list
		for(int i = this.lower; i < this.upper; ++i){

			if(this.wf.dict.word[i].length() == 0){
				return;
			}

			// look for any occurrence of <WordFinder.word[i]> in <text>
			if(text.contains(this.wf.dict.word[i])){
				if(this.wf.addFoundWord(this.wf.dict.word[i],this.workerID)){
					return;
				}
			}
		}
	}

}

