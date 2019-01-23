package foosh.air.foi.hr.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import foosh.air.foi.hr.R;
import foosh.air.foi.hr.model.User;

public class ListingDetailApplicantsAdapter extends ArrayAdapter<User> {
    private ArrayList<User> applicants;
    Context mContext;

    public ListingDetailApplicantsAdapter(@NonNull Context context, ArrayList<User> applicants) {
        super(context, R.layout.fragment_listing_applicant, applicants);
        this.applicants = applicants;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return this.applicants.size();
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return applicants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  0;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_listing_applicant, null);
        }

        CircleImageView applicantProfile = (CircleImageView) view.findViewById(R.id.applicantProfileImage);
        TextView applicantName = (TextView)view.findViewById(R.id.applicantName);

        Button makeDeal = (Button)view.findViewById(R.id.buttonMakeDeal);
        Button message = (Button)view.findViewById(R.id.buttonMessageApplicant);

        makeDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}
