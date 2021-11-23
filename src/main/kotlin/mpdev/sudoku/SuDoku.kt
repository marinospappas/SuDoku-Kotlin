package mpdev.sudoku

import java.io.File

//class SuDoku(fileName: String? = null) {
class SuDoku {


    private var isOk: Boolean = false

    private var data: Array<IntArray> = Array(ROWS) {IntArray(COLS) }

    private val nextEmpty: IntArray = IntArray(2)

    // static vars
    companion object {
        const val ROWS: Int = 9
        const val COLS: Int = 9
        var count: Int = 0; private set
    }

    /**
     * Constructor from file - using invoke
     */
    operator fun invoke(fileName: String?) {
        if (fileName != null) {
            val bufferedReader = File(fileName).bufferedReader()

            var rindx = 0
            var cindx = 0
            bufferedReader.useLines { lines: Sequence<String> ->
                // loop through all the lines
                rindx = 0
                lines.forEach { line ->
                    // loop through all the characters in the line
                    val x: CharArray = line.toCharArray()
                    val row = IntArray(COLS) { 0 }
                    cindx = 0
                    x.forEach { char ->
                        if (cindx < COLS) {
                            if (char in ('0'..'9'))
                                row[cindx++] = char - '0'
                            else
                                if (char == 'X' || char == 'x' || char == '-')
                                    row[cindx++] = 0
                        }
                    }
                    if (cindx >= COLS)
                        data[rindx++] = row
                }
            }
            // if all rows and clos have been read, set to OK
            if (rindx >= ROWS && cindx >= COLS)
                isOk = true
        }
    }

    /** constructor from Int array - using invoke */
    operator fun invoke(data: Array<IntArray>) {
        for (i in 0 until ROWS)
            this.data[i] = data[i].copyOf()
        isOk = true
    }

    /** internal constructor from Int array */
    internal constructor(data: Array<IntArray>) {
        for (i in 0 until ROWS)
            this.data[i] = data[i].copyOf()
        isOk = true
    }

    /** default internal constructor */
    internal constructor()

    /* create Sudoku from Int array *
    fun fromArray(data: Array<IntArray>): SuDoku {
        val s: SuDoku = SuDoku()
        for (i in 0 until ROWS)
            s.data[i] = data[i].copyOf()
        s.isOk = true
        return s
    }*/

    /** returns ok status for this sudoku (= all squares read from file */
    fun isOk(): Boolean = isOk

    /** returns the count of attepmts to solve */
    fun getCount(): Int = count

    /** prints the contents of SuDoku */
    fun print() {
        for (i in 0 until ROWS) {
            if (i > 0 && i % 3 == 0)
                    println()
            for (j in 0 until COLS) {
                if (j > 0) {
                    print(' ')
                    if (j % 3 == 0)
                        print("  ")
                }
                if (data[i][j] == 0)
                    print('x')
                else
                    print(data[i][j])
            }
            println()
        }
    }

    /** try to solve the sudoku */
    fun solveIt(): SuDoku? {

        // get a new object to ry to solve (so that if it fails we retain the original object inact)
        // val mySuDoku: SuDoku = SuDoku().fromArray(data)
        //val mySuDoku: SuDoku = SuDoku()
        //mySuDoku(data) invoke
        val mySuDoku = SuDoku(data)


        // find the next empty square
        if (mySuDoku.findEmpty(nextEmpty)) {
            // try each valid number in it
            for (num in (1..9)) {
                if (okInRow(num, nextEmpty[0]) && okInCol(num, nextEmpty[1]) && okInSquare(num, nextEmpty)) {
                    // if this number is ok then put it in and try to solve further
                    mySuDoku.setSquare(nextEmpty, num)
                    ++count
                    val s: SuDoku? = mySuDoku.solveIt();
                    if (s != null)
                        // good solution - return to caller
                        return s
                }
            }
            // no more numbers to try - cannot solve it
            return null
        }
        else
            // no more empty squares we have reached the end of the recursion - return the SOLVED sudoku object
            return this
    }

    /** find the next empty square */
    private fun findEmpty(coord: IntArray): Boolean {
        // scan the data table for the first empty square
        for (i in 0 until ROWS)
            for (j in 0 until COLS)
                if (data[i][j] == 0) {
                    // found it
                    coord[0] = i
                    coord[1] = j
                    return true
                }

        // not found return null
        return false
    }

    /** check if a number is ok in the row */
    private fun okInRow(num: Int, rowIndx: Int): Boolean {
        for (i in 0 until COLS)
            if (data[rowIndx][i] == num)
            // number found in the row so this number is not ok to add
            return false
        // not found so ok to add this number
        return true
    }

    /** check if a number is ok in the column */
    private fun okInCol(num: Int, colIndx: Int): Boolean {
        for (i in 0 until ROWS)
            if (data[i][colIndx] == num)
                // number found in the column so this number is not ok to add
                return false
        // not found so ok to add this number
        return true
    }

    /** check if the number is ok in te square */
    private fun okInSquare(num: Int, coord: IntArray): Boolean {
        val startRow: Int = (coord[0]/3) * 3
        val startCol: Int = (coord[1]/3) * 3
        for (i in 0..2)
            for (j in 0..2)
                if (data[startRow + i][startCol + j] == num)
                // number found in the square so this number is not ok to add
                    return false
        // not found so ok to add this number
        return true
    }

    /** set the value of a square */
    private fun setSquare(coord: IntArray, value: Int) {
        data[coord[0]][coord[1]] = value
    }
}