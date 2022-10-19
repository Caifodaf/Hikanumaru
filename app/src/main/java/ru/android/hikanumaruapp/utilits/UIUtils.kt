package ru.android.hikanumaruapp.utilits

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import ru.android.hikanumaruapp.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


interface UIUtils {

 fun timerDoubleBtn(view: TextView, time:Long = 1000) {
  val timer = object: CountDownTimer(time, time) {
   override fun onTick(millisUntilFinished: Long) {
    view.isClickable = false}
   override fun onFinish() {
    view.isClickable = true}
  }
  timer.start()
 }

 fun timerDoubleBtn(view: LinearLayout, time:Long = 1000) {
  val timer = object: CountDownTimer(time, time) {
   override fun onTick(millisUntilFinished: Long) {
    view.isClickable = false}
   override fun onFinish() {
    view.isClickable = true}
  }
  timer.start()
 }

 fun timerDoubleBtn(view: ImageView, time:Long = 1000) {
  val timer = object: CountDownTimer(time, time) {
   override fun onTick(millisUntilFinished: Long) {
    view.isClickable = false}
   override fun onFinish() {
    view.isClickable = true}
  }
  timer.start()
 }

fun errorPop(rl1:RelativeLayout,tv1:TextView,rl2:RelativeLayout,tv2:TextView,text1:String,text2:String,time:Long = 5000){
 val timer = object: CountDownTimer(time+1000, 1000) {
  override fun onTick(millisUntilFinished: Long) {
   rl1.visibility = View.VISIBLE
   tv1.text = text1
  }
  override fun onFinish() {
   rl1.visibility = View.GONE
  }
 }
 val timer2 = object: CountDownTimer(time, 1000) {
  override fun onTick(millisUntilFinished: Long) {
   rl2.visibility = View.VISIBLE
   tv2.text = text2
  }
  override fun onFinish() {
   rl2.visibility = View.GONE
  }
 }

 timer.start()
 if (text2 !== "")
 timer2.start()
}

 fun errorPop(text1:String,error_layout:LinearLayout,inflater: LayoutInflater,time:Long = 3000){

  // todo errorPop Все хуйня - переделать

  val to_add: View = inflater.inflate(R.layout.layout_error, error_layout, false)
  val textL = to_add.findViewById<TextView>(R.id.tv_error_1)
  val layoutR = to_add.findViewById<RelativeLayout>(R.id.rl_block_error_1)
  error_layout.removeAllViews()
  error_layout.addView(to_add)

  layoutR.visibility = View.VISIBLE
  textL.text = text1

  layoutR.animate()
   .setDuration(500)
   .y(56f*2.5f)

  val timer = object: CountDownTimer(time, 1000) {
   override fun onTick(millisUntilFinished: Long) {}
   override fun onFinish() {
    layoutR.animate()
     .setDuration(500)
     .alpha(0F)
     .setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator?) {
       error_layout.removeView(to_add)
      } })
   }
  }
  timer.start()
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