package github.nikandpro.mapper;

import github.nikandpro.dto.request.UserCreateRequest;
import github.nikandpro.dto.UserDto;
import github.nikandpro.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "password", target = "password")
    User toUser(UserCreateRequest request);

    UserDto toUserDto(User user);
}
