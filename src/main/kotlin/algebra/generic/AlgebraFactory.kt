package algebra.generic

class AlgebraFactory<T>(var plus: (T, T) -> T, var times: (T, T) -> T) {
    fun makeVector(elems: List<T>): Vector<T> {
        return Vector(plus, times, elems)
    }

    fun makeMatrix(elems: List<List<T>>): Matrix<T> {
        val vectors: List<Vector<T>> = elems.map { Vector(plus, times, it) }
        return Matrix(plus, times, vectors)
    }
}
