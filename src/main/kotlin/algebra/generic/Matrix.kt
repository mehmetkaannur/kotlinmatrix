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
            val sumElems = (0..numRows).map { row ->
                (0..numColumns).map { column -> addition(this[row, column], other[row, column]) }
            }
            return Matrix<T>(addition, multiplication, sumElems.map { Vector(addition, multiplication, it) })
        }
    }

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
        return Matrix<T>(addition, multiplication, this.vectors.map { it * other })
    }

    fun getVectors(): List<Vector<T>> {
        return this.vectors
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

operator fun Any.times(other: Matrix<Any>): Matrix<Any> {
    return Matrix<Any>(other.addition, other.multiplication, other.getVectors().map { it * this })
}
