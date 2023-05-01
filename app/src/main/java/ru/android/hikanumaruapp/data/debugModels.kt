package ru.android.hikanumaruapp.data

import com.google.gson.Gson
import ru.android.hikanumaruapp.data.model.GenresMainModel
import ru.android.hikanumaruapp.data.model.Manga
import ru.android.hikanumaruapp.data.model.MangaList
import ru.android.hikanumaruapp.data.model.MangaListCoverLinks

interface debugModels {

    fun home(): List<MangaList> =
        listOf(
            MangaList(
                id = "041568e4-cfd0-11ed-a047-c95f118785c8",
                title = "Я люблю вас, учитель...",
                coverLinks = MangaListCoverLinks(
                    first = "https://staticrm.rmr.rocks/uploads/pics/00/75/222_o.jpg",
                    last = "https://staticrm.rmr.rocks/uploads/pics/00/75/222_o.jpg"
                ),
                description = "Ито встречалась с учителем, но их отношения были раскрыты. Сенсей уволился, а девушку временно исключили из школы. Ито чувствует себя потерянно и не знает куда идти. Сможет ли она двигаться дальше, оставив прошлое позади?",
                releaseYear = 2011,
                type = "manga",
                sourceLink = "/ia_liubliu_vas__uchitel___",
                genres = listOf(
                    GenresMainModel(
                        id = "5fabb518-cf1d-11ed-99a4-e7d459fe0a9e",
                        title = "драма"
                    ),
                    GenresMainModel(
                        id = "5fb0668a-cf1d-11ed-92c8-e7d459fe0a9e",
                        title = "романтика"
                    ),
                    GenresMainModel(
                        id = "5fb1587e-cf1d-11ed-bf88-e7d459fe0a9e",
                        title = "сёдзё"
                    )
                )
            ),
            MangaList(
                id = "0466674c-cfd2-11ed-9ab0-c95f118785c8",
                title = "Охотник х Охотник додзинси - Без названия",
                coverLinks = MangaListCoverLinks(
                    first = "https://staticrm.rmr.rocks/uploads/pics/00/06/624.jpg",
                    last = "https://staticrm.rmr.rocks/uploads/pics/00/06/624.jpg",
                ),
                description = "Гон/Киллуа",
                releaseYear = null,
                type = "manga",
                sourceLink = "/ohotnik_h_ohotnik_dodzinsi____bez_nazvaniia",
                genres = listOf(
                    GenresMainModel(
                        id = "5fb27ace-cf1d-11ed-b9a6-e7d459fe0a9e",
                        title = "сёнэн-ай"
                    )
                )
            ),
            MangaList(
                id = "041568e4-cfd0-11ed-a047-c95f118785c8",
                title = "Я люблю вас, учитель...",
                coverLinks = MangaListCoverLinks(
                    first = "https://staticrm.rmr.rocks/uploads/pics/00/75/222_o.jpg",
                    last = "https://staticrm.rmr.rocks/uploads/pics/00/75/222_o.jpg"
                ),
                description = "Ито встречалась с учителем, но их отношения были раскрыты. Сенсей уволился, а девушку временно исключили из школы. Ито чувствует себя потерянно и не знает куда идти. Сможет ли она двигаться дальше, оставив прошлое позади?",
                releaseYear = 2011,
                type = "manga",
                sourceLink = "/ia_liubliu_vas__uchitel___",
                genres = listOf(
                    GenresMainModel(
                        id = "5fabb518-cf1d-11ed-99a4-e7d459fe0a9e",
                        title = "драма"
                    ),
                    GenresMainModel(
                        id = "5fb0668a-cf1d-11ed-92c8-e7d459fe0a9e",
                        title = "романтика"
                    ),
                    GenresMainModel(
                        id = "5fb1587e-cf1d-11ed-bf88-e7d459fe0a9e",
                        title = "сёдзё"
                    )
                )
            ),
        )

    fun mangaPage() = Manga(
            id = "fc4ec2e8-b532-11ed-834d-1b0d3f83cd8c",
            title = "Давайте сменим жанр",
            additionalTitle = "Jangreureul bakkwobodorok hagessseupnida",
            othersTitle = listOf(
                "장르를 바꿔보도록 하겠습니다",
                "I will make anything to change the genre",
                "I Will Change the Genre",
                "Я сделаю всё, чтобы изменить жанр",
                "It's Time to Change the Genre"
            ),
            coverLinks = listOf(
                "/uploads/pics/01/65/950_o.jpg",
                "/uploads/pics/01/81/386_o.jpg"
            ),
            description = "Я очутилась в любимом романе, заняв тело злой тёти главного героя, что всячески угнетала своего племянника. Зная о том, что вскоре нам предстоит расстаться, я всячески оберегала его и была сострадательна. Однако, когда за ним приехал обворожительный мужчина, спрашивая меня, может ли он его забрать, мальчик внезапно подбежал ко мне и, обняв за талию, закричал «мама».",
            releaseYear = 2021,
            ageRating = 13,
            type = "manhwa",
            publicationStatus = "ongoing",
            translationStatus = "continues",
            genres = listOf(
                GenresMainModel(
                    id = "5fac7dfe-cf1d-11ed-ac09-e7d459fe0a9e",
                    title = "исэкай",
                ),
                GenresMainModel(
                    id = "5fad4a72-cf1d-11ed-ac09-e7d459fe0a9e",
                    title = "исэкай",
                ),
                GenresMainModel(
                    id = "5fb1587e-cf1d-11ed-ac09-e7d459fe0a9e",
                    title = "исэкай",
                ),
                GenresMainModel(
                    id = "5fb53de0-cf1d-11ed-ac09-e7d459fe0a9e",
                    title = "исэкай",
                ),
            ),
            userStatus = null,
            sourceLink = "/davaite_smenim_janr__A35c96",
            source = "readmanga",
            createTime = "2023-02-25T17:36:50+00:00",
            updateTime = "2023-03-31T11:02:11+00:00",
            chapters = null
        )
}