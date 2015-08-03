/* package main.java.cz2006project.mojojo.Boundary.Timetable;

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
import main.java.cz2006project.mojojo.Boundary.Timetable.CreateTimetableFragment;
import main.java.cz2006project.mojojo.Boundary.Timetable.ListTimetableFragment;

/**
 * Created by Deeksha on 03-08-2015.

/*public class TimetableFragment extends Fragment
{
    ViewPager timetablePager;
    FragmentPagerAdapter fragmentPagerAdapter;

*/

    /**
     * Default constructor for the LeaveFragment

    public TimetableFragment() {
        // Required empty public constructor
    }

    /**
     * This constructor instantiates the fragment class with the arguments.
     * @return fragment


    public static TimetableFragment newInstance() {
        TimetableFragment fragment = new TimetableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method creates the view for the Appointments Fragment by calling the fragmentPagerAdapter
     * and instantiating Upcoming Leave, Create Leave and Past Leave

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        int p = getActivity().getResources().getColor(R.color.eventsColorPrimary);
        int s = getActivity().getResources().getColor(R.color.eventsColorPrimaryDark);
        SampleApplication.setCustomTheme((ActionBarActivity) getActivity(), p, s);

        fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return ListTimetableFragment.newInstance(true);
                    case 1:
                        return new CreateTimetableFragment();


                }
                return new ListTimetableFragment();
            }

            /**
             * This method creates the title for Upcoming Leave, Create Leave and Past Leave Fragments

            @Override
            private CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Timetable list";
                    case 1:
                        return "Enter Timetable";

                }

                return null;
            }

            /**
             * This method returns the count of the fragments called by LeaveFragment.

            @Override
            public int getCount() {
                return 2;
            }
        };

        timetablePager = (ViewPager) view.findViewById(R.id.timetable_pager);
        timetablePager.setAdapter(fragmentPagerAdapter);
      timetablePager.setOffscreenPageLimit(2);
        return view;
    }



}
     */