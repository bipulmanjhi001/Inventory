package com.broadwaybazar.drawer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.broadwaybazar.R;
import com.broadwaybazar.api.URL;
import com.broadwaybazar.model.CompanyModel;
import com.broadwaybazar.model.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class Order extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SHARED_PREF_NAME = "Inventorypref";
    ListView Item, cust_list;
    String Product_data = "", getId2;

    String token, getId, companyId;
    EditText area, mobile, gstin;
    Button select_area,submit_order, customer_name;

    ArrayList item_ids = new ArrayList();
    ArrayList item_names = new ArrayList();
    ArrayList item_ids2 = new ArrayList();
    ArrayList item_names2 = new ArrayList();

    EditText customer, reference, address;
    ListView company_list;
    CalendarView calendar_issue;
    TextView issue_datetext;

    ArrayList<CompanyModel> companyModels;
    CompanyAdapter companyAdapter;
    LinearLayout list_layout;
    String areas, customers, addresss, mobiles, gstins, issue_datetexts;

    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Order() {
    }

    public static Order newInstance(String param1, String param2) {
        Order fragment = new Order();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_form, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        CustomerList();

        area = view.findViewById(R.id.area);
        customer = view.findViewById(R.id.customers);
        reference = view.findViewById(R.id.reference);
        address = view.findViewById(R.id.address);
        mobile = view.findViewById(R.id.mobile);
        gstin = view.findViewById(R.id.gstin);

        issue_datetext = view.findViewById(R.id.issue_datetext);
        calendar_issue = view.findViewById(R.id.calendar_issue);
        calendar_issue.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date = year+"-"+(month + 1)+"-"+dayOfMonth;
                issue_datetext.setText(date);
            }
        });

        select_area = view.findViewById(R.id.select_area);
        select_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupArea(v);
            }
        });

        customer_name = view.findViewById(R.id.customer_name);
        customer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomer(v);
            }
        });

        list_layout = view.findViewById(R.id.list_layout);
        company_list = view.findViewById(R.id.company_list);
        company_list.setDivider(null);

        LayoutInflater myinflater2 = getLayoutInflater();
        ViewGroup myHeader2 = (ViewGroup) myinflater2.inflate(R.layout.headerlayout2, company_list, false);
        company_list.addHeaderView(myHeader2, null, false);

        companyModels = new ArrayList<CompanyModel>();
        submit_order = view.findViewById(R.id.submit_order);
        submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void AreaList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_AREALIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONArray userJson = obj.getJSONArray("state");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String item_id = itemslist.getString("id");
                                    item_ids.add(item_id);
                                    String item_type = itemslist.getString("name");
                                    item_names.add(item_type);

                                }
                            } else {
                                Toast.makeText(getActivity(), "No supplier added", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void showPopupArea(View view) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.list_dialog);
        Item = dialog.findViewById(R.id.List);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, item_names);
        Item.setAdapter(adapter);
        Item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                area.setText(item_names.get(position).toString());
                getId = item_ids.get(position).toString();
                CompanyList();
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    public void CompanyList() {

        companyModels.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_COMPANY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONArray userJson = obj.getJSONArray("products");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("pname");
                                    String pcode = itemslist.getString("pcode");

                                    CompanyModel companyModel = new CompanyModel(id, name,pcode, false);
                                    companyModels.add(companyModel);
                                }
                            } else {
                                Toast.makeText(getActivity(), "No products added", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {

                            companyAdapter = new CompanyAdapter(companyModels, getActivity());
                            company_list.setAdapter(companyAdapter);
                            companyAdapter.notifyDataSetChanged();
                            ListUtils.setDynamicHeight(company_list);

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void attemptRegister() {

        areas = area.getText().toString();
        customers = customer.getText().toString();
        addresss = address.getText().toString();
        mobiles = mobile.getText().toString();
        gstins = gstin.getText().toString();
        issue_datetexts = issue_datetext.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(issue_datetexts)) {
            Toast.makeText(getActivity(), "Add date", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(customers)) {
            customer.setError(getString(R.string.error_field_required));
            focusView = customer;
            cancel = true;
        }
        if (TextUtils.isEmpty(addresss)) {
            address.setError(getString(R.string.error_field_required));
            focusView = address;
            cancel = true;
        }
        if (TextUtils.isEmpty(mobiles)) {
            mobile.setError(getString(R.string.error_field_required));
            focusView = mobile;
            cancel = true;
        }
        if (TextUtils.isEmpty(gstins)) {
            gstin.setError(getString(R.string.error_field_required));
            focusView = gstin;
            cancel = true;
        }
        if (cancel) {

            focusView.requestFocus();

        } else {
            Final_Submit_Data();
        }
    }

    public void Final_Submit_Data() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_ORDERFINALSUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {
                                Toast.makeText(getActivity(), "Order Success", Toast.LENGTH_SHORT).show();

                                area.setText("");
                                customer.setText("");
                                address.setText("");
                                mobile.setText("");
                                gstin.setText("");
                                issue_datetext.setText("");

                            } else {
                                Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("token", token);
                params.put("name", customers);
                params.put("customer_id", getId2);
                params.put("mobile", mobiles);
                params.put("date", issue_datetexts);
                params.put("gstin", gstins);
                params.put("state", getId);
                params.put("address", addresss);
                params.put("product", Product_data);

                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void CustomerList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_CUSTOMERLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONArray userJson = obj.getJSONArray("customers");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String item_id = itemslist.getString("id");
                                    item_ids2.add(item_id);
                                    String item_type = itemslist.getString("sname");
                                    item_names2.add(item_type);

                                }
                            } else {
                                Toast.makeText(getActivity(), "No Customer added", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        AreaList();

    }

    private void showCustomer(View view) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.customer_dialog);
        cust_list = dialog.findViewById(R.id.List);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, item_names2);
        cust_list.setAdapter(adapter);
        cust_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                customer.setText(item_names2.get(position).toString());
                getId2 = item_ids2.get(position).toString();
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static class ListUtils {

        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();

        }
    }

    public class CompanyAdapter extends BaseAdapter {
        ArrayList<CompanyModel> mylist = new ArrayList<>();
        private Context mContext;

        public CompanyAdapter(ArrayList<CompanyModel> itemArray, Context mContext) {
            super();
            this.mContext = mContext;
            mylist = itemArray;
        }
        @Override
        public int getCount() {
            return mylist.size();
        }
        @Override
        public String getItem(int position) {
            return mylist.get(position).toString();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder view;
            LayoutInflater inflator = null;
            if (convertView == null) {
                view = new ViewHolder();
                try {
                    inflator = ((Activity) mContext).getLayoutInflater();
                    convertView = inflator.inflate(R.layout.company_list, null);
                    view.id = convertView.findViewById(R.id.company_id);
                    view.name = convertView.findViewById(R.id.company_name);
                    view.company_code = convertView.findViewById(R.id.company_code);
                    view.checkBox = convertView.findViewById(R.id.company_check);
                    view.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                try {

                                    companyId = mylist.get(position).getId();

                                } catch (Exception e) {
                                }

                            }
                        }
                    });
                    convertView.setTag(view);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            try {
                view.id.setTag(position);
                view.id.setText(mylist.get(position).getId());
                view.name.setText(mylist.get(position).getName());
                view.company_code.setText(mylist.get(position).getPcode());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return convertView;
        }

        public class ViewHolder {
            private TextView id, name,company_code;
            private CheckBox checkBox;
        }
    }
}
