package com.herprogramacion.pedidos.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.herprogramacion.pedidos.R;
import com.herprogramacion.pedidos.sqlite.OperacionesBaseDatos;


/**
 * Created by Ravi on 29/07/15.
 */
public class MessagesFragment extends Fragment {
    Context thiscontext;
    View rootView;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        thiscontext = container.getContext();


        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();


        // accion del boton
        Button buttonClick = (Button) rootView.findViewById(R.id.btnAtras);
        buttonClick.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Fragment fragment = new FriendsFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle("Productos");
                }
            }
        });

        // accion del boton
        Button buttonClick2 = (Button) rootView.findViewById(R.id.btnFinlaizar);
        buttonClick2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Fragment fragment = new HomeFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle("Clientes");
                }
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
