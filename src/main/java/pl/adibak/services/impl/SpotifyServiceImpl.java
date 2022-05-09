package pl.adibak.services.impl;

import org.springframework.stereotype.Service;
import pl.adibak.client.spotify.SpotifyCore;
import pl.adibak.command.Genre;
import pl.adibak.command.RecommendedTrack;
import pl.adibak.services.SpotifyService;
import pl.adibak.utils.MapComparator;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpotifyServiceImpl implements SpotifyService {

    private static final int QUANTITY_TOP_GENERES = 3;

    private final SpotifyCore spotifyCore;

    SpotifyServiceImpl(final SpotifyCore spotifyCore) {
        this.spotifyCore = spotifyCore;
    }

    @Override
    public List<Genre> getFavoriteGeneres() {
        List<Genre> genre = new ArrayList<>();
        Artist[] followedArtists = this.spotifyCore.getFollowedArtists();
        if (followedArtists == null) {
            return genre;
        }
        List<String> genreFromFallowedArtist = Arrays.stream(followedArtists).map(Artist::getGenres).flatMap(Arrays::stream).collect(Collectors.toList());
        Map<String, Long> genresGrouping = genreFromFallowedArtist.stream().collect(Collectors.groupingBy(s -> s.replaceAll("\\d", ""), Collectors.counting()));
        List<Map.Entry<String, Long>> topGenres = MapComparator.findGreatest(genresGrouping, QUANTITY_TOP_GENERES);
        topGenres.stream().map(Map.Entry::getKey).forEach(s -> genre.add(new Genre(s)));
        genre.forEach(genre1 -> findArtistsByGenre(genre1, followedArtists));
        return genre;
    }

    @Override
    public List<Genre> getArtistsForGeneres(List<Genre> genres) {
        genres.forEach(genre -> fillCountAndArtistsForGenre(spotifyCore.searchArtistsByGenres(genre.getName()), genre));
        return genres;
    }

    @Override
    public List<RecommendedTrack> getRecommendations(final String genre) {
        List<RecommendedTrack> recommendedTracks = new ArrayList<>();
        TrackSimplified[] recommendations = spotifyCore.getRecommendations(genre);
        Arrays.stream(recommendations).forEach(trackSimplified -> recommendedTracks.add(prepareRecommendedTrack(trackSimplified)) );
        return recommendedTracks;
    }

    private RecommendedTrack prepareRecommendedTrack(TrackSimplified track) {
        List<String> artists = Arrays.stream(track.getArtists()).map(ArtistSimplified::getName).collect(Collectors.toList());
        return new RecommendedTrack(track.getName(), String.join(", ", artists));
    }

    private void findArtistsByGenre(Genre genre, Artist[] artists){
        Arrays.stream(artists).filter(artist -> Arrays.asList(artist.getGenres()).contains(genre.getName())).forEach(artist -> genre.addArtis(artist.getName()));
    }

    private void fillCountAndArtistsForGenre(Paging<Artist> artists, Genre genre) {
        genre.setCount(Long.valueOf(artists.getTotal()));
        Arrays.stream(artists.getItems()).forEach(artist -> genre.addArtis(artist.getName()));
    }

}
