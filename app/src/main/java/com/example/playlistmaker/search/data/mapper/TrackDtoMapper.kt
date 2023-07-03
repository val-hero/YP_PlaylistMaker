package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.data.model.TrackDto
import com.example.playlistmaker.search.domain.mapper.DomainMapper
import com.example.playlistmaker.search.domain.model.Track

class TrackDtoMapper: DomainMapper<TrackDto, Track> {

    override fun mapToDomainModel(dto: TrackDto): Track {
        return Track(
            trackName = dto.trackName,
            artistName =  dto.artistName,
            trackId = dto.trackId,
            collectionName = dto.collectionName,
            releaseDate = dto.releaseDate,
            country = dto.country,
            previewUrl = dto.previewUrl,
            genre = dto.primaryGenreName,
            duration = dto.trackTimeMillis,
            imageUrl = dto.artworkUrl100
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
            previewUrl = domainModel.previewUrl ?: "",
            primaryGenreName = domainModel.genre,
            trackTimeMillis = domainModel.duration,
            artworkUrl100 = domainModel.imageUrl
        )
    }
}