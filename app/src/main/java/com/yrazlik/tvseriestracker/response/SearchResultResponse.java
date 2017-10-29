package com.yrazlik.tvseriestracker.response;

import com.yrazlik.tvseriestracker.data.SearchResultDto;

import java.util.List;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class SearchResultResponse {

    private List<SearchResultDto> searchResults;

    public List<SearchResultDto> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<SearchResultDto> searchResults) {
        this.searchResults = searchResults;
    }
}
