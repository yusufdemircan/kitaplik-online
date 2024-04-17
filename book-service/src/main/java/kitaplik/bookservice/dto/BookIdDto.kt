package kitaplik.bookservice.dto

import kitaplik.bookservice.model.Book

data class BookIdDto(
        val bookId: String,
        val isbn: String
) {
    companion object {
        fun convert(id:String,isbn: String):BookIdDto{
            return BookIdDto(id,isbn)
        }
    }
}
