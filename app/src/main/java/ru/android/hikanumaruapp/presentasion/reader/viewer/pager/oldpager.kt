package ru.android.hikanumaruapp.presentasion.reader.viewer.pager

//private fun initTouhcEvent() {
////        binding.viewPagerReader.getChildAt(0).setOnTouchListener(this)
//    binding.viewPagerReader.
//    setOnTouchListener(object: MoveViewTouchListener(binding.viewPagerReader){
//
//    })
//}
//
//@SuppressLint("ClickableViewAccessibility")
//override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
//    val y = motionEvent.y
//    val x = motionEvent.x
//
//    Log.d("dadawdmove","motion1 - ${motionEvent.action}")
//    Log.d("dadawdmove","motion1 - ${motionEvent.actionMasked}")
//    Log.d("dadawdmove","motion2 - ${motionEvent}")
//    if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN)
//    when {
//        x < readerPager.width * LEFT_REGION -> {
//            moveToPrevious()
//        }
//        x > readerPager.width * RIGHT_REGION -> {
//            moveToNext()
//        }
//        x > readerPager.width * LEFT_REGION && x < readerPager.width * RIGHT_REGION -> {
//            toggleMenu()
//        }
//        else -> {
//            //Todo: Error
//            //toggleMenu()
//        }
//    }
//
//    return false
//}

//    Log.d("dadawdmove","view - ${view}")
//    Log.d("dadawdmove","motion - ${motion}")
//    Log.d("dadawdmove","motion1 - ${motion.action}")
//    Log.d("dadawdmove","motion2 - ${motion.rawX}")
//    Log.d("dadawdmove","motion3 - ${motion.rawY}")
//    Log.d("dadawdmove","motion4 - ${motion.x}")
//    Log.d("dadawdmove","motion5 - ${motion.y}")
//    Log.d("dadawdmove","motion6 - ${motion.size}")
//    Log.d("dadawdmove","motion7 - ${motion.downTime}")
//    Log.d("dadawdmove","motion8 - ${motion.orientation}")

//private fun viewPager2SetRegisterOnPageChangeCallback(viewPager2: ViewPager2) {
//    viewPager2.registerOnPageChangeCallback(
//        object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                currentProgressSeekBar()
//                val page = readerPagerAdapter.dataSetPage.getOrNull(position)
//                if (page != null) {
//                    readerPagerAdapter.onPageSelected(page, position)
//                }
//            }
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int, ) {}
//
//            @SuppressLint("SwitchIntDef")
//            override fun onPageScrollStateChanged(state: Int) {
//                when(state){
//                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {}
//                    RecyclerView.SCROLL_STATE_DRAGGING -> {}
//                    RecyclerView.SCROLL_STATE_SETTLING -> {}
//                }
//            }
//        })
//}

//binding.llTranslatorsMenuLayout.visibility = if(binding.llTranslatorsMenuLayout.visibility == VISIBLE)
//GONE
//else{ View.GONE }
//
//binding.llReadModeMenuLayout.visibility = if(binding.llReadModeMenuLayout.visibility == GONE)
//VISIBLE
//else
//GONE
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//


//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//