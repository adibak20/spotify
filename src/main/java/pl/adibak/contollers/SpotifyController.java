package pl.adibak.contollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.adibak.command.Genre;
import pl.adibak.command.RecommendedTrack;
import pl.adibak.services.SpotifyService;
import se.michaelthelin.spotify.SpotifyApi;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller()
@RequestMapping(SpotifyController.PREFIX)
class SpotifyController {

    static final String PREFIX = "/spotify";

    private final SpotifyService spotifyService;

    private final SpotifyApi spotifyApi;

    SpotifyController(final SpotifyService spotifyService, final SpotifyApi spotifyApi) {
        this.spotifyService = spotifyService;
        this.spotifyApi = spotifyApi;
    }

    @GetMapping("/main")
    String main(Model model) {
        if (spotifyApi.getAccessToken() == null){
            return "redirect:/";
        }
        List<Genre> favoriteGeneres = spotifyService.getFavoriteGeneres();
        if(favoriteGeneres.size() > 0){
            List<String> genres = favoriteGeneres.stream().map(Genre::getName).collect(Collectors.toList());
            List<RecommendedTrack> recommendations = spotifyService.getRecommendations(String.join(",", genres));
            model.addAttribute("recommendations", recommendations);
        }
        model.addAttribute("favoriteGeneres", favoriteGeneres);
        return "main";
    }

}
