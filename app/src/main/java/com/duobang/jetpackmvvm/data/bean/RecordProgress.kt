package com.duobang.jetpackmvvm.data.bean

class RecordProgress(var fromValue: Float, var toValue: Float) {
    var time: Long = 0
    var accumulateValue = 0f
    var isFinish = false

}