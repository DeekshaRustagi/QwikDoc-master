package main.java.cz2006project.mojojo.Boundary.QwikSearch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import cz2006project.mojojo.R;

/**
 * <h1>Search By Doctor Fragment</h1>
 * This fragment is used by patients to view the list of doctors available under QwikDoc
 * along with the relevant details of the clinic.
 *
 * <p>
 *
 * @author  Lee Sai Mun
 * @version 1.0
 * @since   2014-03-31
 */
public class SearchByDoctor extends Fragment {


    private DoctorAdapter doctoradapter;
    private ListView listView;


    /**
     * Required Default Constructor for SearchByDoctor
     */
    public SearchByDoctor() {
        // Required empty public constructor
    }
    /**
     * This method sets the adapter to display the list of doctors.
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qwiksearch, container, false);


        // Initialize the subclass of ParseQueryAdapter
        doctoradapter = new DoctorAdapter(getActivity());

        // Initialize ListView and set initial view to mainAdapter
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(doctoradapter);
        doctoradapter.loadObjects();

        return view;


    }


    /**
     * This method is an adapter that queries the Parse database for the list of Doctors
     * who are open for patient appointments and also extracts the relevant details of the doctors.
     */
    public class DoctorAdapter extends ParseQueryAdapter<ParseObject> {

        public DoctorAdapter(Context context) {
            // Use the QueryFactory to construct a PQA that will only show
            // Todos marked as high-pri
            super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
                public ParseQuery create() {
                    ParseQuery query = new ParseQuery("Doctor");
                    return query;
                }
            });
        }

        // Customize the layout by overriding getItemView
        @Override
        public View getItemView(ParseObject object, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.clinic_itemview, null);
            }

            super.getItemView(object, v, parent);

            // Add and download the image
            ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.icon);
            ParseFile imageFile = object.getParseFile("image");
            if (imageFile != null) {
                todoImage.setParseFile(imageFile);
                todoImage.loadInBackground();
            }

            // Add the title view
            TextView titleTextView = (TextView) v.findViewById(R.id.text1);
            titleTextView.setText(object.getString("Name"));

            TextView locationTextView = (TextView) v.findViewById(R.id.text2);
            locationTextView.setText(object.getString("Clinic"));


            TextView specialtyTextView = (TextView) v.findViewById(R.id.text3);
            specialtyTextView.setText(object.getString("specialty"));


            TextView contactnumberTextView = (TextView) v.findViewById(R.id.text4);
            contactnumberTextView.setText(object.getString("contactnumber"));




            return v;
        }

    }

}
