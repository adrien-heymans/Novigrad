package com.example.novigrad;
import android.view.View;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

//import junit.framework.TestCase;

public class LoginTest {
    @Rule
    public ActivityTestRule<Login> loginActivityTestRule= new ActivityTestRule<Login>(Login.class);
    private Login mLogin = null;
    private TextView text;

    @Before
    public void setUp() throws Exception {
        mLogin = loginActivityTestRule.getActivity();

    }
    @Test
    public void testLaunch(){
        View view = mLogin.findViewById(R.id.mainLayout);
        assertNotNull(view);
    }
}
