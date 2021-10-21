package com.example.novigrad;

import android.view.View;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class RemoveServiceTest {

    @Rule
    public ActivityTestRule<RemoveService> RemoveServiceActivityTestRule= new ActivityTestRule<RemoveService>(RemoveService.class);
    private RemoveService mRemoveService = null;
    private TextView text;

    @Before
    public void setUp() throws Exception {
        mRemoveService = RemoveServiceActivityTestRule.getActivity();

    }
    @Test
    public void testLaunch(){
        View view = mRemoveService.findViewById(R.id.textView4);
        assertNotNull(view);
    }
}