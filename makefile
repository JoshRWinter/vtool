all: vtool.jar
	java -jar vtool.jar

vtool.jar: GraphicalMain.class Main.class Vigenere.class WordFinder.class WordFinderWorker.class Permuter.class Dictionary.class Vtool.class VtoolStatus.class american-english
	jar cfm vtool.jar manifest *.class american-english

GraphicalMain.class: GraphicalMain.java
	javac GraphicalMain.java

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

Vtool.class: Vtool.java
	javac Vtool.java

VtoolStatus.class: VtoolStatus.java
	javac VtoolStatus.java

clean:
	rm *.class

