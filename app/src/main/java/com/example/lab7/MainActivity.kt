package com.example.lab7

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab7.ui.theme.FireBase2Theme
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireBase2Theme {
                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(backgroundColor = Color.Green,
                                title = {
                                    Text(
                                        text = "GFG",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                }
                            )
                        }
                    ) {
                        innerPadding -> Text(
                            modifier = Modifier.padding(innerPadding),
                            text = "Them du lieu."
                        )
                        FirebaseUI(LocalContext.current)
                    }
                }
            }
        }
    }
}

@Composable
fun FirebaseUI(context: Context) {
    val courseID = remember {
        mutableStateOf("")
    }
    val courseName = remember {
        mutableStateOf("")
    }
    val courseDuration = remember {
        mutableStateOf("")
    }
    val courseDescription = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = courseName.value,
            onValueChange = {courseName.value = it},
            placeholder = {
                Text(
                    text = "Enter your Course name"
                )
            },

            modifier = Modifier.padding(16.dp).fillMaxWidth(),

            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = courseDuration.value,
            onValueChange = {courseDuration.value = it},
            placeholder = {
                Text(
                    text = "Enter your Course duration"
                )
            },

            modifier = Modifier.padding(16.dp).fillMaxWidth(),

            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = courseDescription.value,
            onValueChange = {courseDescription.value = it},
            placeholder = {
                Text(
                    text = "Enter your Course description"
                )
            },

            modifier = Modifier.padding(16.dp).fillMaxWidth(),

            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (TextUtils.isEmpty(courseName.value.toString())) {
                    Toast.makeText(context, "Please enter your course name", Toast.LENGTH_SHORT).show()
                } else if (TextUtils.isEmpty(courseDuration.value.toString())) {
                    Toast.makeText(context, "Please enter your course duration", Toast.LENGTH_SHORT).show()
                } else if (TextUtils.isEmpty(courseDescription.value.toString())) {
                    Toast.makeText(context, "Please enter your course description", Toast.LENGTH_SHORT).show()
                } else {
                    addDatatoFirebase(courseID.value, courseName.value, courseDuration.value, courseDescription.value, context)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(
                text = "Add Data",
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                context.startActivity(Intent(context, CourseDetailsActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Views Courses",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

fun addDatatoFirebase(courseID: String, courseName: String, courseDuration: String, courseDescription: String, context: Context) {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val dbCourses: CollectionReference = db.collection("Courses")

    val courses = Course(courseID, courseName, courseDuration, courseDescription)

    dbCourses.add(courses).addOnSuccessListener {
        Toast.makeText(
            context, "Your course has been added to Firebase Firestore", Toast.LENGTH_SHORT
        ).show()
    }.addOnFailureListener {
        e -> Toast.makeText(context, "Fail to add course \n$e", Toast.LENGTH_SHORT).show()
    }
}
