package shopping.microkart.SellerFragments;

/**
 * Created by brainbreaker.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import shopping.microkart.Adapters.CustomProductListAdapter;
import shopping.microkart.Adapters.DemandListAdapter;
import shopping.microkart.Classes.DemandProductClass;
import shopping.microkart.R;
import shopping.microkart.SellerActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserDemandsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    ArrayList<DemandProductClass> DemandProductList = new ArrayList<>();
    public UserDemandsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static UserDemandsFragment newInstance() {
        UserDemandsFragment fragment = new UserDemandsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((SellerActivity) getActivity())
                .setActionBarTitle("USER DEMANDS");
        View rootView = inflater.inflate(R.layout.demand_fragment, container, false);


        final ListView demandlist = (ListView) rootView.findViewById(R.id.demandlistView);

        /**PROGRESS BAR**/
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("LOADING USER DEMANDS...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        //POPULATING DEMAND PRODUCT LIST FROM FIREBASE. IT CAN ONLY BE UPDATED FROM WEBSITE YET
        DatabaseReference ProductDemandRef = FirebaseDatabase.getInstance().getReference().child("demands");
        ProductDemandRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final DemandProductClass product = dataSnapshot.getValue(DemandProductClass.class);
                DemandProductList.add(product);
                DemandListAdapter adapter = new DemandListAdapter(DemandProductList, inflater, getActivity(), new CustomProductListAdapter.ButtonClickListener() {
                    @Override
                    public void onButtonClick(int position, View view) {
//                        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.container);
//                        viewPager.setCurrentItem(2,true);
//                        TextView ProductName = (TextView) viewPager.findViewById(R.id.demandproductname);
//                        ProductName.setText(product.getName());
                    }
                });
                demandlist.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

        return rootView;
    }
}