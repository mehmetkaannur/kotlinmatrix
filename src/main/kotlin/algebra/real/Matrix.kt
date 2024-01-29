package algebra.real

data class Matrix(private val vectors: List<Vector>) {
    init {
        if (vectors.isEmpty()) {
            throw IllegalArgumentException()
        }
    }

    val numRows = vectors.size
    val numColumns = vectors[1].length
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
        if (this.numRows != other.numRows || this.numColumns != other.numColumns) {
            throw UnsupportedOperationException()
        } else {
            val sumElems = (0..numRows).map { row ->
                (0..numColumns).map { column -> this[row, column] + other[row, column] }
            }
            return Matrix(sumElems.map { Vector(it) })
        }
    }

//    operator fun times(other: Double): Vector {
//        return Vector(this.doubles.map { it * other })
//    }
//
//    infix fun dot(other: Vector): Double {
//        if (this.length != other.length) {
//            throw UnsupportedOperationException()
//        } else {
//            return this.doubles.zip(other.doubles).sumOf { (a, b) -> a * b }
//        }
//    }
//
//    override fun toString(): String {
//        val finalString = StringBuilder()
//        finalString.append("(")
//        this.doubles.forEachIndexed { i, d ->
//            if (i == length - 1) {
//                finalString.append("$d")
//            } else {
//                finalString.append("$d, ")
//            }
//        }
//        finalString.append(")")
//        return finalString.toString()
//    }
}
// operator fun Double.times(other: Vector): Vector {
//    return Vector(other.doubles.map { it * this })
// }
