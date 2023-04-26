package ru.android.hikanumaruapp.data.debug

import ru.android.hikanumaruapp.model.*

class DataSetDebug {
    //val list: MutableList<MangaMainModel> = mutableListOf()

    fun setRecHome(list: MutableList<RecMainModel>): MutableList<RecMainModel> {
        val image = "https://sun9-29.userapi.com/impg/FdW4mJeREcRy86EbYgS--gh4yAFlQr6c7R6vOQ/Y0TnMsalbBo.jpg?size=1280x720&quality=96&sign=5b452d92355534b0368ae2b0feafcbb6&type=album"
        //Rec
        list.add(
            RecMainModel("Bozebeats",
            "«Тот, кто следуя учению будды, спасает жизни.»", image)
        )
        list.add(
            RecMainModel("Клинок, рассекающий демонов",
            "«Я знаю, что он носит с собой демона»", image)
        )
        list.add(
            RecMainModel("Клинок, рассекающий демонов",
            "«Я знаю, что он носит с собой демона»", image)
        )

       return list
    }

    fun setHistoryHome(list:MutableList<MangaMainModel>): MutableList<MangaMainModel>{
        list.add(
            MangaMainModel(
                "Ван Пис",
                "Манга",
                "",
                "https://readmanga.live/van_pis",
                "https://sun9-37.userapi.com/impg/qBa_BP4GTl-R8Ks1I-PHntwO5OVIynba_QVCMA/lsfDA9UkAuE.jpg?size=691x949&quality=96&sign=22ed460b0e8a7f8c027e28c141671214&type=album"
                //"https://sun9-37.userapi.com/impg/qBa_BP4GTl-R8Ks1I-PHntwO5OVIynba_QVCMA/lsfDA9UkAuE.jpg?size=691x949&quality=96&sign=22ed460b0e8a7f8c027e28c141671214&type=album"

            )
        )
        list.add(
            MangaMainModel(
                "Начало после конца",
                "Манга",
                "",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-86.userapi.com/s/v1/if2/YEC-Ce_vbHmkUB2Bb1EkArOMiXfLt6tgmw-SSmQnosZwTCpIOSxnw3cAx6OgcwlrRhEPR8sE7EBp7hxUqq5XSFPy.jpg?size=589x838&quality=96&type=album"
            )
        )
        list.add(
            MangaMainModel(
                "Моя геройская академия",
                "Манга",
                "",
                "https://readmanga.io/moia_geroiskaia_akademiia",
                "https://sun9-6.userapi.com/impg/6qscauyGnDHp0lszWrwVwWKXHuPGFr92zVN3lQ/I2iDtWNhPsU.jpg?size=628x971&quality=96&sign=3681c63d69afec1bef46293e7d53ac65&type=album"
            )
        )

        return list
    }

    fun setGenresHome(list: MutableList<GenresMainModel>): MutableList<GenresMainModel> {
        list.add(GenresMainModel("Сейнен", "1"))
        list.add(GenresMainModel("Комедия", "2"))
        list.add(GenresMainModel("Романтика", "3"))
        list.add(GenresMainModel("Shcoll", "4"))
        list.add(GenresMainModel("End", "5"))
        return list
    }

    fun setGenres2Home(list: MutableList<GenresMainModel>): MutableList<GenresMainModel> {
        list.add(GenresMainModel("End", "5"))
        list.add(GenresMainModel("Shcoll", "4"))
        list.add(GenresMainModel("Романтика", "3"))
        list.add(GenresMainModel("Комедия", "2"))
        list.add(GenresMainModel("Сейнен", "1"))
        return list
    }

