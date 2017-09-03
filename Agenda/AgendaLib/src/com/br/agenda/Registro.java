package com.br.agenda;

@SuppressWarnings("rawtypes")
public interface Registro extends Comparable, Cloneable {

	public void setCodigo(int codigo);

	public int getCodigo();

	public String getString();

	public Object clone() throws CloneNotSupportedException;

	public byte[] getByteArray() throws Exception;

	public void setByteArray(byte[] b) throws Exception;

	public int compareTo(Object b);
}
