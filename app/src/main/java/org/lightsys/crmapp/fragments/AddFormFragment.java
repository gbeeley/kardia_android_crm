package org.lightsys.crmapp.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.lightsys.crmapp.R;
import org.lightsys.crmapp.data.LocalDBTables;

import java.text.DateFormat;
import java.util.Date;


/**
 * Created by otter57 on 9/14/17.
 *
 * Allows a user to create a form.
 *
 */
public class AddFormFragment extends Fragment{
    private static final String LOG_TAG = AddFormFragment.class.getName();

    TextView descriptionTxt;
    Spinner typeSpinner, universitySpinner, eventSpinner;
    String mType, mUniversity, mEvent, mDescription;
    int formId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        formId = getArguments().getInt(FormFragment.FORM_ID);
        View rootView = inflater.inflate(R.layout.fragment_form_input, container, false);
        getActivity().setTitle("Create form");

        // Sets up views.
        //todo getOptions from Database?
        typeSpinner = (Spinner)rootView.findViewById(R.id.typeSpinner);
        universitySpinner = (Spinner)rootView.findViewById(R.id.universitySpinner);
        eventSpinner = (Spinner)rootView.findViewById(R.id.eventSpinner);
        descriptionTxt = (TextView)rootView.findViewById(R.id.descriptionET);

        Button submit = (Button) rootView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mType = typeSpinner.getSelectedItem().toString();
                mUniversity = universitySpinner.getSelectedItem().toString();
                mEvent = eventSpinner.getSelectedItem().toString();
                mDescription = descriptionTxt.getText().toString();


                //submitOnClick();
                addFormToDB();
                openForm(formId);
            }
        });

        return rootView;
    }

    private void addFormToDB(){

        String stringDate = (android.text.format.DateFormat.format("MM-dd\nyyyy", new java.util.Date())).toString();

        ContentValues values = new ContentValues();
        //todo let id be assigned by server
        values.put(LocalDBTables.FormTable.FORM_ID, formId);
        values.put(LocalDBTables.FormTable.FORM_UNIVERSITY, mUniversity);
        values.put(LocalDBTables.FormTable.FORM_TYPE, mType);
        values.put(LocalDBTables.FormTable.FORM_EVENT, mEvent);
        values.put(LocalDBTables.FormTable.FORM_DATE, stringDate);
        values.put(LocalDBTables.FormTable.FORM_DESC, mDescription);
        getActivity().getContentResolver().insert(LocalDBTables.FormTable.CONTENT_URI, values);

    }

    public void openForm(int formId){
        FormFragment newFrag = new FormFragment();

        Bundle args = new Bundle();
        args.putInt(FormListFragment.FORM_ID, formId);

        newFrag.setArguments(args);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_profile_input_container, newFrag, "Form");
        transaction.addToBackStack("Form");
        transaction.commit();
    }

}