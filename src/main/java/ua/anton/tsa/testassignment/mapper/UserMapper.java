package ua.anton.tsa.testassignment.mapper;

import org.mapstruct.*;
import ua.anton.tsa.testassignment.model.User;
import ua.anton.tsa.testassignment.wire.request.CreateUserRequest;
import ua.anton.tsa.testassignment.wire.request.ReplaceUserRequest;
import ua.anton.tsa.testassignment.wire.response.RetrieveUsersResponse;

import java.util.Map;

/**
 * An abstract class, used by Mapstruct to generate a Mapper between model and DTOs
 */
@Mapper(
        componentModel = "spring"
)
public abstract class UserMapper {

    /**
     * An abstract method is used to generate a mapper from {@link CreateUserRequest} object to {@link User} object
     * {@link Mapping} annotation is placed to ignore "id" field while mapping
     *
     * @param createUserRequest - {@link CreateUserRequest} object to map from
     * @return {@link User} object
     */
    @Mapping(target = "id", ignore = true)
    public abstract User toUser(CreateUserRequest createUserRequest);


    /**
     * An abstract method is used to generate a mapper from {@link ReplaceUserRequest} object to {@link User} object
     *
     * @param id                 - {@link Long} id of the mapped object
     * @param replaceUserRequest - {@link ReplaceUserRequest} object to map from
     * @return {@link User} object
     */
    public abstract User toUser(Long id, ReplaceUserRequest replaceUserRequest);


    /**
     * An abstract method is used to generate a mapper from {@link Map} object to {@link User} object.
     * {@link Mapping} annotations describe mapping rules
     *
     * @param user              - a updatable {@link User} object
     * @param modifyUserRequest - a {@link Map} of new fields and corresponding values
     * @return an updated {@link User} object
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "email", defaultExpression = "java(user.getEmail())")
    @Mapping(target = "firstName", source = "firstName", defaultExpression = "java(user.getFirstName())")
    @Mapping(target = "lastName", source = "lastName", defaultExpression = "java(user.getLastName())")
    @Mapping(target = "birthDate", source = "birthDate", defaultExpression = "java(user.getBirthDate())")
    @Mapping(target = "address", source = "address", defaultExpression = "java(user.getAddress())")
    @Mapping(target = "phoneNumber", source = "phoneNumber", defaultExpression = "java(user.getPhoneNumber())")
    public abstract User toUser(@MappingTarget User user, Map<String, String> modifyUserRequest);

    /**
     * An abstract method is used to generate a mapper from {@link User} object to {@link RetrieveUsersResponse} object
     *
     * @param user - {@link User} object to map from
     * @return {@link RetrieveUsersResponse} object
     */
    public abstract RetrieveUsersResponse toRetrieveUsersResponse(User user);

}
