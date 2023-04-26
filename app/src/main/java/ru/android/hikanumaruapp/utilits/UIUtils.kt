package ru.android.hikanumaruapp.utilits

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.widget.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


interface UIUtils {

 fun View.timerDoubleButton(time: Long = 2000) {
  isClickable = false
  Handler(Looper.getMainLooper())
  handler.postDelayed({
   isClickable = true
  }, time)
 }

 fun EditText.setOnEditorActionDoneListener(action: () -> Unit) {
  this.setOnKeyListener(object : View.OnKeyListener {
   override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
    if (event.action == KeyEvent.ACTION_DOWN &&
     keyCode == KeyEvent.KEYCODE_ENTER
    ) {
     action.invoke()
     // clear focus and hide cursor from edit text
     clearFocus()
     return true
    }
    return false
   }
  })
 }

 fun Context.pixelToDP(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
 resources.displayMetrics).toInt()


 fun getDaysPassed(dateString: String): Int {
  //"2022-07-05T16:48:35+00:00"
  val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
  val date = sdf.parse(dateString)
  if (date != null) {
   val currentTime = System.currentTimeMillis()
   val elapsedTime = currentTime - date.time
   val daysPassed = TimeUnit.MILLISECONDS.toDays(elapsedTime)
   return daysPassed.toInt()
  }
  return -1 // Return -1 if there's an error parsing the date
 }

fun getCurrentDataTime(){
 // Текущее время
 val currentDate = Date()
// Форматирование времени как "день.месяц.год"
 val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
 val dateText: String = dateFormat.format(currentDate)
// Форматирование времени как "часы:минуты:секунды"
 val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
 val timeText: String = timeFormat.format(currentDate)

return
}
































//
// private val vm1 by viewModels<MangaPageViewModel>()
// private lateinit var vpAdapter : MangaPageViewPagerAdapter
//
// private fun FragmentResetPasswordStageOneBinding.initPagerBlock() {
//  vpAdapter = MangaPageViewPagerAdapter(vm1, childFragmentManager, lifecycle)
//  ViewPager2.apply {
//   adapter = vpAdapter
//  }
//
//  var backLayout = ViewTabBackGround.width.toFloat()
//  ViewTabBackGround.viewTreeObserver
//   .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//    override fun onGlobalLayout() {
//     backLayout = ViewTabBackGround.width.toFloat()
//     ViewTabBackGround.viewTreeObserver.removeOnGlobalLayoutListener(this)
//    } })
//
//  ViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//   override fun onPageScrolled(
//    position: Int,
//    positionOffset: Float,
//    @Px positionOffsetPixels: Int,
//   ) {
////                ViewTab
////                LLContainerTabs
////                TVTabGeneral
////                TVTabInfo
////                TVTabStatistic
//
//    val selectorLayout = backLayout/3
//    when(position){
//     0->{
//      val sectorS = 0f
//      val sectorE = selectorLayout
//
//      ViewTab.x = sectorS + (sectorE/100)*(positionOffset*100)
//     }
//     1->{
//      val sectorS = selectorLayout
//      val sectorE = selectorLayout
//
//      ViewTab.x = sectorS + (sectorE/100)*(positionOffset*100)
//     }
//     2->{
//      val sectorS = backLayout - selectorLayout*2
//      val sectorE = selectorLayout
//
//      ViewTab.x = sectorS + (sectorE/100)*(positionOffset*100)
//      if (positionOffset == 0f)
//       ViewTab.x = (selectorLayout*2)
//     }
//    }
//
////                ViewPager2.viewTreeObserver.addOnGlobalLayoutListener {
////
////                }
//
//    Log.d("vlajkld","backLayout " + backLayout)
//    Log.d("vlajkld","selectorLayout " + selectorLayout)
//
//    Log.d("vlajkld","position " + position)
//    Log.d("vlajkld","positionOffset " + positionOffset)
//    Log.d("vlajkld","@Px positionOffsetPixels " + positionOffsetPixels)
//    Log.d("vlajkld","@Px positionOffsetPixels " + requireActivity().pixelToDP(positionOffsetPixels.toFloat()))
//   }
//
//   override fun onPageSelected(position: Int) {
//    val selectorLayout = backLayout/3
//    when(position){
//     0-> ViewTab.x = 0f
//     1-> ViewTab.x = selectorLayout
//     2-> ViewTab.x = (selectorLayout*2)
//    }
//   }
//  })
// }




     //public static BitmapImageViewTarget getRoundedImageTarget(@NonNull final Context context, @NonNull final ImageView imageView,
        //final float radius)
        //{
        //    return new BitmapImageViewTarget (imageView) {
        //        @Override
        //        protected void setResource(final Bitmap resource) {
        //            RoundedBitmapDrawable circularBitmapDrawable =
        //            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
        //            circularBitmapDrawable.setCornerRadius(radius);
        //            imageView.setImageDrawable(circularBitmapDrawable);
        //        }
        //    };
        //}
 }