package com.some.micro.mappers;

import com.some.micro.model.dto.UserResponseDto;
import com.some.micro.model.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
 UserResponseDto toUserResponseDto(UserEntity userEntity);
}
