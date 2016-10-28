package com.herprogramacion.pedidos.modelo;

public class CabeceraPedido {

    public String idCabeceraPedido;

    public String fecha;

    public String idCliente;

    public String total;

    public CabeceraPedido(String idCabeceraPedido) {
        this.idCabeceraPedido = idCabeceraPedido;
    }

    public CabeceraPedido(String idCabeceraPedido, String fecha,
                          String idCliente, String total) {
        this.idCabeceraPedido = idCabeceraPedido;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.total = total;
    }

    public String getIdCabeceraPedido() {
        return idCabeceraPedido;
    }

    public void setIdCabeceraPedido(String idCabeceraPedido) {
        this.idCabeceraPedido = idCabeceraPedido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
