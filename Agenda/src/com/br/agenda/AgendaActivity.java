package com.br.agenda;

import java.util.ArrayList;

import com.br.agenda.bd.Banco;
import com.br.agenda.model.Aluno;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AgendaActivity extends Activity {

	private ArrayList<Aluno> alunos;
	private Banco banco;

	FragmentManager manager = getFragmentManager();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agenda);

		alunos = new ArrayList<Aluno>();
		try {
			banco = new Banco(new Aluno());
			alunos = (ArrayList<Aluno>) banco.open();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("alunos", alunos);

		ListaFragment listaFragment = new ListaFragment();
		listaFragment.setArguments(bundle);

		FragmentTransaction beginTransaction = manager.beginTransaction();
		beginTransaction.add(R.id.container, listaFragment);
		beginTransaction.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.agenda, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_exit:
			final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Até logo");
			alertDialog.setMessage("O banco será atualizado\nAté mais!");

			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					banco.close();
					alertDialog.dismiss();
				}
			});

			alertDialog.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
