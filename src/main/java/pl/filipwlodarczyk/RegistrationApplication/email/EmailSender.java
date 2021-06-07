package pl.filipwlodarczyk.RegistrationApplication.email;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public interface EmailSender {
    void send(String to, String email);
}
