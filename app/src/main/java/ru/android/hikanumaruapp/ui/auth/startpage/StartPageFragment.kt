package ru.android.hikanumaruapp.ui.auth.startpage

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentStartPageBinding
import ru.android.hikanumaruapp.ui.auth.startpage.adapter.StartPageImagersAdapter

@AndroidEntryPoint
class StartPageFragment : BaseFragment() {

    private var _binding: FragmentStartPageBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<StartPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStartPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var delayInMillisImages = 10000L

    private lateinit var vpAdapter: StartPageImagersAdapter

    private val adsTextList = arrayOf(
        "Экран премущества Ашот","Экран премущества Даня",
        "Экран премущества Аня","Экран премущества Гусь",
        "Экран премущества Андрей",
    )
    private val adsList = arrayOf(
        R.drawable.ashot, R.drawable.dan9, R.drawable.any,
        R.drawable.maks, R.drawable.andre
    )

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setupOnBackPressed()
            ViewPager2ImagesLoader()
            initButtons()
        }
    }

    private fun FragmentStartPageBinding.ViewPager2ImagesLoader() {
        createImagesTabPager()
        vpAdapter = StartPageImagersAdapter(adsList)
        ViewPager2Start.apply {
            adapter = vpAdapter
            currentItem = 1
        }
        onInfinitePageChangeCallback(adsList.size+2)
    }

    private fun autoSwitcher(ViewPager2Start: ViewPager2) {
        val handler = Handler(Looper.myLooper()!!)
        val runnable = object : Runnable {
            override fun run() {
                ViewPager2Start.currentItem = ViewPager2Start.currentItem + 1
                handler.postDelayed(this, delayInMillisImages)
            }
        }
        handler.postDelayed(runnable, 5000)
        binding.ViewPager2Start.currentItem = 1
    }

    private fun onInfinitePageChangeCallback(listSize: Int) {
        binding.ViewPager2Start.currentItem = 1
        val vp2 = binding.ViewPager2Start
        vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    when (vp2.currentItem) {
                        listSize - 1 -> vp2.setCurrentItem(1, false)
                        0 -> vp2.setCurrentItem(listSize - 2, false)
                    }
                }
            }
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position != 0 && position != listSize - 1) {
                    vm.pagerCounter.value = position-1
                    // Name adsTextList
                    binding.TVTitleImages.text = adsTextList[position-1]
                }
            }
        })
    }

    private fun FragmentStartPageBinding.createImagesTabPager() {
        val dpValue10 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18f,
            resources.displayMetrics).toInt()
        val dpValue4 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f,
            resources.displayMetrics).toInt()
        val layoutParams = LinearLayout.LayoutParams(dpValue10,dpValue10)

        for (element in adsList) {
            val imageView = ImageView(context)
            imageView.layoutParams = layoutParams
            imageView.setPadding(dpValue4,dpValue4,dpValue4,dpValue4)
            imageView.setImageResource(R.drawable.circle)
            imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.grey_200))
            imageView.setOnClickListener {
                val position = LLCounterImages.indexOfChild(imageView)
                ViewPager2Start.currentItem = position+1
            }
            LLCounterImages.addView(imageView,layoutParams)

            vm.pagerCounter.observe(viewLifecycleOwner) {
                if (ViewPager2Start.currentItem-1 == LLCounterImages.indexOfChild(imageView))
                    imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.blue))
                else
                    imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.grey_200))

            }
        }
    }

    private fun FragmentStartPageBinding.initButtons(){
        TVBtnSkipStart.setOnClickListener {
            TVBtnSkipStart.timerDoubleBtn()
            findNavController().navigate(R.id.action_navigation_start_page_to_navigation_login, null)
        }

        TVBtnNext.setOnClickListener {
            TVBtnNext.timerDoubleBtn()
            CCImages.visibility = View.VISIBLE
            ViewPager2Start.setCurrentItem(0,false)
            binding.root.postDelayed({
                LoaderImages.visibility = View.GONE
            }, 200L)
            LLStateZero.visibility = View.GONE
            ImageKitty.visibility = View.GONE
            autoSwitcher(ViewPager2Start)
        }
    }

    private fun View.timerDoubleBtn(time: Long = 2000) {
        isClickable = false
        Handler(Looper.getMainLooper()).postDelayed({
            isClickable = true
        }, time)
    }

}