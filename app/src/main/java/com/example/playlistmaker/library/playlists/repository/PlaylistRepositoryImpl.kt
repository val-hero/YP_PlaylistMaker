package com.example.playlistmaker.library.playlists.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.core.data.database.AppDatabase
import com.example.playlistmaker.core.utils.PLAYLIST_IMAGES_FOLDER
import com.example.playlistmaker.core.utils.PLAYLIST_IMAGE_QUALITY
import com.example.playlistmaker.library.playlists.data.database.entity.mapToDomain
import com.example.playlistmaker.library.playlists.data.database.entity.mapToPlaylistEntity
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.library.playlists.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class PlaylistRepositoryImpl(
    private val database: AppDatabase,
    private val context: Context
) : PlaylistRepository {

    override suspend fun save(playlist: Playlist) {
        database.playlistDao()
            .insert(playlist.mapToPlaylistEntity())
    }

    override fun getDetails(id: Long): Playlist {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<List<Playlist>> = flow {
        val playlists = database.playlistDao().getAll()
        emit(playlists.map { it.mapToDomain() })
    }

    private fun saveImageToFile(uri: Uri): String {
        val imageFileName = Calendar.getInstance().timeInMillis.toString() + ".jpg"
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLIST_IMAGES_FOLDER
        )

        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        BitmapFactory
            .decodeStream(context.contentResolver.openInputStream(uri))
            .compress(
                Bitmap.CompressFormat.JPEG,
                PLAYLIST_IMAGE_QUALITY,
                FileOutputStream(File(filePath, imageFileName))
            )
        return imageFileName
    }
}