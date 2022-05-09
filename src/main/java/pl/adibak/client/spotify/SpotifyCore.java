package pl.adibak.client.spotify;

import com.neovisionaries.i18n.CountryCode;
import org.springframework.stereotype.Service;
import pl.adibak.exceptions.CommonException;
import pl.adibak.exceptions.ExceptionType;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.special.SearchResult;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;
import se.michaelthelin.spotify.requests.data.follow.GetUsersFollowedArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.SearchItemRequest;

import java.io.IOException;
import java.text.ParseException;

@Service
public class SpotifyCore {

    private final SpotifyApi spotifyApi;

    public SpotifyCore(final SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    public String authorization(SpotifyApi spotifyApi, AuthorizationCodeRequest authorizationCodeRequest) throws Exception {
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            throw new CommonException(ExceptionType.UNAUTHORIZED);
        }
        return spotifyApi.getAccessToken();
    }

    public Artist[] getFollowedArtists() {
        GetUsersFollowedArtistsRequest getUsersFollowedArtistsRequest = this.spotifyApi
                .getUsersFollowedArtists(ModelObjectType.ARTIST)
                .build();

        try {
            PagingCursorbased<Artist> artistPagingCursorbased = getUsersFollowedArtistsRequest.execute();
            return artistPagingCursorbased.getItems();
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            throw new CommonException(ExceptionType.INTERNAL_SERVER_ERROR);
        }
    }

    public TrackSimplified[] getRecommendations(String genre) {
        GetRecommendationsRequest getRecommendationsRequest = spotifyApi.getRecommendations()
                .limit(10)
                .max_popularity(50)
                .min_popularity(10)
                .seed_genres(genre)
                .target_popularity(20)
                .build();
        try {
            Recommendations recommendations = getRecommendationsRequest.execute();
            return recommendations.getTracks();
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            throw new CommonException(ExceptionType.INTERNAL_SERVER_ERROR);
        }
    }

    public Paging<Artist> searchArtistsByGenres(String genre) {
        SearchItemRequest searchItemRequest = spotifyApi.searchItem(genre, ModelObjectType.ARTIST.getType())
                .market(CountryCode.PL)
                .includeExternal("audio")
                .build();
        try {
            SearchResult searchResult = searchItemRequest.execute();
            return searchResult.getArtists();
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            throw new CommonException(ExceptionType.INTERNAL_SERVER_ERROR);
        }
    }
}
