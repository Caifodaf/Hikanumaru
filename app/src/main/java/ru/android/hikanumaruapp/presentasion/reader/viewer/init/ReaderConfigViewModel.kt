package ru.android.hikanumaruapp.presentasion.reader.viewer.init

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.android.hikanumaruapp.presentasion.reader.MangaConst
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.READ_MODE_RIGHT
import javax.inject.Inject

@HiltViewModel
class ReaderConfigViewModel @Inject constructor(): ViewModel() {

    var readMode: Int

    init {
        readMode = ReaderPagerConst.READ_MODE_RIGHT
    }

    fun loadConfig(){

    }




    // Добавить проверку на readMode при первом чтении
    //  // Set ReadMode Type
    //  readModeType = when(creativeWorkPage.type){
    //      MangaConst.MANGA_TYPE -> ReaderPagerConst.READ_MODE_RIGHT
    //      MangaConst.MANHVA_TYPE -> ReaderPagerConst.READ_MODE_RIGHT
    //      MangaConst.MANHUYA_TYPE -> ReaderPagerConst.READ_MODE_RIGHT
    //      MangaConst.OEL_MANGA_TYPE -> ReaderPagerConst.READ_MODE_RIGHT
    //      MangaConst.COMICS_TYPE -> ReaderPagerConst.READ_MODE_RIGHT
    //      MangaConst.OTHER_LOCAL_TYPE -> ReaderPagerConst.READ_MODE_RIGHT
    //      else-> ReaderPagerConst.READ_MODE_RIGHT
    //  }

}