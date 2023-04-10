package ru.android.hikanumaruapp.utilits

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ru.android.hikanumaruapp.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


interface UIUtils {

 fun View.timerDoubleButton(time: Long = 2000) {
  isClickable = false
  Handler(Looper.getMainLooper())
  handler.postDelayed({
   isClickable = true
  }, time)
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