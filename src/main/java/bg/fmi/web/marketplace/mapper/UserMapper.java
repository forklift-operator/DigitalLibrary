package bg.fmi.web.marketplace.mapper;

import bg.fmi.web.marketplace.dto.UserRegisterDto;
import bg.fmi.web.marketplace.dto.UserResponseDto;
import bg.fmi.web.marketplace.model.user.User;

public class UserMapper {
    public static UserResponseDto toUserResponseFromUserEntity(User user) {
        UserResponseDto userResponse = new UserResponseDto();

        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());

        return userResponse;
    }

    public static User toUserEntityFromRegisterDto(UserRegisterDto registerDto) {
        User user = new User();

        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setRole(registerDto.getRole());

        return user;
    }
}
