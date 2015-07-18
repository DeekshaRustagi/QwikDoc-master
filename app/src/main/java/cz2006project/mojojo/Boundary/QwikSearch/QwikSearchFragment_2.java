package main.java.cz2006project.mojojo.Boundary.QwikSearch;

/**
 * Created by srishti on 30/3/15.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz2006project.mojojo.R;
import main.java.cz2006project.mojojo.Boundary.Appointments.CreateAppointmentFragment;
import main.java.cz2006project.mojojo.Application.SampleApplication;


/**
 * A simple {@link Fragment} subclass.
 */

/**
 * <h1>Qwik Search</h1>
 * This fragment calls and displays the search by doctor and search by clinic fragment.
 *
 * @author Lee Sai Mun
 * @version 1.0
 * @since 2015-4-5
 *
 */

public class QwikSearchFragment extends Fragment {


    ViewPager peoplePager;
    FragmentPagerAdapter fragmentPagerAdapter;

    public QwikSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * This method creates the view for the QwikSearch Fragment by calling the fragmentPagerAdapter
     * and instantiating Search By Clinic and Search by Doctor Fragments.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        int p = getActivity().getResources().getColor(R.color.peopleColorPrimary);
        int s = getActivity().getResources().getColor(R.color.peopleColorPrimaryDark);
        SampleApplication.setCustomTheme((ActionBarActivity) getActivity(), p, s);

        fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return (new SearchByClinic());
                    case 1:
                        return (new SearchByDoctor());

                }
                return new CreateAppointmentFragment();
            }

            /**
             * This method creates the title for Search by Doctor and Search by Clinic.
             */
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Search By Clinic";
                    case 1:
                        return "Search By Doctor";

                }

                return null;
            }

            /**
             * This method returns the count of the fragments called by QwikSearchFragment.
             */
            @Override
            public int getCount() {
                return 2;
            }
        };

        peoplePager = (ViewPager) view.findViewById(R.id.people_pager);
        peoplePager.setAdapter(fragmentPagerAdapter);
        peoplePager.setOffscreenPageLimit(3);
        return view;
    }


}