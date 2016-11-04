public class Vigenere{
	private StringBuilder ctext;
	private StringBuilder[] alphabet; // the stripped alphabets
	private StringBuilder key;
	private int period;
	private final char[] target = {'e','t','a','o','i'};
	private int targetIndex;

	public Vigenere(String text){
		this.period = 6;
		this.targetIndex = 0;
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

	public String getKey(){
		return this.key.toString();
	}

	// this is where the magic happens
	public String decrypt(){
		this.strip();
		this.key.setLength(0); // clear any previous attempts at the key

		for(int i = 0; i < this.period; ++i){
			int shift = this.alphabetShift(i,this.target[this.targetIndex]);
			this.key.append((char)((shift) + 'A'));
		}
		return Vigenere.vigenere(this.ctext.toString(),this.key.toString());
	}

	private static String vigenere(String ctext,String key){
		StringBuilder ptext = new StringBuilder();
		for(int i = 0; i < ctext.length(); ++i){
			char c = (char)(ctext.charAt(i) - 'A');
			char k = (char)(key.charAt(i % key.length()) - 'A');

			if(c < k)
				c = (char)(c + 26);
			c = (char)(c - k);

			ptext.append((char)(c + 'a'));
		}
		return ptext.toString();
	}

	// this function, given the index into this.alphabet, will
	// guess the shift that, once applied, yields plaintext
	// for that alphabet. if it doesn't think that it is a valid
	// alphabet
	// target is the most common char to check for (eg. e, t, a)
	private int alphabetShift(int index, char target){
		target = (char)(Character.toUpperCase(target) - 'A');
		int mostCommon = findMostCommon(this.alphabet[index].toString()) - 'A';

		if(mostCommon >= target)
			return mostCommon - target;
		else
			return (mostCommon - target) + 26;
	}

	// return true if the most common letter in a string is plausible for english
	private static boolean plausible(String s){
		char mostCommon = Vigenere.findMostCommon(s);
		return mostCommon == 'E' || mostCommon == 'T' || mostCommon == 'A' || mostCommon == 'O';
	}

	// search for and return the most common character in a String
	private static char findMostCommon(String s){
		int[] lf = new int[26];
		for(int i = 0; i < 26; ++i) lf[i] = 0;

		for(int i = 0; i < s.length(); ++i){
			lf[s.charAt(i) - 'A']++;
		}

		int mostCommon = 0;
		for(int i = 1; i < 26; ++i){
			if(lf[i] > lf[mostCommon])
				mostCommon = i;
		}

		return (char)(mostCommon + 'A');
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

