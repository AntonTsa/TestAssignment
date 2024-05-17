package ua.anton.tsa.testassignment.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.JDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.PropertyResolver;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.HttpClientErrorException;
import ua.anton.tsa.testassignment.configuration.UserProperties;
import ua.anton.tsa.testassignment.exceptions.InvalidPeriodException;
import ua.anton.tsa.testassignment.exceptions.MinAgeException;
import ua.anton.tsa.testassignment.mapper.UserMapper;
import ua.anton.tsa.testassignment.mapper.UserMapperImpl;
import ua.anton.tsa.testassignment.repo.UsersRepository;
import ua.anton.tsa.testassignment.wire.response.RetrieveUsersResponse;

import java.sql.SQLException;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static ua.anton.tsa.testassignment.UserFixture.*;

/**
 * Class with unit tests for {@link UsersService}
 */
@SpringJUnitConfig(
        classes = {
                UsersService.class,
                UserMapperImpl.class
        },
        initializers = ConfigDataApplicationContextInitializer.class
)
@EnableConfigurationProperties(UserProperties.class)
@MockBean(
        classes = {
                UsersRepository.class,
                UserMapperImpl.class
        },
        answer = Answers.RETURNS_SMART_NULLS
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final UsersService usersService;

    @Autowired
    private PropertyResolver propertyResolver;

    @SneakyThrows
    @Test
    @DisplayName("""
            GIVEN valid createUserRequest object
            WHEN perform create
            THEN return unique id of created entry in storage
            """)
    void createUserValid() {
        // GIVEN
        given(userMapper.toUser(CREATE_USER_REQUEST_VALID)).willReturn(USER_MAPPED_VALID);
        given(usersRepository.save(USER_MAPPED_VALID)).willReturn(USER_VALID);

        // WHEN
        Long actualResponse = usersService.create(CREATE_USER_REQUEST_VALID);

        // THEN
        assertThat(actualResponse).isEqualTo(USER_ID_VALID);
    }

    @Test
    @DisplayName("""
            GIVEN createUserRequest object with age < "user.age.min" property
            WHEN perform create
            THEN throw MinAgeException with message
            """)
    void createUserInvalidAge() {
        // GIVEN
        given(userMapper.toUser(CREATE_USER_REQUEST_INVALID_AGE)).willReturn(USER_MAPPED_INVALID_AGE);
        given(usersRepository.save(USER_MAPPED_INVALID_AGE)).willReturn(USER_INVALID_AGE);

        // WHEN
        Exception actualException = assertThrows(
                MinAgeException.class,
                () -> usersService.create(CREATE_USER_REQUEST_INVALID_AGE));

        // THEN
        assertThat(actualException.getMessage()).isEqualTo(
                MIN_AGE_EXCEPTION_MESSAGE + propertyResolver.getProperty("user.age.min"));
    }

    @Test
    @DisplayName("""
            GIVEN valid createUserRequest object
            WHEN create
            THEN storage return exception
            """)
    void createJDBCException() {
        // GIVEN
        given(userMapper.toUser(CREATE_USER_REQUEST_VALID)).willReturn(USER_MAPPED_VALID);
        given(usersRepository.save(USER_MAPPED_VALID))
                .willThrow(new JDBCConnectionException(STORAGE_EXCEPTION_MESSAGE, new SQLException()));

        // WHEN
        Exception actualException = assertThrows(
                JDBCException.class,
                () -> usersService.create(CREATE_USER_REQUEST_VALID));

        // THEN
        assertThat(actualException.getMessage()).isEqualTo(STORAGE_EXCEPTION_MESSAGE);
    }


    @Test
    @SneakyThrows
    @DisplayName("""
            GIVEN valid id and valid replaceUserRequest
            WHEN perform replace
            THEN verify that usersRepository calls method save
            """)
    void replaceUserValid() {
        // GIVEN
        given(userMapper.toUser(USER_ID_VALID, REPLACE_USER_REQUEST_VALID)).willReturn(USER_VALID);
        given(usersRepository.existsById(USER_ID_VALID)).willReturn(TRUE);
        given(usersRepository.save(USER_VALID)).willReturn(USER_VALID);

        // WHEN
        usersService.replace(USER_ID_VALID, REPLACE_USER_REQUEST_VALID);

        // THEN
        verify(usersRepository).save(USER_VALID);
    }

    @Test
    @DisplayName("""
            GIVEN invalid id and valid replaceUserRequest object
            WHEN perform replace
            THEN return Not Found exception
            """)
    void replaceUserInvalidId() {
        // GIVEN
        given(usersRepository.existsById(USER_ID_INVALID)).willReturn(FALSE);

        // WHEN
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.class,
                () -> usersService.replace(USER_ID_INVALID, REPLACE_USER_REQUEST_VALID));

        // THEN
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getStatusText()).isEqualTo(NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid id replaceUserRequest object with age < "user.age.min" property
            WHEN perform replace
            THEN return MinAgeException
            """)
    void replaceUserInvalidAge() {
        // GIVEN
        given(usersRepository.existsById(USER_ID_VALID)).willReturn(TRUE);

        // WHEN
        Exception exception = assertThrows(
                MinAgeException.class,
                () -> usersService.replace(USER_ID_VALID, REPLACE_USER_REQUEST_INVALID_AGE));

        // THEN
        assertThat(exception.getMessage()).isEqualTo(
                MIN_AGE_EXCEPTION_MESSAGE + propertyResolver.getProperty("user.age.min"));
    }

    @Test
    @DisplayName("""
            GIVEN valid id and valid replaceUserRequest object
            WHEN replace
            THEN storage return exception
            """)
    void replaceUserJDBCException() {
        // GIVEN
        given(usersRepository.existsById(USER_ID_VALID))
                .willThrow(new JDBCConnectionException(STORAGE_EXCEPTION_MESSAGE, new SQLException()));

        // WHEN
        Exception actualException = assertThrows(
                JDBCException.class,
                () -> usersService.replace(USER_ID_VALID, REPLACE_USER_REQUEST_VALID));

        // THEN
        assertThat(actualException.getMessage()).isEqualTo(STORAGE_EXCEPTION_MESSAGE);
    }

    @Test
    @SneakyThrows
    @DisplayName("""
            GIVEN valid id and valid modifyUserRequest object
            WHEN modify
            THEN verify that userRepository calls method save
            """)
    void modifyUserValid() {
        // GIVEN
        given(usersRepository.findById(USER_ID_VALID)).willReturn(Optional.of(USER_VALID));
        given(userMapper.toUser(USER_VALID, MODIFY_USER_REQUEST_VALID)).willReturn(USER_VALID);

        // WHEN
        usersService.modify(USER_ID_VALID, MODIFY_USER_REQUEST_VALID);
        // THEN
        verify(usersRepository).save(USER_VALID);
    }

    @Test
    @DisplayName("""
            GIVEN invalid id and valid modifyUserRequest object
            WHEN modify
            THEN return Not Found exception
            """)
    void modifyUserInvalidId() {
        // GIVEN
        given(usersRepository.findById(USER_ID_VALID)).willReturn(Optional.empty());

        // WHEN
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.class,
                () -> usersService.modify(USER_ID_INVALID, MODIFY_USER_REQUEST_VALID));

        // THEN
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getStatusText()).isEqualTo(NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN invalid modifyUserRequest object and valid id
            WHEN modify
            THEN return MinAge exception with message
            """)
    void modifyUserInvalidModifyInvalidAge() {
        // GIVEN

        // WHEN
        Exception exception = assertThrows(
                MinAgeException.class,
                () -> usersService.modify(USER_ID_VALID, MODIFY_USER_REQUEST_INVALID_AGE));

        // THEN
        assertThat(exception.getMessage()).isEqualTo(
                MIN_AGE_EXCEPTION_MESSAGE + propertyResolver.getProperty("user.age.min"));
    }

    @Test
    @DisplayName("""
            GIVEN valid modifyUserRequest object and valid id
            WHEN modify
            THEN throw JDBCException with message
            """)
    void modifyUserJDBCException() {
        // GIVEN
        given(usersRepository.findById(USER_ID_VALID))
                .willThrow(new JDBCConnectionException(STORAGE_EXCEPTION_MESSAGE, new SQLException()));

        // WHEN
        Exception actualException = assertThrows(
                JDBCException.class,
                () -> usersService.modify(USER_ID_VALID, MODIFY_USER_REQUEST_VALID));

        // THEN
        assertThat(actualException.getMessage()).isEqualTo(STORAGE_EXCEPTION_MESSAGE);
    }

    @Test
    @SneakyThrows
    @DisplayName("""
            GIVEN valid paging, valid from and valid to params
            WHEN retrieve
            THEN return a page of objects
            """)
    void retrieveUsersValid() {
        // GIVEN
        given(usersRepository.findAllByBirthDateBetween(any(), any(), any())).willReturn(USERS_PAGE);
        given(userMapper.toRetrieveUsersResponse(USERS_PAGE_RESPONSE.getContent().getFirst()))
                .willReturn(FIRST_RETRIEVE_USER_RESPONSE);
        given(userMapper.toRetrieveUsersResponse(USERS_PAGE_RESPONSE.getContent().getLast()))
                .willReturn(SECOND_RETRIEVE_USER_RESPONSE);
        // WHEN
        Page<RetrieveUsersResponse> actualResponse = usersService.retrieve(PAGE_REQUEST, FROM_VALID, FROM_INVALID);

        // THEN
        assertThat(actualResponse.getTotalElements()).isEqualTo(RETRIEVE_USERS_RESPONSE_PAGEABLE.getTotalElements());
        assertThat(actualResponse.getTotalPages()).isEqualTo(RETRIEVE_USERS_RESPONSE_PAGEABLE.getTotalPages());
        assertThat(actualResponse.getContent()).isEqualTo(RETRIEVE_USERS_RESPONSE_PAGEABLE.getContent());
    }

    @Test
    @DisplayName("""
            GIVEN valid paging, invalid from and invalid to
            WHEN retrieve
            THEN throw InvalidPeriodException with message
            """)
    void retrieveUsersInvalidParams() {
        // GIVEN


        // WHEN
        InvalidPeriodException actualException = assertThrows(
                InvalidPeriodException.class,
                () -> usersService.retrieve(PAGE_REQUEST, FROM_INVALID, TO_INVALID));

        // THEN
        assertThat(actualException.getMessage()).isEqualTo(INVALID_PERIOD_EXCEPTION_MESSAGE);

    }

    @Test
    @DisplayName("""
            GIVEN valid paging, from and to
            WHEN retrieve
            THEN throw JDBCException with message
            """)
    void retrieveUsersJDBCException() {
        // GIVEN
        given(usersRepository.findAllByBirthDateBetween(any(),any(),any()))
                .willThrow(new JDBCConnectionException(STORAGE_EXCEPTION_MESSAGE, new SQLException()));

        // WHEN
        Exception actualException = assertThrows(
                JDBCException.class,
                () -> usersService.retrieve(PAGE_REQUEST, FROM_VALID,TO_VALID));

        // THEN
        assertThat(actualException.getMessage()).isEqualTo(STORAGE_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid id
            WHEN remove
            THEN verify that repository calls method deleteById
            """)
    void removeUserValid() {
        // GIVEN
        given(usersRepository.findById(anyLong())).willReturn(Optional.of(USER_VALID));
        doNothing().when(usersRepository).delete(any());

        // WHEN
        usersService.remove(USER_ID_VALID);

        // THEN
        verify(usersRepository).findById(USER_ID_VALID);
        verify(usersRepository).delete(USER_VALID);
    }

    @Test
    @DisplayName("""
            GIVEN valid id
            WHEN remove
            THEN throw JDBCException with message
            """)
    void removeUserJDBCException() {
        // GIVEN
        doThrow(new JDBCConnectionException(STORAGE_EXCEPTION_MESSAGE, new SQLException()))
                .when(usersRepository).findById(USER_ID_VALID);

        // WHEN
        Exception actualException = assertThrows(
                JDBCException.class,
                () -> usersService.remove(USER_ID_VALID));

        // THEN
        assertThat(actualException.getMessage()).isEqualTo(STORAGE_EXCEPTION_MESSAGE);
    }



}
