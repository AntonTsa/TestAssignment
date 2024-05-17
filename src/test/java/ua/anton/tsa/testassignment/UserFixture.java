package ua.anton.tsa.testassignment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ua.anton.tsa.testassignment.model.User;
import ua.anton.tsa.testassignment.wire.request.CreateUserRequest;
import ua.anton.tsa.testassignment.wire.request.ModifyUserRequest;
import ua.anton.tsa.testassignment.wire.request.ReplaceUserRequest;
import ua.anton.tsa.testassignment.wire.response.PageResponse;
import ua.anton.tsa.testassignment.wire.response.RetrieveUsersResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class UserFixture {

    // PAGE REQUEST
    public static final PageRequest PAGE_REQUEST = PageRequest.of(0, 3, Sort.Direction.ASC, "id");

    // USERS
    public static final User USER_VALID = User.builder()
            .id(1L)
            .email("Valid.email@gmail.com")
            .firstName("Firstname")
            .lastName("Lastname")
            .birthDate(LocalDate.parse("1970-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    public static final User USER_INVALID_AGE = User.builder()
            .id(42L)
            .email("Valid.email@gmail.com")
            .firstName("Firstname")
            .lastName("Lastname")
            .birthDate(LocalDate.parse("2024-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    public static final User FIRST_USER = User.builder()
            .id(2L)
            .email("USER1.email@gmail.com")
            .firstName("User1Firstname")
            .lastName("User1Lastname")
            .birthDate(LocalDate.parse("1993-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    public static final User SECOND_USER = User.builder()
            .id(3L)
            .email("Valid.email@gmail.com")
            .firstName("Firstname")
            .lastName("Lastname")
            .birthDate(LocalDate.parse("1995-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    public static final List<User> USERS_LIST = List.of(
            FIRST_USER, SECOND_USER
    );

    public static final Page<User> USERS_PAGE = new PageResponseTest<>(
            USERS_LIST, PAGE_REQUEST
    );

    public static final PageResponse<User> USERS_PAGE_RESPONSE = new PageResponse<>(
            USERS_PAGE
    );

    // USERS MAPPED
    public static final User USER_MAPPED_VALID = User.builder()
            .id(1L)
            .email("Valid.email@gmail.com")
            .firstName("Firstname")
            .lastName("Lastname")
            .birthDate(LocalDate.parse("1970-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    public static final User USER_MAPPED_INVALID_AGE = User.builder()
            .email("Valid.email@gmail.com")
            .firstName("Firstname")
            .lastName("Lastname")
            .birthDate(LocalDate.parse("2024-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    // INVALID REQUEST
    public static final String REQUEST_INVALID = "{\"test\":\"test\"}";

    // CREATE USER REQUESTS
    public static final CreateUserRequest CREATE_USER_REQUEST_VALID = CreateUserRequest.builder()
            .email("Valid.email@gmail.com")
            .firstName("Firstname")
            .lastName("Lastname")
            .birthDate(LocalDate.parse("1970-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    public static final CreateUserRequest CREATE_USER_REQUEST_INVALID = CreateUserRequest.builder()
            .address("")
            .phoneNumber("")
            .build();

    public static final CreateUserRequest CREATE_USER_REQUEST_INVALID_AGE = CreateUserRequest.builder()
            .email("Young.email@gmail.com")
            .firstName("YFirstname")
            .lastName("YLastname")
            .birthDate(LocalDate.parse("2024-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    // REPLACE USER REQUESTS
    public static final ReplaceUserRequest REPLACE_USER_REQUEST_VALID = ReplaceUserRequest.builder()
            .email("Valid.email@gmail.com")
            .firstName("Firstname")
            .lastName("Lastname")
            .birthDate(LocalDate.parse("1970-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    public static final ReplaceUserRequest REPLACE_USER_REQUEST_INVALID = ReplaceUserRequest.builder()
            .address("")
            .phoneNumber("")
            .email("Invalid Email")
            .build();

    public static final ReplaceUserRequest REPLACE_USER_REQUEST_INVALID_AGE = ReplaceUserRequest.builder()
            .email("Valid.email@gmail.com")
            .firstName("Firstname")
            .lastName("Lastname")
            .birthDate(LocalDate.parse("2024-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    // MODIFY USER REQUESTS
    public static final ModifyUserRequest MODIFY_USER_REQUEST_VALID = new ModifyUserRequest(
            Map.of("email", "Valid.Email@gmail.com")
    );

    public static final ModifyUserRequest MODIFY_USER_REQUEST_INVALID = new ModifyUserRequest(
            Map.of("birthDate", "2025-02-04", "email", "Invalid Email")
    );

    public static final ModifyUserRequest MODIFY_USER_REQUEST_INVALID_AGE = new ModifyUserRequest(
            Map.of(
                    "birthDate", "2022-02-04",
                    "firstName", "",
                    "lastName", "",
                    "email", "Invalid Email",
                    "address", "",
                    "phoneNumber", ""
            )
    );

    // RETRIEVE USER REQUESTS
    public static final RetrieveUsersResponse FIRST_RETRIEVE_USER_RESPONSE = RetrieveUsersResponse.builder()
            .id(2L)
            .email("USER1.email@gmail.com")
            .firstName("User1Firstname")
            .lastName("User1Lastname")
            .birthDate(LocalDate.parse("1993-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    public static final RetrieveUsersResponse SECOND_RETRIEVE_USER_RESPONSE = RetrieveUsersResponse.builder()
            .id(3L)
            .email("Valid.email@gmail.com")
            .firstName("Firstname")
            .lastName("Lastname")
            .birthDate(LocalDate.parse("1995-01-01"))
            .phoneNumber("+380931254556")
            .address("Valid Address")
            .build();

    public static final List<RetrieveUsersResponse> RETRIEVE_USERS_RESPONSE = List.of(
            FIRST_RETRIEVE_USER_RESPONSE,
            SECOND_RETRIEVE_USER_RESPONSE
    );

    public static final PageResponseTest<RetrieveUsersResponse> RETRIEVE_USERS_RESPONSE_PAGEABLE = new PageResponseTest<>(
            RETRIEVE_USERS_RESPONSE,
            PAGE_REQUEST
    );

    public static final PageResponseTest<RetrieveUsersResponse> RETRIEVE_USERS_EMPTY_RESPONSE_PAGEABLE = new PageResponseTest<>(List.of(), PAGE_REQUEST);

    public static final LocalDate FROM_VALID = LocalDate.parse("1992-01-01");
    public static final LocalDate TO_VALID = LocalDate.parse("1996-03-06");
    public static final LocalDate FROM_INVALID = LocalDate.parse("1996-01-01");
    public static final LocalDate TO_INVALID = LocalDate.parse("1992-03-06");

    // USER IDS

    public static final Long USER_ID_VALID = 1L;
    public static final Long USER_ID_INVALID = 42L;
    public static final Long USER_ID_NEGATIVE = -42L;
    public static final String USER_ID_MALFORMED = "MALFORMED_ID";

    // EXCEPTIONS' MESSAGES

    public static final String STORAGE_EXCEPTION_MESSAGE = "Connection Error";
    public static final String NOT_FOUND_EXCEPTION_MESSAGE = "NOT_FOUND";
    public static final String INVALID_REQUEST_BODY_MESSAGE = "Invalid request body received";
    public static final String NEGATIVE_ID_EXCEPTION_MESSAGE = "remove.id: must be greater than 0";
    public static final String CREATE_USER_RESPONSE_BAD_REQUEST_MESSAGE = "address: must contain at least one non-whitespace character, " +
            "address: size must be between 1 and 255, birthDate: must be a valid local date earlier than current day, " +
            "email: must be a not blank well-formed email address, firstName: must not be blank, lastName: must not be blank, " +
            "phoneNumber: must contain at least one non-whitespace character, phoneNumber: size must be between 1 and 15";
    public static final String MIN_AGE_EXCEPTION_MESSAGE = "User must be older than ";
    public static final String REPLACE_USER_RESPONSE_BAD_REQUEST_MESSAGE = "address: must contain at least one non-whitespace character, " +
            "address: size must be between 1 and 255, birthDate: must be a valid local date earlier than current day, " +
            "email: must be a not blank well-formed email address, firstName: must not be blank, lastName: must not be blank, " +
            "phoneNumber: must contain at least one non-whitespace character, phoneNumber: size must be between 1 and 15";
    public static final String MODIFY_USER_RESPONSE_BAD_REQUEST_MESSAGE_EMAIL = "email: must be a not blank well-formed email address";
    public static final String MODIFY_USER_RESPONSE_BAD_REQUEST_MESSAGE_BIRTHDATE = "birthDate: must be a valid local date earlier than current day";
    public static final String RETRIEVE_USERS_RESPONSE_BAD_REQUEST_MESSAGE = "\"User must be older than ";
    public static final String INVALID_PERIOD_EXCEPTION_MESSAGE = "from date must be less than to date";
    public static final String DELETE_USER_RESPONSE_BAD_REQUEST_MESSAGE = "id: provided wrong type, expected type is Long";

    // URLS

    private static final String API_V1 = "/api/v1";
    public static final String USERS_URL_VALID = API_V1 + "/users";
    public static final String USERS_URL_VALID_REQUEST_PARAMS = USERS_URL_VALID + "?from=1992-01-01&to=1996-03-06";
    public static final String USERS_URL_INVALID_REQUEST_PARAMS = USERS_URL_VALID + "?from=1996-01-01&to=1992-03-06";
    public static final String USER_URL_VALID = USERS_URL_VALID + "/{id}";
    private static final String URL_PATH_SEPARATOR = "/";
    public static final String EXPECTED_CREATED_URL = USERS_URL_VALID + URL_PATH_SEPARATOR + USER_ID_VALID;

}
