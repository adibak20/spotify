package pl.adibak.contollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import pl.adibak.client.spotify.SpotifyCore;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;

@RestController
@RequestMapping(CallbackController.PREFIX)
class CallbackController {

    static final String PREFIX = "/callback";

    private final SpotifyApi spotifyApi;

    private final SpotifyCore  spotifyCore;

    CallbackController(final SpotifyApi spotifyApi, final SpotifyCore spotifyCore) {
        this.spotifyApi = spotifyApi;
        this.spotifyCore = spotifyCore;
    }

    @GetMapping("/spotify")
    public RedirectView callback(@RequestParam("code") String code) throws Exception {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
        this.spotifyCore.authorization(spotifyApi, authorizationCodeRequest);
        return new RedirectView("/spotify/main");
    }

    @GetMapping("/itunes")
    public RedirectView callbackItunes(@RequestParam("code") String code) throws Exception {
        //TODO
        return null;
    }
}
