package ua.anton.tsa.testassignment.mapper;

import org.mapstruct.*;
import ua.anton.tsa.testassignment.model.User;
import ua.anton.tsa.testassignment.wire.request.CreateUserRequest;
import ua.anton.tsa.testassignment.wire.request.ReplaceUserRequest;
import ua.anton.tsa.testassignment.wire.response.RetrieveUsersResponse;

import java.util.Map;
import java.util.Optional;

@Mapper(
        componentModel = "spring"
)
public abstract class UserMapper {

    @Mapping(target = "id", ignore = true)
    public abstract User toUser(CreateUserRequest createUserRequest);

    public abstract RetrieveUsersResponse toRetrieveUsersResponse(User user);

    public abstract User toUser(Long id, ReplaceUserRequest replaceUserRequest);
    @Named("mapOptionalString")
    public String mapOptionalString(Optional<String> value) {
        return value.orElse(null);
    }

    @Named("mapToOptional")
    protected Optional<String> mapToOptional(String value) {
        return Optional.ofNullable(value);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "email", defaultExpression = "java(user.getEmail())")
    @Mapping(target = "firstName", source = "firstName", defaultExpression = "java(user.getFirstName())")
    @Mapping(target = "lastName", source = "lastName", defaultExpression = "java(user.getLastName())")
    @Mapping(target = "birthDate", source = "birthDate", defaultExpression = "java(user.getBirthDate())")
    @Mapping(target = "address", source = "address", defaultExpression = "java(user.getAddress())")
    @Mapping(target = "phoneNumber", source = "phoneNumber", defaultExpression = "java(user.getPhoneNumber())")
    public abstract User toUser(@MappingTarget User user, Map<String, String> modifyUserRequest);




}
