package com.br.agenda;

import java.util.ArrayList;

import com.br.agenda.model.Aluno;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

public class ListaFragment extends Fragment {

	private ArrayList<Aluno> alunos;

	private ListView lvAlunos;
	private ImageButton ibAdd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_lista, container, false);

		lvAlunos = (ListView) rootView.findViewById(R.id.lvAlunos);
		ibAdd = (ImageButton) rootView.findViewById(R.id.ibAdd);

		Bundle bundle = getArguments();
		alunos = bundle.getParcelableArrayList("alunos");

		// Preenchendo a List View com os nomes dos alunos em alunos
		ArrayList<String> nomesAlunos = new ArrayList<String>();
		for (Aluno aluno : alunos)
			if (aluno.getCodigo() != -1)
				if (!aluno.getNome().isEmpty() && !aluno.isMorto())
					nomesAlunos.add(aluno.getNome());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
				nomesAlunos);
		lvAlunos.setAdapter(adapter);

		ibAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("alunos", alunos);
				CadastroFragment cadastroFragment = new CadastroFragment();
				cadastroFragment.setArguments(bundle);

				FragmentManager manager = getFragmentManager();
				FragmentTransaction beginTransaction = manager.beginTransaction();
				try {
					beginTransaction.replace(R.id.container, cadastroFragment);
					beginTransaction.addToBackStack(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				beginTransaction.commit();
			}
		});

		return rootView;
	}

}
