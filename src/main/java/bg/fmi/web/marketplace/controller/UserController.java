package bg.fmi.web.marketplace.controller;

import bg.fmi.web.marketplace.dto.UserRegisterDto;
import bg.fmi.web.marketplace.dto.UserLoginDto;
import bg.fmi.web.marketplace.dto.UserResponseDto;
import bg.fmi.web.marketplace.mapper.UserMapper;
import bg.fmi.web.marketplace.model.user.User;
import bg.fmi.web.marketplace.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserResponseDto> getUsers() {

        return userService.getAllUsers().stream()
                .map(UserMapper::toUserResponseFromUserEntity).toList();

    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable @NotNull Long id) {

        User exitingUser = userService.getUserById(id);

        UserResponseDto userResponse = UserMapper.toUserResponseFromUserEntity(exitingUser);

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);

    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Validated UserRegisterDto dto) {

        User userRequest = UserMapper.toUserEntityFromRegisterDto(dto);

        User savedUser = userService.register(userRequest);

        UserResponseDto userResponse = UserMapper.toUserResponseFromUserEntity(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);

    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Validated UserLoginDto dto) {

        User userRequest = new User();
        userRequest.setEmail(dto.getEmail());
        userRequest.setPassword(dto.getPassword());

        User loggedUser = userService.login(userRequest);

        UserResponseDto userResponse = UserMapper.toUserResponseFromUserEntity(loggedUser);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponse);

    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotNull Long id) {

        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
