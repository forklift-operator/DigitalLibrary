package bg.fmi.web.marketplace.controller;

import bg.fmi.web.marketplace.dto.UserResponseDto;
import bg.fmi.web.marketplace.model.User;
import bg.fmi.web.marketplace.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    public List<UserResponseDto> getUsers() {

        return userService.getAllUsers().stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();

    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable @NotNull Long id) {

        User exitingUser = userService.getUserById(id);

        UserResponseDto userResponseDto = modelMapper.map(exitingUser, UserResponseDto.class);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);

    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotNull Long id) {

        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
