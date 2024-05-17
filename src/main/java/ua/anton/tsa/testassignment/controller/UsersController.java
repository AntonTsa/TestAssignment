package ua.anton.tsa.testassignment.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.anton.tsa.testassignment.exceptions.InvalidPeriodException;
import ua.anton.tsa.testassignment.exceptions.MinAgeException;
import ua.anton.tsa.testassignment.service.UsersService;
import ua.anton.tsa.testassignment.validation.BirthDate;
import ua.anton.tsa.testassignment.validation.Email;
import ua.anton.tsa.testassignment.validation.NotBlankNullable;
import ua.anton.tsa.testassignment.wire.request.CreateUserRequest;
import ua.anton.tsa.testassignment.wire.request.ModifyUserRequest;
import ua.anton.tsa.testassignment.wire.request.ReplaceUserRequest;
import ua.anton.tsa.testassignment.wire.response.PageResponse;
import ua.anton.tsa.testassignment.wire.response.RetrieveUsersResponse;

import java.net.URI;
import java.time.LocalDate;

import static ua.anton.tsa.testassignment.Constants.API_V1;
import static ua.anton.tsa.testassignment.Constants.URL_SEPARATOR;

/**
 * Entry point for Users endpoint APIs.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1)
public class UsersController {

    public static final int DEFAULT_PAGE_SIZE = 3;
    private static final String USERS_ENDPOINT = "/users";
    private static final String USER_ENDPOINT = USERS_ENDPOINT + "/{id}";
    private final UsersService usersService;

    /**
     * POST to create user
     *
     * @param createUserRequest  - {@link CreateUserRequest} with body
     * @param httpServletRequest - {@link HttpServletRequest} with full request data
     * @return {@link ResponseEntity} with trace and location headers
     */
    @PostMapping(path = USERS_ENDPOINT)
    public ResponseEntity<URI> create(
            @Valid @RequestBody CreateUserRequest createUserRequest,
            HttpServletRequest httpServletRequest
    ) throws MinAgeException {
        return ResponseEntity.created(
                URI.create(httpServletRequest.getRequestURI() + URL_SEPARATOR + usersService.create(createUserRequest))
        ).build();
    }

    /**
     * PUT to update all user fields
     *
     * @param id                 - {@link Long} unique identifier
     * @param replaceUserRequest - {@link ReplaceUserRequest} with body
     * @return {@link ResponseEntity} of {@link Void}
     */
    @PutMapping(path = USER_ENDPOINT)
    public ResponseEntity<Void> replace(
            @PathVariable Long id,
            @Valid @RequestBody ReplaceUserRequest replaceUserRequest
    ) throws MinAgeException {
        usersService.replace(id, replaceUserRequest);
        return ResponseEntity.noContent().build();
    }


    /**
     * PATCH to update one/some user fields
     *
     * @param id                - {@link Long} unique identifier
     * @param modifyUserRequest - {@link ModifyUserRequest} with body
     * @return {@link ResponseEntity} of {@link Void}
     */
    @PatchMapping( path = USER_ENDPOINT)
    public ResponseEntity<Void> modify(
            @PathVariable Long id,
            @Valid @RequestBody @BirthDate @Email
            @NotBlankNullable(keys = {"firstName", "lastName", "address", "phoneNumber"})
            ModifyUserRequest modifyUserRequest
    ) throws MinAgeException {
        log.info(String.valueOf(modifyUserRequest.entrySet().size()));
        usersService.modify(id, modifyUserRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET to retrieve users by birthdate range.
     *
     * @param from      - {@link LocalDate} with minimum searchable date
     * @param to        - {@link LocalDate} with maximum searchable date
     * @param pageable  - {@link Pageable} with page params
     * @return {@link ResponseEntity} with {@link Page} of {@link RetrieveUsersResponse} objects
     */
    @GetMapping(path = USERS_ENDPOINT)
    public ResponseEntity<PageResponse<RetrieveUsersResponse>> retrieve(
            @RequestParam(name = "from") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            @SortDefault.SortDefaults(
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            ) Pageable pageable
    ) throws InvalidPeriodException {
        return ResponseEntity.ok(new PageResponse<>(usersService.retrieve(pageable, from, to)));
    }

    /**
     * DELETE to delete user
     *
     * @param id - {@link Long} unique identifier
     * @return {@link ResponseEntity} without body
     */
    @DeleteMapping(path = USER_ENDPOINT)
    public ResponseEntity<Void> remove(@PathVariable @Positive Long id) {
        usersService.remove(id);
        return ResponseEntity.noContent().build();

    }

}
