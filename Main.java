public class Main{
	public static void main(String[] args){
		Vigenere v;
		String s = "hey there";
		v = new Vigenere(s);
		System.out.println("Encrypted text: \"" + s + "\"");
		System.out.println("Decrypted text: \"" + v.decrypt() + "\"");
	}
}

