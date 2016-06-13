package ru.novikov.novikovthetvdb.Login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Ivan on 13.06.2016.
 */
public class AuthOnGoogle {

    private final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/tasks";
    private final String ACCOUNT_TYPE = "com.google";
    private final String TAG = "AuthOnGoogle";

    private Activity activity;

    public AuthOnGoogle(Activity activity) {
        this.activity = activity;
    }

    public void getGoogleToken() {
        AccountManager am = AccountManager.get(activity); // current Context

        Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
        for (Account account : accounts) {
            Log.d(TAG, account.name + "\n");

            am.getAuthToken(account, AUTH_TOKEN_TYPE, null, activity, new AccountManagerCallback<Bundle>() {
                public void run(AccountManagerFuture<Bundle> future) {
                    try {
                        // If the user has authorized your application to use the tasks API
                        // a token is available.
                        String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                        // Now you can use the Tasks API...
                        Log.d(TAG, token);
                    } catch (OperationCanceledException e) {
                        // TODO: The user has denied you access to the API, you should handle that
                    } catch (Exception e) {
                        //handleException(e);
                    }
                }
            }, null);
        }

        if (accounts.length == 0){

        }
    }

    /*
    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }
*/
}
