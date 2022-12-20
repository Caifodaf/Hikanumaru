package ru.android.hikanumaruapp.ui.reader.viewer.pager.tac

import android.app.DownloadManager
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.MainScope
import ru.android.hikanumaruapp.ui.reader.model.ViewerChapters
import ru.android.hikanumaruapp.ui.reader.viewer.BaseViewer

abstract class PagerViewer : BaseViewer {

    //val downloadManager: DownloadManager by injectLazy()

    private val scope = MainScope()

    /**
     * Просмотр пейджера, используемого этим средством просмотра. Абстрактно реализовывать L2R, R2L
     * и вертикальные пейджеры поверх этого класса.
     */
    val pager = createPager()

    /**
     * Конфигурация, используемая пейджером, например разрешение касаний, режим масштабирования изображений, переходы между страницами.
     */
    //val config = PagerConfig(this, scope)

    //private val adapter = PagerViewerAdapter(this)

    /**
     * В настоящее время активный элемент. Это может быть страница главы или переход главы.
     */
    private var currentPage: Any? = null

    /**
     * Главы средства просмотра для установки, когда пейджер переходит в режим ожидания. В противном случае, если вид оседал
     * или перетаскивание, будет заметный и раздражающий скачок.
     */
    private var awaitingIdleViewerChapters: ViewerChapters? = null

    override fun setChapters(chapters: ViewerChapters) {
        TODO("Not yet implemented")
    }

    override fun handleKeyEvent(event: KeyEvent): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleGenericMotionEvent(event: MotionEvent): Boolean {
        TODO("Not yet implemented")
    }

    abstract fun createPager(): Pager

    override fun getView(): View {
        return pager
    }



















}