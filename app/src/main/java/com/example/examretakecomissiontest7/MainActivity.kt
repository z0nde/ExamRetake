package com.example.examretakecomissiontest7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Database
import androidx.room.Room
import com.example.examretakecomissiontest7.ui.theme.ExamRetakeComissionTest7Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.charset.Charset

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExamRetakeComissionTest7Theme {
                Surface() {
                    val scope = CoroutineScope(Dispatchers.IO)

                    val db = Room.databaseBuilder(
                        context = LocalContext.current.applicationContext,
                        klass = DataBase::class.java,
                        name = "dbu"
                    ).allowMainThreadQueries().build()

                    val pathFile = "JsonFile.json"
                    val file = applicationContext.assets.open(pathFile)
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
            }
        }
    }
}

@Composable
fun MainNAv(db: DataBase)
{
    var navController = rememberNavController()
    var email = ""

    NavHost(navController = navController, startDestination = "authScreen")
    {
        composable("authScreen"){
            email = AuthScreen(db = db, navController = navController)
        }
        composable("showListScreen"){
            ShowListScreen(db = db, navController = navController, email = email)
        }
    }
}

@Composable
fun AuthScreen(db: DataBase, navController: NavController): String
{
    val scope = CoroutineScope(Dispatchers.IO)

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    Column {
        TextField(
            value = email,
            onValueChange = {email = it},
            label = { Text(text = "email")},
            modifier = Modifier.testTag("EMAIL")
        )
        TextField(
            value = password,
            onValueChange = {password = it},
            label = { Text(text = "password")},
            modifier = Modifier.testTag("PASSWORD")
        )

        Row {
            Button(modifier = Modifier.testTag("LOGIN"), onClick = {
                if (db.usersDao.getAllUsers().any{it.email == email && it.password == password}){
                    navController.navigate("showListScreen")
                }
            }) {
                Text(text = "LogIn")
            }

            Button(onClick = {
                if (!db.usersDao.getAllUsers().any{it.email == email} && !email.isNullOrEmpty() && !password.isNullOrEmpty()){
                    scope.launch {
                        db.usersDao.insert(User(email, password))
                    }
                }
            }) {
                Text(text = "Register")
            }
        }
    }
    return email
}

@Composable
fun ShowListScreen(db: DataBase, navController: NavController, email: String)
{
    var list by remember {
        mutableStateOf(db.flightsDao.getAllFlights())
    }

    Column {
        Row{
            Text(text = email)
            Button(onClick = {
                navController.navigate("authScreen")
            }) {
                Text(text = "LogOut")
            }
        }

        LazyColumn {
            items(list){item ->
                Box{
                    Column {
                        Text("Start city: " + item.startCity)
                        Text("End city: " + item.endCity)
                        Text("Start city code: "+ item.startCityCode)
                        Text("End city code: " + item.endCityCode)
                        Text("Start date: " + item.startDate)
                        Text("End date: " + item.endDate)
                        Text("Price: " + item.price)
                        Text("Search token: " + item.searchToken)
                    }
                }
            }
        }
    }
}