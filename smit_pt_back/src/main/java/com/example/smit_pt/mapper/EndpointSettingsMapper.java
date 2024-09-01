package com.example.smit_pt.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.smit_pt.configClasses.EndpointSettings;
import com.example.smit_pt.dto.EndpointSettingsDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface EndpointSettingsMapper {

	EndpointSettingsMapper INSTANCE = Mappers.getMapper(EndpointSettingsMapper.class);

	@Mapping(target = "desc", source = "desc")
	@Mapping(target = "queryParams", source = "queryParams")
	EndpointSettingsDto toDto(EndpointSettings endpointSettings);

	List<EndpointSettingsDto> toDtos(List<EndpointSettings> endpointSettings);
}
