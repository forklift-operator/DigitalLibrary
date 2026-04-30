package bg.fmi.web.marketplace.controller;

import bg.fmi.web.marketplace.dto.UserRegisterDto;
import bg.fmi.web.marketplace.dto.UserLoginDto;
import bg.fmi.web.marketplace.dto.UserResponseDto;
import bg.fmi.web.marketplace.model.user.User;
import bg.fmi.web.marketplace.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
                .map(user -> {
                    UserResponseDto userResponse = new UserResponseDto();

                    userResponse.setId(user.getId());
                    userResponse.setLastName(user.getFirstName());
                    userResponse.setFirstName(user.getFirstName());
                    userResponse.setEmail(user.getEmail());
                    userResponse.setRole(user.getRole());

                    return userResponse;
                }).toList();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Validated UserRegisterDto dto) {

        User userRequest = new User();

        userRequest.setEmail(dto.getEmail());
        userRequest.setFirstName(dto.getFirstName());
        userRequest.setLastName(dto.getLastName());
        userRequest.setPassword(dto.getPassword());
        userRequest.setRole(dto.getRole());

        User savedUser = userService.register(userRequest);

        UserResponseDto userResponse = new UserResponseDto(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Validated UserLoginDto dto) {

        User userRequest = new User();
        userRequest.setEmail(dto.getEmail());
        userRequest.setPassword(dto.getPassword());

        User loggedUser = userService.login(userRequest);

        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setId(loggedUser.getId());
        userResponse.setEmail(loggedUser.getEmail());
        userResponse.setFirstName(loggedUser.getFirstName());
        userResponse.setLastName(loggedUser.getLastName());
        userResponse.setRole(loggedUser.getRole());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponse);

    }
}
