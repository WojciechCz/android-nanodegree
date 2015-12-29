package it.jaschke.alexandria.fragments.callbacks;

import com.google.zxing.integration.android.IntentResult;

/**
 * Created by fares on 12/27/15.
 */
public interface ScanRequest {
    void onScanRequestDone(IntentResult result);
}
