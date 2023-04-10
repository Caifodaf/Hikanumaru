package ru.android.hikanumaruapp.utilits.popdialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.android.hikanumaruapp.R

class PopAlertDialog(
    private val context: Context,private val lifecycleScope
    :LifecycleCoroutineScope
    ) {

    /**
     * Declare variables
     *
     */
    private lateinit var job: Job
    private val dialogStart: Dialog = Dialog(context)
    private val dialogEnd: Dialog = Dialog(context)

    init {
        // Set up dialog views
        dialogStart.setContentView(R.layout.dialog_pop_alert_registration)
        dialogEnd.setContentView(R.layout.dialog_pop_alert_registration)
        setupDialogStart()
        setupDialogEnd()
    }

    /**
     * Setup startDialog
     */
    private fun setupDialogStart() {
        val lp = dialogStart.window!!.attributes
        lp.dimAmount = 0.0f // уровень затемнения от 1.0 до 0.0
        lp.windowAnimations = (R.style.DialogAnimation)
        lp.gravity = Gravity.TOP
        lp.y = 30
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialogStart.window!!.attributes = lp
    }

    /**
     * Setup endDialog
     */
    private fun setupDialogEnd() {
        val lp1 = dialogEnd.window!!.attributes
        lp1.dimAmount = 0.0f // уровень затемнения от 1.0 до 0.0
        lp1.gravity = Gravity.TOP
        lp1.y = 30
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp1.windowAnimations = (R.style.DialogAnimationExit)
        dialogEnd.window!!.attributes = lp1
    }

    /**
     * Set the message and drawableIcon of the dialog.
     *
     * @param message The message of the dialog.
     * @param drawableIcon Drawable image for icon.
     */
    fun setDataDialog(message: String, drawableIcon: Drawable? = null){
        val messageTextViewStart: TextView = dialogStart.findViewById(R.id.TVTitlePop)
        val messageTextViewEnd: TextView = dialogEnd.findViewById(R.id.TVTitlePop)
        val imageViewStart: ImageView = dialogStart.findViewById(R.id.Image)
        val imageViewEnd: ImageView = dialogEnd.findViewById(R.id.Image)

        messageTextViewStart.text = message
        messageTextViewEnd.text = message

        if (drawableIcon != null){
            imageViewStart.setImageDrawable(drawableIcon)
            imageViewEnd.setImageDrawable(drawableIcon)
        }
    }

    /**
     * Show the pop-up dialog
     */
    fun show() {
        dialogStart.show()
        job = lifecycleScope.launch {
            delay(3000)
            dialogEnd.show()
            delay(50)
            dialogStart.dismiss()
            delay(950)
            dialogEnd.dismiss()
        }
    }

    /**
     *  Hide the pop-up dialog and cancel the delay job if it exists
     */
    fun hide(){
        if (::job.isInitialized && job.isActive){
            job.cancel()
            dialogStart.dismiss()
            dialogEnd.dismiss()
        }
    }
}