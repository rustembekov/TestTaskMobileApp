package com.example.testtaskmobileapp.core.mapper;

interface Mapper<DTO, Domain> {
    fun toDomain(dto: DTO): Domain
}
