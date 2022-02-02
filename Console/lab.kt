import java.io.File
import kotlin.collections.arrayListOf
import kotlin.text.toIntOrNull 
import java.util.Scanner

var alphabet = ('A'..'Z').toMutableList() + ('a'..'z').toMutableList()
val scanner = Scanner(System.`in`)


fun <T : Any> Iterable<T>.cycle(): Sequence<T> = sequence {
    val saved = mutableListOf<T>()
    for (elem in this@cycle) {
        saved.add(elem)
        yield(elem)
    }
    while (true) {for (elem in saved) yield(elem)}
}

fun main(args: Array<String>) {

    println("This program is encrypting a text from txt file with Viegener algorythm")
    println("Written by Melnik")

    var ans:Boolean = true
    var numStr:Char = ' '

    while (ans) {
        println("1 - encrypt text from input")
        println("2 - decrypt text from input")
        println("3 - encrypt text from a file")
        println("4 - decrypt text from a file")
        println("0 - exit")
        print("\nPlease, enter any num: ")

        try {
            numStr = scanner.next().single()
        } catch(e: IllegalArgumentException) {}
        
        if (numStr == '0') ans = false

        else if (numStr == '1') {
            print("Enter a word to encrypt: ")
            var word = readLine().toString()
            //word = String(word.toByteArray(), charset("UTF-8"))

            print("Enter your key: ")
            var key = readLine().toString()
            var check = true
            run test@ {
                key.forEach{ 
                    if (it.isLetter()) check = true
                    else {
                        check = false
                        return@test
                    }
                }
            }
            
            if(check == true) encrypt(word, key)
            else println("Wrong key input")
            //key = String(key.toByteArray(), charset("UTF-8"))

            //encrypt(word, key)

        } else if (numStr == '2') {
            print("Enter your word: ")
            var wordN = readLine().toString()

            print("Enter your key: ")
            var keyN = readLine().toString()

            var fileName = decrypt(wordN, keyN)
            println("\nOutput file created: $fileName")

        } else if (numStr == '3') {
            println("Your text")
            var word = fileRead()
            if (word == " ") continue

            print("Enter your key: ")
            var key = readLine().toString()

            encrypt(word, key)
        } else if (numStr == '4') {
            println("Your text")
            var word = fileRead()
            if (word == " ") continue

            print("Enter your key: ")
            var key = readLine().toString()

            println("Output file created: ${decrypt(word, key)}")
        }
        else println("\nWrong num input")
        numStr = ' '
    }
}

fun fileRead(): String {
    print("Enret input file name:")
    val fileName = readLine()
    val file = File(fileName)
    if(file.exists() && !file.isDirectory()) {
        var str:String = ""
        file.forEachLine() {str += it}
        return str
    } else {
        println("File doesn't exist!\n")
    }
    return " "
}

fun fileWrite(x:ArrayList<Char>):String {
    var i:Int = 0
    var text:String = ""
    x.forEach {text += it}
    var file = File("out_$i.txt")
    while (file.exists()) {
        i++
        file = File("out_$i.txt")
    }
    file.writeText(text)
    file.createNewFile()
    
    return file.name
}

fun encrypt(word:String = "", key:String = "") {

    var initialText = arrayListOf<Char>()
    var initialKey = arrayListOf<Char>()
    var cyphered = arrayListOf<Char>()

    for (x in key) initialKey.add(x)
    var finalKey = arrayListOf<Char>()

    for (x in word) initialText.add(x)
    var iter:Int = 0
    var i:Int = 0
    initialText.forEach {
        if (it.isLetter()) {
            finalKey.add(initialKey.cycle().elementAt(i-iter))
            i++
        }
        else {
            if(i == 0 || i <= iter) {finalKey.add(it) }
            else {
                iter++
                finalKey.add(it)
            }
        }
    }
    i = 0

    initialText.forEach() {
        if (it in alphabet) {
            var x = (alphabet.indexOf(it) + alphabet.indexOf(finalKey[i]))%52
            cyphered.add(alphabet[x])
        } else {
            cyphered.add(it)
        }
        i++
    }

    print("Encrypted text: ")
    cyphered.forEach { print(it) }
    println("\n")
}

fun decrypt(word:String = "", key:String = ""):String {
    var finalKey = arrayListOf<Char>()
    var decrypted = arrayListOf<Char>()

    var initialText = arrayListOf<Char>()
    var initialKey = arrayListOf<Char>()

    for (x in word) {initialText.add(x)}
    for (x in key)  {initialKey.add(x)}
    var fileIter:Int = 0
    val fileName = "demo_$fileIter.txt"
    val file = File(fileName)

    var iter:Int = 0
    var i:Int = 0

    initialText.forEach {
        if (it.isLetter()) {
            finalKey.add(initialKey.cycle().elementAt(i-iter))
            i++
        } else {
            if(i == 0 || i <= iter) {finalKey.add(it) }
            else {
                iter++
                finalKey.add(it)
            }
        }
    }

    i = 0
    initialText.forEach {
        if (it in alphabet) {
            var x = (alphabet.indexOf(it) - alphabet.indexOf(finalKey[i]) + 52)%52
            decrypted.add(alphabet[x])
        } else {
            decrypted.add(it)
        }
        i++
    }

    var filename = fileWrite(decrypted)
    return (filename)
}