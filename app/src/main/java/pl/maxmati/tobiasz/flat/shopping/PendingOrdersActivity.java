package pl.maxmati.tobiasz.flat.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;

import pl.maxmati.tobiasz.flat.api.shopping.PendingOrder;
import pl.maxmati.tobiasz.flat.R;


public class PendingOrdersActivity extends AppCompatActivity implements PendingOrdersFragment.OnPendingOrderActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pending_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        new Intent(PendingOrdersActivity.this, AddPendingOrderActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        PendingOrdersFragment pendingOrdersFragment = (PendingOrdersFragment) getFragmentManager()
                .findFragmentById(R.id.pending_orders_fragment);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pending_orders, menu);
        if(pendingOrdersFragment != null)
            menu.findItem(R.id.action_remove).setVisible(!pendingOrdersFragment.getSelectedOrders()
                    .isEmpty());
        return true;
    }

    @Override
    public void orderSelected(PendingOrder order) {
        invalidateOptionsMenu();
    }
}
