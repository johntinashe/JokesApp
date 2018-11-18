package com.udacity.gradle.builditbigger;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class AsyncTaskTest  {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void nonEmpty() {

        EndpointsAsyncTask endpointsAsyncTask = new EndpointsAsyncTask(mActivityTestRule.getActivity(), null, null, null);
        try {
            String joke = endpointsAsyncTask.execute().get();
            assertNotEquals(joke, null);
            Log.d("jokes",joke);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
