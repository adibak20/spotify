package pl.adibak.services;

import pl.adibak.command.Genre;
import pl.adibak.command.RecommendedTrack;

import java.util.List;

public interface SpotifyService {
    List<Genre> getFavoriteGeneres();
    List<Genre> getArtistsForGeneres(List<Genre> genres);
    List<RecommendedTrack> getRecommendations(String genre);

}
