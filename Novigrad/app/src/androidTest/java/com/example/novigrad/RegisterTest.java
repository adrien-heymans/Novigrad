package com.example.novigrad;

import android.view.View;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterTest {
    @Rule
    public ActivityTestRule<Register> registerActivityTestRule= new ActivityTestRule<Register>(Register.class);
    private Register mRegister = null;
    private TextView text;

    @Before
    public void setUp() throws Exception {
        mRegister = registerActivityTestRule.getActivity();

    }
    @Test
    public void testLaunch(){
        View view = mRegister.findViewById(R.id.firstName);
        assertNotNull(view);
    }
}