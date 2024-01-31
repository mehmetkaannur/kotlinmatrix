package algebra.generic

data class Vector<T>(var addition: (T, T) -> T, var multiplication: (T, T) -> T, private var contents: List<T>) {
    init {
        if (contents.isEmpty()) {
            throw IllegalArgumentException()
        }
    }

    val length = contents.size
    operator fun get(index: Int): T {
        if (index > length) {
            throw IndexOutOfBoundsException()
        } else {
            return contents[index]
        }
    }

    operator fun plus(other: Vector<T>): Vector<T> {
        if (this.length != other.length) {
            throw UnsupportedOperationException()
        } else {
            return Vector(addition, multiplication, (0..<length).map { elem -> addition(this[elem], other[elem]) })
        }
    }

    operator fun times(other: T): Vector<T> {
        return Vector(addition, multiplication, this.contents.map { multiplication(it, other) })
    }

    infix fun dot(other: Vector<T>): T {
        if (this.length != other.length) {
            throw UnsupportedOperationException()
        } else {
            return this.contents.zip(other.contents).map { (a, b) -> multiplication(a, b) }.reduce(addition)
        }
    }

    override fun toString(): String {
        val finalString = StringBuilder()
        finalString.append("(")
        this.contents.forEachIndexed { i, d ->
            if (i == length - 1) {
                finalString.append("$d")
            } else {
                finalString.append("$d, ")
            }
        }
        finalString.append(")")
        return finalString.toString()
    }

    fun getContents(): List<T> {
        return this.contents
    }

    fun getMax(): Int {
        return this.contents.maxOf { elem -> elem.toString().length }
    }
}

operator fun Any.times(other: Vector<Any>): Vector<Any> {
    return Vector(other.addition, other.multiplication, other.getContents().map { other.multiplication(it, this) })
}
