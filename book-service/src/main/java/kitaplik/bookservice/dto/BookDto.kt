package kitaplik.bookservice.dto

import kitaplik.bookservice.model.Book

data class BookDto(
        val id: BookIdDto? = null,
        val title: String = "",
        val bookYear: Int = 0,
        val author: String = "",
        val pressName: String = "",
) {
    companion object {
        fun convert(from: Book): BookDto {
            return BookDto(
                    from.id?.let { BookIdDto.convert(it, from.isbn)},
                    from.title,
                    from.bookYear,
                    from.author,
                    from.pressName)
        }
    }
}