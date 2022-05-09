package pl.adibak.contollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.adibak.exceptions.CommonException;
import pl.adibak.exceptions.ExceptionType;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@RestController
@RequestMapping(LoginController.PREFIX)
class LoginController {

    static final String PREFIX = "/login";

   private final AuthorizationCodeUriRequest authorizationCodeUriRequest;

    LoginController(final AuthorizationCodeUriRequest authorizationCodeUriRequest) {
        this.authorizationCodeUriRequest = authorizationCodeUriRequest;
    }

    @GetMapping("/spotify")
    @ResponseBody
    public void login(HttpServletResponse resp) {
        try {
            final URI uri = authorizationCodeUriRequest.execute();
            resp.sendRedirect(uri.toString());
        } catch (Exception e) {
            throw new CommonException(ExceptionType.UNAUTHORIZED);
        }
    }
}
