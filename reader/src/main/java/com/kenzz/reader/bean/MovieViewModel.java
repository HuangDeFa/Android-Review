package com.kenzz.reader.bean;

import java.util.List;

/**
 * Created by ken.huang on 12/6/2017.
 * MovieViewModel
 */

public class MovieViewModel {
    public String imageUrl;
    public String title;
    public String directors;
    public String casts;
    public String type;
    public String rate;

    public MovieViewModel(String title,String directors,String casts, String type, String rate) {
        this.title = title;
        this.directors = directors;
        this.casts = casts;
        this.type = type;
        this.rate = rate;
    }

    public MovieViewModel() {
    }

    public static MovieViewModel build(MovieEntity.Subject subject){
        MovieViewModel model=new MovieViewModel();
        model.title=subject.title;
        model.imageUrl=subject.images.medium;
        StringBuilder sb=new StringBuilder();
        for (MovieEntity.Directors director : subject.directors) {
            sb.append(director.name);
            sb.append("/");
        }
        model.directors = sb.subSequence(0,sb.length()-1).toString();
        sb=new StringBuilder();
        for (MovieEntity.Casts cast : subject.casts) {
            sb.append(cast.name);
            sb.append("/");
        }
        model.casts = sb.subSequence(0,sb.length()-1).toString();
        sb=new StringBuilder();
        for (String genre : subject.genres) {
            sb.append(genre);
            sb.append("/");
        }
        model.type = sb.subSequence(0,sb.length()-1).toString();
        model.rate=subject.rating.average+"";
        return model;
    }
}
