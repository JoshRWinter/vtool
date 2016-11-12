Main.class: Main.java Vigenere.class WordFinder.class DictionarySearchWorker.class
	javac Main.java
	java Main

Vigenere.class: Vigenere.java
	javac Vigenere.java

WordFinder.class: WordFinder.java
	javac WordFinder.java

DictionarySearchWorker.class: DictionarySearchWorker.java
	javac DictionarySearchWorker.java

clean:
	rm *.class

