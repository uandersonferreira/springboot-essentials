package br.com.uanderson.springboot.controller;

import br.com.uanderson.springboot.domain.DevDojoUserDetails;
import br.com.uanderson.springboot.requests.DevDojoUserPostRequest;
import br.com.uanderson.springboot.requests.DevDojoUserPutRequest;
import br.com.uanderson.springboot.service.DevDojoUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class DevDojoUserController {

    private final DevDojoUserService devDojoUserService;

    @PostMapping(path = "/admin/save")
    @Operation(summary = "Create User", description = "Endpoint to create a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad Request Exception, Invalid fields"),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<DevDojoUserDetails> createUser(@RequestBody DevDojoUserPostRequest devDojoUserPostRequest) {
        return new ResponseEntity<>(devDojoUserService.saveUser(devDojoUserPostRequest), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List Users", description = "Endpoint to list all registered users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users returned successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<List<DevDojoUserDetails>> getAllUsers() {
        return new ResponseEntity<>(devDojoUserService.listAllNoPageable(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get User by ID", description = "Endpoint to get a user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully."),
            @ApiResponse(responseCode = "400", description = "User not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<DevDojoUserDetails> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(devDojoUserService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    @PutMapping()
    @Operation(summary = "Update User", description = "Endpoint to update user data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User updated successfully."),
            @ApiResponse(responseCode = "400", description = "User not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<DevDojoUserDetails> updateUser(@RequestBody DevDojoUserPutRequest devDojoUserPutRequest) {
        devDojoUserService.replaceUser(devDojoUserPutRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/admin/delete/{id}")
    @Operation(summary = "Delete User", description = "Endpoint to delete a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully."),
            @ApiResponse(responseCode = "400", description = "User not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID of the user to be deleted") @PathVariable Long id) {
        devDojoUserService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

