package com.estatica.servicos.controllers;

import java.awt.Toolkit;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.estatica.servicos.dto.Reator1DTO;
import com.estatica.servicos.model.Produto;
import com.estatica.servicos.service.ProdutoDBService;
import com.estatica.servicos.service.impl.ProdutoDBServiceImpl;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import masktextfield.MaskTextField;

public class ConfigProcessoController implements Initializable {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private MaskTextField txtProduto;
	@FXML
	private MaskTextField txtLote;
	@FXML
	private TextField txtQuantidade;
	@FXML
	private TextField txtOperador;
	@FXML
	private Label lblProduto;
	@FXML
	private Label lblLote;
	@FXML
	private Label lblQuantidade;
	@FXML
	private Label lblOperador;
	@FXML
	private ProgressIndicator progLote;
	@FXML
	private Button btOk;
	@FXML
	private Button btCancelar;

	private static ProdutoDBService produtoService = new ProdutoDBServiceImpl();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtProduto.requestFocus();
			}
		});

		mainPane.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if (ev.getCode() == KeyCode.ENTER) {
				if (btOk.isFocused())
					btOk.fire();
				if (btCancelar.isFocused())
					btCancelar.fire();
				ev.consume();
			}
		});

		txtProduto.setMask("NNNNNNNNNNN");
		txtLote.setMask("NNNNNNNNNNN");
		addTextLimiter(txtQuantidade, 11);
		addTextLimiter(txtOperador, 50);
	}

	@FXML
	private void confirma() {
		if (txtProduto.getText().trim().equals("") || txtLote.getText().trim().equals("")
				|| txtQuantidade.getText().trim().equals("") || txtOperador.getText().trim().equals("")) {
			Toolkit.getDefaultToolkit().beep();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Atenção");
			alert.setHeaderText("Preencha todos os campos corretamente antes de salvar o lote.");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				return;
			}
		}

		Task<Void> persistTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				progLote.setVisible(Boolean.TRUE);
				txtLote.setDisable(Boolean.TRUE);
				txtOperador.setDisable(Boolean.TRUE);
				txtProduto.setDisable(Boolean.TRUE);
				txtQuantidade.setDisable(Boolean.TRUE);
				lblLote.setDisable(Boolean.TRUE);
				lblOperador.setDisable(Boolean.TRUE);
				lblProduto.setDisable(Boolean.TRUE);
				lblQuantidade.setDisable(Boolean.TRUE);
				btOk.setDisable(Boolean.TRUE);
				btCancelar.setDisable(Boolean.TRUE);

				Produto produto = new Produto(null, Integer.parseInt(txtProduto.getText()),
						Integer.parseInt(txtLote.getText()), "Reator 1", txtOperador.getText(),
						Double.parseDouble(txtQuantidade.getText().replace(".", "").replace(",", ".")), null, null);
				produtoService.saveProduto(produto);
				Reator1DTO.setProduto(produto);
				Reator1DTO.setCodProduto(txtProduto.getText());
				Reator1DTO.setLote(txtLote.getText());
				Reator1DTO.setQuantidade(txtQuantidade.getText());
				Reator1DTO.setOperador(txtOperador.getText());
				Reator1DTO.setConfirmation(Boolean.TRUE);
				return null;
			}
		};

		persistTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(final WorkerStateEvent event) {
				Stage stage = (Stage) btCancelar.getScene().getWindow();
				stage.close();
			}
		});
		Thread t = new Thread(persistTask);
		t.start();
	}

	@FXML
	private void cancela() {
		Reator1DTO.setConfirmation(Boolean.FALSE);
		Stage stage = (Stage) btCancelar.getScene().getWindow();
		stage.close();
	}

	public static void addTextLimiter(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				}
			}
		});
	}

}