    fun setSpecialHome(list:MutableList<MangaMainModel>): MutableList<MangaMainModel> {
        list.add(
            MangaMainModel(
                "Поднятие уровня в одиночку",
                "Манхва",
                "",
                "https://readmanga.live/podniatie_urovnia_v_odinochku",
                "https://sun9-37.userapi.com/impg/qBa_BP4GTl-R8Ks1I-PHntwO5OVIynba_QVCMA/lsfDA9UkAuE.jpg?size=691x949&quality=96&sign=22ed460b0e8a7f8c027e28c141671214&type=album"
            )
        )
        list.add(
            MangaMainModel(
                "Башня Бога",
                "Манхва",
                "",
                "https://readmanga.live/bashnia_boga",
                "https://sun9-45.userapi.com/impg/Et1LzlgdHlBBq5BV41EUB-Db85cr7AWwKN2SvA/wM7MCG6k3sM.jpg?size=697x946&quality=96&sign=d279449908f35875e7377222d59e1ab0&type=album"
            )
        )
        list.add(
            MangaMainModel(
                "Клинок, рассекающий демонов",
                "Манга",
                "",
                "https://readmanga.live/klinok__rassekaiuchii_demonov",
                "https://sun9-72.userapi.com/impg/FNoU_4mamVdHMCa6_48KOsAvfAh7UxT-sOmgEw/GERagVnk6ao.jpg?size=628x956&quality=96&sign=36fc6a476f8652f7289e7b32cd69d6e0&type=album"
            )
        )
        list.add(
            MangaMainModel(
                "Убивая слизней 300 лет, сама того не заметив, я достигла максимального уровня",
                "Манга",
                "",
                "https://readmanga.live/while_killing_slimes_for_300_years__i_became_the_max_level_unknowingly",
                "https://sun9-10.userapi.com/impg/HyYaIkNd9tba7gbHCZ83Tkqdo8d-XMT2130VjQ/ySDOLkFcysI.jpg?size=691x950&quality=96&sign=0008c5af901e4a4da5d7a6f10bca822f&type=album"
            )
        )
        list.add(
            MangaMainModel(
                "Начало после конца",
                "Манга",
                "",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-15.userapi.com/impg/aES0JbkQSEQqwoXvu8ZZlNwuL7oBVIOAXfyM3w/vXfvsy_PQ2Y.jpg?size=645x970&quality=96&sign=fe720bf9af3e63252a6804d43164a847&type=album"
            )
        )
        list.add(
            MangaMainModel(
                "Пик боевых искусств",
                "Маньхуа",
                "",
                "https://readmanga.live/martial_peak",
                "https://sun9-4.userapi.com/impg/HZkfsFsmx5VYXhezSv5FRG3hmw4Jh_u-rToWhA/8f7FAQ6Z778.jpg?size=791x1012&quality=96&sign=064f8e366172ffa8256015fa483d4dac&type=album"
            )
        )
        return list
    }

    fun setJournalHome(list: MutableList<JournalMainModel>): MutableList<JournalMainModel> {
        list.add(JournalMainModel("Weekly Shonen Jump", "4"))
        list.add(JournalMainModel("Shonen Sunday", "6"))
        list.add(JournalMainModel("Jump Next!", "3"))

        return list
    }

    fun setNewHome(list:MutableList<MangaMainModel>): MutableList<MangaMainModel> {
        list.add(
            MangaMainModel(
                "Бездарная Нана",
                "Манга ",
                "",
                "https://readmanga.live/ungifted_nana",
                "https://sun9-35.userapi.com/impg/6FjSYjXyydpbWHfFUV-BmCzL0bfwcJpGLfxBrw/zRuR7xvz_uM.jpg?size=698x960&quality=96&sign=6a62182a38ed044cab79fd2eeb76b793&type=album"
            )
        )
        list.add(
            MangaMainModel(
                "Система жизни",
                "Манхва ",
                "",
                "https://readmanga.live/sistema_jizni",
                "https://sun1-47.userapi.com/impg/tlE6RBu9sW9g-RYNZVKV_jln96cbw63OzqDYRg/i3yDUGkQND0.jpg?size=771x1009&quality=96&sign=9132285ce796bc8008940d4b654ffeb4&type=album"
            )
        )
        list.add(
            MangaMainModel(
                "О моём перерождении в слизь",
                "Манга ",
                "",
                "https://readmanga.live/o_moem_pererojdenii_v_sliz",
                "https://sun9-17.userapi.com/impg/1dB3e9kMmzKR2XO8NwbTqzAOgRvjMUz0G4_nPQ/_k0V3LhUhR8.jpg?size=691x964&quality=96&sign=47f096380d428b5762c73b5c189fc3ae&type=album"
            )
        )
        list.add(
            MangaMainModel(
                "Как и ожидал, моя школьная романтическая жизнь не удалась.",
                "Манга",
                "",
                "https://readmanga.live/my_youth_romantic_comedy_is_wrong_as_i_expected",
                "https://sun1-18.userapi.com/impg/7XpY_Pofc8MJJispzZWcSw1fteGkwTcJBMYgYw/4eIX_RM6ECs.jpg?size=694x963&quality=96&sign=0fc28edfb22dde5bc1d93e113a88c0dd&type=album"
            )
        )
        list.add(
            MangaMainModel(
                "Перерождение близняшек ",
                "Манхва ",
                "",
                "https://readmanga.live/pererojdenie_blizniashek",
                "https://sun9-36.userapi.com/impg/u1xARNqaEQzXjx3d2FJ-VgN_En5hHvEiICE7aA/ly32D4u0_V0.jpg?size=747x1022&quality=96&sign=626a50fd4307ca1a9269ad255225c5da&type=album"
            )
        )

        return list
    }

