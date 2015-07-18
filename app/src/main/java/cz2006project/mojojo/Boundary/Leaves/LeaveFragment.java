package main.java.cz2006project.mojojo.Boundary.Leaves;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz2006project.mojojo.R;
import main.java.cz2006project.mojojo.Application.SampleApplication;



/**
 * A simple {@link Fragment} subclass.
 */


/**
 * <h1>Leave Fragment</h1>
 * This fragment calls and displays the appointment fragment classes using a switch statement
 *
 * @author Lee Sai Mun
 * @version 1.0
 * @since 2015-4-5
 *
 */

public class LeaveFragment extends Fragment {

    ViewPager appointmentsPager;
    FragmentPagerAdapter fragmentPagerAdapter;


    /**
     * Default constructor for the LeaveFragment
     */
    public LeaveFragment() {
        // Required empty public constructor
    }

    /**
     * This constructor instantiates the fragment class with the arguments.
     * @return fragment
     */

    public static LeaveFragment newInstance() {
        LeaveFragment fragment = new LeaveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method creates the view for the Appointments Fragment by calling the fragmentPagerAdapter
     * and instantiating Upcoming Leave, Create Leave and Past Leave
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        int p = getActivity().getResources().getColor(R.color.eventsColorPrimary);
        int s = getActivity().getResources().getColor(R.color.eventsColorPrimaryDark);
        SampleApplication.setCustomTheme((ActionBarActivity) getActivity(), p, s);

        fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return UpcomingLeaveFragment.newInstance(true);
                    case 1:
                        return new CreateLeaveFragment();
                    case 2:
                        return PastLeaveFragment.newInstance(true);

                }
                return new UpcomingLeaveFragment();
            }

            /**
             * This method creates the title for Upcoming Leave, Create Leave and Past Leave Fragments
             */
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Upcoming Leaves";
                    case 1:
                        return "Enter new Leave";
                    case 2:
                        return "Past leaves";
                }

                return null;
            }

            /**
             * This method returns the count of the fragments called by LeaveFragment.
             */
            @Override
            public int getCount() {
                return 3;
            }
        };

        appointmentsPager = (ViewPager) view.findViewById(R.id.appointments_pager);
        appointmentsPager.setAdapter(fragmentPagerAdapter);
        appointmentsPager.setOffscreenPageLimit(2);
        return view;
    }



}
