package com.example.containerization

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kotlin.collections.emptyList

@Composable
fun PhotoGalleryScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    var photos by remember {
        mutableStateOf<List<Bitmap>>(emptyList())
    }

    var loading by remember {mutableStateOf(true)}
    var errorMessage by remember {mutableStateOf("")}

    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            errorMessage = "Log in"
            loading = false
            return@LaunchedEffect
        }

        try {
            val documents = FirebaseFirestore.getInstance().collection("photos")
                .whereEqualTo("ownerId", user.uid).get().await()

            val storage = FirebaseStorage.getInstance()
            val loadedPhotos = mutableListOf<Bitmap>()

            for (document in documents.documents) {
                val path = document.getString("storagePath")
                    ?: continue

                try {
                    val bytes = storage.reference.child(path).getBytes(5L * 1024 * 1024).await()

                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                    if (bitmap != null) {
                        loadedPhotos.add(bitmap)
                    }

                } catch (e: Exception) {
                    errorMessage = "Couldn't load some of your photos"
                }
            }

            photos = loadedPhotos
        } catch (e: Exception) {
            errorMessage = "Couldn't load any of your photos"
        } finally {
            loading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(15.dp)
    ){
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ){
            Button(onClick = onBack){
                Text("Back to camera")
            }

            Button(onClick = onLogout){
                Text("Logout")
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = "My Photos",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer (Modifier.height(15.dp))

        if(loading) {
            CircularProgressIndicator()
        } else if (photos.isEmpty()) {
            Text("Your library is empty.")
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(photos){ bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Saved photo",
                        modifier = Modifier.fillMaxWidth().height(150.dp)
                    )
                }
            }
        }

        if (errorMessage.isNotBlank()){
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
