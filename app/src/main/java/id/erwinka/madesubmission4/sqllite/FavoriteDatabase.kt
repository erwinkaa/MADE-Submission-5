package id.erwinka.madesubmission4.sqllite

data class FavoriteDatabase(
    var id: Long? = -1,
    val filmId: String,
    val filmType: String,
    val filmTitle: String,
    val posterPath: String) {

    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE"
        const val ID: String = "ID_"
        const val FILM_ID: String = "FILM_ID"
        const val FILM_TYPE: String = "FILM_TYPE"
        const val FILM_TITLE: String = "FILM_TITLE"
        const val POSTER_PATH: String = "POSTER"
    }

}