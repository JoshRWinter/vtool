/* Vigenere.java
   The idea here is to split the plaintext into <period> alphabets,
   and try to find the most common letter in each alphabet.

   Then a shift is applied to each alphabet in order to solve the text.
   The shift that is applied will use 4 of the most common english letter frequencies,
   "e, t, a".

   the WordFinder class will be needed to determine if valid plaintext results
   from the decrypt() method of this class.

   This solver works best on large sets of english text, with short keys.
 */
public class Vigenere extends Thread{
	private StringBuilder ctext; // target ciphertext
	private StringBuilder[] alphabet; // the stripped alphabets
	private StringBuilder key;
	private int period; // proposed length of key
	private Dictionary dict;
	private String[] foundWords; // the words that were found in the suspected plaintext

	private int lower, upper;
	public Vtool vtool;

	public Vigenere(Vtool vtool, String text, int lower, int upper){
		this.lower = lower;
		this.upper = upper;
		this.vtool = vtool;

		this.key = new StringBuilder();
		this.ctext = Vigenere.convertToUpper(text);
		this.dict = new Dictionary();
	}

	public String getKey(){
		return this.key.toString();
	}

	public String getCText(){
		return this.ctext.toString();
	}

	public String[] getFoundWords(){
		return this.foundWords;
	}

	// this is where the magic happens
	// ---------
	// set the key and return the plain text.
	public void run(){
		String configuration = "e"; // start with this, contains the letters to assume are the most common in each stripped alphabet
								    // this will grow to "et" and finally "eta"
		int attempt = 0; // total number of attempts
		String decrypted = null;
		boolean success = false; // plain text found!
		boolean giveUp = false; // tried all target configurations
		char[] p = null; // for the permutation array
		int ioc = Vigenere.indexOfCoincedence(this.ctext.toString()); // try to guess key size given index of coincedence
		if(ioc == 0)
			ioc = 1;

		while(!giveUp){ // try different target configurations until give up
			boolean tryingIOC = true;
			for(this.period = ioc; this.period < upper + 1; ++this.period){ // try range of keys

				if(!tryingIOC && this.period == ioc)
					continue; // already tried the ioc key length

				decrypted = null;
				success = false;
				this.strip(); // strip the cipher text into <this.period> alphabets
				Permuter perm = new Permuter(this.period, configuration);

				while(!success){
					++attempt;
					WordFinder wf = new WordFinder(this.dict);

					// set the status, but only necessary to do it sometimes
					int counter = perm.getCounter();
					if(counter % 12 == 0)
						this.vtool.status(new VtoolStatus(
							"working: period=" + this.period + ", conf=" + configuration + ", " + (int)((counter/Math.pow(configuration.length(),this.period))*100) + "%",
							counter/(int)Math.pow(3,this.period),
							null,
							attempt
						));

					this.key.setLength(0); // clear the key from the previous attempt

					p = perm.nextPerm();
					// skip permutations that have already been tried
					if(p != null){
						if(configuration.equals("et")){
							while(!Vigenere.arrayContains(p,'t'))
								p = perm.nextPerm();
						}
						else if(configuration.equals("eta")){
							while(!Vigenere.arrayContains(p,'a'))
								p = perm.nextPerm();
						}
					}
					else if(p == null){
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
				if(success)
					break;
				if(tryingIOC){
					tryingIOC = false;
					period = 1;
				}
			}
			if(!success){
				if(configuration.equals("e"))
					configuration = "et";
				else if(configuration.equals("et"))
					configuration = "eta";
				else{
					giveUp = true; // don't bother trying "etao", would just take too long, give up.
					decrypted = "!";
				}
			}
			else break;

		}
		StringBuilder pstring;
		if(p != null){
			pstring = new StringBuilder(p.length);
			for(int i = 0; i < p.length; ++i)
				pstring.append(p[i]);
		}
		else
			pstring = new StringBuilder("nil");
		this.vtool.status(new VtoolStatus("done! period=" + this.period + ", conf=" + configuration + ", targets=" + pstring.toString() + " attempts=" + attempt, 100, decrypted, attempt));
	}

	// decrypt <ctext> with <key>
	public static String vigenere(String ctext,String key){
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

	// encrypt <ptextString> with key <key>
	public static String encrypt(String ptextString, String key){
		StringBuilder ptext = new StringBuilder(ptextString.length());
		key = key.toUpperCase();

		// convert to uppercase, remove whitespace and incompatible characters
		for(int i = 0; i < ptextString.length(); ++i){
			char c = Character.toUpperCase(ptextString.charAt(i));
			if(Vigenere.isValidChar(c))
				ptext.append(c);
		}

		// do the thing
		StringBuilder ctext = new StringBuilder(ptext.length());
		for(int i = 0; i < ptext.length(); ++i){
			char c = ptext.charAt(i);
			c = (char)(c + (key.charAt(i % key.length()) - 'A'));
			if(c > 'Z')
				c = (char)(c - 26);
			ctext.append(c);
		}

		return ctext.toString();
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

	// calculates index of coincedence and returns suspected key period
	public static int indexOfCoincedence(String text){

		double sum = Vigenere.ioc(text);

		double[] ioctable = {
			0.0,	// 0
			0.0660, // 1
			0.0520, // 2
			0.0473, // 3
			0.0449, // 4
			0.0435, // 5
			0.0426, // 6
			0.0419, // 7
			0.0414, // 8
			0.0410, // 9
			0.0407  // 10
		};

		double[] difference = new double[10];
		// find out which value in the table above most closely matches <sum>
		for(int i = 0; i < 10; ++i)
			difference[i] = Math.abs(ioctable[i] - sum);

		int smallestDifference = 0;
		for(int i = 0; i < 10; ++i)
			if(difference[i] < difference[smallestDifference])
				smallestDifference = i;

		return smallestDifference;
	}

	public static double ioc(String text){
		int[] lf = new int[26];
		for(int i = 0; i < 26; ++i)
			lf[i] = 0;

		for(int i = 0; i < text.length(); ++i)
			++lf[text.charAt(i) - 'A'];

		double sum = 0.0;
		for(int i = 0; i < 26; ++i)
			sum = sum + (lf[i] * (lf[i] - 1.0));

		sum = sum * (1.0 / (text.length() * (text.length() - 1.0)));

		return sum;
	}

	public static StringBuilder convertToUpper(String s){
		StringBuilder sb = new StringBuilder(s.length());
		// set cipher text to all caps with no puct or spaces, just letters
		for(int i = 0; i < s.length(); ++i){
			if(Vigenere.isValidChar(s.charAt(i)))
				sb.append(Character.toUpperCase(s.charAt(i)));
		}
		return sb;
	}

	private static boolean isValidChar(char c){
		char upper = Character.toUpperCase(c);
		return upper >= 'A' && upper <= 'Z'; // only valid char if uppercase letter.
											 // this is so the solver can ignore whitespace
									 		 // and only worry about letters
	}

	// look for char <c> in <arr>
	private static boolean arrayContains(char[] arr, char c){
		for(int i = 0; i < arr.length; ++i)
			if(arr[i] == c)
				return true;

		return false;
	}
}

