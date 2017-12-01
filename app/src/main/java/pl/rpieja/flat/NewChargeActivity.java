package pl.rpieja.flat;

import android.arch.lifecycle.ViewModelProviders;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import pl.rpieja.flat.viewmodels.NewChargeViewModel;
import android.widget.EditText;

public class NewChargeActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_charge);

        toolbar = findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NewChargeViewModel newChargeViewModel =
                ViewModelProviders.of(this).get(NewChargeViewModel.class);
        newChargeViewModel.loadUsers(getApplicationContext());


        EditText newChargeDate = findViewById(R.id.newChargeDate);
        newChargeDate.setOnFocusChangeListener((view, b) -> {
            if (b) {
                DateDialog dialog = new DateDialog();
                dialog.setEditText(view);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "Set Date");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_charge, menu);
        return true;
    }

}
