package mobi.wrapper.listings.model.peopleCasting;

import mobi.wrapper.listings.model.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Rohit on 3/26/2016.
 */
public class PersonCasting extends BaseResponse {
    ArrayList<Casting> cast;
    ArrayList<PersonsCrew> crew;

    public PersonCasting(ArrayList<Casting> cast, ArrayList<PersonsCrew> crew) {
        this.cast = cast;
        this.crew = crew;
    }

    public ArrayList<Casting> getCast() {
        return cast;
    }

    public ArrayList<PersonsCrew> getCrew() {
        return crew;
    }
}
