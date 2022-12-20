package ru.android.hikanumaruapp.ui.reader.viewer

import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import ru.android.hikanumaruapp.ui.reader.model.ViewerChapters

interface BaseViewer {

    /**
     * Возвращает представление, используемое этим средством просмотра.
     */
    fun getView(): View

    /**
     * Уничтожает этого зрителя. Вызывается при выходе из считывающего устройства или замене зрителей.
     */
    fun destroy() {}

    /**
     * Сообщает этому средству просмотра установить данные [главы] активными.
     */
    fun setChapters(chapters: ViewerChapters)

    ///**
    // * Сообщает этому просмотрщику перейти к заданной [странице].
    // */
    //fun moveToPage(page: ReaderPage)

    /**
     * Вызывается из содержащего действия при получении ключа [события]. Он должен возвращать значение true
     * если событие было обработано, в противном случае значение false.
     */
    fun handleKeyEvent(event: KeyEvent): Boolean

    /**
     * Вызывается из содержащего действия при получении общего движения [события]. Это должно
     * возвращает true, если событие было обработано, в противном случае false.
     */
    fun handleGenericMotionEvent(event: MotionEvent): Boolean
}