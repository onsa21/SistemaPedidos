package com.herprogramacion.pedidos.modelo;

public class CabeceraPedido {

    public String idCabeceraPedido;

    public String fecha;

    public String idCliente;

    public String total;

    public CabeceraPedido(String idCabeceraPedido, String fecha,
                          String idCliente, String total) {
        this.idCabeceraPedido = idCabeceraPedido;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.total = total;
    }
}
