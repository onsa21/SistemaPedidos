package com.herprogramacion.pedidos.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.herprogramacion.pedidos.modelo.CabeceraPedido;
import com.herprogramacion.pedidos.modelo.Cliente;
import com.herprogramacion.pedidos.modelo.DetallePedido;
import com.herprogramacion.pedidos.modelo.Producto;
import com.herprogramacion.pedidos.sqlite.BaseDatosPedidos.Tablas;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.CabecerasPedido;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.Clientes;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.DetallesPedido;
import com.herprogramacion.pedidos.sqlite.ContratoPedidos.Productos;


public final class OperacionesBaseDatos {

    private static BaseDatosPedidos baseDatos;

    private static OperacionesBaseDatos instancia = new OperacionesBaseDatos();


    public OperacionesBaseDatos() {
    }

    public static OperacionesBaseDatos obtenerInstancia(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new BaseDatosPedidos(contexto);
        }
        return instancia;
    }

    // [OPERACIONES_CABECERA_PEDIDO]
    public Cursor obtenerCabecerasPedidos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE);

        return builder.query(db, proyCabeceraPedido, null, null, null, null, null);
    }

    public Cursor obtenerCabeceraPorId(String id) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String selection = String.format("%s=?", CabecerasPedido.ID);
        String[] selectionArgs = {id};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE);

        String[] proyeccion = {
                Tablas.CABECERA_PEDIDO + "." + CabecerasPedido.ID,
                CabecerasPedido.FECHA,
                Clientes.NOMBRES,
                Clientes.APELLIDOS};

        return builder.query(db, proyeccion, selection, selectionArgs, null, null, null);
    }

    public String insertarCabeceraPedido(CabeceraPedido pedido) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Generar Pk
        String idCabeceraPedido = CabecerasPedido.generarIdCabeceraPedido();

        ContentValues valores = new ContentValues();
        valores.put(CabecerasPedido.ID, idCabeceraPedido);
        valores.put(CabecerasPedido.FECHA, pedido.fecha);
        valores.put(CabecerasPedido.ID_CLIENTE, pedido.idCliente);
        valores.put(CabecerasPedido.TOTAL, pedido.total);

        // Insertar cabecera
        db.insertOrThrow(Tablas.CABECERA_PEDIDO, null, valores);

        return idCabeceraPedido;
    }

    public boolean actualizarCabeceraPedido(CabeceraPedido pedidoNuevo) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(CabecerasPedido.FECHA, pedidoNuevo.fecha);
        valores.put(CabecerasPedido.ID_CLIENTE, pedidoNuevo.idCliente);

        String whereClause = String.format("%s=?", CabecerasPedido.ID);
        String[] whereArgs = {pedidoNuevo.idCabeceraPedido};

        int resultado = db.update(Tablas.CABECERA_PEDIDO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarCabeceraPedido(String idCabeceraPedido) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = CabecerasPedido.ID + "=?";
        String[] whereArgs = {idCabeceraPedido};

        int resultado = db.delete(Tablas.CABECERA_PEDIDO, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_CABECERA_PEDIDO]

    // [OPERACIONES_DETALLE_PEDIDO]
    public Cursor obtenerDetallesPedido() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.DETALLE_PEDIDO);

        return db.rawQuery(sql, null);
    }

    public Cursor obtenerDetallesPorIdPedido(String idCabeceraPedido) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.DETALLE_PEDIDO, CabecerasPedido.ID);

        String[] selectionArgs = {idCabeceraPedido};

        return db.rawQuery(sql, selectionArgs);

    }

    public String insertarDetallePedido(DetallePedido detalle) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DetallesPedido.ID, detalle.idCabeceraPedido);
        valores.put(DetallesPedido.SECUENCIA, detalle.secuencia);
        valores.put(DetallesPedido.ID_PRODUCTO, detalle.idProducto);
        valores.put(DetallesPedido.CANTIDAD, detalle.cantidad);
        valores.put(DetallesPedido.PRECIO, detalle.precio);

        db.insertOrThrow(Tablas.DETALLE_PEDIDO, null, valores);

        return String.format("%s#%d", detalle.idCabeceraPedido, detalle.secuencia);

    }

    public boolean actualizarDetallePedido(DetallePedido detalle) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DetallesPedido.SECUENCIA, detalle.secuencia);
        valores.put(DetallesPedido.CANTIDAD, detalle.cantidad);
        valores.put(DetallesPedido.PRECIO, detalle.precio);

        String selection = String.format("%s=? AND %s=?",
                DetallesPedido.ID, DetallesPedido.SECUENCIA);
        final String[] whereArgs = {detalle.idCabeceraPedido, String.valueOf(detalle.secuencia)};

        int resultado = db.update(Tablas.DETALLE_PEDIDO, valores, selection, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarDetallePedido(String idCabeceraPedido, int secuencia) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String selection = String.format("%s=? AND %s=?",
                DetallesPedido.ID, DetallesPedido.SECUENCIA);
        String[] whereArgs = {idCabeceraPedido, String.valueOf(secuencia)};

        int resultado = db.delete(Tablas.DETALLE_PEDIDO, selection, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_DETALLE_PEDIDO]

    // [OPERACIONES_PRODUCTO]
    public Cursor obtenerProductos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.PRODUCTO);

        return db.rawQuery(sql, null);
    }

    public String insertarProducto(Producto producto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        // Generar Pk
        String idProducto = Productos.generarIdProducto();
        valores.put(Productos.ID, idProducto);
        valores.put(Productos.NOMBRE, producto.nombre);
        valores.put(Productos.PRECIO, producto.precio);
        valores.put(Productos.EXISTENCIAS, producto.existencias);

        db.insertOrThrow(Tablas.PRODUCTO, null, valores);

        return idProducto;

    }

    public boolean actualizarProducto(Producto producto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Productos.NOMBRE, producto.nombre);
        valores.put(Productos.PRECIO, producto.precio);
        valores.put(Productos.EXISTENCIAS, producto.existencias);

        String whereClause = String.format("%s=?", Productos.ID);
        String[] whereArgs = {producto.idProducto};

        int resultado = db.update(Tablas.PRODUCTO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarProducto(String idProducto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Productos.ID);
        String[] whereArgs = {idProducto};

        int resultado = db.delete(Tablas.PRODUCTO, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_PRODUCTO]

    // [OPERACIONES_CLIENTE]
    public Cursor obtenerClientes() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.CLIENTE);

        return db.rawQuery(sql, null);
    }

    public String insertarCliente(Cliente cliente) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Generar Pk
        String idCliente = Clientes.generarIdCliente();

        ContentValues valores = new ContentValues();
        valores.put(Clientes.ID, idCliente);
        valores.put(Clientes.NOMBRES, cliente.nombres);
        valores.put(Clientes.APELLIDOS, cliente.apellidos);
        valores.put(Clientes.TELEFONO, cliente.telefono);
        valores.put(Clientes.DIRECCION, cliente.direccion);

        return db.insertOrThrow(Tablas.CLIENTE, null, valores) > 0 ? idCliente : null;
    }

    public boolean actualizarCliente(Cliente cliente) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Clientes.NOMBRES, cliente.nombres);
        valores.put(Clientes.APELLIDOS, cliente.apellidos);
        valores.put(Clientes.TELEFONO, cliente.telefono);
        valores.put(Clientes.DIRECCION, cliente.direccion);

        String whereClause = String.format("%s=?", Clientes.ID);
        final String[] whereArgs = {cliente.idCliente};

        int resultado = db.update(Tablas.CLIENTE, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarCliente(String idCliente) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Clientes.ID);
        final String[] whereArgs = {idCliente};

        int resultado = db.delete(Tablas.CLIENTE, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_CLIENTE]



    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }





    private static final String CABECERA_PEDIDO_JOIN_CLIENTE= "cabecera_pedido " +
            "INNER JOIN cliente " +
            "ON cabecera_pedido.id_cliente = cliente.id ";


    private final String[] proyCabeceraPedido = new String[]{
            Tablas.CABECERA_PEDIDO + "." + CabecerasPedido.ID,
            CabecerasPedido.FECHA,
            Clientes.NOMBRES,
            Clientes.APELLIDOS};

}
