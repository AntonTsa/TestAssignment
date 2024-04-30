package ua.anton.tsa.testassigment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import ua.anton.tsa.testassigment.service.UsersService;
import ua.anton.tsa.testassigment.wire.response.RestContractExceptionResponse;
import ua.anton.tsa.testassigment.wire.response.RetrieveUsersResponse;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.anton.tsa.testassigment.controller.UserControllerFixture.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UsersController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@MockBean(
        classes = {
                UsersService.class,
        },
        answer = Answers.RETURNS_SMART_NULLS
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UsersService usersService;

    @Test
    @DisplayName("""
            GIVEN valid createUserRequest object
            WHEN performing POST request
            THEN return response with code 201, valid location and empty body
            """)
    void createUserValid() throws Exception {
        // GIVEN
        given(usersService.create(CREATE_USER_REQUEST)).willReturn(USER_ID_VALID);

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(post(USERS_URL_VALID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(CREATE_USER_REQUEST))
                )
                // THEN
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getHeader(HttpHeaders.LOCATION))
                .isNotEmpty()
                .isEqualTo(EXPECTED_CREATED_URL);
        assertThat(actualResponse.getContentAsString()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN invalid createUserRequest object
            WHEN performing POST request
            THEN return response with code 400 and message "Bad Request"
            """)
    void createUserBadRequest() throws Exception {
        // GIVEN

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(post(USERS_URL_VALID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CREATE_USER_REQUEST_INVALID)
                        )
                        // THEN
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(CREATE_USER_RESPONSE_BAD_REQUEST_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN invalid createUserRequest object
            WHEN performing POST request
            THEN return response with code 400 and message "Bad Request"
            """)
    void createUserBadRequestTooYoung() throws Exception {
        // GIVEN
        doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, CREATE_USER_RESPONSE_BAD_REQUEST_TOO_YOUNG_MESSAGE))
                .when(usersService).create(CREATE_USER_REQUEST_TOO_YOUNG);

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(post(USERS_URL_VALID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(CREATE_USER_REQUEST_TOO_YOUNG))
                        )
                        // THEN
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(CREATE_USER_RESPONSE_BAD_REQUEST_TOO_YOUNG_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid user id and valid user object
            WHEN performing PUT request
            THEN return response with code 200 and no user entry
            """)
    void replaceUserByValidId() throws Exception {
        // GIVEN
        doNothing().when(usersService).replace(USER_ID_VALID, REPLACE_USER_REQUEST);

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(put(USER_URL_VALID, USER_ID_VALID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(REPLACE_USER_REQUEST))
                )
                // THEN
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getContentAsString()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN invalid user id and valid user object
            WHEN performing PUT request
            THEN return response with code 404 and no user entry
            """)
    void replaceUserByInvalidId() throws Exception {
        // GIVEN
        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND))
                .when(usersService).replace(USER_ID_INVALID, REPLACE_USER_REQUEST);

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(put(USER_URL_VALID, USER_ID_INVALID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(REPLACE_USER_REQUEST))
                        )
                        // THEN
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid user id and invalid user object
            WHEN performing PUT request
            THEN return response with code 400 and message "Bad Request"
            """)
    void replaceUserByValidIdAndInvalidReplaceUserRequest() throws Exception {
        // GIVEN
        doNothing().when(usersService).replace(USER_ID_VALID, REPLACE_USER_REQUEST_INVALID);

        // WHEN
        RestContractExceptionResponse actualResponse =
                fromJson(mockMvc
                                .perform(put(USER_URL_VALID, USER_ID_VALID)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toJson(REPLACE_USER_REQUEST_INVALID))
                                )
                                .andExpect(status().isBadRequest())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        RestContractExceptionResponse.class);
        //AND THEN
        assertThat(actualResponse.message()).isEqualTo(REPLACE_USER_RESPONSE_BAD_REQUEST_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid user id and valid user object
            WHEN performing PATCH request
            THEN return response with code 204
            """)
    void modifyUserByValidId() throws Exception {
        // GIVEN
        doNothing().when(usersService).modify(USER_ID_VALID, MODIFY_USER_REQUEST);

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(patch(USER_URL_VALID, USER_ID_VALID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(MODIFY_USER_REQUEST))
                )
                // THEN
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getContentAsString()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN invalid user id and valid user object
            WHEN performing PATCH request
            THEN return response with code 404 and not found entry
            """)
    void modifyUserByInvalidId() throws Exception {
        // GIVEN
        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND))
                .when(usersService).modify(USER_ID_INVALID, MODIFY_USER_REQUEST);

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(patch(USER_URL_VALID, USER_ID_INVALID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(MODIFY_USER_REQUEST))
                        )
                        // THEN
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid user id and invalid user object
            WHEN performing PATCH request
            THEN return response with code 400 and message "Bad Request"
            """)
    void modifyUserByValidIdAndInvalidModifyUserRequest() throws Exception {
        // GIVEN
        doNothing().when(usersService).modify(USER_ID_VALID, MODIFY_USER_REQUEST_INVALID);

        // WHEN
        RestContractExceptionResponse actualResponse =
                fromJson(mockMvc
                                .perform(patch(USER_URL_VALID, USER_ID_VALID)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toJson(MODIFY_USER_REQUEST_INVALID))
                                )
                                .andExpect(status().isBadRequest())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        RestContractExceptionResponse.class);
        //AND THEN
        assertThat(actualResponse.message()).isEqualTo(MODIFY_USER_RESPONSE_BAD_REQUEST_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN default page number and page size, valid date range
            WHEN performing GET request
            THEN return response with code 200 and list of users
            """)
    void retrieveUsersValid() throws Exception {
        // GIVEN
        given(usersService.retrieve(PAGE_REQUEST, MIN_DATE_VALID, MAX_DATE_VALID))
                .willReturn(GET_USERS_RESPONSE_PAGEABLE);

        // WHEN
        PageResponse<RetrieveUsersResponse> actualResponse = fromJson(mockMvc
                        .perform(get(USERS_URL_VALID_REQUEST_PARAMS)
                                .accept(MediaType.APPLICATION_JSON))
                        // THEN
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                new TypeReference<>() {
                }
        );

        // AND THEN
        assertThat(actualResponse.getTotalElements()).isEqualTo(2L);
        assertThat(actualResponse.getTotalPages()).isEqualTo(1);
        assertThat(actualResponse.getContent()).isEqualTo(RETRIEVE_USERS_RESPONSE);
    }

    @Test
    @DisplayName("""
            GIVEN default page number and page size
            WHEN performing GET request
            THEN return response with code 200 and empty list of users
            """)
    void retrieveUsersEmptyValid() throws Exception {
        // GIVEN
        given(usersService.retrieve(PAGE_REQUEST, MIN_DATE_VALID, MAX_DATE_VALID)).willReturn(GET_USERS_EMPTY_RESPONSE_PAGEABLE);

        // WHEN
        PageResponse<RetrieveUsersResponse> actualResponse =
                fromJson(mockMvc
                        .perform(get(USERS_URL_VALID_REQUEST_PARAMS)
                                .accept(MediaType.APPLICATION_JSON))
                        // THEN
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                new TypeReference<>() {
                }
        );

        // AND THEN
        assertThat(actualResponse.getTotalElements()).isZero();
        assertThat(actualResponse.getTotalPages()).isZero();
        assertThat(actualResponse.getContent()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN default page number and page size, invalid date range
            WHEN performing GET request
            THEN return response with code 400 and message "Bad Request"
            """)
    void retrieveUsersBirthDateRangeRequestInvalid() throws Exception {
        // GIVEN
        doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, RETRIEVE_USERS_RESPONSE_BAD_REQUEST_MESSAGE))
                .when(usersService).retrieve(PAGE_REQUEST, MIN_DATE_INVALID, MAX_DATE_INVALID);

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(get(USERS_URL_INVALID_REQUEST_PARAMS)
                                .accept(MediaType.APPLICATION_JSON))
                        // THEN
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class
        );
        //AND THEN
        assertThat(actualResponse.message()).isEqualTo(RETRIEVE_USERS_RESPONSE_BAD_REQUEST_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN default page number and page size, valid date range
            WHEN performing GET request and Storage returns exception
            THEN return response with code 500 and error message
            """)
    void retrieveUsersInternalServiceError() throws Exception {
        // GIVEN
        given(usersService.retrieve(PAGE_REQUEST, MIN_DATE_VALID, MAX_DATE_VALID))
                .willThrow(new JDBCConnectionException(STORAGE_EXCEPTION_MESSAGE, null));

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(get(USERS_URL_VALID_REQUEST_PARAMS)
                                .accept(MediaType.APPLICATION_JSON))
                        // THEN
                        .andExpect(status().isInternalServerError())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(STORAGE_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid user id
            WHEN performing DELETE request
            THEN return response with code 204 and remove entry
            """)
    void removeUserByValidId() throws Exception {
        // GIVEN
        doNothing().when(usersService).remove(USER_ID_VALID);

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(delete(USER_URL_VALID, USER_ID_VALID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                // THEN
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getContentAsString()).isBlank();
    }

    @Test
    @DisplayName("""
            GIVEN malformed user id
            WHEN performing DELETE request
            THEN return response with code 400
            """)
    void removeUserByMalformedId() throws Exception {
        // GIVEN

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(delete(USER_URL_VALID, USER_ID_MALFORMED)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        // THEN
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(DELETE_USER_RESPONSE_BAD_REQUEST_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN invalid user id
            WHEN performing DELETE request
            THEN return response with code 404
            """)
    void removeUserByInvalidId() throws Exception {
        // GIVEN
        doThrow(new EmptyResultDataAccessException(0))
                .when(usersService).remove(USER_ID_INVALID);

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(delete(USER_URL_VALID, USER_ID_INVALID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        // THEN
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @SneakyThrows(JsonProcessingException.class)
    private String toJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows(JsonProcessingException.class)
    public <T> T fromJson(String string, Class<T> type) {
        return Objects.nonNull(string) ? objectMapper.readValue(string, type) : null;
    }

    @SneakyThrows(JsonProcessingException.class)
    public <T> T fromJson(String string, TypeReference<T> type) {
        return Objects.nonNull(string) ? objectMapper.readValue(string, type) : null;
    }

}