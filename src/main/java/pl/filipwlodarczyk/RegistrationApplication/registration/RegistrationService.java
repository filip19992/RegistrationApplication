package pl.filipwlodarczyk.RegistrationApplication.registration;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.filipwlodarczyk.RegistrationApplication.AppUser.AppUser;
import pl.filipwlodarczyk.RegistrationApplication.AppUser.AppUserRole;
import pl.filipwlodarczyk.RegistrationApplication.AppUser.AppUserService;
import pl.filipwlodarczyk.RegistrationApplication.registration.Token.ConfirmationToken;
import pl.filipwlodarczyk.RegistrationApplication.registration.Token.ConfirmationTokenService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidation;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;


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

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }

}
