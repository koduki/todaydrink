package pascal.orz.cn.todaytrink;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private int REQUEST_CODE = 0;
    private String token = "";
    private String account_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            // クリック時に呼ばれるメソッド
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "hello",
                        Toast.LENGTH_SHORT).show();

                AccountManager manager = AccountManager.get(LoginActivity.this);
                manager.getAuthToken(new Account(account_name, "com.google"), "54950899978-ke6k6oq5m76bf3b8vfftgn0ncuu9pjo1.apps.googleusercontent.com", null,
                        LoginActivity.this, new AccountManagerCallback<Bundle>() {
                            @Override
                            public void run(AccountManagerFuture<Bundle> future) {
                                Bundle bundle = null;
                                try {
                                    bundle = future.getResult();
                                    String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                                    String accountType = bundle.getString(AccountManager.KEY_ACCOUNT_TYPE);
                                    String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                                    Log.d("hoge", "name=" + accountName);
                                    Log.d("hoge", "type=" + accountType);
                                    Log.d("hoge", "token" + authToken);
                                    token = authToken;
                                    Toast.makeText(LoginActivity.this, bundle.getString(AccountManager.KEY_AUTHENTICATOR_TYPES), Toast.LENGTH_SHORT).show();
                                } catch (OperationCanceledException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (AuthenticatorException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, null);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Firebase firebase = new Firebase("https://shining-heat-6127.firebaseio.com/");
                firebase.authWithOAuthToken("google", token, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        // Authenticated successfully with payload authData
                        Toast.makeText(getApplication(), "success: " + authData.getUid(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        // Authenticated failed with error firebaseError
                        Toast.makeText(getApplication(), "error:" + firebaseError.getCode(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        Context context = getApplicationContext();
        AccountManager manager = AccountManager.get(context);
        Account[] accountArray = manager.getAccountsByType("com.google");

        Intent intent = AccountManager.get(this).newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null,
                null, null, null);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
                account_name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Toast.makeText(this, account_name, Toast.LENGTH_SHORT).show();
        }
    }
}
