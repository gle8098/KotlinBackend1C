package blog.repository.exceptions

class BadParamsException(
    val code: Code
): Exception() {
    enum class Code {
        NAME_OCCUPIED,
        NAME_TOO_LONG,
        WEAK_PASSWORD,
        WRONG_USER
    }

    override fun toString(): String {
        return "BadParamsException($code)"
    }
}