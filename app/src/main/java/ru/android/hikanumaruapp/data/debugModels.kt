package ru.android.hikanumaruapp.data

import com.google.gson.Gson
import com.squareup.moshi.Json
import ru.android.hikanumaruapp.data.model.GenresMainModel
import ru.android.hikanumaruapp.data.model.Manga
import ru.android.hikanumaruapp.data.model.MangaList
import ru.android.hikanumaruapp.data.model.MangaListCoverLinks

interface debugModels {

    fun homeManga(): List<MangaList> =
        listOf(
            MangaList(
                id = "041568e4-cfd0-11ed-a047-c95f118785c8",
                title = "Я люблю вас, учитель...",
                coverLinks = MangaListCoverLinks(
                    first = "/uploads/pics/00/75/222_o.jpg",
                    last = "/uploads/pics/00/75/222_o.jpg"
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
                id = "fc4ec2e8-b532-11ed-834d-1b0d3f83cd8c",
                title = "Звёздное дитя",
                coverLinks = MangaListCoverLinks(
                    first = "/uploads/pics/01/50/517_o.jpg",
                    last = "/uploads/pics/01/50/517_o.jpg",
                ),
                description = "Я была самой младшей принцессой в семье. Когда началось восстание против императорской семьи, меня первой бросили. Именно поэтому мне пришлось жить не как принцесса Стелла, а как простолюдинка Этель в течение десяти лет. Но по воле случая мне пришлось вернуться.",
                releaseYear = 2022,
                type = "manga",
                sourceLink = "/zvezdnoe_ditia__A533b",
                genres = listOf(
                    GenresMainModel(
                        id = "5fb27ace-cf1d-11ed-b9a6-e7d459fe0a9e",
                        title = "сёнэн-ай"
                    ),
                    GenresMainModel(
                        id = "5fb1587e-cf1d-11ed-bf88-e7d459fe0a9e",
                        title = "сёдзё"
                    ),
                    GenresMainModel(
                        id = "5fb1587e-cf1d-11ed-bf88-e7d459fe0a9e",
                        title = "сёдзё"
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

    fun homeGenres(): List<GenresMainModel> =
        listOf(
            GenresMainModel(
                id="0",
                title="арт",
                description=null,
                source=""
            ),
            GenresMainModel(
                id="1",
                title="боевик",
                description=null,
                source=""
            ),
            GenresMainModel(
                id="2",
                title="школа",
                description=null,
                source=""
            ),
            GenresMainModel(
                id="3",
                title="боевые искусства",
                description=null,
                source=""
            ),
            GenresMainModel(
                id="4",
                title="вампиры",
                description=null,
                source=""
            ),
            GenresMainModel(
                id="5",
                title="гарем",
                description=null,
                source=""
            ),
            GenresMainModel(
                id="6",
                title="гендерная интрига",
                description=null,
                source=""
            ),
            GenresMainModel(
                id="7",
                title="героическое",
                description=null,
                source=""
            ),
            GenresMainModel(
                id="8",
                title="фэнтези",
                description=null,
                source=""
            ),
            GenresMainModel(
                id="9",
                title="детектив",
                description=null,
                source=""
            ),
        )

    fun mangaPage() = Manga(
            id = "fc4ec2e8-b532-11ed-834d-1b0d3f83cd8c",
            title = "Звёздное дитя",
            additionalTitle = "Oshi no Ko",
            othersTitle = listOf(
                "장르를 바꿔보도록 하겠습니다",
                "推しの子",
                "Idol of Child",
                "Я сделаю всё, чтобы изменить жанр",
                "It's Time to Change the Genre"
            ),
            coverLinks = listOf(
                "/uploads/pics/01/50/517_o.jpg",
                "/uploads/pics/01/50/517_o.jpg"
            ),
            description = "Я очутилась в любимом романе, заняв тело злой тёти главного героя, что всячески угнетала своего племянника. Зная о том, что вскоре нам предстоит расстаться, я всячески оберегала его и была сострадательна. Однако, когда за ним приехал обворожительный мужчина, спрашивая меня, может ли он его забрать, мальчик внезапно подбежал ко мне и, обняв за талию, закричал «мама».",
            releaseYear = 2020,
            ageRating = 13,
            type = "manga",
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
            sourceLink = "/zvezdnoe_ditia__A533b",
            source = "readmanga",
            createTime = "2023-02-25T17:36:50+00:00",
            updateTime = "2023-03-31T11:02:11+00:00",
            chapters = null
        )
}