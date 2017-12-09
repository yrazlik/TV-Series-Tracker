package com.yrazlik.tvseriestracker.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class ShowDto {

    private static final String STATUS_ENDED = "Ended";
    private static final String STATUS_RUNNING = "Running";

    private long id;
    private String url;
    private String name;
    private String type;
    private String language;
    private List<String> genres;
    private String status;
    private long runtime;
    private String premiered;
    private String officialsite;


    private ScheduleDto schedule;
    private RatingDto rating;
    private long weight;
    private NetworkDto network;
    private WebChannelDto webChannel;
    private ExternalsDto externals;
    private ImageDto image;
    private String summary;
    private long updated;
    @SerializedName("_links")
    private LinksDto links;
    private EmbeddedDto _embedded;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    public String getPremiered() {
        return premiered;
    }

    public void setPremiered(String premiered) {
        this.premiered = premiered;
    }

    public String getOfficialsite() {
        return officialsite;
    }

    public void setOfficialsite(String officialsite) {
        this.officialsite = officialsite;
    }

    public ScheduleDto getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleDto schedule) {
        this.schedule = schedule;
    }

    public RatingDto getRating() {
        return rating;
    }

    public void setRating(RatingDto rating) {
        this.rating = rating;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public NetworkDto getNetwork() {
        return network;
    }

    public void setNetwork(NetworkDto network) {
        this.network = network;
    }

    public WebChannelDto getWebChannel() {
        return webChannel;
    }

    public void setWebChannel(WebChannelDto webChannel) {
        this.webChannel = webChannel;
    }

    public ExternalsDto getExternals() {
        return externals;
    }

    public void setExternals(ExternalsDto externals) {
        this.externals = externals;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public LinksDto getLinks() {
        return links;
    }

    public void setLinks(LinksDto links) {
        this.links = links;
    }

    public EmbeddedDto get_embedded() {
        return _embedded;
    }

    public void set_embedded(EmbeddedDto _embedded) {
        this._embedded = _embedded;
    }

    public double getWeightedRating() {
        double avgRating = rating == null ? 0 : rating.getAverage();
        double runningBonus = (status != null && status.equalsIgnoreCase(STATUS_RUNNING)) ? (avgRating * 0.05) : 0;
        return ((weight / 10.0) * 0.62) + (avgRating * 0.38) + runningBonus;
    }

    public String getGenresText() {
        String genresText = "";
        if(genres != null && genres.size() > 0) {
            for(String genre : genres) {
                if (genre != null) {
                    genresText += genre + ", ";
                }
            }
            if(genresText != null && genresText.length() > 0) {
                genresText = genresText.substring(0, genresText.length() - 2);
            }
        }
        return (genresText != null && genresText.length() > 0) ? genresText : "-";
    }
}
