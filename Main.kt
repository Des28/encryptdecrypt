package encryptdecrypt

import java.io.File

fun main(args: Array<String>) {
    var command = "enc"
    var str = ""
    var n = 0
    var inputFileName = ""
    var outputFileName = ""
    var algorithm = "shift"
    for (i in 0..args.size-1 step 2) {
        when (args[i]) {
            "-mode" -> command = args[i + 1]
            "-key" -> n = args[i + 1].toInt()
            "-data" -> str = args[i + 1]
            "-in" -> inputFileName = args[i + 1]
            "-out" -> outputFileName = args[i + 1]
            "-alg" -> algorithm = args[i + 1]
        }
    }

    val inputData = getInputData(inputFileName, str)

    val result = when (algorithm) {
        "shift" -> when (command) {
            "enc" -> shiftEnc(inputData, n)
            "dec" -> shiftDec(inputData, n)
            else -> {
                println("Error: unknown command")
                ""
            }
        }
        "unicode" -> when (command) {
            "enc" -> unicodeEnc(inputData, n)
            "dec" -> unicodeDec(inputData, n)
            else -> {
                println("Error: unknown command")
                ""
            }
        }
        else -> {
            println("Error: unknown algorithm")
            ""
        }
    }

    if (outputFileName.isNotEmpty()) {
        writeResultToFile(outputFileName, result)
    } else {
        println(result)
    }
}

fun unicodeEnc(str: String, number: Int): String {
    return str.map { (it + number) }.joinToString("")
}

fun unicodeDec(str: String, number: Int): String {
    return str.map { (it - number) }.joinToString("")
}

fun shiftEnc(str: String, number: Int): String {
    fun shift(ch: Char, num: Int, start: Char, end: Char): Char {
        return if (ch in start..end) {
            val shifted = ch + num % 26
            if (shifted > end) shifted - 26 else shifted
        } else ch
    }
    return str.map { shift(shift(it, number, 'a', 'z'), number, 'A', 'Z') }.joinToString("")
}

fun shiftDec(str: String, number: Int): String {
    fun shift(ch: Char, num: Int, start: Char, end: Char): Char {
        return if (ch in start..end) {
            val shifted = ch - num % 26
            if (shifted < start) shifted + 26 else shifted
        } else ch
    }
    return str.map { shift(shift(it, number, 'a', 'z'), number, 'A', 'Z') }.joinToString("")
}

fun getInputData(inputFileName: String, data: String): String {
    return if (inputFileName.isNotEmpty()) {
        try {
            File(inputFileName).readText()
        } catch (e: Exception) {
            println("Error: input file not found")
            ""
        }
    } else {
        data
    }
}

fun writeResultToFile(outputFileName: String, result: String) {
    try {
        File(outputFileName).writeText(result)
    } catch (e: Exception) {
        println("Error: could not write to file")
    }
}
