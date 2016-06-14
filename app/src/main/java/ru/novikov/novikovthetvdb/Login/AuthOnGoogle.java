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
import android.widget.Toast;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.R;
import ru.novikov.novikovthetvdb.SeriesApp;

public class AuthOnGoogle {

    private static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/tasks";
    private static final String ACCOUNT_TYPE = "com.google";
    private static final String DUMMY_ACCOUNT_NAME = "UnknownAccount";
    private static final String TAG = "AuthOnGoogle";

    private Activity activity;
    private String googleToken = null;
    private String accountName = null;

    private SeriesApp getApp(){
        return (SeriesApp) activity.getApplication();
    }

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
                        googleToken = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                        accountName = future.getResult().getString(AccountManager.KEY_ACCOUNT_NAME);
                        getApp().getDataProvider().updateGoogleName(accountName);

                        // Now you can use the Tasks API...
                        callBackMessage(activity.getString(R.string.google_authrized));
                    } catch (OperationCanceledException e) {
                        // The user has denied you access to the API
                        callBackMessage(activity.getString(R.string.google_authrized_denied));
                    } catch (Exception e) {
                        //handleException(e);
                    }
                }
            }, null);
        }

        if (accounts.length == 0){
            getApp().getDataProvider().updateGoogleName(DUMMY_ACCOUNT_NAME);
            callBackMessage(activity.getString(R.string.google_acc_not_found));
        }
    }

    private void callBackMessage(String message){
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }
}
