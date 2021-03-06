package mobi.wrapper.listings.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.model.peopleCasting.PersonCasting;
import mobi.wrapper.listings.view.callback.DisplayPersonDetails;

/**
 * Created by Rohit on 3/16/2016.
 */
public class PeopleRecyclerViewAdapter extends RecyclerView.Adapter<PeopleRecyclerViewAdapter.PeopleHolder> {
    RequestQueue mQueue1;
    PersonCasting mPeople;
    Boolean isCast;
    DisplayPersonDetails mContext;

    public PeopleRecyclerViewAdapter(PersonCasting mPeople, RequestQueue queue, Context context, Boolean isCast) {
        Log.i("sanju", "in seasons recycler view");
        this.mPeople = mPeople;
        mQueue1 = queue;
        this.isCast = isCast;
        mContext = (DisplayPersonDetails) context;
    }

    public class PeopleHolder extends RecyclerView.ViewHolder {
        NetworkImageView image;
        TextView characterName;
        TextView realName;

        public PeopleHolder(View itemView, int viewType) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.adapter_people_recycler_view_poster_networkimageview);
            characterName = (TextView) itemView.findViewById(R.id.adapter_people_recycler_view_character_name_text_view);
            realName = (TextView) itemView.findViewById(R.id.adapter_people_recycler_view_real_name_text_view);
        }
    }

    @Override
    public PeopleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PeopleHolder myPeopleHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_people_recycler_view, parent, false);
        myPeopleHolder = new PeopleHolder(view, viewType);
        return myPeopleHolder;
    }

    @Override
    public void onBindViewHolder(PeopleHolder holder, final int position) {
        if (isCast) {

            final String poster;
            Log.i("sanju", "in people,s holder");
            if ((mPeople.getCast().get(position).getProfile_path()) != null && !mPeople.getCast().get(position).getProfile_path().isEmpty()) {
                poster = String.format(UrlConstants.IMAGE_URLW_185, mPeople.getCast().get(position).getProfile_path());
                holder.image.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
            }else {
                poster = "null";
                holder.image.setImageUrl("http://www.cens.res.in/images/noimage.png", TVListingNetworkClient.getInstance().getImageLoader());
            }holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.displayPersonDetails(mPeople.getCast().get(position).getId(), mPeople.getCast().get(position).getName(), poster);
                }
            });
            holder.characterName.setText(mPeople.getCast().get(position).getCharacter());
            holder.realName.setText(mPeople.getCast().get(position).getName());
        }else {
            final String poster;
            Log.i("sanju", "in people,s holder");
            if ((mPeople.getCrew().get(position).getProfile_path()) != null && !mPeople.getCrew().get(position).getProfile_path().isEmpty()) {
                poster = String.format(UrlConstants.IMAGE_URLW_185, mPeople.getCrew().get(position).getProfile_path());
                holder.image.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
            }else {
                poster = "null";
                holder.image.setImageUrl("http://www.cens.res.in/images/noimage.png", TVListingNetworkClient.getInstance().getImageLoader());
            }holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.displayPersonDetails(mPeople.getCrew().get(position).getId(), mPeople.getCrew().get(position).getName(), poster);
                }
            });
            holder.characterName.setText(mPeople.getCrew().get(position).getJob());
            holder.realName.setText(mPeople.getCrew().get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        if (mPeople == null) {
            return 0;
        }else {
            if (isCast) {
                return mPeople.getCast().size();
            }else {
                return mPeople.getCrew().size();
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
