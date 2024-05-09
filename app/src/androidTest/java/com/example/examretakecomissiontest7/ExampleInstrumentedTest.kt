package com.example.examretakecomissiontest7

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import java.nio.charset.Charset

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.examretakecomissiontest7", appContext.packageName)
    }

    @RunWith(AndroidJUnit4::class)
    class MainActivityTest
    {
        @get:Rule
        val composeRuleTest = createComposeRule()

        @Test
        fun loginScreenError(context: Context)
        {
            composeRuleTest.setContent {
                val scope = CoroutineScope(Dispatchers.IO)

                val db = Room.databaseBuilder(
                    context = LocalContext.current.applicationContext,
                    klass = DataBase::class.java,
                    name = "dbu"
                ).allowMainThreadQueries().build()

                val pathFile = "JsonFile.json"
                val file = context.applicationContext.assets.open(pathFile)
                val jsonString = file.readBytes().toString(Charset.defaultCharset())
                val json = parseJson(jsonString)
                val list = db.flightsDao.getAllFlights()

                json.data.forEach{
                    if(!list.contains(it)){
                        scope.launch {
                            db.flightsDao.insert(it)
                        }
                    }
                }

                MainNAv(db = db)
            }

            composeRuleTest.onNodeWithTag("EMAIL").performTextInput("login")
            composeRuleTest.onNodeWithTag("PASSWORD").performTextInput("password")
            composeRuleTest.onNodeWithTag("LOGIN").performClick()
        }
    }
}