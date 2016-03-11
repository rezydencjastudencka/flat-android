package pl.maxmati.tobiasz.flat.shopping;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.HashSet;

import pl.maxmati.tobiasz.flat.api.shopping.PendingOrder;
import pl.maxmati.tobiasz.flat.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PendingOrdersFragment extends Fragment {
    private OnPendingOrderActionListener actionListener;
    private ArrayAdapter<PendingOrder> pendingOrdersAdapter;

    private HashSet<PendingOrder> selectedOrders;

    public PendingOrdersFragment() {
    }

    private PendingOrder[] getDummyPendingOrders() {
        return new PendingOrder[] {
                new PendingOrder(1, new Date(), 1, "Nice product to buy", 0, "must be " +
                        "awesome", 9.11, 3),
                new PendingOrder(2, new Date(), 2, "Much vodka", 1, "at least 98 volt", 6.44,
                        1)
        };
    }

    @TargetApi(23)
    @Override public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < 23) {
            onAttachToContext(activity);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedOrders = new HashSet<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_orders, container, false);
        ListView pendingOrderListView = (ListView) view.findViewById(R.id.pending_orders_listview);
        pendingOrderListView.setAdapter(pendingOrdersAdapter);
        return view;
    }

    private void onAttachToContext(Context context) {
        try {
            actionListener = (OnPendingOrderActionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnQuantityActionListener");
        }

        pendingOrdersAdapter = new ArrayAdapter<PendingOrder>(context, R.layout.pending_order,
                R.id.pending_order_title_textview, getDummyPendingOrders()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                PendingOrder pendingOrder = getItem(position);
                CardView frameLayout = (CardView) view.findViewById(R.id.pending_order_framelayout);

                frameLayout.setUseCompatPadding(true); // FIXME: move to xml

                setViewValues(pendingOrder, frameLayout);
                bindExpandDetailsAction(pendingOrder, frameLayout);
                bindSelectAction(pendingOrder, frameLayout);
                setPriorityColor(pendingOrder, frameLayout);

                return view;
            }
        };
    }

    private void setPriorityColor(PendingOrder pendingOrder, CardView frameLayout) {
        /*int priorityColor;
        switch (pendingOrder.getPriority()) {
            case PendingOrder.PRIORITY_LOW:
                priorityColor = getResources().getColor(R.color.priorityLow);
                break;
            case PendingOrder.PRIORITY_NORMAL:
                priorityColor = getResources().getColor(R.color.priorityNormal);
                break;
            case PendingOrder.PRIORITY_HIGH:
                priorityColor = getResources().getColor(R.color.priorityHigh);
                break;
            default:
                priorityColor = Color.DKGRAY;
        }*/
        int priorityColor = getResources().getColor(Priority.getPredefined()[pendingOrder
                .getPriority()].getColorResId());
        frameLayout.setCardBackgroundColor(priorityColor);
    }

    private void setViewValues(PendingOrder pendingOrder, CardView frameLayout) {
        ((TextView) frameLayout.findViewById(R.id.pending_order_user_textview))
                .setText(pendingOrder.getUser());
        ((TextView) frameLayout.findViewById(R.id.pending_order_quantity_textview))
                .setText(String.format("%d", pendingOrder.getQuantity()));
    }

    private void bindExpandDetailsAction(final PendingOrder pendingOrder, final CardView cardView) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedOrders.isEmpty()) {
                    View innerView = view.findViewById(R.id
                            .pending_order_details_inflated_viewstub);
                    if (innerView == null) {
                        ((ViewStub) view.findViewById(R.id.pending_order_details_viewstub))
                                .inflate();
                        ((TextView) view.findViewById(R.id.pending_order_details_price_textview))
                                .setText(String.format("%.2f PLN", pendingOrder.getPrice()));
                        ((TextView) view
                                .findViewById(R.id.pending_order_details_description_textview))
                                .setText(pendingOrder.getDescription());
                        ((TextView) view.findViewById(R.id.pending_order_details_date_textview))
                                .setText(pendingOrder.getCreationDate().toString());
                    } else {
                        innerView.setVisibility(innerView.getVisibility() == View.GONE
                                ? View.VISIBLE : View.GONE);
                    }
                } else {
                    toggleOrderSelection(pendingOrder, cardView);
                }
            }
        });
    }

    private void bindSelectAction(final PendingOrder pendingOrder, final CardView cardView) {
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                toggleOrderSelection(pendingOrder, cardView);
                return true;
            }
        });
    }

    private void toggleOrderSelection(PendingOrder pendingOrder, CardView cardView) {
        View innerView = cardView.findViewById(R.id.pending_order_inner_framelayout);
        if (selectedOrders.contains(pendingOrder)) {
            setPriorityColor(pendingOrder, cardView);
            innerView.setBackgroundColor(Color.WHITE); // FIXME: get color from style
            selectedOrders.remove(pendingOrder);
        } else {
            cardView.setCardBackgroundColor(
                    getResources().getColor(R.color.colorPrimaryDark));
            innerView.setBackgroundColor(Color.parseColor("#e3f2fd"));
            selectedOrders.add(pendingOrder);
        }
        actionListener.orderSelected(pendingOrder);
    }

    public HashSet<PendingOrder> getSelectedOrders() {
        return selectedOrders;
    }

    public interface OnPendingOrderActionListener {
        void orderSelected(PendingOrder order);
    }
}
