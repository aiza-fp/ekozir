/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Asset {

    @Property()
    private final String id;

    @Property()
    private final String tipo;

    @Property()
    private final String origen;

    @Property()
    private final String destino;

    @Property()
    private final String lote;

    @Property()
    private final String material;

    @Property()
    private final String peso;

    @Property()
    private final String fecha;

    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public String getLote() {
        return lote;
    }

    public String getMaterial() {
		return material;
	}

	public String getPeso() {
		return peso;
	}

	public String getFecha() {
		return fecha;
	}

	public Asset(@JsonProperty("id") final String id, @JsonProperty("tipo") final String tipo,
            @JsonProperty("origen") final String origen, @JsonProperty("destino") final String destino,
            @JsonProperty("lote") final String lote, @JsonProperty("material") final String material,
            @JsonProperty("peso") final String peso, @JsonProperty("fecha") final String fecha) {
        this.id = id;
        this.tipo = tipo;
        this.origen = origen;
        this.destino = destino;
        this.lote = lote;
        this.material = material;
        this.peso = peso;
        this.fecha = fecha;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Asset other = (Asset) obj;

        return Objects.deepEquals(
                new String[] {getId(), getTipo(), getOrigen(), getDestino(), getLote(), getMaterial(), getPeso(), getFecha()},
                new String[] {other.getId(), other.getTipo(), other.getOrigen(), other.getDestino(), other.getLote(), other.getMaterial(), other.getPeso(), other.getFecha()});
                //&&
                //Objects.deepEquals(
                //new int[] {getOrigen(), getLote()},
                //new int[] {other.getOrigen(), other.getLote()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTipo(), getOrigen(), getDestino(), getLote(), getMaterial(), getPeso(), getFecha());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [id=" + id + ", tipo="
                + tipo + ", origen=" + origen + ", destino=" + destino + ", lote=" + lote + ", material=" + material + ", peso=" + peso + ", fecha=" + fecha + "]";
    }
}
