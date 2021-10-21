package com.example.novigrad;

import android.view.View;
import android.widget.TextView;
import androidx.test.rule.ActivityTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class UpdateServicesTest {

        @Rule
        public ActivityTestRule<UpdateServices> updateServicesActivityTestRule= new ActivityTestRule<UpdateServices>(UpdateServices.class);
        private UpdateServices mUpdateServicesActivity =null;
        private TextView text;

        @Before
        public void setUp() throws Exception {
            mUpdateServicesActivity = updateServicesActivityTestRule.getActivity();

        }
        @Test
        public void testLaunch(){
            View view = mUpdateServicesActivity.findViewById(R.id.updateBtn);
            assertNotNull(view);
        }
    }