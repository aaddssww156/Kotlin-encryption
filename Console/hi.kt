package vigener
import java.io.File
import kotlin.collections.arrayListOf
import kotlin.text.toIntOrNull 
//import org.example.*

var alphabet = ('A'..'Z').toMutableList() + ('a'..'z').toMutableList()
//var alphabet = arrayListOf<Int>()


fun <T : Any> Iterable<T>.cycle(): Sequence<T> = sequence {
    val saved = mutableListOf<T>()
    for (elem in this@cycle) {
    saved.add(elem)
    yield(elem)
    }
    while (true) {
    for (elem in saved) yield(elem)
    }
}

fun main(args: Array<String>) {
    //var strToEncode = fileRead()
    //encrypt(strToEncode, "key")
    //println(strToEncode.length)
    //encrypt("hello, world!", "hi")
    //decrypt("word", "key")
    //println(alphabet)
    /*for (x in alphabetLetters) {
        alphabet.add(x.code)
    }
    alphabet.forEach { println(it) }*/
    //alphabet.forEach { println(it) }
    println("This program is encrypting a text from txt file with Viegener algorythm")
    println("Written by Melnik")
    var ans:Boolean = true
    var numStr:String
    while (ans) {
        println("1 - encrypt text from a file")
        println("2 - decrypt text to a file")
        println("0 - exit")
        print("\nPlease, enter any num: ")
        numStr = readLine().toString()
        var num = numStr.toIntOrNull()
        if (num == 0) ans = false
        else if (num == 1) {
            print("Enter a word to encrypt: ")
            var word = readLine().toString()
            //var word = openTextFile()

            print("Enter your key: ")
            var key = readLine().toString()

            encrypt(word, key)
        } else if (num == 2) {
            print("Enter your word: ")
            var wordN = readLine().toString()
            print("Enter your key: ")
            var keyN = readLine().toString()
            var fileName = decrypt(wordN, keyN)
            println("\nOutput file created: $fileName")

        }
        else println("\nWrong num input")
    }
}

fun fileRead(): String {
    print("Enret input file name:")
    val fileName = readLine()
    val file = File(fileName)
    var str:String = ""
    file.forEachLine() {str += it}
    //println(str)
    return str
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
    //var cyphered:String = ""

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
    iter = 0
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

    /*initialText.forEach {
       if (it.code in alphabet) {
            var x = (alphabet.indexOf(it.code) + alphabet.indexOf(finalKey[i].code))%52
            cyphered.add(alphabet.elementAt(x).toChar())
       } else {
           cyphered.add(it)
       }
    }*/
    println("Encrypted text: ")
    cyphered.forEach { print(it) }
    println()
    //return strings()

   // println(finalKey)
    
   // for (i in 1..finalKey.size-1) {
   //     println("The ${finalKey[i]} is ${finalKey[i].code}")
   // }
    

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

    //print("Decrypted text: ")
    var filename = fileWrite(decrypted)
    return (filename)
    //var text:String = ""
    //decrypted.forEach { text+=it }
    //file.writeText(text)
    //file.createNewFile()
}

//data class strings(val word, val key:String)
/**
* Make an [Sequence] returning elements from the iterable and saving a copy of each.
* When the iterable is exhausted, return elements from the saved copy. Repeats indefinitely.
*
*/
// Перебираем элементы, возвращаем генератор элементов и заново их пускает

//changeText()