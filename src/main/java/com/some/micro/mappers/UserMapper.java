package com.some.micro.mappers;

import com.some.micro.model.dto.UserResponseDto;
import com.some.micro.model.entities.UserEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {
 UserResponseDto toUserResponseDto(UserEntity userEntity);
}
