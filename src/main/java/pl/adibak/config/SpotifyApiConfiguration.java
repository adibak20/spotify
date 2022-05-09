package pl.adibak.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;


@Configuration
class SpotifyApiConfiguration {

    @Value("${spotify.clientId}")
    private String clientId;

    @Value("${spotify.clientSecret}")
    private String clientSecret;

    @Value("${spotify.callback}")
    private String callback;

    @Value("${spotify.scope}")
    private String scope;

    @Bean
    public SpotifyApi spotifyApi() {
        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(SpotifyHttpManager.makeUri(callback))
                .build();
    }

    @Bean
    public AuthorizationCodeUriRequest authorizationCodeUriRequest(){
     return this.spotifyApi().authorizationCodeUri()
             .scope(scope)
             .show_dialog(true)
             .build();

    }
}
