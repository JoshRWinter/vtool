public class Vigenere{
	private StringBuilder ctext;
	private StringBuilder[] alphabet; // the stripped alphabets
	private StringBuilder key;
	private int period;

	public Vigenere(String text){
		this.period = 6;
		this.key = new StringBuilder();
		this.ctext = new StringBuilder(text.length());
		for(int i = 0; i < text.length(); ++i){
			if(this.isValidChar(text.charAt(i)))
				this.ctext.append(Character.toUpperCase(text.charAt(i)));
		}
	}

	public int getPeriod(){
		return this.period;
	}

	public String decrypt(){
		this.strip();
		this.key.setLength(0); // clear any previous attempts at the key

		for(int i = 0; i < this.period; ++i){
			System.out.println("\033[33mMost common is " + this.alphabetShift(i) + "\033[0m");
			this.key.append((char)(this.alphabetShift(i) + 'A'));
		}
		System.out.println("\033[32mThe key is: \"" + key.toString() + "\"\033[0m");
		return "no idea browski";
	}

	// this function, given the index into this.alphabet, will
	// guess the shift that, once applied, yields plaintext
	// for that alphabet. if it doesn't think that it is a valid
	// alphabet, (based on letter frequencies) returns -1.
	private int alphabetShift(int index){
		int[] lf = new int[26];
		for(int i = 0; i < 26; ++i)lf[i] = 0;

		for(int i = 0; i < this.alphabet[index].length(); ++i){
			lf[this.alphabet[index].charAt(i) - 'A']++;
		}

		int common = 0;
		for(int i = 1; i < 26; ++i){
			if(lf[i] > lf[common])
				common = i;
		}

		if(common >= 4)
			return common - 4; // 4 is 'E'
		else
			return (common - 4) + 26;
	}


	private void strip(){
		this.alphabet = new StringBuilder[this.period];

		// one loop for <this.period> stripped alphabets
		for(int i = 0; i < period; ++i){
			this.alphabet[i] = new StringBuilder();

			// loop and extract the <this.period>th letter
			for(int j = 0; j < this.ctext.length(); ++j){
				if(j % period == i)
					this.alphabet[i].append(this.ctext.charAt(j));
			}
		}
	}

	private boolean isValidChar(char c){
		char upper = Character.toUpperCase(c);
		return upper >= 'A' && upper <= 'Z'; // only valid char if uppercase letter.
											 // this is so the solver can ignore whitespace
									 		 // and only worry about letters
	}
}

