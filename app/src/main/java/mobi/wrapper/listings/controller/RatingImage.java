package mobi.wrapper.listings.controller;

import mobi.wrapper.listings.R;

/**
 * Created by Rohit on 4/3/2016.
 */
public class RatingImage {
    public static int getRatingImage(double rating) {
        int image;
        if (rating>=80) {
            image = R.mipmap.ic_sentiment_very_satisfied_white_48dp;
        }else if (rating>=70){
            image = R.mipmap.ic_sentiment_satisfied_white_48dp;
        }else if (rating>=60) {
            image = R.mipmap.ic_sentiment_neutral_white_48dp;
        }else if (rating>=50){
            image = R.mipmap.ic_sentiment_dissatisfied_white_48dp;
        }else {
            image = R.mipmap.ic_sentiment_very_dissatisfied_white_48dp;
        }
        return image;
    }
}