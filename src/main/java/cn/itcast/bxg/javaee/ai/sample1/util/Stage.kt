/*
 * Copyright (c) 博学谷 2019.
 * Author：Jam Fang
 * https://www.jianshu.com/u/0977ede560d4
 *
 */

package cn.itcast.bxg.javaee.ai.sample1.util

class Stage constructor(name: String, totalStage: Int, totalStep: Int) {
    var name = name
    var currentStage = 1
    var totalStage = totalStage
    var currentStep = 1
    var totalStep = totalStep
    var percent = 0.0
        //currentStep/totalStep
        get() = currentStep * 1.0 / totalStep
}
