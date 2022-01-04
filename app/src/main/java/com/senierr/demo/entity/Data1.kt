package com.senierr.demo.entity

/**
 *
 * @author chunjiezhou
 */
data class Data1(
    val content: String,
    val height: Int
) : IData {
    override fun getType(): Int = 1
}
