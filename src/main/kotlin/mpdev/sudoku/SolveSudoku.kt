package mpdev.sudoku

import java.util.*

fun main (args: Array<String>) {

    val mySudoku = SuDoku()

    // load the game from file
    if (args.isNotEmpty())
        mySudoku(args[0])
    else
        error("please specify input file")

    if (mySudoku.isOk()) {
        println("Trying to solve:")
        mySudoku.print()
    }
    else
        error("sudoku could not be read")

    val timestart = Date()
    println(timestart.toString())

    val solvedSuDoku: SuDoku? = mySudoku.solveIt()

    println()

    if (solvedSuDoku != null) {
        println()
        println(solvedSuDoku.getCount().toString()+" attempts")
        println("SuDoku was solved successfully")
        println("-----------------------")
        solvedSuDoku.print()
        println("-----------------------")
    }
    else {
        println()
        println(mySudoku.getCount().toString()+" attempts")
        println("No solution was found")
    }

    val timeEnd = Date()
    val duration: Long = timeEnd.time - timestart.time
    println(timeEnd.toString())
    println("KOTLIN Solution took: $duration msec")
}
