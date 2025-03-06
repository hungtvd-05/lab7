package com.example.lab7

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
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
import com.example.lab7.ui.theme.Lab7Theme
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCourse : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab7Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                backgroundColor = Color.Green,
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
                    ) { innerPadding ->
                        Text(modifier = Modifier.padding(innerPadding),
                            text = "Cap nhat du lieu"
                        )

                        firebaseUI(
                            LocalContext.current,
                            intent.getStringExtra("courseName"),
                            intent.getStringExtra("courseDuration"),
                            intent.getStringExtra("courseDescription"),
                            intent.getStringExtra("courseID")
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun firebaseUI(context: Context, name: String?, duration: String?, description: String?, courseID: String?) {
    val courseName = remember { mutableStateOf(name) }
    val courseDuration = remember { mutableStateOf(duration) }
    val courseDescription = remember { mutableStateOf(description) }

    Column(
        modifier = Modifier.fillMaxHeight().fillMaxWidth().background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = courseName.value.toString(),
            onValueChange = {
                courseName.value = it
            },
            placeholder = {
                Text(
                    text = "Enter your course name"
                )
            },
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = courseDuration.value.toString(),
            onValueChange = {
                courseDuration.value = it
            },
            placeholder = {
                Text(
                    text = "Enter your course duration"
                )
            },
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = courseDescription.value.toString(),
            onValueChange = {
                courseDescription.value = it
            },
            placeholder = {
                Text(
                    text = "Enter your course name"
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
                    Toast.makeText(context, "Please enter course name", Toast.LENGTH_SHORT).show()
                } else if (TextUtils.isEmpty(courseDuration.value.toString())) {
                    Toast.makeText(context, "Please enter course duration", Toast.LENGTH_SHORT).show()
                } else if (TextUtils.isEmpty(courseDescription.value.toString())) {
                    Toast.makeText(context, "Please enter course description", Toast.LENGTH_SHORT).show()
                } else {
                    updateDatatoFireBase (
                        courseID,
                        courseName.value,
                        courseDuration.value,
                        courseDescription.value,
                        context
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(text = "Update Data", modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                deleteDatatoFireBase (
                    courseID,
                    context
                )
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(text = "Delete Data", modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

fun deleteDatatoFireBase(courseID: String?, context: Context) {
    val db = FirebaseFirestore.getInstance()
    db.collection("Courses").document(courseID.toString())
        .delete().addOnSuccessListener {
            Toast.makeText(context, "Data deleted successfully", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, CourseDetailsActivity::class.java))
        }.addOnFailureListener {
            Toast.makeText(context, "Fail to delete data", Toast.LENGTH_SHORT).show()
        }
}

fun updateDatatoFireBase(courseID: String?, courseName: String?, courseDuration: String?, courseDescription: String?, context: Context) {
    val updateCourse = Course(courseID, courseName, courseDuration, courseDescription)

    val db = FirebaseFirestore.getInstance()

    db.collection("Courses").document(courseID.toString())
        .set(updateCourse)
        .addOnSuccessListener {
            Toast.makeText(context, "Data updated successfully", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, CourseDetailsActivity::class.java))
        }.addOnFailureListener {
            Toast.makeText(context, "Fail to update data", Toast.LENGTH_SHORT).show()
        }

}
