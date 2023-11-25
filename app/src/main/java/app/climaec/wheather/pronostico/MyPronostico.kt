package app.climaec.wheather.pronostico

data class MyPronostico(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Pronostico>,
    val message: Int
)