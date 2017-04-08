package com.liisa.chatbotapp;

import android.content.res.Resources;
import android.test.ActivityTestCase;
import android.test.AndroidTestRunner;
import android.test.suitebuilder.annotation.LargeTest;

import com.chatbotapp.MambaWebApi;
import com.chatbotapp.mambaObj.SearchResult;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by aossa on 08.04.2017.
 */

@LargeTest
public class MambaWebAPI_Test {

    public static final double DEFAULT_LAT = 53.599815d;

    public static final double DEFAULT_LON = 9.933121d;

    private String cookie;

    @Before
    public void init() {
      cookie= Resources.getSystem().getString(R.string.cookie);
    }


    @Test
    public void mambaWebApi_() throws Exception {
        MambaWebApi webApi = new MambaWebApi(cookie);
        SearchResult res = null;

        try {
            webApi.search("active", "", DEFAULT_LAT, DEFAULT_LON);
        } catch (Exception e) {
        }

        assertNotNull(res);
    }

}
