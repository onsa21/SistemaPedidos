package com.herprogramacion.pedidos.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.herprogramacion.pedidos.modelo.CabeceraPedido;
import com.herprogramacion.pedidos.modelo.Cliente;
import com.herprogramacion.pedidos.modelo.DetallePedido;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos;
import com.herprogramacion.pedidos.sqlite.OperacionesBaseDatos;

import java.util.Calendar;


/**
 * Created by Ravi on 29/07/15.
 */
public class ProductoFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    Context thiscontext;
    View rootView;
    String idcliente;
    String idcabecera;
    Integer putprecio;
    Integer putcantidad;
    Integer puttotal;
    Integer secuencia;
    String fechaActual = Calendar.getInstance().getTime().toString();

    public ProductoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idcliente  = getArguments().getString("idcliente");
        idcabecera  = getArguments().getString("cabecera");
        secuencia  = getArguments().getInt("secuencia");



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
                    if(cantidad.getText() != null){
                        Integer tot = Integer.parseInt(String.valueOf(cantidad.getText())) * Integer.parseInt(String.valueOf(precio.getText()));
                        //   Toast.makeText(getContext(), "lost focus " + tot.toString(), Toast.LENGTH_LONG).show(); //onStart Called;
                        //total
                        EditText total =  (EditText) rootView.findViewById(R.id.total);
                        total.setText(tot.toString(),TextView.BufferType.NORMAL);
                    }
                    ;
                }
            }
        });



        // accion del boton
        Button buttonClick = (Button) rootView.findViewById(R.id.btnProducto);
        buttonClick.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Fragment fragment = new PedidosFragment();

                //guardar el cliente seleccionado
                String idproducto = null;
                //obtener texto del spinner
                Spinner spinner =  (Spinner) rootView.findViewById(R.id.spinProducto);
                TextView textView = (TextView)spinner.getSelectedView();
                String result = textView.getText().toString();

                //recorrer la base de datos
                OperacionesBaseDatos datos = new OperacionesBaseDatos();
                Cursor c = datos.obtenerProductos();
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros

                    int idColumn = c.getColumnIndex(ContratoPedidos.Productos.ID);
                    int nombreColumn = c.getColumnIndex(ContratoPedidos.Productos.NOMBRE);

                    //Recorremos el cursor
                    for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                        String name = c.getString(nombreColumn);
                        String id = c.getString(idColumn);

                        Log.d("IF: ", result+" = " +name);
                        if(result.equals(name)){
                            idproducto = id;
                            Toast.makeText(getContext(), name + " =  " +  result, Toast.LENGTH_LONG).show(); //onStart Called
                        }
                    }
                }

                 EditText p =  (EditText) rootView.findViewById(R.id.precio);
                EditText can =  (EditText) rootView.findViewById(R.id.cantidad);
                EditText t =  (EditText) rootView.findViewById(R.id.total);
                putprecio = Integer.parseInt(String.valueOf(p.getText()));
                putcantidad = Integer.parseInt(String.valueOf(can.getText()));
                puttotal = Integer.parseInt(String.valueOf(t.getText()));

                //crear cabecera pediddo
                String pedido1 =null;
                if(idcabecera.equals("0")){
                     pedido1 = datos.insertarCabeceraPedido( new CabeceraPedido(null, fechaActual, idcliente, String.valueOf(t.getText())));
                }else{
                     pedido1 = idcabecera;
                }


                // Inserción Detalles
                datos.insertarDetallePedido(new DetallePedido(pedido1, secuencia, idproducto, Integer.parseInt(String.valueOf(can.getText())), Float.parseFloat(String.valueOf(p.getText()))));

                Bundle parametro = new Bundle();
                parametro.putString("idcliente" , idcliente);
                parametro.putString("pedido" , pedido1);


                fragment.setArguments(parametro);

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
                  //  Toast.makeText(getContext(), name + " =  " + text.getText(), Toast.LENGTH_LONG).show(); //onStart Called
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
