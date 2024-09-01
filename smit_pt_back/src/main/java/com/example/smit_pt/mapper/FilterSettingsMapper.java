package com.example.smit_pt.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.smit_pt.WorkshopsConfig.Workshop;
import com.example.smit_pt.dto.FilterSettingsDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface FilterSettingsMapper {

	FilterSettingsMapper INSTANCE = Mappers.getMapper(FilterSettingsMapper.class);

	@Mapping(target = "id", source = "id")
	@Mapping(target = "name", source = "name")
	@Mapping(target = "location", source = "location")
	@Mapping(target = "vehicleTypes", source = "vehicleTypes")
	FilterSettingsDto toDto(Workshop workshop);

	List<FilterSettingsDto> toDtos(List<Workshop> workshops);

}
