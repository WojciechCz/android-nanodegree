package com.udacity.gradle.builditbigger;

import android.app.Instrumentation;
import android.os.ConditionVariable;
import android.test.AndroidTestCase;

import java.util.List;

/**
 * Created by fares on 03.01.16.
 */
public class EndpointTaskTest extends AndroidTestCase {

    private EndpointTask mEndpointTask;
    private ConditionVariable mWaitForResponseBlock;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mWaitForResponseBlock = new ConditionVariable();
        mEndpointTask = new EndpointTask(
                new Instrumentation().getContext(),
                new EndpointTask.EndpointTaskCallback() {
                    @Override
                    public void onJokesDownloaded(List<String> jokes) {
                        assertTrue(jokes != null);
                        assertTrue(!jokes.isEmpty());
                        mWaitForResponseBlock.open();
                    }
                },
                null);
    }

    public void testServerResponse(){
        mEndpointTask.execute();
        mWaitForResponseBlock.block();
    }
}
