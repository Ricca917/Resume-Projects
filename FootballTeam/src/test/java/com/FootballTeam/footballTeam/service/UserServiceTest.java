package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.UserRequestDto;
import com.FootballTeam.footballTeam.dto.response.UserResponseDto;
import com.FootballTeam.footballTeam.model.Role;
import com.FootballTeam.footballTeam.model.User;
import com.FootballTeam.footballTeam.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("admin");
        user1.setPassword("encodedPassword1");
        user1.setRole(Role.ADMIN);

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("user");
        user2.setPassword("encodedPassword2");
        user2.setRole(Role.USER);
    }

    @Test
    void testCreateUser_Success() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("newuser");
        requestDto.setPassword("password123");
        requestDto.setRole("USER");

        User newUser = new User();
        newUser.setId(3L);
        newUser.setUsername("newuser");
        newUser.setPassword("encodedPassword");
        newUser.setRole(Role.USER);

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        UserResponseDto responseDto = userService.createUser(requestDto);

        assertNotNull(responseDto);
        assertEquals(newUser.getId(), responseDto.getId());
        assertEquals(requestDto.getUsername(), responseDto.getUsername());
        assertEquals("USER", responseDto.getRole());

        verify(userRepository, times(1)).findByUsername("newuser");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_UsernameAlreadyExists() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("admin");
        requestDto.setPassword("password123");
        requestDto.setRole("USER");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user1));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(requestDto);
        });
        assertTrue(thrown.getMessage().contains("Username 'admin' already exists."));

        verify(userRepository, times(1)).findByUsername("admin");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_WithDefaultRole() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("newuser");
        requestDto.setPassword("password123");
        requestDto.setRole(null); // Ruolo null, dovrebbe usare USER di default

        User newUser = new User();
        newUser.setId(3L);
        newUser.setUsername("newuser");
        newUser.setPassword("encodedPassword");
        newUser.setRole(Role.USER);

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        UserResponseDto responseDto = userService.createUser(requestDto);

        assertNotNull(responseDto);
        assertEquals("USER", responseDto.getRole());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_WithInvalidRole() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("newuser");
        requestDto.setPassword("password123");
        requestDto.setRole("INVALID_ROLE"); // Ruolo non valido

        User newUser = new User();
        newUser.setId(3L);
        newUser.setUsername("newuser");
        newUser.setPassword("encodedPassword");
        newUser.setRole(Role.USER); // Dovrebbe usare USER di default

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        UserResponseDto responseDto = userService.createUser(requestDto);

        assertNotNull(responseDto);
        assertEquals("USER", responseDto.getRole()); // Dovrebbe essere USER di default

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetAllUsers_Success() {
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponseDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user1.getId(), result.get(0).getId());
        assertEquals(user1.getUsername(), result.get(0).getUsername());
        assertEquals("ADMIN", result.get(0).getRole());
        assertEquals(user2.getId(), result.get(1).getId());
        assertEquals(user2.getUsername(), result.get(1).getUsername());
        assertEquals("USER", result.get(1).getRole());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        Optional<UserResponseDto> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(user1.getId(), result.get().getId());
        assertEquals(user1.getUsername(), result.get().getUsername());
        assertEquals("ADMIN", result.get().getRole());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserResponseDto> result = userService.getUserById(99L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void testUpdateUser_Success() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("updatedUser");
        requestDto.setPassword("newPassword");
        requestDto.setRole("ADMIN");

        User updatedUser = new User();
        updatedUser.setId(user1.getId());
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("newEncodedPassword");
        updatedUser.setRole(Role.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("updatedUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponseDto responseDto = userService.updateUser(1L, requestDto);

        assertNotNull(responseDto);
        assertEquals(user1.getId(), responseDto.getId());
        assertEquals("updatedUser", responseDto.getUsername());
        assertEquals("ADMIN", responseDto.getRole());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByUsername("updatedUser");
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void testUpdateUser_NotFound() {
        Long nonExistentId = 99L;
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("updatedUser");
        requestDto.setPassword("newPassword");
        requestDto.setRole("USER");

        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            userService.updateUser(nonExistentId, requestDto);
        });
        assertTrue(thrown.getMessage().contains("Utente non trovato con ID: " + nonExistentId));

        verify(userRepository, times(1)).findById(nonExistentId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_UsernameAlreadyExists() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("user"); // Username dell'user2
        requestDto.setPassword("newPassword");
        requestDto.setRole("ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user2)); // Username già esistente

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(1L, requestDto);
        });
        assertTrue(thrown.getMessage().contains("Username 'user' esiste già"));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByUsername("user");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_WithoutPasswordChange() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("updatedUser");
        requestDto.setPassword(null); // Non cambia password
        requestDto.setRole("USER");

        User updatedUser = new User();
        updatedUser.setId(user1.getId());
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword(user1.getPassword()); // Password rimane la stessa
        updatedUser.setRole(Role.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("updatedUser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponseDto responseDto = userService.updateUser(1L, requestDto);

        assertNotNull(responseDto);
        assertEquals("updatedUser", responseDto.getUsername());

        verify(passwordEncoder, never()).encode(anyString()); // Password non dovrebbe essere encodata
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void testUpdateUser_WithInvalidRole() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("updatedUser");
        requestDto.setPassword("newPassword");
        requestDto.setRole("INVALID_ROLE");

        User updatedUser = new User();
        updatedUser.setId(user1.getId());
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("newEncodedPassword");
        updatedUser.setRole(Role.ADMIN); // Dovrebbe mantenere il ruolo esistente

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("updatedUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponseDto responseDto = userService.updateUser(1L, requestDto);

        assertNotNull(responseDto);
        assertEquals("ADMIN", responseDto.getRole()); // Dovrebbe mantenere il ruolo originale

        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        Long nonExistentId = 99L;
        when(userRepository.existsById(nonExistentId)).thenReturn(false);

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            userService.deleteUser(nonExistentId);
        });
        assertTrue(thrown.getMessage().contains("Utente non trovato con ID: " + nonExistentId));

        verify(userRepository, times(1)).existsById(nonExistentId);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void testConvertToDto_WithNullRole() {
        User userWithNullRole = new User();
        userWithNullRole.setId(3L);
        userWithNullRole.setUsername("testuser");
        userWithNullRole.setRole(null);

        when(userRepository.findById(3L)).thenReturn(Optional.of(userWithNullRole));

        Optional<UserResponseDto> result = userService.getUserById(3L);

        assertTrue(result.isPresent());
        assertEquals("Sconosciuto", result.get().getRole());

        verify(userRepository, times(1)).findById(3L);
    }
}