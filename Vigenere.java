/* Vigenere.java
   The idea here is to split the plaintext into <period> alphabets,
   and try to find the most common letter in each alphabet.

   Then a shift is applied to each alphabet in order to solve the text.
   The shift that is applied will use 4 of the most common english letter frequencies,
   "e, t, a, o".

   the WordFinder class will be needed to determine if valid plaintext results
   from the decrypt() method of this class.

   This solver works best on large sets of english text, with short keys.
 */
public class Vigenere{
	private StringBuilder ctext; // target ciphertext
	private StringBuilder[] alphabet; // the stripped alphabets
	private StringBuilder key;
	private int period; // proposed length of key
	private Dictionary dict;
	private String[] foundWords; // the words that were found in the suspected plaintext

	public Vigenere(String text){
		this.period = 6;
		this.key = new StringBuilder();
		this.ctext = new StringBuilder(text.length());
		this.dict = new Dictionary();

		// set cipher text to all caps with no puct or spaces, just letters
		for(int i = 0; i < text.length(); ++i){
			if(this.isValidChar(text.charAt(i)))
				this.ctext.append(Character.toUpperCase(text.charAt(i)));
		}
	}

	public String getKey(){
		return this.key.toString();
	}

	public String[] getFoundWords(){
		return this.foundWords;
	}

	// this is where the magic happens
	// ---------
	// set the key and return the plain text.
	public String decrypt(int lower, int upper){
		for(this.period = 1; this.period < upper + 1; ++this.period){
			String decrypted = null;
			boolean success = false;
			this.strip();
			Permuter perm = new Permuter(this.period);

			while(!success){
				WordFinder wf = new WordFinder(this.dict);
				this.key.setLength(0);
				char[] p = perm.nextPerm();
					if(p == null){
					// tried all permutations and didn't find the plaintext
					decrypted = null;
					break;
				}

					// generate the key
					for(int i = 0; i < this.alphabet.length; ++i){
						int shift = this.alphabetShift(i,p[i]);
					this.key.append((char)(shift + 'A'));
			}

					// decrypt given the key
					decrypted = Vigenere.vigenere(this.ctext.toString(), this.key.toString());

				// use dictionary search to see if it's right
				success = wf.isEnglish(decrypted);
				if(success){
					this.foundWords = wf.getFoundWords();
				}
			}

			return decrypted;
		}
	}

	// decrypt <ctext> with <key>
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

	// strip the cipher text into <this.period> alphabets
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

