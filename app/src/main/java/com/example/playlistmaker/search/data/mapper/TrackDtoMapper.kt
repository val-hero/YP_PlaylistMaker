package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.data.model.TrackDto
import com.example.playlistmaker.search.domain.mapper.DomainMapper
import com.example.playlistmaker.search.domain.model.Track

class TrackDtoMapper: DomainMapper<TrackDto, Track> {

    override fun mapToDomainModel(model: TrackDto): Track {
        return Track(
            trackName = model.trackName ?: "",
            artistName =  model.artistName ?: "",
            trackId = model.trackId ?: -1,
            collectionName = model.collectionName ?: "",
            releaseDate = model.releaseDate ?: "",
            country = model.country ?: "",
            previewUrl = if(!model.previewUrl.isNullOrBlank()) model.previewUrl else "",
            genre = model.primaryGenreName ?: "",
            duration = model.trackTimeMillis ?: -1,
            imageUrl = model.artworkUrl100 ?: ""
        )
    }

    override fun mapFromDomainModel(domainModel: Track): TrackDto {
        return TrackDto(
            trackName = domainModel.trackName,
            artistName =  domainModel.artistName,
            trackId = domainModel.trackId,
            collectionName = domainModel.collectionName,
            releaseDate = domainModel.releaseDate,
            country = domainModel.country,
            previewUrl = domainModel.previewUrl,
            primaryGenreName = domainModel.genre,
            trackTimeMillis = domainModel.duration,
            artworkUrl100 = domainModel.imageUrl
        )
    }
}