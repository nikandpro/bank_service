package github.nikandpro.mapper;

import github.nikandpro.dto.EmailDataDto;
import github.nikandpro.entity.EmailData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface EmailMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    EmailDataDto toDto(EmailData emailData);
}
