package ru.android.hikanumaruapp.ui.reader.viewer.pager.tac

import android.annotation.SuppressLint
import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import ru.android.hikanumaruapp.ui.reader.viewer.init.GestureDetectorWithLongTap

open class Pager(
    context: Context,
    isHorizontal: Boolean = true,
) : ViewPager(context) {

    /**
     * Функция прослушивания касания для выполнения при обнаружении касания.
     */
    var tapListener: ((MotionEvent) -> Unit)? = null

    /**
     * Функция прослушивания длинного касания для выполнения при обнаружении длинного касания.
     */
    var longTapListener: ((MotionEvent) -> Boolean)? = null

    /**
     * Слушатель жестов, который реализует события касания и долгого касания.
     */
    private val gestureListener = object : GestureDetectorWithLongTap.Listener() {
        override fun onSingleTapConfirmed(ev: MotionEvent): Boolean {
            tapListener?.invoke(ev)
            return true
        }

        override fun onLongTapConfirmed(ev: MotionEvent) {
            val listener = longTapListener
            if (listener != null && listener.invoke(ev)) {
                performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            }
        }
    }

    /**
     * Детектор жестов, который обрабатывает события движения.
     */
    private val gestureDetector = GestureDetectorWithLongTap(context, gestureListener)

    /**
     * Включен ли в данный момент детектор жестов.
     */
    private var isGestureDetectorEnabled = true

    /**
     * Отправляет событие касания.
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val handled = super.dispatchTouchEvent(ev)
        if (isGestureDetectorEnabled) {
            gestureDetector.onTouchEvent(ev)
        }
        return handled
    }

    /**
     * Должен ли данный [ev] быть перехвачен. Используется только для предотвращения сбоев, когда ребенок
     * представления манипулируют [requestDisallowInterceptTouchEvent].
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    /**
     * Обрабатывает событие касания. Используется только для предотвращения сбоев, когда дочерние представления манипулируют
     * [requestDisallowInterceptTouchEvent].
     */
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return try {
            super.onTouchEvent(ev)
        } catch (e: NullPointerException) {
            false
        } catch (e: IndexOutOfBoundsException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    /**
     * Выполняет данное ключевое событие, когда этот пейджер имеет фокус. Просто ничего не делайте, потому что читатель
     * уже отправляет ключевые события средству просмотра и имеет больший контроль, чем этот метод.
     */
    override fun executeKeyEvent(event: KeyEvent): Boolean {
        // Disable viewpager's default key event handling
        return false
    }

    /**
     * Включает или отключает детектор жестов.
     */
    fun setGestureDetectorEnabled(enabled: Boolean) {
        isGestureDetectorEnabled = enabled
    }
















}