package com.example.smit_pt.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.smit_pt.WorkshopsConfig.Workshop;
import com.example.smit_pt.dto.WorkshopInfoDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface WorkshopInfoMapper {

	WorkshopInfoMapper INSTANCE = Mappers.getMapper(WorkshopInfoMapper.class);

	@Mapping(target = "id", source = "id")
	@Mapping(target = "name", source = "name")
	@Mapping(target = "location", source = "location")
	@Mapping(target = "vehicleTypes", source = "vehicleTypes")
	WorkshopInfoDto toDto(Workshop workshop);

	List<WorkshopInfoDto> toDtos(List<Workshop> workshops);

}
