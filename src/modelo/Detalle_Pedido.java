package modelo;

import java.math.BigDecimal;


public class Detalle_Pedido {
    private int id_detalle;
    private int id_pedido;
    private int id_sopa;
    private int cantidad;
    private BigDecimal precio_venta;
    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_sopa() {
        return id_sopa;
    }

    public void setId_sopa(int id_sopa) {
        this.id_sopa = id_sopa;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(BigDecimal precio_venta) {
        this.precio_venta = precio_venta;
    }
    
    
}
