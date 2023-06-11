package at.ac.fhcampuswien.fhmdb.builderPattern;

import at.ac.fhcampuswien.fhmdb.models.Genre;



public class MovieAPIRequestBuilder {

    private StringBuilder baseUrl;
    private boolean isFirst = true;
    public MovieAPIRequestBuilder(String baseUrl){
        this.baseUrl = new StringBuilder(baseUrl);
    }


    public MovieAPIRequestBuilder query(String query){
        if(query == null || query.isEmpty()){
            return this;
        }
        this.appendDelimiter();
        this.baseUrl.append("query=").append(query);
        return this;
    }

    public MovieAPIRequestBuilder genre(Genre genre){
        if(genre == null){
            return this;
        }
        this.appendDelimiter();
        this.baseUrl.append("genre=").append(genre.toString());
        return this;
    }
    public MovieAPIRequestBuilder releaseYear(String releaseYear){
        if(releaseYear == null || releaseYear.isEmpty()){
            return this;
        }
        this.appendDelimiter();
        this.baseUrl.append("releaseYear=").append(releaseYear);
        return this;
    }
    public MovieAPIRequestBuilder ratingFrom(String ratingFrom){
        if(ratingFrom == null || ratingFrom.isEmpty()){
            return this;
        }
        this.appendDelimiter();
        this.baseUrl.append("ratingFrom=").append(ratingFrom);
        return this;
    }

    public String build(){
        return this.baseUrl.toString();
    }


    private void appendDelimiter(){


        if (isFirst){
            this.baseUrl.append("?");
            isFirst = false;
        } else this.baseUrl.append("&");
    }
}
