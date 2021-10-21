package com.example.novigrad;

import android.view.View;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class FindBranchbyhoursTest {
    @Rule
    public ActivityTestRule<FindBranch> findBranchActivityTestRule= new ActivityTestRule<FindBranch>(FindBranch.class);
    private FindBranch mFindBranch = null;


    @Before
    public void setUp() throws Exception {
        mFindBranch = findBranchActivityTestRule.getActivity();

    }
    @Test
    public void testLaunch(){
        View view = mFindBranch.findViewById(R.id.findByHours);
        assertNotNull(view);
    }
}