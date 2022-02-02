import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import java.awt.event.*
import java.awt.Font
import java.io.File
import java.awt.FlowLayout

var frame = JFrame("Melnik")
var textLable = JLabel("Text")
var keyLable = JLabel("Key")
var wordTextField = JTextArea()
var keyTextField = JTextArea()
var chooseFileButton = JButton("Choose a file")
var encryptButton = JButton("Encrypt!")
var decryptButton = JButton("Decrypt!")
var howToButton = JButton("?")
var fileChooser = JFileChooser()
var wordFile = File("")
var encryptFile = File("")
var decryptFile = File("")
var textDropBoxArr = arrayOf("Choose an option", "Text", "File")
var alphabet = ('A'..'Z').toMutableList() + ('a'..'z').toMutableList()
var textDropBox = JComboBox(textDropBoxArr)

var key:String = ""
var word:String = ""

fun main() {

    textLable.setBounds(10, 0, 100, 100)
    textLable.setFont(Font("Sans Serif", Font.PLAIN, 32))

    textDropBox.addActionListener() { selectingDropbox() }
    textDropBox.setBounds(100, 40, 100, 30)

    howToButton.addActionListener() { howToMessage() }
    howToButton.setBounds(360, 0, 20, 20)

    wordTextField.setBounds(10, 100, 360, 150)
    wordTextField.setToolTipText("Enter your text")
    wordTextField.addCaretListener() { wordInputed() }
    wordTextField.setWrapStyleWord(true);
    wordTextField.setVisible(false)

    chooseFileButton.setBounds(10, 100, 360, 150)
    chooseFileButton.addActionListener() { openTextFile() }
    chooseFileButton.setVisible(false)
    
    keyLable.setBounds(10, 250, 100, 100)
    keyLable.setFont(Font("Sans Serif", Font.PLAIN, 32))

    keyTextField.setBounds(10, 350, 360, 50)
    keyTextField.addCaretListener() { keyInputed() }

    encryptButton.setBounds(10, 410, 360, 50)
    decryptButton.setBounds(10, 470, 360, 50)
    encryptButton.addActionListener() {saveEncrypt()}
    decryptButton.addActionListener() {saveDecrypt()}

    fileChooser.setDialogTitle("Choose a txt file")
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
    fileChooser.setCurrentDirectory(wordFile.getAbsoluteFile())
    fileChooser.setFileFilter(FileNameExtensionFilter("Text", "txt"))

    frame.add(textLable)
    frame.add(textDropBox)
    frame.add(wordTextField)
    frame.add(chooseFileButton)
    frame.add(keyLable)
    frame.add(keyTextField)
    frame.add(encryptButton)
    frame.add(decryptButton)
    frame.add(howToButton)

    frame.setSize(400, 570)
    frame.setLayout(null)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setResizable(false)
    frame.setVisible(true)

}

fun openTextFile() {
    var res = fileChooser.showOpenDialog(null)
    if (res == JFileChooser.APPROVE_OPTION){
        try {
            wordFile = fileChooser.getSelectedFile()
            
            if (wordFile.extension != "txt") {
                JOptionPane.showMessageDialog(null, "Only txt files allowed!")
            } else {
                word = ""
                wordFile.forEachLine() { word += it }
                wordTextField.setText(word)
            }
        } catch(e: NullPointerException) {}
    } else {
        JOptionPane.showMessageDialog(null, "Canceled!")
    }
}

fun selectingDropbox() {
    var selectedText = textDropBox.getSelectedItem().toString()
    textLable.setText(selectedText)

    if(selectedText == "Text") {
        wordTextField.setVisible(true)
        chooseFileButton.setVisible(false)
    } else if (selectedText == "File") {
        wordTextField.setVisible(false)
        chooseFileButton.setVisible(true)
        wordTextField.setText("")
    } else {
        wordTextField.setVisible(false)
        chooseFileButton.setVisible(false)
        wordTextField.setText("")
    }
}

