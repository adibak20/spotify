package pl.adibak.command;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Genre {

    private String name;

    private Long count = 0L;

    private List<String> artists = new ArrayList<>();

    public Genre(String name) {
        this.name = name;
    }

    public void addArtis(String artists) {
        this.artists.add(artists);
        this.count++;
    }
}
