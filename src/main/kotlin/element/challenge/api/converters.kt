package element.challenge.api

import element.challenge.persistence.BookingEntity
import element.challenge.persistence.HikerEntity
import element.challenge.persistence.TrailEntity

fun HikerDTO.toEntity() = with(this) {
    HikerEntity(id, name, surname, dateOfBirth, phoneNumber, email)
}

fun HikerEntity.toDTO() = with(this) {
    HikerDTO(id, name, surname, dateOfBirth, phoneNumber, email)
}

fun TrailEntity.toDTO() = with(this) {
    TrailDTO(id!!, name, startAt, endAt, minimumAge, maximumAge, unitPrice)
}

fun BookingEntity.toDTO() = with(this) {
    BookingDTO(id!!, this.trail.toDTO(), this.hikers.map { it.toDTO() }.toSet(), this.date)
}