package com.security20.service;

import com.security20.dto.LoginDto;
import com.security20.dto.UserDto;
import com.security20.entity.PropertyUser;
import com.security20.repository.PropertyUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRegistrationService {

    private PropertyUserRepository userRepository;

    private JwtService jwtService;

    private ModelMapper modelMapper;

    public UserRegistrationService(PropertyUserRepository userRepository, JwtService jwtService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
    }

    public UserDto registerUser(UserDto dto) throws Exception {
        Optional<PropertyUser> opUser = userRepository.findByUsername(dto.getUsername());

        if (opUser.isPresent()) {
            throw new Exception("User already registered");
        }
        PropertyUser user = mapToEntity(dto);
        user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(10)));
//        user.setUsername(dto.getUsername());
//        user.setEmail(dto.getEmail());
//        user.setFirstName(dto.getFirstName());
//        user.setLastName(dto.getLastName());
        PropertyUser saveUser = userRepository.save(user);
        return mapToDto(saveUser);
    }

    PropertyUser mapToEntity(UserDto userDto) {
        PropertyUser user = modelMapper.map(userDto, PropertyUser.class);
        return user;
    }

    UserDto mapToDto(PropertyUser user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    public String verifyLogin(LoginDto loginDto) {
        Optional<PropertyUser> OpUser = userRepository.findByUsername(loginDto.getUsername());
        if (OpUser.isPresent()) {
            PropertyUser user = OpUser.get();
            if(BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {
                return jwtService.generateToken(user);
            }
        }
        return null;
    }
}


