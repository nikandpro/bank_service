package github.nikandpro.mapper;

import github.nikandpro.dto.request.UserCreateRequest;
import github.nikandpro.dto.UserDto;
import github.nikandpro.dto.response.UserResponseDto;
import github.nikandpro.entity.EmailData;
import github.nikandpro.entity.PhoneData;
import github.nikandpro.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "password", target = "password")
    User toUser(UserCreateRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(target = "balance", source = "account.balance")
    @Mapping(target = "emails", source = "emails", qualifiedByName = "emailsToList")
    @Mapping(target = "phones", source = "phones", qualifiedByName = "phonesToList")
    UserDto toUserDto(User user);

    @Mapping(target = "emails", source = "emails", qualifiedByName = "emailsToList")
    @Mapping(target = "phones", source = "phones", qualifiedByName = "phonesToList")
    @Mapping(target = "accountBalance", source = "account.balance")
    UserResponseDto toResponseDto(User user);

    @Named("emailsToList")
    default List<String> emailsToList(Set<EmailData> emails) {
        return emails.stream().map(EmailData::getEmail).collect(Collectors.toList());
    }

    @Named("phonesToList")
    default List<String> phonesToList(Set<PhoneData> phones) {
        return phones.stream().map(PhoneData::getPhone).collect(Collectors.toList());
    }
}
