package github.nikandpro.mapper;

import github.nikandpro.dto.PhoneDataDto;
import github.nikandpro.entity.PhoneData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PhoneMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "phone", target = "phone")
    PhoneDataDto toDto(PhoneData phoneData);
}
