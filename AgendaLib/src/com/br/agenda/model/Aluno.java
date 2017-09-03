package com.br.agenda.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.br.agenda.Registro;
import com.br.agenda.dao.AlunoDAO;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("SimpleDateFormat")
public class Aluno implements Registro, Parcelable {

	private int codigo;
	private String nome;
	private String cpf;
	private Date nascimento;
	private boolean isMorto;

	static final SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();

	}

	@Override
	public int getCodigo() {
		return codigo;
	}

	@Override
	public String getString() {
		return toString();
	}

	@Override
	public byte[] getByteArray() throws Exception {
		ByteArrayOutputStream registro = new ByteArrayOutputStream();
		DataOutputStream saida = new DataOutputStream(registro);
		saida.writeInt(codigo);
		saida.writeUTF(nome);
		saida.writeUTF(cpf);
		saida.writeUTF(getNascimento());

		return registro.toByteArray();
	}

	@Override
	public void setByteArray(byte[] b) throws IOException, Exception {
		ByteArrayInputStream registro = new ByteArrayInputStream(b);
		DataInputStream entrada = new DataInputStream(registro);

		codigo = entrada.readInt();
		nome = entrada.readUTF();
		cpf = entrada.readUTF();
		setNascimento(entrada.readUTF());

	}

	@Override
	public int compareTo(Object b) {
		return codigo - ((Aluno) b).getCodigo();
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNascimento() {
		return nascimento != null ? dt.format(nascimento) : "";
	}

	public void setNascimento(String nascimento) throws Exception {
		this.nascimento = (nascimento != null && !nascimento.isEmpty()) ? dt.parse(nascimento) : null;
	}

	public String getNome() {
		return nome;
	}

	public void matar(boolean matou) {
		this.setMorto(matou);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void salvar() throws Exception {
		AlunoDAO.salvaRegistro(this);
	}

	public boolean excluir() throws Exception {
		return AlunoDAO.deleteAluno(codigo);
	}

	public boolean update() throws Exception {
		return AlunoDAO.updateAluno(this);
	}

	@Override
	public String toString() {
		return "Aluno [codigo=" + codigo + ", nome=" + nome + ", cpf=" + cpf + ", nascimento=" + getNascimento() + "]";
	}

	public Aluno(int codigo, String nome, String cpf, String nascimento) throws Exception {
		this.codigo = codigo;
		this.nome = nome;
		this.cpf = cpf;
		this.setMorto(false);
		setNascimento(nascimento);
	}

	public Aluno() throws Exception {
		this(-1, null, null, null);
	}

	public Aluno(int codigo) throws Exception {
		this(codigo, null, null, null);
	}

	public Aluno(Parcel parcel) throws Exception {
		setCodigo(parcel.readInt());
		setNome(parcel.readString());
		setCpf(parcel.readString());
		setNascimento(parcel.readString());
		setMorto(parcel.readByte() != 0);
	}

	public boolean isMorto() {
		return isMorto;
	}

	private void setMorto(boolean isMorto) {
		this.isMorto = isMorto;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(getCodigo());
		parcel.writeString(getNome());
		parcel.writeString(getCpf());
		parcel.writeString(getNascimento());
		parcel.writeByte((byte) (isMorto ? 1 : 0));
	}

}
