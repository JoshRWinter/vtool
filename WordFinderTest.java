public class WordFinderTest{
	public static void main(String[] args){
		WordFinder wf = new WordFinder();

		String s1 =
			"hello. this is some english text." +
			"this is more english text abaft abacus aback abalone"
			;

		String s2 =
			"this should not pass"
			;

		String s3 =
			"this should pass pretty well" +
			"there's really no reason why it wouldn't pass."
			;

		String s4 =
			"there"
			;

		System.out.println("s1: " + wf.isEnglish(s1));
		System.out.println();

		System.out.println("s2: " + wf.isEnglish(s2));
		System.out.println();

		System.out.println("s3: " + wf.isEnglish(s3));
		System.out.println();

		System.out.println("s4: " + wf.isEnglish(s4));
		System.out.println();

	}
}

