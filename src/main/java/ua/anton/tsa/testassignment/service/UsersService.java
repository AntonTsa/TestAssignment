package ua.anton.tsa.testassignment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ua.anton.tsa.testassignment.configuration.UserProperties;
import ua.anton.tsa.testassignment.exceptions.InvalidPeriodException;
import ua.anton.tsa.testassignment.exceptions.MinAgeException;
import ua.anton.tsa.testassignment.mapper.UserMapper;
import ua.anton.tsa.testassignment.model.User;
import ua.anton.tsa.testassignment.repo.UsersRepository;
import ua.anton.tsa.testassignment.wire.request.CreateUserRequest;
import ua.anton.tsa.testassignment.wire.request.ModifyUserRequest;
import ua.anton.tsa.testassignment.wire.request.ReplaceUserRequest;
import ua.anton.tsa.testassignment.wire.response.RetrieveUsersResponse;

import java.time.LocalDate;
import java.time.Period;

/**
 * User Service to realize business logic while working with @{@link User} object
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final UserProperties userProperties;

    /**
     * Creates new User entry in storage.
     *
     * @param createUserRequest - {@link CreateUserRequest} create request object
     * @return {@link Long} unique id of the User in storage
     */
    public Long create(CreateUserRequest createUserRequest) throws MinAgeException {
        User user = userMapper.toUser(createUserRequest);
        if(Period.between(user.getBirthDate(), LocalDate.now()).getYears() >= userProperties.age().min()) {
            return usersRepository.save(user).getId();
        } else {
            throw new MinAgeException("User must be older than " + userProperties.age().min());
        }
    }

    /**
     * Replaces e.g. Updates User entry.
     *
     * @param id                 - {@link Long} unique id of the User in storage
     * @param replaceUserRequest - {@link ReplaceUserRequest} replace request object
     */
    public void replace(Long id, ReplaceUserRequest replaceUserRequest) throws MinAgeException {
        if (!usersRepository.existsById(id)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User with given id is not found");
        }
        if(Period.between(replaceUserRequest.birthDate(), LocalDate.now()).getYears() < userProperties.age().min()) {
            throw new MinAgeException("User must be older than " + userProperties.age().min());
        }
        usersRepository.save(userMapper.toUser(id, replaceUserRequest));
    }

    /**
     * Partially Updates User entry.
     *
     * @param id                - {@link Long} unique id of the Host in storage
     * @param modifyUserRequest - {@link ModifyUserRequest} modify request object
     */

    public void modify(Long id, ModifyUserRequest modifyUserRequest) throws MinAgeException {
        if(modifyUserRequest.containsKey("birthDate") &&
                Period.between(
                        LocalDate.parse(modifyUserRequest.get("birthDate")),
                        LocalDate.now()
                ).getYears() < userProperties.age().min()) {
            throw new MinAgeException("User must be older than " + userProperties.age().min());
        }
        usersRepository.save(userMapper.toUser(
                usersRepository.findById(id)
                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User with given id is not found")),
                modifyUserRequest));
    }

    /**
     *
     * @param pageable - {@link Pageable}
     * @param from     - {@link LocalDate} param of min date
     * @param to       - {@link LocalDate} param of max date
     * @return {@link Page} of {@link RetrieveUsersResponse} objects
     */
    public Page<RetrieveUsersResponse> retrieve(Pageable pageable, LocalDate from, LocalDate to)
            throws InvalidPeriodException {
        if(to.isBefore(from)) {
            throw new InvalidPeriodException("from date must be less than to date");
        } else {
            return usersRepository
                    .findAllByBirthDateBetween(from, to, pageable)
                    .map(userMapper::toRetrieveUsersResponse);
        }
    }

    /**
     * Remove one User by unique identifier
     *
     * @param id - {@link Long} unique entry identifier
     */
    public void remove(Long id) {

        usersRepository.delete(
                usersRepository.findById(id)
                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User with given id is not found"))
        );
    }
}
