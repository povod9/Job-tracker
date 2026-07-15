package com.job_tracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.job_tracker.create_exception.InvalidCredentialsException;
import com.job_tracker.create_exception.InvalidPasswordException;
import com.job_tracker.create_exception.SamePasswordException;
import com.job_tracker.dto.*;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.enums.Role;
import com.job_tracker.enums.VacancySource;
import com.job_tracker.enums.VacancyStatus;
import com.job_tracker.mapper.UserMapper;
import com.job_tracker.repository.UserRepository;
import com.job_tracker.security.JwtCore;
import com.job_tracker.service.impl.SecurityContextServiceImpl;
import com.job_tracker.service.impl.UserServiceImpl;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock SecurityContextServiceImpl securityContextService;
  @Mock UserRepository userRepository;
  @Mock JwtCore jwtCore;
  @Mock UserMapper userMapper;
  @Mock PasswordEncoder passwordEncoder;
  @InjectMocks UserServiceImpl userService;

  @Test
  void successfullyCreateUser(){
    UserCreateRequestDto userCreateRequestDto = createUserRequestDto();
    UserResponseDto userResponseDto = createUserResponseDto();
    UserEntity userEntity = createUserEntity();

    when(userRepository.existsByEmail(userCreateRequestDto.email())).thenReturn(false);
    when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
    when(passwordEncoder.encode(userCreateRequestDto.password())).thenReturn("HashPassword");
    when(userMapper.userToDto(userEntity)).thenReturn(userResponseDto);

    var actual = userService.userToCreate(userCreateRequestDto);
    ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);

    verify(userRepository).save(userCaptor.capture());
    verify(userMapper).userToDto(userEntity);
    verify(passwordEncoder).encode(userCreateRequestDto.password());
    UserEntity captured = userCaptor.getValue();

    assertEquals(captured.getName(),actual.name());
    assertEquals(captured.getEmail(),actual.email());
    assertEquals(captured.getRole(),actual.role());
    assertEquals("HashPassword", captured.getPassword());
  }

  @Test
  void doNotCreateUserIfEmailExists(){
    UserCreateRequestDto userCreateRequestDto = createUserRequestDto();

    when(userRepository.existsByEmail(userCreateRequestDto.email())).thenReturn(true);

    var actual = assertThrows(IllegalArgumentException.class,
            () -> userService.userToCreate(userCreateRequestDto));

    verify(passwordEncoder, never()).encode(anyString());
    verify(userRepository, never()).save(any(UserEntity.class));

    assertEquals("Email already exists: " + userCreateRequestDto.email(), actual.getMessage());
  }

  @Test
  void updateUserSuccessfully(){
    PrincipalDto principalDto = createPrincipal();
    UserEntity userEntity = createUserEntity();
    UserUpdateDto userUpdateDto = createUserUpdateDto();
    UserResponseDto userResponseDto = createUserResponseDto();

    when(userRepository.findById(principalDto.id())).thenReturn(Optional.of(userEntity));
    when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
    when(userRepository.existsByEmail(userUpdateDto.email())).thenReturn(false);
    when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
    when(userMapper.userToDto(userEntity)).thenReturn(userResponseDto);

    userService.userToUpdate(userUpdateDto);
    ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
    verify(userRepository).save(userCaptor.capture());
    verify(userMapper).userToDto(userEntity);

    UserEntity captured = userCaptor.getValue();

    assertEquals(captured.getEmail(), userUpdateDto.email());
    assertEquals(captured.getName(), userUpdateDto.name());
  }

  @Test
  void doNotUpdateUserIfEmailTheSame(){
    PrincipalDto principalDto = createPrincipal();
    UserEntity userEntity = createUserEntity();
    UserUpdateDto userUpdateDto = new UserUpdateDto("Jahn","jahn@gmail.com");
    when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
    when(userRepository.findById(principalDto.id())).thenReturn(Optional.of(userEntity));

    var actual = assertThrows(IllegalArgumentException.class,
            () -> userService.userToUpdate(userUpdateDto));

    verify(userRepository, never()).save(any(UserEntity.class));
    verify(userMapper, never()).userToDto(any(UserEntity.class));

    assertEquals("Email cannot be the same", actual.getMessage());
  }

  @Test
  void doNotUpdateUserIfEmailExists(){
    PrincipalDto principalDto = createPrincipal();
    UserEntity userEntity = createUserEntity();
    UserUpdateDto userUpdateDto = new UserUpdateDto("Jahn","jahn1@gmail.com");
    when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
    when(userRepository.findById(principalDto.id())).thenReturn(Optional.of(userEntity));
    when(userRepository.existsByEmail(userUpdateDto.email())).thenReturn(true);

    var actual = assertThrows(IllegalArgumentException.class,
            () -> userService.userToUpdate(userUpdateDto));

    verify(userRepository, never()).save(any(UserEntity.class));
    verify(userMapper, never()).userToDto(any(UserEntity.class));

    assertEquals("Email already exists", actual.getMessage());
  }

  @Test
  void doNotUpdatePasswordIfPasswordIsWrong(){
    UserUpdatePasswordRequestDto userUpd = createUpdPassRequest();
    PrincipalDto principalDto = createPrincipal();
    UserEntity userEntity = createUserEntity();

    when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
    when(userRepository.findById(principalDto.id())).thenReturn(Optional.of(userEntity));

    var actual = assertThrows(InvalidPasswordException.class,
            () -> userService.userUpdatePassword(userUpd));

    verify(passwordEncoder, never()).encode(anyString());
    assertEquals("Wrong current password", actual.getMessage());
  }

  @Test
  void doNotUpdateIfPasswordTheSame(){
    UserUpdatePasswordRequestDto userUpd = new UserUpdatePasswordRequestDto(
            "SecretPassword",
            "SecretPassword");
    PrincipalDto principalDto = createPrincipal();
    UserEntity userEntity = createUserEntity();


    when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
    when(userRepository.findById(principalDto.id())).thenReturn(Optional.of(userEntity));
    when(passwordEncoder.matches(userUpd.currentPassword(), userEntity.getPassword())).thenReturn(true);

    var actual = assertThrows(SamePasswordException.class,
            () -> userService.userUpdatePassword(userUpd));

    verify(passwordEncoder, never()).encode(anyString());
    verify(userRepository, never()).save(any(UserEntity.class));

    assertEquals("The new password cannot be the same as the old", actual.getMessage());
  }

  @Test
  void getUserByEmail() {
    UserEntity userEntity = createUserEntity();

    UserResponseDto expectedDto = createUserResponseDto();

    when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
    when(userMapper.userToDto(userEntity)).thenReturn(expectedDto);

    var result = userService.getUserByEmail(userEntity.getEmail());

    assertEquals(expectedDto, result);
  }
  @Test
  void throwNotFoundIfUserNotExists(){
    String email = "test@user.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    var result = assertThrows(EntityNotFoundException.class,
            () -> userService.getUserByEmail("test@user.com"));

    assertEquals("Cannot find user by email " + email, result.getMessage());
  }

  @Test
  void deleteUser() {
    UserEntity userEntity = createUserEntity();

    UserResponseDto expectedDto = createUserResponseDto();

    when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
    when(userMapper.userToDto(userEntity)).thenReturn(expectedDto);

    var result = userService.deleteUser(userEntity.getEmail());

    verify(userRepository).delete(userEntity);
    assertEquals(expectedDto, result);
  }

  @Test
  void successfullyLogin(){
    LoginResponseDto loginResponseDto = createLoginResponse();
    PrincipalDto principalDto = createPrincipal();
    UserEntity userEntity = createUserEntity();
    RequestLoginDto loginRequestDto = createLoginRequest();

    when(userRepository.findByEmail(principalDto.email())).thenReturn(Optional.of(userEntity));
    when(passwordEncoder.matches(loginRequestDto.password(), userEntity.getPassword())).thenReturn(true);
    when(jwtCore.generateJwtToken(userEntity)).thenReturn("token");
    var actual = userService.userToLogin(loginRequestDto);

    assertEquals(loginResponseDto.accessToken(), actual.accessToken());
    assertEquals(loginResponseDto.tokenType(), actual.tokenType());
  }

  @Test
  void  invalidCredentialsWhenLogin(){
    RequestLoginDto loginRequestDto = createLoginRequest();
    PrincipalDto principalDto = createPrincipal();
    UserEntity userEntity = createUserEntity();

    when(userRepository.findByEmail(principalDto.email())).thenReturn(Optional.of(userEntity));
    when(passwordEncoder.matches(loginRequestDto.password(), userEntity.getPassword())).thenReturn(false);

    var actual = assertThrows(InvalidCredentialsException.class,
            () -> userService.userToLogin(loginRequestDto));

    verify(jwtCore, never()).generateJwtToken(any(UserEntity.class));
    assertEquals("Wrong password or email", actual.getMessage());
  }

  @Test
  void returnAllUsers(){
    UserEntity userEntity1 = createUserEntity();
    UserEntity userEntity2 = createUserEntity();
    UserEntity userEntity3 = createUserEntity();
    UserEntity userEntity4 = createUserEntity();
    UserResponseDto userResponseDto1 = createUserResponseDto();
    UserResponseDto userResponseDto2 = createUserResponseDto();
    UserResponseDto userResponseDto3 = createUserResponseDto();
    UserResponseDto userResponseDto4 = createUserResponseDto();

    Page<UserEntity> page = new PageImpl<>(List.of(userEntity1, userEntity2, userEntity3, userEntity4));
    Pageable pageable = PageRequest.of(0, 10);
    when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
    when(userMapper.userToDto(userEntity1)).thenReturn(userResponseDto1);
    when(userMapper.userToDto(userEntity2)).thenReturn(userResponseDto2);
    when(userMapper.userToDto(userEntity3)).thenReturn(userResponseDto3);
    when(userMapper.userToDto(userEntity4)).thenReturn(userResponseDto4);

    userService.getAllUsers(pageable);
    verify(userRepository).findAll(any(Pageable.class));
  }

  private UserUpdateDto createUserUpdateDto(){
    return new UserUpdateDto("Lisa", "lisa@user.com");
  }

  private RequestLoginDto createLoginRequest(){
    return new RequestLoginDto("jahn@gmail.com", "SecretPassword");
  }

  private LoginResponseDto createLoginResponse(){
    return new LoginResponseDto("token", "Bearer");
  }

  private UserUpdatePasswordRequestDto createUpdPassRequest(){
    return new UserUpdatePasswordRequestDto("SecretPassword", "123");
  }

  private UserEntity createUserEntity() {
    return new UserEntity(
        1L,
        "Jahn",
        "jahn@gmail.com",
        "SecretPassword",
        Role.USER,
        OffsetDateTime.now(),
        OffsetDateTime.now());
  }

  private UserResponseDto createUserResponseDto() {
    return new UserResponseDto("Jahn", "jahn@gmail.com", Role.USER);
  }

  private UserCreateRequestDto createUserRequestDto(){
    return new UserCreateRequestDto("Jahn", "jahn@gmail.com", "SecretPassword");
  }

  private PrincipalDto createPrincipal(){
    return new PrincipalDto("jahn@gmail.com", "USER", 1L);
  }

}
