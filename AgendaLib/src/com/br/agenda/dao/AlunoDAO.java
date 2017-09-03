package com.br.agenda.dao;

import java.io.RandomAccessFile;
import java.util.ArrayList;

import com.br.agenda.Registro;
import com.br.agenda.bd.Banco;
import com.br.agenda.model.Aluno;



public class AlunoDAO {

	public static void salvaRegistro(Registro r) throws Exception {
		RandomAccessFile file = new RandomAccessFile(Banco.path, "rw");
		file.seek(file.length());
		file.writeBoolean(false);
		file.writeInt(r.getByteArray().length);
		file.write(r.getByteArray());
		file.close();
	}

	public static ArrayList<Aluno> listarArquivo() throws Exception {
		ArrayList<Aluno> alunos = new ArrayList<Aluno>();
		RandomAccessFile file = new RandomAccessFile(Banco.path, "r");
		while (file.getFilePointer() < file.length()) {
			boolean isMorto = file.readBoolean();
			int tam = file.readInt();
			byte[] b = new byte[tam];
			file.read(b);
			Aluno aluno = new Aluno();
			aluno.setByteArray(b);
			if (aluno != null && !isMorto)
				alunos.add(aluno);
		}
		file.close();
		return alunos;
	}

	public static Registro getAluno(int codigo) throws Exception {
		Registro r = new Aluno(codigo);
		RandomAccessFile file = new RandomAccessFile(Banco.path, "r");
		while (file.getFilePointer() < file.length()) {
			boolean isMorto = file.readBoolean();
			int tam = file.readInt();
			byte[] b = new byte[tam];
			file.read(b);
			Aluno aluno = new Aluno();
			aluno.setByteArray(b);
			if (aluno != null && !isMorto) {
				r = aluno.compareTo(r) == 0 ? aluno : r;
			}
		}
		file.close();
		return r;
	}

	public static boolean deleteAluno(int codigo) throws Exception {
		Registro r = new Aluno(codigo);
		RandomAccessFile file = new RandomAccessFile(Banco.path, "rw");
		while (file.getFilePointer() < file.length()) {
			long inicialfp = file.getFilePointer();
			boolean isMorto = file.readBoolean();
			int tam = file.readInt();
			byte[] b = new byte[tam];
			file.read(b);
			Aluno aluno = new Aluno();
			aluno.setByteArray(b);
			if (aluno != null && !isMorto) {
				if (aluno.compareTo(r) == 0) {
					file.seek(inicialfp);
					file.writeBoolean(true);
					file.close();
					return true;
				}
			}
		}
		file.close();
		return false;
	}

	public static boolean updateAluno(Registro r) throws Exception {
		boolean update = true;
		if (deleteAluno(r.getCodigo()))
			salvaRegistro(r);
		else
			update = false;
		return update;
	}

}
