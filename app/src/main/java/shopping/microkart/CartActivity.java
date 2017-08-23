package shopping.microkart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import shopping.microkart.Adapters.CustomCartListAdapter;
import shopping.microkart.Adapters.CustomProductListAdapter;
import shopping.microkart.Classes.ProductClass;

public class CartActivity extends AppCompatActivity {
	private static ArrayList<ProductClass> CartProductList;
	private ArrayList<String> ProductID;
	CustomCartListAdapter adapter;
	private Integer TotalPrice = 0;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);

		/** ENABLING THE HOME BUTTON ON ACTION BAR **/
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		/** FINDING THE RELEVANT ELEMENTS **/
		final ListView cartlistView = (ListView) findViewById(R.id.cartlistView);
		info.hoang8f.widget.FButton CheckoutButton = (info.hoang8f.widget.FButton) findViewById(R.id.Checkoutbutton);
		/** INITIALISATION **/
		CartProductList = new ArrayList<>();
		ProductID = new ArrayList<>();

		/** PROGRESS BAR CODE **/
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setMessage("LOADING CART...");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);
		progress.show();

		/** MAKING A REFERENCE TO THE CART URL IN FIREBASE **/
		DatabaseReference CartRef = FirebaseDatabase.getInstance().getReference().child("Carts");

		CartRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {

				for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
					System.out.println("POST SNAPSHOT IS " + postSnapshot);

					/** RETRIEVING DETAILS OF EACH PRODUCT IN THE FOR LOOP, ADDING THEM TO LIST AND UPDATING THE TOTAL PRICE **/
					ProductClass CartProduct = postSnapshot.getValue(ProductClass.class);
					ProductClass product = new ProductClass(CartProduct.getName()
							, CartProduct.getImage()
							, CartProduct.getprice()
							, CartProduct.getDescription()
							, CartProduct.getSeller()
							, CartProduct.getId()
							, CartProduct.getQuantity());
					TotalPrice = TotalPrice + CartProduct.getprice();
					CartProductList.add(product);
					ProductID.add(product.getId());
				}
				/** SETTING THE ADAPTER IF CARTPRODUCTLIST IS NOT NULL **/
				if (CartProductList.isEmpty()) {
					progress.dismiss();
					/** CUSTOM TOAST MESSAGE **/
					Toast toast = Toast.makeText(CartActivity.this, "", Toast.LENGTH_SHORT);
					View view = toast.getView();
					view.setBackgroundResource(R.drawable.rounded_square);
					TextView text = (TextView) view.findViewById(android.R.id.message);
					text.setText(" YOU GOT TO PUT SOMETHING IN THE CART! ");
					text.setTextColor(getResources().getColor(R.color.colorWhite));
					toast.show();
					Intent intent = new Intent(CartActivity.this, UserActivity.class);
					CartActivity.this.startActivity(intent);
				} else {
					adapter = new CustomCartListAdapter(CartProductList, getLayoutInflater(), CartActivity.this, new CustomProductListAdapter.ButtonClickListener() {

						/**
						 * CODE FOR DELETE BUTTON ON EACH ITEM- DELETING THE PRODUCT FROM FIREBASE AND RELOADING THE ACTIVITY
						 **/
						@Override
						public void onButtonClick(int position, View v) {

							/** MAKING A REFERENCE TO DELETE THE PARTICULAR PRODUCT-ID **/
							DatabaseReference deleteref = FirebaseDatabase.getInstance().getReference().child("Carts").child(ProductID.get(position));
							deleteref.removeValue();
							Snackbar.make(cartlistView, getResources().getString(R.string.Deleted_Cart), Snackbar.LENGTH_LONG)
									.setAction("Action", null).show();
							adapter.notifyDataSetChanged();
							//RELOAD THE ACTIVITY FOR ANY FUTURE CHANGES
							Intent intent = getIntent();
							finish();
							startActivity(intent);
						}
					});
					/** SETTING THE ADAPTER IN THE LIST VIEW **/
					cartlistView.setAdapter(adapter);
				}
			}

			@Override
			public void onCancelled(DatabaseError firebaseError) {
				System.out.println("The read failed: " + firebaseError.getMessage());
			}
		});
		progress.dismiss();


		/**THIS IS THE CODE FOR OPENING UP OF DESCRIPTION ACTIVITY WHEN USER CLICKS THE ITEM IN THE CART**/
		cartlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent DescriptionIntent = new Intent(CartActivity.this, DescriptionActivity.class);
				DescriptionIntent.putExtra("ProductDetails", CartProductList.get(position));
				CartActivity.this.startActivity(DescriptionIntent);
			}
		});

		/** SETTING ONCLICKLISTENER ON THE CHECKOUT BUTTON **/
		CheckoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/** CUSTOM TOAST MESSAGE **/
				Toast toast = Toast.makeText(CartActivity.this, "", Toast.LENGTH_LONG);
				View view = toast.getView();
				view.setBackgroundResource(R.drawable.rounded_square);
				TextView text = (TextView) view.findViewById(android.R.id.message);
				text.setTextColor(getResources().getColor(R.color.colorWhite));
				text.setText(" TOTAL AMOUNT PAYABLE IS Rs " + TotalPrice + " ");
				toast.show();
			}
		});

	}

	/**
	 * FUNCTION TO GET REALTIME CART SIZE IN OFFLINE MODE
	 **/
	public static int getCartListSize() {
		try {
			if (CartProductList.size() != 0) {
				return CartProductList.size();
			} else {
				return 0;
			}
		} catch (NullPointerException e) {
			return 0;
		}
	}

	/**
	 * WHEN CLICKED ON BACK BUTTON
	 **/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// BACK CLICKED. GO TO HOME.
				Intent intent = new Intent(this, UserActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				//FINISH THE CURRENT ACTIVITY
				this.finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}