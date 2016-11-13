all: Main.class Vigenere.class WordFinder.class WordFinderWorker.class Permuter.class Dictionary.class
	java Main

Main.class: Main.java
	javac Main.java

Vigenere.class: Vigenere.java
	javac Vigenere.java

WordFinder.class: WordFinder.java
	javac WordFinder.java

WordFinderWorker.class: WordFinderWorker.java
	javac WordFinderWorker.java

Permuter.class: Permuter.java
	javac Permuter.java

Dictionary.class: Dictionary.java
	javac Dictionary.java

clean:
	rm *.class

