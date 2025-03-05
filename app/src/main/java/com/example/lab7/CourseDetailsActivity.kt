package com.example.lab7

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab7.ui.theme.FireBase2Theme
import com.google.firebase.firestore.FirebaseFirestore

class CourseDetailsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireBase2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "GFG",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.Black
                                    )
                                }
                            )
                        }
                    ) {innerPadding ->
                        Text(
                            modifier = Modifier.padding(innerPadding),
                            text = "Cap nhat du lieu."
                        )
                        var courseList = mutableStateListOf<Course?>()

                        var db: FirebaseFirestore = FirebaseFirestore.getInstance()
                        db.collection("Courses").get()
                            .addOnSuccessListener { queryDocumentSnapshots ->
                                if (!queryDocumentSnapshots.isEmpty) {
                                    val list = queryDocumentSnapshots.documents

                                    for (d in list) {
                                        val c: Course? = d.toObject(Course::class.java)
                                        c?.courseID = d.id
                                        Log.e("TAG", "Course id is : " + c!!.courseID)
                                        courseList.add(c)
                                    }
                                } else {
                                    Toast.makeText(
                                        this@CourseDetailsActivity,
                                        "No data found in Database",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@CourseDetailsActivity,
                                    "Fail to get the data.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        FirebaseUI(LocalContext.current, courseList)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FirebaseUI(context: Context, courseList: SnapshotStateList<Course?>) {
    Column(
        modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(top = 120.dp).background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn (
//            modifier = Modifier.padding(4.dp)
        ) {
            itemsIndexed(courseList) {index, item ->
                Card(
                    onClick = {
                        val i = Intent(context, UpdateCourse::class.java)
                        i.putExtra("courseName", item?.courseName)
                        i.putExtra("courseDuration", item?.courseDuration)
                        i.putExtra("courseDescription", item?.courseDescription)
                        i.putExtra("courseID", item?.courseID)
                        context.startActivity(i)
                    },
                    modifier = Modifier.padding(8.dp),
                    elevation = 6.dp
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp).fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.width(5.dp))
                        courseList[index]?.courseName?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(4.dp),
                                color = Color.Blue,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        courseList[index]?.courseDuration?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(4.dp),
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 15.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        courseList[index]?.courseDescription?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(4.dp),
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 15.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
