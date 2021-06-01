package pl.filipwlodarczyk.RegistrationApplication.registration;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.filipwlodarczyk.RegistrationApplication.AppUser.AppUser;
import pl.filipwlodarczyk.RegistrationApplication.AppUser.AppUserRole;
import pl.filipwlodarczyk.RegistrationApplication.AppUser.AppUserService;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidation;
    private final AppUserService appUserService;


    public String register(RegistrationRequest request) {
        boolean isValid = emailValidation.test(request.toString());
        if (isValid) {
            return appUserService.
                    singUpUser(new AppUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                            AppUserRole.USER_ROLE));
            } else {
            throw new IllegalStateException("This email is taken");
        }
    }
}
