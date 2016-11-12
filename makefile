Main.class: Main.java Vigenere.class WordFinder.class WordFinderWorker.class Permuter.class
	javac Main.java
	java Main

Vigenere.class: Vigenere.java
	javac Vigenere.java

WordFinder.class: WordFinder.java
	javac WordFinder.java

WordFinderWorker.class: WordFinderWorker.java
	javac WordFinderWorker.java

Permuter.class: Permuter.java
	javac Permuter.java

clean:
	rm *.class

