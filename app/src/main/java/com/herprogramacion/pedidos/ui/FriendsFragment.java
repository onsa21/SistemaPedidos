package com.herprogramacion.pedidos.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.herprogramacion.pedidos.R;
import com.herprogramacion.pedidos.modelo.Producto;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos;
import com.herprogramacion.pedidos.sqlite.OperacionesBaseDatos;


/**
 * Created by Ravi on 29/07/15.
 */
public class FriendsFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    Context thiscontext;
    View rootView;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public void onStart() {
        super.onStart();
        OperacionesBaseDatos datos = new OperacionesBaseDatos();
        datos.getDb().beginTransaction();
        // poblar el spinner
        Spinner prueba = (Spinner) rootView.findViewById(R.id.spinProducto);
        //ViewClient vc = new ViewClient();
        // SimpleCursorAdapter adapter;
        //Creamos el cursor
        Cursor c = datos.obtenerProductos();
        //Creamos el adaptador
        SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, c, new String[]{"nombre"}, new int[]{android.R.id.text1});
        //Añadimos el layout para el menú
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prueba.setAdapter(adapter2);
        String cant = String.valueOf(prueba.getCount());
       // Toast.makeText(getContext(), "Now onStart() calls " + cant, Toast.LENGTH_LONG).show(); //onStart Called

        //seleccionar el spin
        prueba.setOnItemSelectedListener(this);

        // setear total
        final EditText cantidad =  (EditText) rootView.findViewById(R.id.cantidad);
        final EditText precio =  (EditText) rootView.findViewById(R.id.precio);

        cantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Integer tot = Integer.parseInt(String.valueOf(cantidad.getText())) * Integer.parseInt(String.valueOf(precio.getText()));
                 //   Toast.makeText(getContext(), "lost focus " + tot.toString(), Toast.LENGTH_LONG).show(); //onStart Called;
                    //total
                    EditText total =  (EditText) rootView.findViewById(R.id.total);
                    total.setText(tot.toString(),TextView.BufferType.NORMAL);;
                }
            }
        });



        // accion del boton
        Button buttonClick = (Button) rootView.findViewById(R.id.btnProducto);
        buttonClick.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Fragment fragment = new MessagesFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle("Pedidos");
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        thiscontext = container.getContext();


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView text = (TextView) view;


        OperacionesBaseDatos datos = new OperacionesBaseDatos();
        Cursor c = datos.obtenerProductos();
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros

            int nombreColumn = c.getColumnIndex(ContratoPedidos.Productos.NOMBRE);
            int precioColumn = c.getColumnIndex(ContratoPedidos.Productos.PRECIO);

//Recorremos el cursor
            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                String name = c.getString(nombreColumn);
                String precio = c.getString(precioColumn);

                Log.d("IF: ", text.getText()+" = " +name);
                if(text.getText().equals(name)){
                    Toast.makeText(getContext(), name + " =  " + text.getText(), Toast.LENGTH_LONG).show(); //onStart Called
                    EditText inputprecio =  (EditText) rootView.findViewById(R.id.precio);
                    inputprecio.setText(precio,TextView.BufferType.NORMAL);

                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
