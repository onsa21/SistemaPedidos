package com.herprogramacion.pedidos.ui;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
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
import com.herprogramacion.pedidos.modelo.Producto;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos;
import com.herprogramacion.pedidos.sqlite.OperacionesBaseDatos;

import java.util.Calendar;


public class ClienteFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    String selection;
    OperacionesBaseDatos datos;
    Context thiscontext;
    View rootView;
    String idcliente = "0";

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class TareaPruebaDatos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            // [INSERCIONES]
            String fechaActual = Calendar.getInstance().getTime().toString();

            try {

                datos.getDb().beginTransaction();

                // Inserción Clientes
                String cliente1 = datos.insertarCliente(new Cliente(null, "Veronica", "Del Topo", "4552000","Av. Brasil"));
                String cliente2 = datos.insertarCliente(new Cliente(null, "Carlos", "Villagran", "4440000","Av. Mcal Lopez"));



                // Inserción Productos
                String producto1 = datos.insertarProducto(new Producto(null, "Manzana", 10000, 10));
                String producto2 = datos.insertarProducto(new Producto(null, "Pera", 20000, 20));
                String producto3 = datos.insertarProducto(new Producto(null, "naranja", 30000, 30));
                String producto4 = datos.insertarProducto(new Producto(null, "Maní", 40000, 40));

                // Inserción Pedidos
           //     String pedido1 = datos.insertarCabeceraPedido(
            //            new CabeceraPedido(null, fechaActual, cliente1, "1000"));
            //    String pedido2 = datos.insertarCabeceraPedido(
                 //       new CabeceraPedido(null, fechaActual,cliente2, "20000"));

                // Inserción Detalles
              //  datos.insertarDetallePedido(new DetallePedido(pedido1, 1, producto1, 5, 2));
              //  datos.insertarDetallePedido(new DetallePedido(pedido1, 2, producto2, 10, 3));
              //  datos.insertarDetallePedido(new DetallePedido(pedido2, 1, producto3, 30, 5));
              //  datos.insertarDetallePedido(new DetallePedido(pedido2, 2, producto4, 20, 1));

                // Eliminación Pedido
                // datos.eliminarCabeceraPedido(pedido1);

                // Actualización Cliente
                //  datos.actualizarCliente(new Cliente(cliente2, "Carlos Alberto", "Villagran", "3333333", "sdfsd"));

                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }

            // [QUERIES]
            Log.d("Clientes","Clientes");
            DatabaseUtils.dumpCursor(datos.obtenerClientes());
            Log.d("Productos", "Productos");
            DatabaseUtils.dumpCursor(datos.obtenerProductos());
            Log.d("Cabeceras de pedido", "Cabeceras de pedido");
            DatabaseUtils.dumpCursor(datos.obtenerCabecerasPedidos());
            Log.d("Detalles de pedido", "Detalles de pedido");
            DatabaseUtils.dumpCursor(datos.obtenerDetallesPedido());

            return null;
        }
    }

    public ClienteFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public void onStart(){
        super.onStart();




        // accion del boton
        Button buttonClick = (Button) rootView.findViewById(R.id.btnCliente);
        buttonClick.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Fragment fragment = new ProductoFragment();

                //guardar el cliente seleccionado

                //obtener texto del spinner
                Spinner spinner =  (Spinner) rootView.findViewById(R.id.spinCliente);
                TextView textView = (TextView)spinner.getSelectedView();
                String result = textView.getText().toString();

                //recorrer la base de datos
                Cursor c = datos.obtenerClientes();
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros

                    int idColumn = c.getColumnIndex(ContratoPedidos.Clientes.ID);
                    int nombreColumn = c.getColumnIndex(ContratoPedidos.Clientes.NOMBRES);

                    //Recorremos el cursor
                    for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                        String name = c.getString(nombreColumn);
                        String id = c.getString(idColumn);

                        Log.d("IF: ", result+" = " +name);
                        if(result.equals(name)){
                            idcliente = id;

                        }
                    }
                }

                Bundle parametro = new Bundle();
                 parametro.putString("idcliente" , idcliente);
                parametro.putString("cabecera" , "0");
                parametro.putInt("secuencia" , 1);
                fragment.setArguments(parametro);

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_cliente, container, false);
        thiscontext = container.getContext();

        // creacion base de datos
       // getContext().deleteDatabase("pedidos.db");
        datos = OperacionesBaseDatos.obtenerInstancia(getContext());

        new TareaPruebaDatos().execute();

        // poblar el spinner
        Spinner prueba = (Spinner) rootView.findViewById(R.id.spinCliente);
        // SimpleCursorAdapter adapter;
        //Creamos el cursor
        Cursor c = datos.obtenerClientes();
        //Creamos el adaptador
        SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(thiscontext,android.R.layout.simple_spinner_item,c,new String[] {"nombres"},new int[] {android.R.id.text1});
        //Añadimos el layout para el menú
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prueba.setAdapter(adapter2);
        String cant = String.valueOf(prueba.getCount());
        //Toast.makeText(getContext(),"Now onStart() calls "+cant, Toast.LENGTH_LONG).show(); //onStart Called



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

    // evento del spinner
    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView parent, View view, int pos,long id) {
            if (parent.getId() == R.id.spinCliente) {
                selection = ((String) parent.getItemAtPosition(pos)).toString();
                //Mostramos la selección actual del Spinner
                // Toast.makeText(getContext(),"Selección actual: "+ selection,Toast.LENGTH_SHORT).show();
            }

        }
        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }



}
