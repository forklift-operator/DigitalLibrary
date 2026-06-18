package bg.fmi.web.marketplace.controller;

import bg.fmi.web.marketplace.dto.UserLoginDto;
import bg.fmi.web.marketplace.dto.UserRegisterDto;
import bg.fmi.web.marketplace.dto.UserResponseDto;
import bg.fmi.web.marketplace.model.User;
import bg.fmi.web.marketplace.repository.UserRepository;
import bg.fmi.web.marketplace.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    public AuthController(UserRepository userRepository, ModelMapper modelMapper, AuthService authService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Validated UserLoginDto dto, HttpSession session) {
        User userRequest = modelMapper.map(dto, User.class);

        User loggedUser = authService.login(userRequest);

        UserResponseDto userResponse = modelMapper.map(loggedUser, UserResponseDto.class);

        session.setAttribute("USER_ID", userResponse.getId());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponse);

    }


    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Validated UserRegisterDto dto, HttpSession session) {

        User userRequest = modelMapper.map(dto, User.class);

        User savedUser = authService.register(userRequest);

        UserResponseDto userResponse = modelMapper.map(savedUser, UserResponseDto.class);

        session.setAttribute("USER_ID", userResponse.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);

    }


    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.status(200).build();
    }

}
