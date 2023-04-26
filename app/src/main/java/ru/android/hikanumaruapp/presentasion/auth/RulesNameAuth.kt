package ru.android.hikanumaruapp.presentasion.auth

interface RulesNameAuth {

    companion object {
        const val DEFAULT_BUTTON_VIEW = 0
        const val ACTIVE_BUTTON_VIEW = 1
        const val LOADING_BUTTON_VIEW = 2
        const val ERROR_BUTTON_VIEW = 3

        private const val LENGTH_MAIL_MIN = 5
        private const val LENGTH_MAIL_MAX = 60
        val LENGTH_MAIL_RANGE = LENGTH_MAIL_MIN..LENGTH_MAIL_MAX

        private const val AUTH_LOGIN_PATTERN = ""
        private const val LENGTH_LOGIN_MIN = 3
        private const val LENGTH_LOGIN_MAX = 20
        val LENGTH_LOGIN_RANGE = LENGTH_LOGIN_MIN..LENGTH_LOGIN_MAX

        private const val AUTH_NAME_PATTERN = ""
        private const val LENGTH_NAME_MIN = 3
        private const val LENGTH_NAME_MAX = 20
        val LENGTH_NAME_RANGE = LENGTH_NAME_MIN..LENGTH_NAME_MAX

        private const val AUTH_PASSWORD_PATTERN = ""
        private const val LENGTH_PASSWORD_MIN = 5
        private const val LENGTH_PASSWORD_MAX = 40
        val LENGTH_PASSWORD_RANGE = LENGTH_PASSWORD_MIN..LENGTH_PASSWORD_MAX
    }

}