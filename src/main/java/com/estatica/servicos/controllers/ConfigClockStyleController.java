package com.estatica.servicos.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.estatica.servicos.dto.StyleClockDTO;

import eu.hansolo.medusa.LcdDesign;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ConfigClockStyleController implements Initializable {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private Button btOk;
	@FXML
	private RadioButton rdStandardGreen;
	@FXML
	private RadioButton rdBlue;
	@FXML
	private RadioButton rdRed;
	@FXML
	private RadioButton rdBeige;
	@FXML
	private RadioButton rdYellow;
	@FXML
	private RadioButton rdWhite;
	@FXML
	private RadioButton rdGray;
	@FXML
	private RadioButton rdBlack;
	@FXML
	private RadioButton rdGreen;
	@FXML
	private RadioButton rdGreenDarkGreen;
	@FXML
	private RadioButton rdBlue2;
	@FXML
	private RadioButton rdBlueBlack;
	@FXML
	private RadioButton rdBlueDarkBlue;
	@FXML
	private RadioButton rdBlueLightBlue;
	@FXML
	private RadioButton rdBlueGray;
	@FXML
	private RadioButton rdStandard;
	@FXML
	private RadioButton rdLightGreen;
	@FXML
	private RadioButton rdBlueBlue;
	@FXML
	private RadioButton rdRedDarkRed;
	@FXML
	private RadioButton rdDarkBlue;
	@FXML
	private RadioButton rdPurple;
	@FXML
	private RadioButton rdBlackRed;
	@FXML
	private RadioButton rdDarkGreen;
	@FXML
	private RadioButton rdAmber;
	@FXML
	private RadioButton rdLightBlue;
	@FXML
	private RadioButton rdGreenBlack;
	@FXML
	private RadioButton rdYellowBlack;
	@FXML
	private RadioButton rdBlackYellow;
	@FXML
	private RadioButton rdLightGreenBlack;
	@FXML
	private RadioButton rdDarkPurple;
	@FXML
	private RadioButton rdDarkAmber;
	@FXML
	private RadioButton rdBlueLightBlue2;
	@FXML
	private RadioButton rdGrayPurple;
	@FXML
	private RadioButton rdYoctopuce;
	@FXML
	private RadioButton rdSections;
	@FXML
	private RadioButton rdFlatCustom;
	@FXML
	private RadioButton rdOrange;

	ToggleGroup group = new ToggleGroup();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainPane.setPrefSize(680, 440);
		initGroup();
		initSelection();
	}

	@FXML
	public void setStdGreen() {
		StyleClockDTO.setStyle(LcdDesign.STANDARD_GREEN);
	}

	@FXML
	public void setBlue() {
		StyleClockDTO.setStyle(LcdDesign.BLUE);
	}

	@FXML
	public void setRed() {
		StyleClockDTO.setStyle(LcdDesign.RED);
	}

	@FXML
	public void setBeige() {
		StyleClockDTO.setStyle(LcdDesign.BEIGE);
	}

	@FXML
	public void setYellow() {
		StyleClockDTO.setStyle(LcdDesign.YELLOW);
	}

	@FXML
	public void setWhite() {
		StyleClockDTO.setStyle(LcdDesign.WHITE);
	}

	@FXML
	public void setGray() {
		StyleClockDTO.setStyle(LcdDesign.GRAY);
	}

	@FXML
	public void setBlack() {
		StyleClockDTO.setStyle(LcdDesign.BLACK);
	}

	@FXML
	public void setGreen() {
		StyleClockDTO.setStyle(LcdDesign.GREEN);
	}

	@FXML
	public void setGreenDarkGreen() {
		StyleClockDTO.setStyle(LcdDesign.GREEN_DARKGREEN);
	}

	@FXML
	public void setBlue2() {
		StyleClockDTO.setStyle(LcdDesign.BLUE2);
	}

	@FXML
	public void setBlueBlack() {
		StyleClockDTO.setStyle(LcdDesign.BLUE_BLACK);
	}

	@FXML
	public void setBlueDarkBlue() {
		StyleClockDTO.setStyle(LcdDesign.BLUE_DARKBLUE);
	}

	@FXML
	public void setBlueLightBlue() {
		StyleClockDTO.setStyle(LcdDesign.BLUE_LIGHTBLUE);
	}

	@FXML
	public void setBlueGray() {
		StyleClockDTO.setStyle(LcdDesign.BLUE_GRAY);
	}

	@FXML
	public void setStandard() {
		StyleClockDTO.setStyle(LcdDesign.STANDARD);
	}

	@FXML
	public void setLightGreen() {
		StyleClockDTO.setStyle(LcdDesign.LIGHTGREEN);
	}

	@FXML
	public void setBlueBlue() {
		StyleClockDTO.setStyle(LcdDesign.BLUE_BLUE);
	}

	@FXML
	public void setRedDarkRed() {
		StyleClockDTO.setStyle(LcdDesign.RED_DARKRED);
	}

	@FXML
	public void setDarkBlue() {
		StyleClockDTO.setStyle(LcdDesign.DARKBLUE);
	}

	@FXML
	public void setPurple() {
		StyleClockDTO.setStyle(LcdDesign.PURPLE);
	}

	@FXML
	public void setBlackRed() {
		StyleClockDTO.setStyle(LcdDesign.BLACK_RED);
	}

	@FXML
	public void setDarkGreen() {
		StyleClockDTO.setStyle(LcdDesign.DARKGREEN);
	}

	@FXML
	public void setAmber() {
		StyleClockDTO.setStyle(LcdDesign.AMBER);
	}

	@FXML
	public void setLightBlue() {
		StyleClockDTO.setStyle(LcdDesign.LIGHTBLUE);
	}

	@FXML
	public void setGreenBlack() {
		StyleClockDTO.setStyle(LcdDesign.GREEN_BLACK);
	}

	@FXML
	public void setYellowBlack() {
		StyleClockDTO.setStyle(LcdDesign.YELLOW_BLACK);
	}

	@FXML
	public void setBlackYellow() {
		StyleClockDTO.setStyle(LcdDesign.BLACK_YELLOW);
	}

	@FXML
	public void setLightGreenBlack() {
		StyleClockDTO.setStyle(LcdDesign.LIGHTGREEN_BLACK);
	}

	@FXML
	public void setDarkPurple() {
		StyleClockDTO.setStyle(LcdDesign.DARKPURPLE);
	}

	@FXML
	public void setDarkAmber() {
		StyleClockDTO.setStyle(LcdDesign.DARKAMBER);
	}

	@FXML
	public void setBlueLightBlue2() {
		StyleClockDTO.setStyle(LcdDesign.BLUE_LIGHTBLUE2);
	}

	@FXML
	public void setGrayPurple() {
		StyleClockDTO.setStyle(LcdDesign.GRAY_PURPLE);
	}

	@FXML
	public void setYoctopuce() {
		StyleClockDTO.setStyle(LcdDesign.YOCTOPUCE);
	}

	@FXML
	public void setSections() {
		StyleClockDTO.setStyle(LcdDesign.SECTIONS);
	}

	@FXML
	public void setFlatCustom() {
		StyleClockDTO.setStyle(LcdDesign.FLAT_CUSTOM);
	}

	@FXML
	public void setOrange() {
		StyleClockDTO.setStyle(LcdDesign.ORANGE);
	}

	@FXML
	public void exit() {
		Stage stage = (Stage) btOk.getScene().getWindow();
		stage.close();
	}

	private void initSelection() {

		if (StyleClockDTO.getStyle() == LcdDesign.AMBER) {
			rdAmber.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.BEIGE) {
			rdBeige.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.BLACK) {
			rdBlack.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.BLACK_RED) {
			rdBlackRed.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.BLACK_YELLOW) {
			rdBlackYellow.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.BLUE) {
			rdBlue.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.BLUE2) {
			rdBlue2.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.BLUE_BLACK) {
			rdBlueBlack.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.BLUE_BLUE) {
			rdBlueBlue.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.BLUE_DARKBLUE) {
			rdBlueDarkBlue.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.BLUE_GRAY) {
			rdBlueGray.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.BLUE_LIGHTBLUE) {
			rdBlueLightBlue.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.BLUE_LIGHTBLUE2) {
			rdBlueLightBlue2.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.DARKAMBER) {
			rdDarkAmber.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.DARKBLUE) {
			rdDarkBlue.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.DARKGREEN) {
			rdDarkGreen.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.DARKPURPLE) {
			rdDarkPurple.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.FLAT_CUSTOM) {
			rdFlatCustom.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.GRAY) {
			rdGray.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.GRAY_PURPLE) {
			rdGrayPurple.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.GREEN) {
			rdGreen.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.GREEN_BLACK) {
			rdGreenBlack.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.GREEN_DARKGREEN) {
			rdGreenDarkGreen.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.LIGHTBLUE) {
			rdLightBlue.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.LIGHTGREEN) {
			rdLightGreen.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.LIGHTGREEN_BLACK) {
			rdLightGreenBlack.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.ORANGE) {
			rdOrange.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.PURPLE) {
			rdPurple.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.RED) {
			rdRed.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.RED_DARKRED) {
			rdRedDarkRed.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.SECTIONS) {
			rdSections.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.STANDARD) {
			rdStandard.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.STANDARD_GREEN) {
			rdStandardGreen.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.WHITE) {
			rdWhite.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.YELLOW) {
			rdYellow.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.YELLOW_BLACK) {
			rdYellowBlack.setSelected(Boolean.TRUE);
		}
		if (StyleClockDTO.getStyle() == LcdDesign.YOCTOPUCE) {
			rdYoctopuce.setSelected(Boolean.TRUE);
		}
	}

	private void initGroup() {
		rdStandardGreen.setToggleGroup(group);
		rdBlue.setToggleGroup(group);
		rdRed.setToggleGroup(group);
		rdAmber.setToggleGroup(group);
		rdBeige.setToggleGroup(group);
		rdBlack.setToggleGroup(group);
		rdBlackRed.setToggleGroup(group);
		rdBlackYellow.setToggleGroup(group);
		rdBlue2.setToggleGroup(group);
		rdBlueBlack.setToggleGroup(group);
		rdBlueBlue.setToggleGroup(group);
		rdBlueDarkBlue.setToggleGroup(group);
		rdBlueGray.setToggleGroup(group);
		rdBlueLightBlue.setToggleGroup(group);
		rdBlueLightBlue2.setToggleGroup(group);
		rdDarkAmber.setToggleGroup(group);
		rdDarkBlue.setToggleGroup(group);
		rdDarkGreen.setToggleGroup(group);
		rdDarkPurple.setToggleGroup(group);
		rdFlatCustom.setToggleGroup(group);
		rdGray.setToggleGroup(group);
		rdGrayPurple.setToggleGroup(group);
		rdGreen.setToggleGroup(group);
		rdGreenBlack.setToggleGroup(group);
		rdGreenDarkGreen.setToggleGroup(group);
		rdLightBlue.setToggleGroup(group);
		rdLightGreen.setToggleGroup(group);
		rdLightGreenBlack.setToggleGroup(group);
		rdOrange.setToggleGroup(group);
		rdPurple.setToggleGroup(group);
		rdRedDarkRed.setToggleGroup(group);
		rdSections.setToggleGroup(group);
		rdStandard.setToggleGroup(group);
		rdWhite.setToggleGroup(group);
		rdYellow.setToggleGroup(group);
		rdYellowBlack.setToggleGroup(group);
		rdYoctopuce.setToggleGroup(group);
	}

}