fun wordInputed() {word = wordTextField.getText()}
fun keyInputed()  {key = keyTextField.getText()}

fun saveEncrypt() {
    var res = fileChooser.showSaveDialog(null)
    if (res == JFileChooser.APPROVE_OPTION) {
        try {
            encryptFile = fileChooser.getSelectedFile()

            if (encryptFile.extension != "txt") {
                JOptionPane.showMessageDialog(null, "Only txt files allowed!", "Whoops!", JOptionPane.ERROR_MESSAGE)
            } else if (word.length == 0 || key.length == 0){
                JOptionPane.showMessageDialog(null, "Text or Key can't be empty!!", "Whoops!", JOptionPane.ERROR_MESSAGE)
            } else {
                encryptFile.writeText(encrypt(word, key))
            }
        } catch(e: NullPointerException) {}
    } else {
        JOptionPane.showMessageDialog(null, "Canceled!")
    }
}

fun encrypt(wordE:String =" ", keyE:String =" "): String {
    var initialText = arrayListOf<Char>()
    var initialKey = arrayListOf<Char>()

    var cyphered = arrayListOf<Char>()
    var finalKey = arrayListOf<Char>()

    for (x in keyE) initialKey.add(x)
    for (x in wordE) initialText.add(x)
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

    var res:String = ""
    println("Cool")
    cyphered.forEach { res+=it }

    return res
}

fun saveDecrypt() {
    var res = fileChooser.showSaveDialog(null)
    if (res == JFileChooser.APPROVE_OPTION) {
        try {
            decryptFile = fileChooser.getSelectedFile()

            if (decryptFile.extension != "txt") {
                JOptionPane.showMessageDialog(null, "Only txt files allowed!")
            } else if (word.length == 0 || key.length == 0){
                JOptionPane.showMessageDialog(null, "Text or Key can't be empty!!", "Whoops!", JOptionPane.ERROR_MESSAGE)
            } else {
                decryptFile.writeText(decrypt(word, key))
            }
        } catch(e: NullPointerException) {}
    }
}

fun decrypt(wordD: String = " ", keyD: String = " "): String {
    var finalKey = arrayListOf<Char>()
    var decrypted = arrayListOf<Char>()

    var initialText = arrayListOf<Char>()
    var initialKey = arrayListOf<Char>()

    for (x in wordD) {initialText.add(x)}
    for (x in keyD)  {initialKey.add(x)}

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

    var res:String = ""
    println("Cool")
    decrypted.forEach { res+=it }

    return res
}

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

fun howToMessage() {
    var messageHTML ="<HTML><p>&nbsp;</p><h1 style=\"color: #5e9ca0;\">Quick HowTo</h1><p>This program works with vigenere cipher</p><p><span style=\"text-decoration: underline;\">Written by Melnik</span></p><h2 style=\"color: #2e6c80;\">How to use this program:</h2><h3 style=\"color: #2e6c80;\">If you want to encrypt a message:</h3><ol><li>Choose and option from dropbox</li><li>Choose a file \\ type your text</li><li>Type a key</li><li>Click \"Ecnrypt!\"</li><li>Save a file</li></ol><h3 style=\"color: #2e6c80;\">If you want to decrypt a message:</h3><ol><li>Choose and option from dropbox</li><li>Choose a file \\ type your text</li><li>Type a key</li><li>Click \"Decrypt!\"</li><li>Save a file</li></ol><h3 style=\"color: #2e6c80;\">Some useful features:</h3><p>&nbsp; &nbsp; You can edit your input file's text straight from the program - switch to \"Text\" in dropbox after choosing a file.</p><p>Text from your input file will be in the text block and you can edit it.</p><p>&nbsp;</p></HTML>"

    var msg = JLabel(messageHTML)
    JOptionPane.showMessageDialog(null, msg)
}