public class Vigenere{
	private StringBuilder ctext;
	private StringBuilder[] alphabet; // the stripped alphabets

	public Vigenere(String text){
		this.ctext = new StringBuilder(text.length());
		for(int i = 0; i < text.length(); ++i){
			this.ctext.append(Character.toUpperCase(text.charAt(i)));
		}
	}

	public String decrypt(){
		return this.ctext.toString();
	}

	private boolean isValidChar(char c){
		return c >= 'A' && c <= 'Z'; // only valid char if uppercase letter.
									 // this is so the solver can ignore whitespace
									 // and only worry about letters
	}
}

