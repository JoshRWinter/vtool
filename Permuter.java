/* Permuter.java (josh winter)

   for key length 'n', there will be 'n' stripped alphabets.
   each of the 'n' stripped alphabets will have it's own key,
   a shift to be applied to encrypt and decrypt. vtool uses
   letter frequency analysis to guess the shift that needs to
   be applied.

   a reasonable guess for the most common letter in each stripped
   alphabet is 'e'. so whatever the most common letter is, (say: 'd'
   for example), the shift to get back to 'e' will be calculated
   (in this case, a shift of one). however, 'e' might, by random
   chance, not be the most common letter. the next choices will
   be 't', and 'a'. in addition to 'n' stripped alphabets, vtool
   will maintain a list of 'n' elements: where each element can be
   one of 'e', 't', or 'a'. all permutations of this list
   (with repetion) will be applied to the stripped alphabets in order
   to eventually get the desired plaintext (that's 3^n different
   versions!).

   the purpose of this class is to handle the calculation of the
   permutations (with repetition) and supply the Vigenere class
   with each list starting with "e,e,e,..." all the way down to
   "a,a,a,...".
*/

public class Permuter{
	private char[] target;
	private char[] copy; // to be returned to caller
	private int n;
	private int counter; // which permutation is it currently on

	private String elements; // the targets to permute

	public Permuter(int n, String elements){
		this.n = n;
		this.elements = elements;
		this.target = new char[this.n];
		this.copy = new char[this.n];
		this.counter = 0;

		for(int i = 0; i < this.n; ++i)
			this.target[i] = this.elements.charAt(0);
	}

	public int getCounter(){
		return this.counter;
	}

	// returns null if no more permutations are left
	public char[] nextPerm(){
		if(this.counter == 0){
			++this.counter;
			return this.target;
		}
		if(this.finished())
			return null;

		++this.counter;

		int focus = this.n - 1;
		while(focus >= 0){
			this.target[focus] = this.nextTarget(this.target[focus]);
			if(this.target[focus] == this.elements.charAt(0)){
				--focus;
			}
			else
				break;
		}

		for(int i = 0; i < this.n; ++i)
			this.copy[i] = this.target[i];

		return this.copy;
	}

	// no more permutations?
	private boolean finished(){
		for(int i = 0; i < this.n; ++i){
			if(this.target[i] != this.elements.charAt(this.elements.length() - 1))
				return false;
		}
		return true;
	}

	private char nextTarget(char c){
		if(c == this.elements.charAt(this.elements.length() - 1))
			return this.elements.charAt(0);

		// find <c> in <this.elements> and return the next char in the string after <c>
		int index;
		for(int i = 0; i < this.elements.length(); ++i)
			if(c == this.elements.charAt(i))
				return this.elements.charAt(i+1);

		// shouldn't ever get to this point i hope
		return '!';
	}
}
