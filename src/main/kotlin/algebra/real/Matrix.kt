package algebra.real

data class Matrix(private val vectors: List<Vector>) {
    init {
        if (vectors.isEmpty()) {
            throw IllegalArgumentException()
        }
    }

    val numRows = this.vectors.size
    val numColumns = this.vectors[1].length
    operator fun get(row: Int): Vector {
        if (row > numRows) {
            throw IndexOutOfBoundsException()
        } else {
            return vectors[row]
        }
    }

    fun getRow(row: Int): Vector {
        if (row > numRows) {
            throw IndexOutOfBoundsException()
        } else {
            return this.vectors[row]
        }
    }

    fun getColumn(column: Int): Vector {
        if (column > numColumns) {
            throw IndexOutOfBoundsException()
        } else {
            return Vector(this.vectors.map { it[column] })
        }
    }

    operator fun get(row: Int, column: Int): Double {
        if (row > numRows || column > numColumns) {
            throw IndexOutOfBoundsException()
        } else {
            return this.vectors[row][column]
        }
    }

    operator fun plus(other: Matrix): Matrix {
        if (this.numColumns == other.numColumns && this.numRows == other.numRows) {
            val sumElems = (0..numRows).map { row ->
                (0..numColumns).map { column -> this[row, column] + other[row, column] }
            }
            return Matrix(sumElems.map { Vector(it) })
        } else {
            throw UnsupportedOperationException()
        }
    }

    operator fun times(other: Matrix): Matrix {
        if (this.numColumns != other.numRows) {
            throw UnsupportedOperationException()
        } else {
            val dotVectors = (0..<this.numRows).map { row ->
                Vector(
                    (0..<other.numColumns).map { column ->
                        this.getRow(row) dot other.getColumn(column)
                    },
                )
            }
            return Matrix(dotVectors)
        }
    }

    operator fun times(other: Double): Matrix {
        return Matrix(this.vectors.map { it * other })
    }

    fun getVectors(): List<Vector> {
        return this.vectors
    }

    override fun toString(): String {
        val finalString = StringBuilder()
        this.vectors.forEachIndexed { rIndex, _ ->
            val rowElems = this.getRow(rIndex).getDoubles()
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

operator fun Double.times(other: Matrix): Matrix {
    return Matrix(other.getVectors().map { it * this })
}
