package pl.filipwlodarczyk.RegistrationApplication.AppUser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.filipwlodarczyk.RegistrationApplication.registration.Token.ConfirmationToken;
import pl.filipwlodarczyk.RegistrationApplication.registration.Token.ConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

private final AppUserRepository appUserRepository;
private final BCryptPasswordEncoder bCryptPasswordEncoder;
private final ConfirmationTokenService confirmationTokenService;

private final static String USER_NOT_FOUND_MSG = "Nie znaleziono użytkownika z taki emailem %s";

//    public AppUserService(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenService confirmationTokenService) {
//        this.appUserRepository = appUserRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//        this.confirmationTokenService = confirmationTokenService;
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       return appUserRepository.findByEmail(email)
               .orElseThrow(() -> new UsernameNotFoundException(
                       String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String singUpUser(AppUser appUser) {
        boolean isExisting = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();
        if (isExisting) {
            throw new IllegalStateException("Email is taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }
}
