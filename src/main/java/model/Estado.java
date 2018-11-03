/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author SAULOD
 */
public class Estado {
    private int idTbEstado;
    private String varDesc;
    private String varUf;

    public int getIdTbEstado() {
        return idTbEstado;
    }

    public void setIdTbEstado(int idTbEstado) {
        this.idTbEstado = idTbEstado;
    }

    public String getVarDesc() {
        return varDesc;
    }

    public void setVarDesc(String varDesc) {
        this.varDesc = varDesc;
    }

    public String getVarUf() {
        return varUf;
    }

    public void setVarUf(String varUf) {
        this.varUf = varUf;
    }
}