    fun setPopularHome(list: MutableList<MangaPopularMainModel>): MutableList<MangaPopularMainModel> {
        list.add(
            MangaPopularMainModel(
                "Стальной алхимик",
                "Аракава Хирому",
                "Манга",
                "",
                3.33,
                "Маленькие мальчики, Эдвард и Альфонс, всегда интересовались алхимией. Впрочем, чем же им ещё увлекаться, если их отец — потрясающий алхимик? И любая задача казалась им выполнимой, если, конечно, приложить все усилия и что-то отдать взамен. Поэтому, когда умерла мама, дети не отчаялись, а тотчас приступили к сложнейшему обряду — к преобразованию человека. Но это категорически запрещено! И, как оказалось, не напрасно. Цена за неудавшуюся попытку оказалась очень высока: у Альфонса забирают тело, а у Эдварда — ногу. В последний момент Эд отдаёт свою руку в обмен на возможность прикрепить душу Ала к доспеху. Вы думаете, что их жизни на этом закончились? Нет! Теперь у них есть другая цель — вернуть свои прежние тела. А что в мире алхимиков даёт возможность свершать любые преобразования? Конечно, философский камень! Приключения Эда и Ала только начинаются! Их друзья твёрдо верят в это! ",
                "https://readmanga.live/stalnoi_alhimik",
            "https://sun9-69.userapi.com/impg/j94G8zbtDVSZrKqMwZH4gURpyDEfewiu0y9Ghw/k32wLoNy80s.jpg?size=669x953&quality=96&sign=d8d1adc663fbcccd5ea24d4b6d5de520&type=album")
        )

        list.add(
            MangaPopularMainModel(
                "Ванпанчмен",
                "Мурата Юсукэ",
                "Манга ",
                "",
                4.82,
                "Во вселенной комиксов никто не удивляется монстрам, злодеям и безумным ученым. Они как явления природы — неприятно, конечно, но что поделаешь? Главное, чтобы хватало героев для защиты простых людей, а дальше — как повезет. И надо же такому случиться, что Сайтама — сильнейший из героев Зет-Сити — на борца за мир и справедливость вообще не похож: лысый, невысокий, лицо невыразительное, одежда тем более, даже встать в позу и выразиться пафосно не умеет!",
                "https://readmanga.live/one_punch_man__A1bc88e",
                "https://sun9-73.userapi.com/impg/1xiccs1uGbbUtJDq-8je-i00nuKnHtaX7nfW8Q/pjTTm8OFnSw.jpg?size=630x970&quality=96&sign=43198d8da72f718eaf2534d755e4122f&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Форма Голоса",
                "Ойма Ёситоки",
                "Манга ",
                "",
                4.82,
                "Эту мангу не сразу решились издавать, поскольку она затрагивает темы, о которых японцы хотели бы молчать. Но в итоге она стала новым хитом. Является редким примером достойного воплощения социально-психологической драмы.  Глава 0 - сингл - это история глухой девочки, которая пытается учиться на одном уровне со всеми, но в конце концов подвергается травле. В более глубоком смысле - это манга о том, как люди часто не слышат друг друга, не могут общаться, а потому не понимают друг друга и отделяются.",
                "https://readmanga.live/the_shape_of_voice",
                "https://sun9-20.userapi.com/impg/URPDcwN7YQJxm91ht_zX1r60Fv72gF4fbqOouA/w7eEm_XEXI8.jpg?size=670x961&quality=96&sign=525b13e33c4d4bd25a0fcd09ad8f1205&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Волейбол!!",
                "Фурудатэ Харуити",
                "Манга",
                "",
                4.85,
                "Хината Шоё с детства был маленького роста. Однажды он увидел по телевизору матч национального чемпионата по волейболу среди старшеклассников. Там, в команде старшей школы Карасуно, сражался «Маленький гигант» — невысокий игрок, который на примере своих атак доказывал, что рост — это не главное в волейболе. И теперь у Хинаты появилась мечта: тоже стать «Маленьким гигантом» из волейбольного клуба старшей школы Карасуно...",
                "https://readmanga.live/voleibol__",
                "https://sun9-75.userapi.com/impg/DRzEns-A3m_EjgBS_B7j9ci1Rud5YfO38sNOhw/gauFdKndpxI.jpg?size=624x957&quality=96&sign=3483edf6534315e264b2e04a9f78fb27&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Бездомный Бог",
                "Адати Тока",
                "Манга",
                "",
                4.85,
                "«Бог, который может мечом отменить приговор». В мире людей есть Бог, который может проникнуть в самое сердце человека. Когда вы грустите, когда вы устали, если вы посмотрите наверх, до самого рая, то увидите номер телефона. Но самое важное – не звонить! Если вы позвоните, перед вами появится парень, который с наглым выражением лица скажет: «Возрадуйся, я – Бог». Если это произойдет, то что вы будете делать?",
                "https://readmanga.live/noragami__A1b916d",
                "https://sun9-36.userapi.com/impg/76f8w1P0aOToDUa7Uq3iUVzFFDSvBFSXzt0UmA/hokX4HLTbxo.jpg?size=662x973&quality=96&sign=6832aab58d11165f55d3a1e89e3bc0b5&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Поднятие уровня в одиночку",
                "Ки Сорён",
                "Манхва",
                "",
                4.78,
                "10 лет назад, после того как распахнулись Врата, связавшие наш мир с миром монстров, некоторые люди приобрели способности, позволяющие им охотиться на монстров внутри Врат. Их стали именовать Охотниками. Однако не все Охотники были сильны. Моё имя — Сун Джин-У, я охотник ранга Е. Мне приходится рисковать своей жизнью в самых низкоуровневых подземельях. Не имея необходимых навыков, я едва мог зарабатывать деньги, сражаясь со слабейшими монстрами... По крайней мере это длилось до того, пока я не нашёл скрытое подземелье, сложнейшее из всех подземелий ранга D! Находясь на грани жизни и смерти, я внезапно получил странную силу — «Систему», содержащую секреты и особенности поднятия уровня, о которых я даже и не догадывался! Если я хочу, чтобы мой уровень постоянно рос, я должен начать тренировки в соответствии со списком квестов. От самого слабого Охотника E-ранга к сильнейшему Охотнику S-ранга!",
                "https://readmanga.live/podniatie_urovnia_v_odinochku",
                "https://sun9-37.userapi.com/impg/qBa_BP4GTl-R8Ks1I-PHntwO5OVIynba_QVCMA/lsfDA9UkAuE.jpg?size=691x949&quality=96&sign=22ed460b0e8a7f8c027e28c141671214&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Стальной алхимик1",
                "Аракава Хирому",
                "Манга",
                "",
                3.33,
                "Маленькие мальчики, Эдвард и Альфонс, всегда интересовались алхимией. Впрочем, чем же им ещё увлекаться, если их отец — потрясающий алхимик? И любая задача казалась им выполнимой, если, конечно, приложить все усилия и что-то отдать взамен. Поэтому, когда умерла мама, дети не отчаялись, а тотчас приступили к сложнейшему обряду — к преобразованию человека. Но это категорически запрещено! И, как оказалось, не напрасно. Цена за неудавшуюся попытку оказалась очень высока: у Альфонса забирают тело, а у Эдварда — ногу. В последний момент Эд отдаёт свою руку в обмен на возможность прикрепить душу Ала к доспеху. Вы думаете, что их жизни на этом закончились? Нет! Теперь у них есть другая цель — вернуть свои прежние тела. А что в мире алхимиков даёт возможность свершать любые преобразования? Конечно, философский камень! Приключения Эда и Ала только начинаются! Их друзья твёрдо верят в это! ",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-69.userapi.com/impg/j94G8zbtDVSZrKqMwZH4gURpyDEfewiu0y9Ghw/k32wLoNy80s.jpg?size=669x953&quality=96&sign=d8d1adc663fbcccd5ea24d4b6d5de520&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Стальной алхимик2",
                "Аракава Хирому",
                "Манга",
                "",
                3.33,
                "Маленькие мальчики, Эдвард и Альфонс, всегда интересовались алхимией. Впрочем, чем же им ещё увлекаться, если их отец — потрясающий алхимик? И любая задача казалась им выполнимой, если, конечно, приложить все усилия и что-то отдать взамен. Поэтому, когда умерла мама, дети не отчаялись, а тотчас приступили к сложнейшему обряду — к преобразованию человека. Но это категорически запрещено! И, как оказалось, не напрасно. Цена за неудавшуюся попытку оказалась очень высока: у Альфонса забирают тело, а у Эдварда — ногу. В последний момент Эд отдаёт свою руку в обмен на возможность прикрепить душу Ала к доспеху. Вы думаете, что их жизни на этом закончились? Нет! Теперь у них есть другая цель — вернуть свои прежние тела. А что в мире алхимиков даёт возможность свершать любые преобразования? Конечно, философский камень! Приключения Эда и Ала только начинаются! Их друзья твёрдо верят в это! ",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-69.userapi.com/impg/j94G8zbtDVSZrKqMwZH4gURpyDEfewiu0y9Ghw/k32wLoNy80s.jpg?size=669x953&quality=96&sign=d8d1adc663fbcccd5ea24d4b6d5de520&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Стальной алхимик3",
                "Аракава Хирому",
                "Манга",
                "",
                3.33,
                "Маленькие мальчики, Эдвард и Альфонс, всегда интересовались алхимией. Впрочем, чем же им ещё увлекаться, если их отец — потрясающий алхимик? И любая задача казалась им выполнимой, если, конечно, приложить все усилия и что-то отдать взамен. Поэтому, когда умерла мама, дети не отчаялись, а тотчас приступили к сложнейшему обряду — к преобразованию человека. Но это категорически запрещено! И, как оказалось, не напрасно. Цена за неудавшуюся попытку оказалась очень высока: у Альфонса забирают тело, а у Эдварда — ногу. В последний момент Эд отдаёт свою руку в обмен на возможность прикрепить душу Ала к доспеху. Вы думаете, что их жизни на этом закончились? Нет! Теперь у них есть другая цель — вернуть свои прежние тела. А что в мире алхимиков даёт возможность свершать любые преобразования? Конечно, философский камень! Приключения Эда и Ала только начинаются! Их друзья твёрдо верят в это! ",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-69.userapi.com/impg/j94G8zbtDVSZrKqMwZH4gURpyDEfewiu0y9Ghw/k32wLoNy80s.jpg?size=669x953&quality=96&sign=d8d1adc663fbcccd5ea24d4b6d5de520&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Стальной алхимик4",
                "Аракава Хирому",
                "Манга",
                "",
                3.33,
                "Маленькие мальчики, Эдвард и Альфонс, всегда интересовались алхимией. Впрочем, чем же им ещё увлекаться, если их отец — потрясающий алхимик? И любая задача казалась им выполнимой, если, конечно, приложить все усилия и что-то отдать взамен. Поэтому, когда умерла мама, дети не отчаялись, а тотчас приступили к сложнейшему обряду — к преобразованию человека. Но это категорически запрещено! И, как оказалось, не напрасно. Цена за неудавшуюся попытку оказалась очень высока: у Альфонса забирают тело, а у Эдварда — ногу. В последний момент Эд отдаёт свою руку в обмен на возможность прикрепить душу Ала к доспеху. Вы думаете, что их жизни на этом закончились? Нет! Теперь у них есть другая цель — вернуть свои прежние тела. А что в мире алхимиков даёт возможность свершать любые преобразования? Конечно, философский камень! Приключения Эда и Ала только начинаются! Их друзья твёрдо верят в это! ",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-69.userapi.com/impg/j94G8zbtDVSZrKqMwZH4gURpyDEfewiu0y9Ghw/k32wLoNy80s.jpg?size=669x953&quality=96&sign=d8d1adc663fbcccd5ea24d4b6d5de520&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Стальной алхимик5",
                "Аракава Хирому",
                "Манга",
                "",
                3.33,
                "Маленькие мальчики, Эдвард и Альфонс, всегда интересовались алхимией. Впрочем, чем же им ещё увлекаться, если их отец — потрясающий алхимик? И любая задача казалась им выполнимой, если, конечно, приложить все усилия и что-то отдать взамен. Поэтому, когда умерла мама, дети не отчаялись, а тотчас приступили к сложнейшему обряду — к преобразованию человека. Но это категорически запрещено! И, как оказалось, не напрасно. Цена за неудавшуюся попытку оказалась очень высока: у Альфонса забирают тело, а у Эдварда — ногу. В последний момент Эд отдаёт свою руку в обмен на возможность прикрепить душу Ала к доспеху. Вы думаете, что их жизни на этом закончились? Нет! Теперь у них есть другая цель — вернуть свои прежние тела. А что в мире алхимиков даёт возможность свершать любые преобразования? Конечно, философский камень! Приключения Эда и Ала только начинаются! Их друзья твёрдо верят в это! ",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-69.userapi.com/impg/j94G8zbtDVSZrKqMwZH4gURpyDEfewiu0y9Ghw/k32wLoNy80s.jpg?size=669x953&quality=96&sign=d8d1adc663fbcccd5ea24d4b6d5de520&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Стальной алхимик6",
                "Аракава Хирому",
                "Манга",
                "",
                3.33,
                "Маленькие мальчики, Эдвард и Альфонс, всегда интересовались алхимией. Впрочем, чем же им ещё увлекаться, если их отец — потрясающий алхимик? И любая задача казалась им выполнимой, если, конечно, приложить все усилия и что-то отдать взамен. Поэтому, когда умерла мама, дети не отчаялись, а тотчас приступили к сложнейшему обряду — к преобразованию человека. Но это категорически запрещено! И, как оказалось, не напрасно. Цена за неудавшуюся попытку оказалась очень высока: у Альфонса забирают тело, а у Эдварда — ногу. В последний момент Эд отдаёт свою руку в обмен на возможность прикрепить душу Ала к доспеху. Вы думаете, что их жизни на этом закончились? Нет! Теперь у них есть другая цель — вернуть свои прежние тела. А что в мире алхимиков даёт возможность свершать любые преобразования? Конечно, философский камень! Приключения Эда и Ала только начинаются! Их друзья твёрдо верят в это! ",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-69.userapi.com/impg/j94G8zbtDVSZrKqMwZH4gURpyDEfewiu0y9Ghw/k32wLoNy80s.jpg?size=669x953&quality=96&sign=d8d1adc663fbcccd5ea24d4b6d5de520&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Стальной алхимик7",
                "Аракава Хирому",
                "Манга",
                "",
                3.33,
                "Маленькие мальчики, Эдвард и Альфонс, всегда интересовались алхимией. Впрочем, чем же им ещё увлекаться, если их отец — потрясающий алхимик? И любая задача казалась им выполнимой, если, конечно, приложить все усилия и что-то отдать взамен. Поэтому, когда умерла мама, дети не отчаялись, а тотчас приступили к сложнейшему обряду — к преобразованию человека. Но это категорически запрещено! И, как оказалось, не напрасно. Цена за неудавшуюся попытку оказалась очень высока: у Альфонса забирают тело, а у Эдварда — ногу. В последний момент Эд отдаёт свою руку в обмен на возможность прикрепить душу Ала к доспеху. Вы думаете, что их жизни на этом закончились? Нет! Теперь у них есть другая цель — вернуть свои прежние тела. А что в мире алхимиков даёт возможность свершать любые преобразования? Конечно, философский камень! Приключения Эда и Ала только начинаются! Их друзья твёрдо верят в это! ",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-69.userapi.com/impg/j94G8zbtDVSZrKqMwZH4gURpyDEfewiu0y9Ghw/k32wLoNy80s.jpg?size=669x953&quality=96&sign=d8d1adc663fbcccd5ea24d4b6d5de520&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Стальной алхимик8",
                "Аракава Хирому",
                "Манга",
                "",
                3.33,
                "Маленькие мальчики, Эдвард и Альфонс, всегда интересовались алхимией. Впрочем, чем же им ещё увлекаться, если их отец — потрясающий алхимик? И любая задача казалась им выполнимой, если, конечно, приложить все усилия и что-то отдать взамен. Поэтому, когда умерла мама, дети не отчаялись, а тотчас приступили к сложнейшему обряду — к преобразованию человека. Но это категорически запрещено! И, как оказалось, не напрасно. Цена за неудавшуюся попытку оказалась очень высока: у Альфонса забирают тело, а у Эдварда — ногу. В последний момент Эд отдаёт свою руку в обмен на возможность прикрепить душу Ала к доспеху. Вы думаете, что их жизни на этом закончились? Нет! Теперь у них есть другая цель — вернуть свои прежние тела. А что в мире алхимиков даёт возможность свершать любые преобразования? Конечно, философский камень! Приключения Эда и Ала только начинаются! Их друзья твёрдо верят в это! ",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-69.userapi.com/impg/j94G8zbtDVSZrKqMwZH4gURpyDEfewiu0y9Ghw/k32wLoNy80s.jpg?size=669x953&quality=96&sign=d8d1adc663fbcccd5ea24d4b6d5de520&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Стальной алхимик9",
                "Аракава Хирому",
                "Манга",
                "",
                3.33,
                "Маленькие мальчики, Эдвард и Альфонс, всегда интересовались алхимией. Впрочем, чем же им ещё увлекаться, если их отец — потрясающий алхимик? И любая задача казалась им выполнимой, если, конечно, приложить все усилия и что-то отдать взамен. Поэтому, когда умерла мама, дети не отчаялись, а тотчас приступили к сложнейшему обряду — к преобразованию человека. Но это категорически запрещено! И, как оказалось, не напрасно. Цена за неудавшуюся попытку оказалась очень высока: у Альфонса забирают тело, а у Эдварда — ногу. В последний момент Эд отдаёт свою руку в обмен на возможность прикрепить душу Ала к доспеху. Вы думаете, что их жизни на этом закончились? Нет! Теперь у них есть другая цель — вернуть свои прежние тела. А что в мире алхимиков даёт возможность свершать любые преобразования? Конечно, философский камень! Приключения Эда и Ала только начинаются! Их друзья твёрдо верят в это! ",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-69.userapi.com/impg/j94G8zbtDVSZrKqMwZH4gURpyDEfewiu0y9Ghw/k32wLoNy80s.jpg?size=669x953&quality=96&sign=d8d1adc663fbcccd5ea24d4b6d5de520&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Стальной алхимик10",
                "Аракава Хирому",
                "Манга",
                "",
                3.33,
                "Маленькие мальчики, Эдвард и Альфонс, всегда интересовались алхимией. Впрочем, чем же им ещё увлекаться, если их отец — потрясающий алхимик? И любая задача казалась им выполнимой, если, конечно, приложить все усилия и что-то отдать взамен. Поэтому, когда умерла мама, дети не отчаялись, а тотчас приступили к сложнейшему обряду — к преобразованию человека. Но это категорически запрещено! И, как оказалось, не напрасно. Цена за неудавшуюся попытку оказалась очень высока: у Альфонса забирают тело, а у Эдварда — ногу. В последний момент Эд отдаёт свою руку в обмен на возможность прикрепить душу Ала к доспеху. Вы думаете, что их жизни на этом закончились? Нет! Теперь у них есть другая цель — вернуть свои прежние тела. А что в мире алхимиков даёт возможность свершать любые преобразования? Конечно, философский камень! Приключения Эда и Ала только начинаются! Их друзья твёрдо верят в это! ",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-69.userapi.com/impg/j94G8zbtDVSZrKqMwZH4gURpyDEfewiu0y9Ghw/k32wLoNy80s.jpg?size=669x953&quality=96&sign=d8d1adc663fbcccd5ea24d4b6d5de520&type=album")
        )


        return list
    }

    fun setPopularHome2(list: MutableList<MangaPopularMainModel>): MutableList<MangaPopularMainModel> {
        list.add(
            MangaPopularMainModel(
                "Начало после конца",
                "TurtleMe",
                "Манга",
                "",
                4.72,
                "Знакомьтесь, Грей — правитель высокоразвитой страны. У него уже нет тех амбиций и целей, что были когда-то. Однажды заснув, он просыпается в теле младенца…",
                "https://readmanga.live/nachalo_posle_konca",
                "https://sun9-15.userapi.com/impg/aES0JbkQSEQqwoXvu8ZZlNwuL7oBVIOAXfyM3w/vXfvsy_PQ2Y.jpg?size=645x970&quality=96&sign=fe720bf9af3e63252a6804d43164a847&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Реинкарнация безработного ~История о приключениях в другом мире~",
                "Рифудзин на Магонотэ",
                "Манга",
                "",
                4.62,
                "34-летнего отаку-затворника выгнали из дома родственники после похорон его родителей. Этот жирный и уродливый девственник без гроша в кармане осознал, что его жизнь подошла к концу. Затем он понял, что его жизнь вообще могла бы быть намного лучше, если бы тот мог избавиться от тёмных страниц своей жизни. Пребывая в сожалениях, мужчина увидел, как грузовик со спящим водителем быстро надвигается на трёх школьников. Собрав все свои силы, он попытался спасти их и погиб.",
                "https://readmanga.live/mushoku_tensei__if_i_get_to_a_parallel_universe_i_ll_bring",
                "https://sun9-54.userapi.com/impg/hGNdnPHgw5oyBdFk7rOR1cToE8Y_dmnLxhYTfg/OpOvU9fK2yw.jpg?size=698x940&quality=96&sign=b6a86e432d8a54b004cf3eac58b6e70e&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "О моём перерождении в слизь",
                "Фусэ",
                "Манга ",
                "",
                4.75,
                "37-летний японец-холостяк был зарезан на улице каким-то мерзавцем-грабителем. Тут бы и истории конец, да всё обернулось иначе: неожиданно он переродился слизью в фэнтезийном мире. Однако что может сделать, пускай и разумная, но слизь?",
                "https://readmanga.live/o_moem_pererojdenii_v_sliz",
                "https://sun9-17.userapi.com/impg/1dB3e9kMmzKR2XO8NwbTqzAOgRvjMUz0G4_nPQ/_k0V3LhUhR8.jpg?size=691x964&quality=96&sign=47f096380d428b5762c73b5c189fc3ae&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Магическая битва",
                "Акутами Гэгэ",
                "Манга ",
                "",
                4.69,
                "Талантливого Юдзи школьная жизнь мало привлекает. Но всё изменилось, когда он становится частью клуба оккультных исследований и находит один из пальцев великого Проклятого духа по имени «Сукуна».",
                "https://readmanga.live/sorcery_fight",
                "https://sun1-90.userapi.com/impg/yNpNSFkDykosnZN1Qop_b5Y9_UO8-0AQv3a-DA/Opgm-aPCHsw.jpg?size=617x956&quality=96&sign=3e47c20d70e42a5c43a8c6fd96cda60f&type=album")
        )
        list.add(
            MangaPopularMainModel(
                "Невеста герцога по контракту",
                "Мильчха",
                "Манхва ",
                "",
                4.76,
                "После неожиданной гибели Пак Ын Ха попадает в волшебный мир, где всё идёт по знакомому ей сюжету романа. Однако она - не главная героиня этой истории, ей досталась второстепенная роль со скорым трагичным концом! Чтобы избежать своей незавидной судьбы, она заключает контракт с герцогом Ноа Волстео Виннайтом. Но всё не так просто... За добрым и ангельским лицом герцога скрывается настоящий дьявол! Сможет ли она избежать судьбы, некогда прописанной на страницах?",
                "https://readmanga.live/nevesta_gercoga_po_kontraktu",
                "https://sun1-99.userapi.com/impg/1K8uYqem_AwlSvp3xa6BWarpvZD9-vB13ryoFw/wx20-jg9ldo.jpg?size=711x960&quality=96&sign=3f0a3428f632245d9549229402bcc37f&type=album")
        )

        return list
    }
}