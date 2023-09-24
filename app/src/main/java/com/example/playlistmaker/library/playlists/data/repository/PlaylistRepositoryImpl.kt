package com.example.playlistmaker.library.playlists.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.core.data.database.AppDatabase
import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.utils.PLAYLIST_IMAGES_FOLDER
import com.example.playlistmaker.core.utils.PLAYLIST_IMAGE_QUALITY
import com.example.playlistmaker.library.playlists.data.database.entity.mapToDomain
import com.example.playlistmaker.library.playlists.data.database.entity.mapToPlaylistEntity
import com.example.playlistmaker.library.playlists.data.database.entity.mapToPlaylistTrackEntity
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
        var imageFile: String? = null
        if (playlist.image != null) {
            imageFile = saveImageToFile(playlist.image.toUri())
        }
        database.playlistDao().insert(playlist.mapToPlaylistEntity().copy(image = imageFile))
    }

    override suspend fun saveToPlaylist(playlist: Playlist, track: Track) {
        database.playlistTracksDao().insert(track.mapToPlaylistTrackEntity())

        val updatedPlaylist = playlist.apply {
            tracksIds?.add(track.id)
            tracksCount++
        }
        database.playlistDao().update(updatedPlaylist.mapToPlaylistEntity())
    }

    override suspend fun getById(id: Long): Playlist {
        return database.playlistDao().get(id).mapToDomain()
    }

    override fun getAll(): Flow<List<Playlist>> = flow {
        val playlists = database.playlistDao().getAll()
        emit(playlists.map { it.mapToDomain() })
    }

    private fun saveImageToFile(uri: Uri): String? {
        val imageFileName = Calendar.getInstance().timeInMillis.toString() + ".jpg"
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLIST_IMAGES_FOLDER
        )
        return try {
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
            imageFileName
        } catch (e: Exception) {
            null
        }
    }
}