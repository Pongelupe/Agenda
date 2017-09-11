package com.br.agenda;

import java.util.ArrayList;

import com.br.agenda.model.Aluno;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ListaFragment extends Fragment {

	private ArrayList<Aluno> alunos;
	private boolean isUpdate = false;

	private ListView lvAlunos;
	private ImageButton ibAdd;

	// Views Pop-up
	private TextView tvNomePop;
	private EditText etNomePop;
	private EditText etCpfPop;
	private EditText etNascPop;
	private Button btUpdate;
	private Button btDelete;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_lista, container, false);

		lvAlunos = (ListView) rootView.findViewById(R.id.lvAlunos);
		ibAdd = (ImageButton) rootView.findViewById(R.id.ibAdd);

		Bundle bundle = getArguments();
		alunos = bundle.getParcelableArrayList("alunos");

		// Preenchendo a List View com os nomes dos alunos em alunos
		ArrayList<String> nomesAlunos = new ArrayList<String>();
		final ArrayList<Aluno> alunosVivos = new ArrayList<Aluno>();
		for (Aluno aluno : alunos)
			if (aluno.getCodigo() != -1)
				if (!aluno.getNome().isEmpty() && !aluno.isMorto()) {
					alunosVivos.add(aluno);
					nomesAlunos.add(aluno.getNome());
				}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, nomesAlunos);
		lvAlunos.setAdapter(adapter);

		lvAlunos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final Aluno alunoSelected = alunosVivos.get(position);
				final String nomeInicial = alunoSelected.getNome();

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				View rootViewPopup = getActivity().getLayoutInflater().inflate(R.layout.dialog_aluno, null);

				builder.setView(rootViewPopup);
				final AlertDialog dialog = builder.create();

				// Init pop-up views
				tvNomePop = (TextView) rootViewPopup.findViewById(R.id.tvNomeAluno);
				etNomePop = (EditText) rootViewPopup.findViewById(R.id.etNomePop);
				etCpfPop = (EditText) rootViewPopup.findViewById(R.id.etCpfPop);
				etNascPop = (EditText) rootViewPopup.findViewById(R.id.etNascimentoPop);
				btUpdate = (Button) rootViewPopup.findViewById(R.id.btUpdate);
				btDelete = (Button) rootViewPopup.findViewById(R.id.btDelete);

				tvNomePop.setText(alunoSelected.getNome() + "-" + alunoSelected.getCodigo());
				etNomePop.setText(alunoSelected.getNome());
				etCpfPop.setText(alunoSelected.getCpf());
				etNascPop.setText(alunoSelected.getNascimento());

				btUpdate.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!isUpdate) {
							swicherCadastro(true);
							initTextWatchers();
							btUpdate.setText("Salvar alterações");
							isUpdate = true;
						} else {
							if (validaCampos()) {
								try {
									update(alunoSelected);
									isUpdate = false;
									int positionAdapter = adapter.getPosition(nomeInicial);
									adapter.remove(nomeInicial);
									adapter.insert(alunoSelected.getNome(), positionAdapter);
									dialog.dismiss();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
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
						}
					}

				});

				btDelete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							alunos.remove(alunoSelected);
							alunosVivos.remove(alunoSelected);
							adapter.remove(alunoSelected.getNome());
							alunoSelected.excluir();
							dialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});

				dialog.show();
			}
		});

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

	private void initTextWatchers() {
		etCpfPop.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() == 11) {
					String cleanString = s.toString().trim();
					String initChars = cleanString.substring(0, 3);
					String middleChars = cleanString.substring(3, 6);
					String middleFinalChars = cleanString.substring(6, 9);
					String finalChars = cleanString.substring(cleanString.length() - 2);

					cleanString = String.format("%s.%s.%s-%s", initChars, middleChars, middleFinalChars, finalChars);
					etCpfPop.setText(cleanString);
				} else if (s.length() > 11) {
					etCpfPop.setText("");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		etNascPop.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() == 8) {
					String cleanString = s.toString().trim();
					String daysChars = cleanString.substring(0, 2);
					String monthsChars = cleanString.substring(2, 4);
					String yearsChars = cleanString.substring(cleanString.length() - 4);

					cleanString = String.format("%s-%s-%s", daysChars, monthsChars, yearsChars);
					etNascPop.setText(cleanString);
				} else if (s.length() > 8) {
					etNascPop.setText("");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private boolean validaCampos() {
		String nome = etNomePop.getText().toString();
		String cpf = etCpfPop.getText().toString();
		String nascimento = etNascPop.getText().toString();

		return !(nome.isEmpty() || cpf.length() < 11 || nascimento.length() < 8);
	}

	private void update(final Aluno alunoSelected) throws Exception {
		alunos.remove(alunoSelected);

		alunoSelected.setNome(etNomePop.getText().toString());
		alunoSelected.setCpf(etCpfPop.getText().toString());
		alunoSelected.setNascimento(etNascPop.getText().toString());

		alunoSelected.update();
	}

	private void swicherCadastro(boolean estado) {
		etNomePop.setEnabled(estado);
		etCpfPop.setEnabled(estado);
		etNascPop.setEnabled(estado);
	}
}
