package ru.android.hikanumaruapp.utilits.navigation

import android.os.Bundle

interface NavigationFragmentinViewModel {
    class NavigationFrag(val navigation: Int, val bundle: Bundle? = null) : NavigationEvent()
}