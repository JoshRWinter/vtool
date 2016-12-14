vtool - vigenere encrypter and decrypter by josh winter

SOURCE CODE --------------------------------
source code is provided inside the .jar (you can unzip a .jar as if it were a .zip file by renaming the extension)
source code in git repository format can be found at https://bitbucket.org/JoshRWinter/vtool

BASIC USAGE ------------------------------------
type or paste text into the main window
 -encrypt with the encrypt button and provide a key
 -decrypt with the decrypt button (key optional)

if you don't provide a key when decrypting, then vtool will try to guess the key by stripping the alphabets (guessing the key size by first applying the "index of coincedence" then trying key sizes from 1 to 12) and assuming that the most common letters in each stripped alphabet will be one of e, t, or a. a dictionary (50,000 english words and lots of proper nouns) is used to confirm correctly decrypted plain text. the dictionary must be able to find at least 12 words of at least 7 letters in order for any of this to work.

COMPILATION INSTRUCTIONS -------------------------------
run compile.sh or compile.bat

or

javac *.java

jar cfm vtool.jar manifest *.class american-english
(vtool must be run from jar format, because it's expecting to read the "american-english" dictionary file from inside a jar)

then to run vtool do
java -jar vtool.jar

or

double click on vtool.jar

vtool also provides a (limited) text mode in environments where a window can not be created (such as over an ssh connection).

