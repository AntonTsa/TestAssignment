package ua.anton.tsa.testassigment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ua.anton.tsa.testassigment.configuration.UserProperties;
import ua.anton.tsa.testassigment.mapper.UserMapper;
import ua.anton.tsa.testassigment.model.User;
import ua.anton.tsa.testassigment.repo.UsersRepository;
import ua.anton.tsa.testassigment.wire.request.CreateUserRequest;
import ua.anton.tsa.testassigment.wire.request.ModifyUserRequest;
import ua.anton.tsa.testassigment.wire.request.ReplaceUserRequest;
import ua.anton.tsa.testassigment.wire.response.RetrieveUsersResponse;

import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository repository;
    private final UserMapper mapper;
    private final UserProperties properties;

    /**
     * Creates new User entry in storage.
     *
     * @param createUserRequest - {@link CreateUserRequest} create request object
     * @return {@link Long} unique id of the User in storage
     */
    public Long create(CreateUserRequest createUserRequest) {
        User user = mapper.toUser(createUserRequest);
        if(Period.between(LocalDate.now(), user.getBirthDate()).getYears() >= properties.age().min()) {
            return repository.save(user).getId();
        } else {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "User must be older than " + properties.age().min());
        }
    }

    /**
     * Replaces e.g. Updates User entry.
     *
     * @param id                 - {@link Long} unique id of the User in storage
     * @param replaceUserRequest - {@link ReplaceUserRequest} replace request object
     */
    public void replace(Long id, ReplaceUserRequest replaceUserRequest) {
        if (!repository.existsById(id)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        repository.save(mapper.toUser(id, replaceUserRequest));
    }

    /**
     * Partially Updates User entry.
     *
     * @param id                - {@link Long} unique id of the Host in storage
     * @param modifyUserRequest - {@link ModifyUserRequest} modify request object
     */
    public void modify(Long id, ModifyUserRequest modifyUserRequest) {
        repository.save(mapper.toUser(
                repository.findById(id)
                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND)),
                modifyUserRequest)
        );
    }

    /**
     *
     * @param pageable
     * @param min
     * @param max
     * @return
     */
    public Page<RetrieveUsersResponse> retrieve(Pageable pageable, LocalDate min, LocalDate max) {
        if(max.isBefore(min)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    "min date must be less than max date");
        } else {
            return repository
                    .findAllByBirthDateBetween(min, max, pageable)
                    .map(mapper::toRetrieveUsersResponse);
        }
    }

    /**
     * Remove one User by unique identifier
     *
     * @param id - {@link Long} unique entry identifier
     */
    public void remove(Long id) {
        repository.deleteById(id);
    }
}
