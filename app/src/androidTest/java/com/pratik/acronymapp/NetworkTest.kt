package com.pratik.acronymapp

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pratik.acronymapp.utils.NetworkUtil
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//Network test to check if internet/network is available or not
@RunWith(AndroidJUnit4::class)
class NetworkTest {

    lateinit var mContext: Context

    @Before
    fun setUp() {
        mContext = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun testNetwork() {
        val result = NetworkUtil.isNetworkAvailable(mContext)
        assertTrue(result)
    }
}