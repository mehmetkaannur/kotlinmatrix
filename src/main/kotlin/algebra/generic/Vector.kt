package algebra.generic

interface Vector<T> {
    fun add(a: T, b: T): T
    fun mul(a: T, b: T): T
}
data class Vector(private val doubles: List<Double>) {
    init {
        if (doubles.isEmpty()) {
            throw IllegalArgumentException()
        }
    }

    val length = doubles.size
    operator fun get(index: Int): Double {
        if (index > length) {
            throw IndexOutOfBoundsException()
        } else {
            return doubles[index]
        }
    }

    operator fun plus(other: Vector): Vector {
        if (this.length != other.length) {
            throw UnsupportedOperationException()
        } else {
            return Vector((0..<length).map { elem -> this[elem] + other[elem] })
        }
    }

    operator fun times(other: Double): Vector {
        return Vector(this.doubles.map { it * other })
    }

    infix fun dot(other: Vector): Double {
        if (this.length != other.length) {
            throw UnsupportedOperationException()
        } else {
            return this.doubles.zip(other.doubles).sumOf { (a, b) -> a * b }
        }
    }

    override fun toString(): String {
        val finalString = StringBuilder()
        finalString.append("(")
        this.doubles.forEachIndexed { i, d ->
            if (i == length - 1) {
                finalString.append("$d")
            } else {
                finalString.append("$d, ")
            }
        }
        finalString.append(")")
        return finalString.toString()
    }

    fun getDoubles(): List<Double> {
        return this.doubles
    }

    fun getMax(): Int {
        return this.doubles.maxOf { elem -> elem.toString().length }
    }
}

operator fun Double.times(other: Vector): Vector {
    return Vector(other.getDoubles().map { it * this })
}
