package com.example.novigrad;

import android.view.View;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckStatusTest {
    @Rule
    public ActivityTestRule<CheckStatus> checkStatusActivityTestRule= new ActivityTestRule<CheckStatus>(CheckStatus.class);
    private CheckStatus mCheckStatus = null;


    @Before
    public void setUp() throws Exception {
        mCheckStatus = checkStatusActivityTestRule.getActivity();

    }
    @Test
    public void testLaunch(){
        View view = mCheckStatus.findViewById(R.id.textView30);
        assertNotNull(view);
    }
}