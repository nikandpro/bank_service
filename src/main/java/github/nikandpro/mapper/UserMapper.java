package github.nikandpro.mapper;

import github.nikandpro.dto.UserCreateRequest;
import github.nikandpro.dto.UserDto;
import github.nikandpro.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UserMapper {

    User toUser(UserCreateRequest request);

    UserDto toUserDto(User user);
}
