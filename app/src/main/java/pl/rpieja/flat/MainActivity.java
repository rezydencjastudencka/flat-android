package pl.rpieja.flat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button signInButton;
    private EditText passwordTextEdit, usernameTextEdit;

    public void onSignInButtonClick(View view){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInButton=(Button) findViewById(R.id.signInButton);
        usernameTextEdit=(EditText) findViewById(R.id.usernameTextEdit);
        passwordTextEdit=(EditText) findViewById(R.id.passwordTextEdit);

        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                APILogIn apiLogIn;

                String username = usernameTextEdit.getText().toString();
                String password = passwordTextEdit.getText().toString();
                String apiURL= "https://api.flat.memleak.pl/session/create";
                //String apiURL= "http://j3b.tobiasz.maxmati.pl:8080";

                if(username.equals("") || password.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Username or password empty.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    apiLogIn=new APILogIn(MainActivity.this.getApplicationContext());
                    apiLogIn.execute(apiURL, username, password);
                }

            }
        });
    }



}
