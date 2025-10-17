package modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pedido {
    private int id_pedido;
    private int id_usuario;
    private int id_cliente;
    private LocalDateTime fecha_pedido;
    private String fecha_pedido_formateada;
    private String estado;
    private LocalDateTime fecha_entrega;
    private String fecha_entrega_formateada;
    private BigDecimal total_pagar;

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public LocalDateTime getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(LocalDateTime fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(LocalDateTime fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public String getFecha_pedido_formateada() {
        return fecha_pedido_formateada;
    }

    public void setFecha_pedido_formateada(String fecha_pedido_formateada) {
        this.fecha_pedido_formateada = fecha_pedido_formateada;
    }

    public String getFecha_entrega_formateada() {
        return fecha_entrega_formateada;
    }

    public void setFecha_entrega_formateada(String fecha_entrega_formateada) {
        this.fecha_entrega_formateada = fecha_entrega_formateada;
    }

    public BigDecimal getTotal_pagar() {
        return total_pagar;
    }

    public void setTotal_pagar(BigDecimal total_pagar) {
        this.total_pagar = total_pagar;
    }
    
    
}
