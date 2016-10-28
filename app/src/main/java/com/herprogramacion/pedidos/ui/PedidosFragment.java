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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.herprogramacion.pedidos.R;
import com.herprogramacion.pedidos.modelo.Tabla;
import com.herprogramacion.pedidos.sqlite.BaseDatosPedidos;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos;
import com.herprogramacion.pedidos.sqlite.OperacionesBaseDatos;

import java.util.ArrayList;


/**
 * Created by Ravi on 29/07/15.
 */
public class PedidosFragment extends Fragment {
    Context thiscontext;
    View rootView;
    String idpedido;
    String idcliente;
    Integer cont =1;


    public PedidosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idcliente  = getArguments().getString("idcliente");
       //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pedidos, container, false);
        thiscontext = container.getContext();


        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();

        rellenarDatosTabla1();
        // accion del boton
        Button buttonClick = (Button) rootView.findViewById(R.id.btnAtras);
        buttonClick.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Fragment fragment = new ProductoFragment();
                Bundle parametro = new Bundle();
                parametro.putString("idcliente" , idcliente);
                parametro.putString("cabecera" , idpedido);
                parametro.putInt("secuencia" , cont);
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

        // accion del boton
        Button buttonClick2 = (Button) rootView.findViewById(R.id.btnFinlaizar);

        buttonClick2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Fragment fragment = new ClienteFragment();
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

    //metodo para rellenar una tabla
    public void rellenarDatosTabla1() {
        TableLayout tl =  (TableLayout) rootView.findViewById(R.id.tabla);

        Tabla tabla = new Tabla(getActivity(),(TableLayout) rootView.findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.cabecera_tabla);

        OperacionesBaseDatos datos = new OperacionesBaseDatos();
        idpedido  = getArguments().getString("pedido");
        Cursor c = datos.obtenerDetallesPorIdPedido(idpedido);

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros

            int idColumn = c.getColumnIndex(ContratoPedidos.DetallesPedido.ID_PRODUCTO);
            int precioColumn = c.getColumnIndex(ContratoPedidos.DetallesPedido.PRECIO);
            int cantidadColumn = c.getColumnIndex(ContratoPedidos.DetallesPedido.CANTIDAD);
            int proColumn = c.getColumnIndex(ContratoPedidos.Productos.NOMBRE);

            //Recorremos el cursor

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                String producto = c.getString(proColumn);
                String precio = c.getString(precioColumn);
                String cantidad = c.getString(cantidadColumn);
                Integer total = Integer.parseInt(precio) * Integer.parseInt(cantidad);
                ArrayList<String> elementos = new ArrayList<String>();
                elementos.add(Integer.toString(cont));
                elementos.add(producto);
                elementos.add(precio);
                elementos.add(cantidad);
                elementos.add(String.valueOf(total));
                tabla.agregarFilaTabla(elementos);
                cont++;
            }
        }

        /*TableLayout tl =  (TableLayout) rootView.findViewById(R.id.tabla);

        Tabla tabla = new Tabla(getActivity(),(TableLayout) rootView.findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.cabecera_tabla);
        for(int i = 0; i < 15; i++)
        {
            ArrayList<String> elementos = new ArrayList<String>();
            elementos.add(Integer.toString(i));
            elementos.add(datos.get(0));
            elementos.add(datos.get(1));
            elementos.add(datos.get(2));
            elementos.add(datos.get(3));
            tabla.agregarFilaTabla(elementos);
        }*/

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
