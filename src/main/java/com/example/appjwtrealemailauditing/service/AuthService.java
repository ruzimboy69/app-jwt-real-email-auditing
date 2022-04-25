package com.example.appjwtrealemailauditing.service;

import com.example.appjwtrealemailauditing.entity.Role;
import com.example.appjwtrealemailauditing.entity.User;
import com.example.appjwtrealemailauditing.entity.enums.RoleName;
import com.example.appjwtrealemailauditing.payload.LoginDto;
import com.example.appjwtrealemailauditing.payload.RegisterDto;
import com.example.appjwtrealemailauditing.repository.RoleRepository;
import com.example.appjwtrealemailauditing.repository.UserRepository;
import com.example.appjwtrealemailauditing.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService  implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;
    public  ApiResponse registerUser(RegisterDto registerDto){
        boolean b = userRepository.existsByEmail(registerDto.getEmail());
        if(b){
            return new ApiResponse("already exist",false);
        }

        User user=new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoleList(Collections.singletonList(roleRepository.findByRoleName(RoleName.ROLE_USER)));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);
        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("Successfully registered,to activate your account please verify your email",true);
    }
    public  boolean sendEmail(String sendEmail,String emailCode){
       try {
           SimpleMailMessage  message=new SimpleMailMessage();
           message.setFrom("ruzimboykholmurotov@gmail.com");
           message.setTo(sendEmail);
           message.setSubject("Akkountni tasdiqlash");
           message.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode="+emailCode+"+&email="+sendEmail+"'>Tasdiqlash</a>");
           javaMailSender.send(message);
           return true;
       }catch (Exception e) {
           return false;
       }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser=userRepository.findByEmailAndEmailCode(email,emailCode);
        if(optionalUser.isPresent()){
            User user=optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            User save = userRepository.save(user);
            return new ApiResponse("Account tasdqlandi",true);
        }
        return new ApiResponse("account allaqachon tasdiqlangan",false);
    }

    public ApiResponse login(LoginDto loginDto){
      try {
          Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                  loginDto.getUsername(), loginDto.getPassword()

          ));
          User user=(User) authenticate.getPrincipal();
          String s = jwtProvider.generateToken(loginDto.getUsername(), (Set<Role>) user.getRoleList());
          return new ApiResponse("token",true,s);

      }catch (BadCredentialsException badCredentialsException){
          return new ApiResponse("something went wrong",false);
      }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()->
                new UsernameNotFoundException(username+" topilmadi"));
    }
}
