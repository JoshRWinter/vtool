import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Dictionary{
	public static final int DICTIONARY_LENGTH = 52864; // divisible by 8
	public String[] word;

	public Dictionary(){
		// load the file
		InputStream is = getClass().getResourceAsStream("/american-english");
		if(is == null){
			System.err.println("\033[31;1man error occurred while trying to open dictionary file\033[0m");
			System.exit(1);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		// allocate
		this.word = new String[Dictionary.DICTIONARY_LENGTH]; // file "american_english" contains 52,855 words

		// fill the word list
		int index = 0;
		String s = null;
		try{
			while((s = br.readLine()) != null){
				s.toLowerCase();
				if(s.length() >= WordFinder.MIN_LETTERS){
					this.word[index++] = s;
				}
			}
		}catch(Exception ioe){
			System.out.println("\033[31;1man error occurred while reading dictionary file:\n" + ioe.getLocalizedMessage() + "\033[0m");
		}

		// fill the rest of 'em up with empties
		for(int i = index; i < Dictionary.DICTIONARY_LENGTH; ++i){
			this.word[i] = new String("");
		}

		try{
			br.close();
		}catch(Exception e){
			System.out.println("\033[31;1man error occurred when attempting to close input stream\033[0m");
		}
	}

	public void print(){
		for(int i = 0; i < 300; ++i)
			System.out.print(this.word[i] + "; ");
	}
}

