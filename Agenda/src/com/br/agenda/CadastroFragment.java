package com.br.agenda;

import java.util.ArrayList;

import com.br.agenda.model.Aluno;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class CadastroFragment extends Fragment {

	private ArrayList<Aluno> alunos;

	// Componentes da view
	private EditText etNome;
	private EditText etCpf;
	private EditText etNascimento;
	private Button btAdd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_formulario, container, false);

		etNome = (EditText) rootView.findViewById(R.id.etNome);
		etCpf = (EditText) rootView.findViewById(R.id.etCpf);
		etNascimento = (EditText) rootView.findViewById(R.id.etNascimento);
		btAdd = (Button) rootView.findViewById(R.id.btAdd);

		Bundle bundle = getArguments();
		alunos = bundle.getParcelableArrayList("alunos");

		initTextListeners();

		return rootView;
	}

	private void initTextListeners() {
		etCpf.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() == 11) {
					// TODO
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		etNascimento.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		btAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (validaCampos())
						salvarAluno();
					else {
						final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
						alertDialog.setTitle("Atenção");
						alertDialog.setMessage("Preencha os campos para prossegir");
						alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								alertDialog.dismiss();
							}
						});
						alertDialog.show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private boolean validaCampos() {
		String nome = etNome.getText().toString();
		String cpf = etCpf.getText().toString();
		String nascimento = etNascimento.getText().toString();

		return !(nome.isEmpty() && cpf.isEmpty() && nascimento.isEmpty());
	}

	private void salvarAluno() throws Exception {
		String nome = etNome.getText().toString();
		String cpf = etCpf.getText().toString();
		String nascimento = etNascimento.getText().toString();

		Aluno aluno = new Aluno(1, nome, cpf, nascimento);
		aluno.salvar();
		alunos.add(aluno);
	}

}
