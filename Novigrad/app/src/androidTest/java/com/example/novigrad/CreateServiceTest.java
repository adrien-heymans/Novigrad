package com.example.novigrad;
import android.view.View;
import android.widget.TextView;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CreateServiceTest {
    @Rule
    public ActivityTestRule<CreateService> createServiceTestActivityTestRule= new ActivityTestRule<CreateService>(CreateService.class);
    private CreateService mCreateService= null;
    private TextView text;
    @Before
    public void setUp() throws Exception {
        mCreateService = createServiceTestActivityTestRule.getActivity();
    }
    @Test
    @UiThreadTest
    public void  createServiceLaunch()throws  Exception{
        View view = mCreateService.findViewById(R.id.textView5);
        assertNotNull(view);
    }
}