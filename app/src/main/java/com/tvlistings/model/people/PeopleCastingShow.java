package com.tvlistings.model.people;

import com.tvlistings.model.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/26/2016.
 */
public class PeopleCastingShow extends BaseResponse {
    private ArrayList<Cast> cast;

    public PeopleCastingShow(ArrayList<Cast> cast) {
        this.cast = cast;
    }

    public ArrayList<Cast> getCast() {
        return cast;
    }
}
