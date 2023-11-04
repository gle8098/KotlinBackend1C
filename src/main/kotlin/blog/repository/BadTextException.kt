package blog.repository

class BadTextException(val code: Code) : Exception() {
    enum class Code {
        TEXT_TOO_LONG
    }
}
