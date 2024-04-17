package kitaplik.libraryservice.dto

data class BookDto @JvmOverloads constructor(
        val id: BookIdDto? = null,
        val title: String? = "",
        val bookYear: Int? = 0,
        val author: String? = "",
        val pressName: String? = "",
)