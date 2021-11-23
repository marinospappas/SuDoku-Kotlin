package mpdev.sudoku

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class SuDokuConverted {
    //
    // private class variables
    //
    private val data = Array(ROWS) { IntArray(COLS) } // our initial SuDoku puzzle

    /**
     * Returns the state of the SuDoku data (used when initialising from file)
     *
     * @return        True if SuDoku data is loaded (all 9x9 squares with 1-9 or 0), false otherwise
     */ // boolean isDataComplete()
    var isDataComplete // the state of our SuDoku puzzle (used when initialising from file)
            = false
        private set
    private val nextEmpty = IntArray(2) // the coordinates of the next empty square (top left is 0,0)

    /**
     * Constructor from file
     * 9 lines, 9 numbers (1-9) or 'x' or '-' for empty square) in each line
     * empty lines and spaces to improve the look of the file are allowed
     *
     * @param filename        the String filename containing the SuDoku puzzle
     */
    internal constructor(filename: String) {
        isDataComplete = false
        try {
            // open file
            val infile = FileReader(filename)
            val textLine = BufferedReader(infile)

            // read 9 non-empty lines
            var i = 0
            var s: String?
            do {
                s = textLine.readLine()
                if (s != null) {
                    val charsRead = s.toCharArray()
                    var j = 0
                    var charIndx = 0
                    while (j < COLS && charIndx < charsRead.size) {
                        if (charsRead[charIndx] == 'x' || charsRead[charIndx] == 'X' || charsRead[charIndx] == '-') data[i][j++] =
                            0 else if (charsRead[charIndx] >= '0' && charsRead[charIndx] <= '9') data[i][j++] =
                            charsRead[charIndx] - '0'
                        ++charIndx
                    }
                    if (j == COLS) ++i
                }
            } while (s != null && i < ROWS)
            if (i == ROWS) isDataComplete = true
            // close file and return
            textLine.close()
        } catch (e: IOException) {
            println(e.message)
        }
    } // SuDoku (String filename)

    /**
     * Constructor form integer 2xdimensional array
     * numbers 1-9 or 0 for empty square
     *
     * @param inputData        the 9x9 int[][] array containing the SuDoku puzzle
     */
    internal constructor(inputData: Array<IntArray>) {
        for (i in 0 until ROWS) System.arraycopy(inputData[i], 0, data[i], 0, COLS)
    } // SuDoku (int[][] inputData)

    internal constructor()

    /** returns ok status for this sudoku (= all squares read from file */
    fun isOk(): Boolean = isDataComplete


    /**
     * Prints the SuDoku contents
     */
    fun print() {
        for (i in 0 until ROWS) {
            if (i > 0 && i % 3 == 0) println()
            for (j in 0 until COLS) {
                if (j > 0 && j % 3 == 0) print("   ")
                if (data[i][j] == 0) print("x ") else print(data[i][j].toString() + " ")
            }
            println()
        }
    } // void printData()

    /**
     * Tries to solve the SuDoku puzzle
     *
     * @return        the SuDoku object that contains the solution or null if unsolvable
     */
    fun solveIt(): SuDokuConverted? {

        // get a new object to ry to solve (so that if it fails we retain the original object inact)
        val mySuDoku: SuDokuConverted = SuDokuConverted(data)

        // find the next empty square
        if (mySuDoku.findNextEmpty(nextEmpty)) {
            // try each valid number in it
            for (num in (1..9)) {
                if (okInRow(num, nextEmpty[0]) && okInCol(num, nextEmpty[1]) && okInSquare(num, nextEmpty)) {
                    // if this number is ok then put it in and try to solve further
                    mySuDoku.setSquare(nextEmpty, num)
                    ++count
                    val s: SuDokuConverted? = mySuDoku.solveIt();
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

    /**
     * Set a SuDoku square to a number
     *
     * @param coord        the coordinates of the SuDoku square
     * @param num        the number to set it to
     */
    fun setSquare(coord: IntArray, value: Int): Unit {
        data[coord[0]][coord[1]] = value
    }

    /**
     * Finds the next empty square in the 9x9 matrix
     *
     * @param coord        returns the coordinates of the next empty square
     * @return            true if an empty square was found, false otherwise
     */
    fun findNextEmpty(coord: IntArray): Boolean {
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

    /**
     * Checks if a specific number is acceptable within a specific row
     *
     * @param number    the number to be checked
     * @param row        the row against which the number will be checked
     * @return            true if the number does not already exist in the row
     */
    fun okInRow(num: Int, rowIndx: Int): Boolean {
        for (i in 0 until COLS)
            if (data[rowIndx][i] == num)
            // number found in the row so this number is not ok to add
                return false
        // not found so ok to add this number
        return true
    }

    /**
     * Checks if a specific number is acceptable within a specific column
     *
     * @param number    the number to be checked
     * @param col        the column against which the number will be checked
     * @return            true if the number does not already exist in the column
     */
    fun okInCol(num: Int, colIndx: Int): Boolean {
        for (i in 0 until ROWS)
            if (data[i][colIndx] == num)
            // number found in the column so this number is not ok to add
                return false
        // not found so ok to add this number
        return true
    }

    /**
     * Checks if a specific number is acceptable within the 3x3 square
     *
     * @param number    the number to be checked
     * @param coord        the coordinates of the number in question in the 9x9 square
     * @return            true if the number does not already exist in the 3x3 square
     */
    fun okInSquare(num: Int, coord: IntArray): Boolean {
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

    companion object {
        const val ROWS = 9
        const val COLS = 9

        /**
         * Returns the count of the attempts to solve the puzzle
         * @return        count of attempts
         */ // int getCount()
        var count = 0 // the count of the attempts
            private set
    }

    /** returns the count of attepmts to solve */
    fun getCount(): Int = count

} // class SuDoku

