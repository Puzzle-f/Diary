package com.example.diary.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diary.data.Lesson
import com.example.diary.data.domain.Repository
import java.time.LocalDateTime

class HomeViewModel(val repository: Repository) : ViewModel() {

    private var _time = MutableLiveData<Long>()
    val time: LiveData<Long> = _time

    private var _isStart = MutableLiveData<Boolean>()
    val isStart: LiveData<Boolean> = _isStart

    private var _listLesson = MutableLiveData<List<Lesson>>()
    val listLesson: LiveData<List<Lesson>> = _listLesson

    fun getTime() {
        val currentData = repository.getCurrentTime()
        val examRepo = repository.getExamDate()
        if ((parseLocalDateTimeToLong(examRepo) - parseLocalDateTimeToLong(currentData)) >= 0) {
            _time.postValue(
                parseLocalDateTimeToLong(examRepo) - parseLocalDateTimeToLong(
                    currentData
                )
            )
            _isStart.postValue(true)
        } else {
            _isStart.postValue(false)
        }
    }

    fun getLessons() {
        _listLesson.postValue(repository.getListLessons())
    }

    fun parseLocalDateTimeToLong(time: LocalDateTime): Long {
        val day = time.dayOfYear * 24 * 60 * 60 * 1000L
        val hour = time.hour * 60 * 60 * 1000L
        val min = time.minute * 60 * 1000L
        return day + hour + min
    }


}