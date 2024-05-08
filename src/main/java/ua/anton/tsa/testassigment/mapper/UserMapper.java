package ua.anton.tsa.testassigment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import ua.anton.tsa.testassigment.model.User;
import ua.anton.tsa.testassigment.wire.request.CreateUserRequest;
import ua.anton.tsa.testassigment.wire.request.ModifyUserRequest;
import ua.anton.tsa.testassigment.wire.request.ReplaceUserRequest;
import ua.anton.tsa.testassigment.wire.response.RetrieveUsersResponse;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class UserMapper {

    @Mapping(target = "id", ignore = true)
    public abstract User toUser(CreateUserRequest createUserRequest);

    public abstract RetrieveUsersResponse toRetrieveUsersResponse(User user);

    public abstract User toUser(Long id, ReplaceUserRequest replaceUserRequest);

    @Mapping(target = "email", source = "modifyUserRequest.email", defaultExpression = "java(user.getEmail())")
    @Mapping(target = "firstName", source = "modifyUserRequest.firstName", defaultExpression = "java(user.getFirstName())")
    @Mapping(target = "lastName", source = "modifyUserRequest.lastName", defaultExpression = "java(user.getLastName())")
    @Mapping(target = "address", source = "modifyUserRequest.address", defaultExpression = "java(user.getAddress())")
    @Mapping(target = "phoneNumber", source = "modifyUserRequest.phoneNumber", defaultExpression = "java(user.getPhoneNumber())")
    @Mapping(
            target = "birthDate",
            source = "modifyUserRequest.birthDate",
            defaultExpression = "java(user.getBirthDate())"
    )
    public abstract User toUser(User user, ModifyUserRequest modifyUserRequest);

}
