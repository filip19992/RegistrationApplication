package pl.filipwlodarczyk.RegistrationApplication.AppUser;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppUserService implements UserDetailsService {

private final AppUserRepository appUserRepository;
private final BCryptPasswordEncoder bCryptPasswordEncoder;

private final static String USER_NOT_FOUND_MSG = "Nie znaleziono użytkownika z taki emailem %s";

    public AppUserService(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

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
        return "";
    }
}
