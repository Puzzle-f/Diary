package com.example.diary.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diary.R
import com.example.diary.databinding.FragmentHomeBinding
import com.example.diary.databinding.ItemLessonBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    val vm: HomeViewModel by viewModel()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var itemLesBinding: ItemLessonBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTimer()
        getLessons()
    }

    fun getTimer() {
        var isStrt = true
        vm.getTime()
        val isStartObserver = Observer<Boolean> {
            isStrt = it
        }
        val nameObserver = Observer<Long> { time ->
            if (isStrt) {
                binding.chronometer.setBase(SystemClock.elapsedRealtime() + time)
                binding.chronometer.start()
            } else {
                startExam()
            }
        }
        vm.isStart.observe(viewLifecycleOwner, isStartObserver)
        vm.time.observe(viewLifecycleOwner, nameObserver)
    }

    fun startExam() {
        binding.chronometer.stop()
        binding.timeBeforeExamText.text = " R.string.start_exam.toString()"
        Toast.makeText(context, R.string.start_exam, Toast.LENGTH_SHORT).show()
    }

    private fun getLessons() {
        vm.getLessons()
        vm.listLesson.observe(viewLifecycleOwner) {
            binding.lessons.adapter = AdapterLesson(it)
            binding.homeWorks.adapter = AdapterHomeWork(it)
//            for (i in 0..it.size-1){
//                if(it.get(i).isOpenIn&&binding.lessons.size!=0){
//                    binding.lessons.get(i)
//                        .setOnClickListener {
////                        openTelegram("https://t.me/meduzalive")
//                        Log.e("", "itemBinding.lessonName ")
//                        }
//                }
//
//            }
            if (binding.lessons.size != 0) {
//                binding.lessons.setOnClickListener { openTelegram("https://t.me/meduzalive") }
            }
        }
        binding.lessons.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeWorks.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun openTelegram(link: String) {
        val telegram = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        telegram.setPackage("org.telegram.messenger")
        startActivity(telegram)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}