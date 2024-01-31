package algebra.generic

data class Matrix<T>(var addition: (T, T) -> T, var multiplication: (T, T) -> T, var vectors: List<Vector<T>>) {
    init {
        if (vectors.isEmpty()) {
            throw IllegalArgumentException()
        }
    }

    val numRows = this.vectors.size
    val numColumns = this.vectors[1].length
    operator fun get(row: Int): Vector<T> {
        if (row > numRows) {
            throw IndexOutOfBoundsException()
        } else {
            return vectors[row]
        }
    }

    fun getRow(row: Int): Vector<T> {
        if (row > numRows) {
            throw IndexOutOfBoundsException()
        } else {
            return this.vectors[row]
        }
    }

    fun getColumn(column: Int): Vector<T> {
        if (column > numColumns) {
            throw IndexOutOfBoundsException()
        } else {
            return Vector(addition, multiplication, this.vectors.map { it[column] })
        }
    }

    operator fun get(row: Int, column: Int): T {
        if (row > numRows || column > numColumns) {
            throw IndexOutOfBoundsException()
        } else {
            return this.vectors[row][column]
        }
    }

    operator fun plus(other: Matrix<T>): Matrix<T> {
        if (this.numColumns != other.numColumns || this.numRows != other.numRows) {
            throw UnsupportedOperationException()
        } else {
            val sumElems = (0..<numRows).map { row ->
                (0..<numColumns).map { column -> addition(this[row, column], other[row, column]) }
            }
            return Matrix<T>(addition, multiplication, sumElems.map { Vector(addition, multiplication, it) })
        }
    }

    @JvmName("matricies")
    operator fun times(other: Matrix<Matrix<T>>): Matrix<Matrix<T>> {
        if (this.numColumns != other.numRows) {
            throw UnsupportedOperationException()
        } else {
            val elems = (0..<this.numRows).map { row ->
                (0..<this.numColumns).map { column -> this * other[row, column] }
            }
            return makeMatrix({x, y -> x.plus(y)}, {x, y -> times(y)}, elems)
        }
    }

    @JvmName("matrix")
    operator fun times(other: Matrix<T>): Matrix<T> {
        if (this.numColumns != other.numRows) {
            throw UnsupportedOperationException()
        } else {
            val dotVectors = (0..<this.numRows).map { row ->
                Vector(addition, multiplication,
                        (0..<other.numColumns).map { column ->
                            this.getRow(row) dot other.getColumn(column)
                        },
                )
            }
            return Matrix<T>(addition, multiplication, dotVectors)
        }
    }

    operator fun times(other: T): Matrix<T> {
            val elems = (0..<this.numRows).map { row ->
                (0..<this.numColumns).map { column ->
                            multiplication(this[row, column], other)
                        }
            }
            return Matrix(addition, multiplication, elems.map { Vector(addition, multiplication, it) })
    }

    override fun toString(): String {
        val finalString = StringBuilder()
        this.vectors.forEachIndexed { rIndex, _ ->
            val rowElems = this.getRow(rIndex).getContents()
            finalString.append("[ ")
            rowElems.forEachIndexed { cIndex, elem ->
                val longest = this.getColumn(cIndex).getMax()
                val difference = longest - elem.toString().length
                val add = (" ".repeat(difference)) + elem.toString() + " "
                finalString.append(add)
            }
            if (rIndex != this.numRows - 1) {
                finalString.append("]\n")
            } else {
                finalString.append("]")
            }
        }
        return finalString.toString().trimIndent()
    }
}
fun <T> makeMatrix(plus: (T, T) -> T, times: (T, T) -> T, list: List<List<T>>): Matrix<T> {
    val vectorList: List<Vector<T>> = list.map { Vector(plus, times, it) }
    return Matrix(plus, times, vectorList)
}
operator fun Any.times(other: Matrix<Any>): Matrix<Any> {
    val elems = (0..<other.numRows).map { row ->
        (0..<other.numColumns).map { column ->
            other.multiplication(other[row, column], other)
        }
    }
    return Matrix(other.addition, other.multiplication, elems.map { Vector(other.addition, other.multiplication, it) })
}
