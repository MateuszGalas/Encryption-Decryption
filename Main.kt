package encryptdecrypt

import java.io.File

// Encrypting by shifting letters by key amount, after "z" comes "a"
fun shiftEncrypt(text: String, key: Int): String {
    var result = ""
    text.forEach {
        result += when (it) {
            in 'a'..'z' -> if (it + key > 'z') (it + key - 26) else it + key
            in 'A'..'Z' -> if (it + key > 'Z') (it + key - 26) else it + key
            else -> it
        }
    }
    return result
}

// Decrypting by shifting letters by key amount, after "a" comes "z"
fun shiftDecrypt(text: String, key: Int): String {
    var result = ""
    text.forEach {
        result += when (it) {
            in 'a'..'z' -> if (it - key < 'a') (it - key + 26) else it - key
            in 'A'..'Z' -> if (it - key < 'A') (it - key + 26) else it - key
            else -> it
        }
    }
    return result
}

// Encrypting by adding key value to letters
fun unicodeEncrypt(text: String, key: Int): String {
    var result = ""
    text.forEach { result += (it + key) }
    return result
}

// Decrypting by adding key value to letters
fun unicodeDecrypt(text: String, key: Int): String {
    var result = ""
    text.forEach { result += (it - key) }
    return result
}

// Saving result to File
fun writeToFile(file: String, result:String) {
    val fileOut = File(file)
    when {
        file.isEmpty() -> println(result)
        fileOut.exists() -> fileOut.writeText(result)

        else -> {
            fileOut.createNewFile()
            fileOut.writeText(result)
        }
    }
}

fun decrypt(text: String, key: Int, file: String, algorithm: String) {
    val result = if (algorithm == "shift") shiftDecrypt(text, key) else unicodeDecrypt(text, key)
    writeToFile(file, result)
}

fun encrypt(text: String, key: Int, file: String, algorithm: String) {
    val result = if (algorithm == "shift") shiftEncrypt(text, key) else unicodeEncrypt(text, key)
    writeToFile(file, result)
}

// Takes mode, data, key, fileIn, FileOut, algorithm arguments and determines what user wants to do.
fun main(args: Array<String>) {
    var mode = "enc"
    var data = ""
    var key = 0
    var fileIn = ""
    var fileOut = ""
    var algorithm = "shift"


    for (i in args.indices) {
        if (args[i] == "-mode") mode = args[i + 1]
        if (args[i] == "-data") data = args[i + 1]
        if (args[i] == "-key") key = args[i + 1].toInt()
        if (args[i] == "-in") fileIn = args[i + 1]
        if (args[i] == "-out") fileOut = args[i + 1]
        if (args[i] == "-alg") algorithm = args[i + 1]
    }

    if (data.isEmpty()) {
        val file = File(fileIn)
        if (file.exists()) data = file.readText()
    }

    when(mode){
        "enc" -> encrypt(data, key, fileOut, algorithm)
        "dec" -> decrypt(data, key, fileOut, algorithm)
    }
}