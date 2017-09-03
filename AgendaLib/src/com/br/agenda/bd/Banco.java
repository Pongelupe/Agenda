package com.br.agenda.bd;

import java.io.Closeable;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import com.br.agenda.Registro;
import com.br.agenda.dao.AlunoDAO;

import android.os.Environment;
import android.util.Log;

public class Banco implements Closeable {

	public static final String path = Environment.getExternalStorageDirectory() + "/agenda/meuBanquinho.txt";
	private static final String pathTemp = Environment.getExternalStorageDirectory() + "/agenda/meuBanquinhoTemp.txt";
	private Registro registro;

	public void clear() throws Exception {
		new File(path).delete();

	}

	@Override
	public void close() {
		try {
			manageEntries();
			killInstance();
			Log.i("INFO:", "Fechado com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("ERROR:", "Erro ao fechar!");
		}
	}

	public ArrayList<? extends Registro> open() throws Exception {
		return AlunoDAO.listarArquivo();
	}

	private void manageEntries() throws Exception {
		RandomAccessFile bd = new RandomAccessFile(Banco.path, "rw");
		RandomAccessFile temp = new RandomAccessFile(Banco.pathTemp, "rw");
		bd.seek(0);
		while (bd.getFilePointer() < bd.length()) {
			boolean isMorto = bd.readBoolean();
			int tam = bd.readInt();
			byte[] b = new byte[tam];
			bd.read(b);
			registro.setByteArray(b);
			if (registro != null && !isMorto) {
				temp.seek(temp.length());
				temp.writeBoolean(false);
				temp.writeInt(registro.getByteArray().length);
				temp.write(registro.getByteArray());
			}
		}
		temp.close();
		bd.close();
	}

	private void killInstance() throws Exception {
		clear();
		File newBdInstance = new File(Banco.pathTemp);
		newBdInstance.renameTo(new File(Banco.path));
	}

	public Banco(Registro r) throws Exception {
		super();
		this.registro = r;
		File file = new File(Environment.getExternalStorageDirectory() + "/agenda");
		file.mkdir();
	}
}
