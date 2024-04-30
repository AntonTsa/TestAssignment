package ua.anton.tsa.testassigment.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ua.anton.tsa.testassigment.wire.request.CreateUserRequest;
import ua.anton.tsa.testassigment.wire.request.ModifyUserRequest;
import ua.anton.tsa.testassigment.wire.request.ReplaceUserRequest;
import ua.anton.tsa.testassigment.wire.response.RetrieveUsersResponse;

import java.time.LocalDate;
import java.util.List;

public class UserControllerFixture {

    static final Long USER_ID_VALID = 1024L;
    static final Long USER_ID_INVALID = 42L;
    static final String USER_ID_MALFORMED = "MALFORMED_ID";
    static final String STORAGE_EXCEPTION_MESSAGE = "Connection Error";
    static final String NOT_FOUND_EXCEPTION_MESSAGE = "NOT_FOUND";
    static final PageRequest PAGE_REQUEST = PageRequest.of(0, 3, Sort.Direction.ASC, "id");
    static final CreateUserRequest CREATE_USER_REQUEST = CreateUserRequest.builder()
            .email("Valid.email@gmail.com")
            .firstName("Firstname")
            .lastName("Lastname")
            .birthDate("1970-01-01")
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();
    static final CreateUserRequest CREATE_USER_REQUEST_TOO_YOUNG = CreateUserRequest.builder()
            .email("Young.email@gmail.com")
            .firstName("YFirstname")
            .lastName("YLastname")
            .birthDate("2022-01-01")
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    static final ReplaceUserRequest REPLACE_USER_REQUEST = ReplaceUserRequest.builder()
            .email("Valid.email@gmail.com")
            .firstName("Firstname")
            .lastName("Lastname")
            .birthDate("1970-01-01")
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    static final ReplaceUserRequest REPLACE_USER_REQUEST_INVALID = ReplaceUserRequest.builder()
            .build();

    static final ModifyUserRequest MODIFY_USER_REQUEST_INVALID = ModifyUserRequest.builder()
            .birthDate("Invalid birthdate")
            .build();

    static final ModifyUserRequest MODIFY_USER_REQUEST = ModifyUserRequest.builder()
            .build();

    static final RetrieveUsersResponse FIRST_RETRIEVE_USER_RESPONSE = RetrieveUsersResponse.builder()
            .email("USER1.email@gmail.com")
            .firstName("User1Firstname")
            .lastName("User1Lastname")
            .birthDate(LocalDate.parse("1993-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    static final RetrieveUsersResponse SECOND_RETRIEVE_USER_RESPONSE = RetrieveUsersResponse.builder()
            .email("Valid.email@gmail.com")
            .firstName("Firstname")
            .lastName("Lastname")
            .birthDate(LocalDate.parse("1995-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    static final List<RetrieveUsersResponse> RETRIEVE_USERS_RESPONSE = List.of(
            FIRST_RETRIEVE_USER_RESPONSE,
            SECOND_RETRIEVE_USER_RESPONSE
    );

    static final PageResponse<RetrieveUsersResponse> GET_USERS_RESPONSE_PAGEABLE = new PageResponse<>(
            RETRIEVE_USERS_RESPONSE,
            PAGE_REQUEST
    );

    static final PageResponse<RetrieveUsersResponse> GET_USERS_EMPTY_RESPONSE_PAGEABLE = new PageResponse<>(List.of(), PAGE_REQUEST);
    static final LocalDate MIN_DATE_VALID = LocalDate.parse("1992-01-01");
    static final LocalDate MAX_DATE_VALID = LocalDate.parse("1996-03-06");
    static final LocalDate MIN_DATE_INVALID = LocalDate.parse("1996-01-01");
    static final LocalDate MAX_DATE_INVALID = LocalDate.parse("1992-03-06");

    static final String CREATE_USER_REQUEST_INVALID = "{\"test\":\"test\"}";
    static final String CREATE_USER_RESPONSE_BAD_REQUEST_MESSAGE = "birthDate: must be a valid local date earlier than current day, email: must not be blank, firstName: must not be blank, lastName: must not be blank";
    static final String CREATE_USER_RESPONSE_BAD_REQUEST_TOO_YOUNG_MESSAGE = "User must be older than ";
    static final String REPLACE_USER_RESPONSE_BAD_REQUEST_MESSAGE = "birthDate: must be a valid local date earlier than current day, email: must not be blank, firstName: must not be blank, lastName: must not be blank";
    static final String MODIFY_USER_RESPONSE_BAD_REQUEST_MESSAGE = "birthDate: must be a valid local date earlier than current day or null";
    static final String RETRIEVE_USERS_RESPONSE_BAD_REQUEST_MESSAGE = "";
    static final String DELETE_USER_RESPONSE_BAD_REQUEST_MESSAGE = "id: provided wrong type, expected type is Long";
    private static final String API_V1 = "/api/v1";
    static final String USERS_URL_VALID = API_V1 + "/users";
    static final String USERS_URL_VALID_REQUEST_PARAMS = USERS_URL_VALID + "?min=1992-01-01&max=1996-03-06";
    static final String USERS_URL_INVALID_REQUEST_PARAMS = USERS_URL_VALID + "?min=1996-01-01&max=1992-03-06";
    static final String USER_URL_VALID = USERS_URL_VALID + "/{id}";
    private static final String URL_PATH_SEPARATOR = "/";
    static final String EXPECTED_CREATED_URL = USERS_URL_VALID + URL_PATH_SEPARATOR + USER_ID_VALID;

}
