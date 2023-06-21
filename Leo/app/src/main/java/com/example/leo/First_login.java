package com.example.leo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.leo.bubblenavigation.BubbleNavigationLinearView;
import com.example.leo.bubblenavigation.listener.BubbleNavigationChangeListener;

public class First_login extends AppCompatActivity {
    Fragment selectedFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        BubbleNavigationLinearView bubbleNavigation = findViewById(R.id.bubbleNavigationStudent);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle b = new Bundle();
        RepelFragment repelFragment=new RepelFragment();
        repelFragment.setArguments(b);
        fragmentTransaction.replace(R.id.student_fragment_container,repelFragment).commit();



//        getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
//                new ProfileFragmentStudent()).commit();

        bubbleNavigation.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                switch (position) {
                    case 0:
                        selectedFragment = new RepelFragment();
                        break;
                    case 1:
                        selectedFragment = new MapsFragment();
                        break;
                    case 2:
                        selectedFragment = new FeedFragment();
                        break;

                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.student_fragment_container,
//                        selectedFragment).commit();

                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();

                selectedFragment.setArguments(b);
                fragmentTransaction1.replace(R.id.student_fragment_container,selectedFragment).commit();


            }
        });

    }
    }
