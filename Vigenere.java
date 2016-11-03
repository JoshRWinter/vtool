public class Vigenere{
	private StringBuilder ctext;
	private StringBuilder[] alphabet; // the stripped alphabets
	private int period;

	public Vigenere(String text){
		this.period = 3;
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

		for(int i = 0; i < this.period; ++i){
			System.out.println(i + ": " + this.alphabet[i].toString());
		}
		return "no idea bruh";
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

