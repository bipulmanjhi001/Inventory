package com.broadwaybazar.drawer;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.broadwaybazar.R;
import com.broadwaybazar.api.URL;
import com.broadwaybazar.model.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Register extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SHARED_PREF_NAME = "Inventorypref";
    ListView Item;
    String token, getId;
    EditText state, name, address, mobile, email, gst, remarks;
    String states, names, addresss, mobiles, emails, gsts, remarkss;
    Button select_area, submit_register;
    ArrayList item_ids = new ArrayList();
    ArrayList item_names = new ArrayList();
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Register() {
    }

    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_form, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        AreaList();
        state = view.findViewById(R.id.state);
        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        mobile = view.findViewById(R.id.mobile);
        email = view.findViewById(R.id.email);
        gst = view.findViewById(R.id.gst);
        remarks = view.findViewById(R.id.remarks);

        select_area = view.findViewById(R.id.select_area);
        select_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupArea(v);
            }
        });
        submit_register = view.findViewById(R.id.submit_register);
        submit_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
        return view;
    }

    private void attemptRegister() {

        states = state.getText().toString();
        names = name.getText().toString();
        addresss = address.getText().toString();
        mobiles = mobile.getText().toString();
        emails = email.getText().toString();
        gsts = gst.getText().toString();
        remarkss = remarks.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(names)) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        }
        if (TextUtils.isEmpty(addresss)) {
            address.setError(getString(R.string.error_field_required));
            focusView = address;
            cancel = true;
        }
        if (TextUtils.isEmpty(states)) {
            state.setError(getString(R.string.error_field_required));
            focusView = state;
            cancel = true;
        }
        if (TextUtils.isEmpty(mobiles)) {
            mobile.setError(getString(R.string.error_field_required));
            focusView = mobile;
            cancel = true;
        }
        if (TextUtils.isEmpty(emails)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        }
        if (TextUtils.isEmpty(gsts)) {
            gst.setError(getString(R.string.error_field_required));
            focusView = gst;
            cancel = true;
        }
        if (cancel) {

            focusView.requestFocus();

        } else {
            AddCustomer();
        }
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

                                JSONArray userJson = obj.getJSONArray("area");
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
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

                state.setText(item_names.get(position).toString());
                getId = item_ids.get(position).toString();
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    public void AddCustomer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_ADDCUSTOMER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                state.setText("");
                                name.setText("");
                                address.setText("");
                                mobile.setText("");
                                email.setText("");
                                gst.setText("");
                                remarks.setText("");

                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("name", names);
                params.put("mobile", mobiles);
                params.put("email", emails);
                params.put("gstin", gsts);
                params.put("state", getId);
                params.put("address", addresss);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

