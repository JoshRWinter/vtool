Main.class: Main.java Vigenere.class WordFinder.class
	javac Main.java
	java Main

Vigenere.class: Vigenere.java
	javac Vigenere.java

WordFinder.class: WordFinder.java
	javac WordFinder.java

clean:
	rm *.class

