package com.example.uris

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.uris.ui.theme.UrisTheme
import java.io.File
import java.io.FileOutputStream


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        //1 - Resource Uri
        // In Android development, the ContentResolver class is a central component for accessing data stored by other applications or the
        // system itself. It acts as an intermediary between your app and content providers, which are specialized services that manage
        // specific data types.
//      InputStream: Represents a source of bytes that your app can read from. It's commonly used to read data from files, network connections, or other sources.
//      OutputStream: Represents a destination for bytes that your app can write to. It's often used to write data to files, network connections, or other destinations.

        val uri = Uri.parse("android.resource://$packageName/drawable/kermit1")
        val kermit = contentResolver.openInputStream(uri)?.use {
            it.readBytes()
        }
        Log.d("pokemon", "The size = ${kermit?.size}")


        //In Android every single app live in it own sandbox kinda thing you got internal storage which can only be accessed in your app
        //we are in activity else context.filesDir
        //2 - File Uri
        val file = File(filesDir, "Kermit.jpg")
        FileOutputStream(file).use {
            it.write(kermit)
        }
        Log.d("pokemon", "${file.toURI()}")

        //3 - Content Uri  - Use to share specific content with the other apps
        //Like here the gallery app is sharing the images and the documents with the other apps
        //TODO The composable example is given below Alright

        //4 - Data Uri - A data uri is a uri that already contains the actual context it encodes inside the uri itself it's often encoded as Bade 64
        val dataUri = Uri.parse("data:text/plain;charset=UTF-8,Hello%20World")


        setContent {
            UrisTheme {
                val pickImage = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = {contentUri ->
                        Log.d("pokemon", "$contentUri")
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Button(
                        onClick = {
                            pickImage.launch("image/*")
                        }
                    ) {
                        Text(text = "Choose")
                    }
                }
            }
        }
    }

}