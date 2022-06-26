/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="btnMassimo"
    private Button btnMassimo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCanzone"
    private ComboBox<Track> cmbCanzone; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<Genre> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtMemoria"
    private TextField txtMemoria; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void btnCreaLista(ActionEvent event) {
    	Track traccia = cmbCanzone.getValue();
    	String dimensione = txtMemoria.getText();
    	int d = Integer.parseInt(dimensione);
    	if(traccia==null) {
    		txtResult.setText("Seleziona canzone preferita.");
    		return;
    	} else {
    		//try {
//    			dimensione = Integer.parseInt(txtMemoria.getText());
//    		} catch (NumberFormatException e) {
//        		txtResult.setText("Inserisci dimensione valida.");
//			}
    		txtResult.setText(model.cercaLista(traccia, d).size()+"");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	Genre genere = cmbGenere.getValue();
    	if(genere==null) {
    		txtResult.setText("Seleziona genere.");
    		return;
    	} else {
    		model.getTracks(genere);
    		model.adiacenze(genere);
    		txtResult.setText(model.creaGrafo());
    		btnMassimo.setDisable(false);;
    		cmbCanzone.getItems().addAll(model.getVertex());
    	}
    }

    @FXML
    void doDeltaMassimo(ActionEvent event) {
    	Genre genere = cmbGenere.getValue();
    	if(genere==null) {
    		txtResult.setText("Seleziona prima un genere e crea il grafo.");
    		return;
    	} else {
    		txtResult.setText("COPPIA CANZONI DELTA MASSIMO:\n"+model.adiacenzaMax());
    		btnMassimo.setDisable(true);;
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMassimo != null : "fx:id=\"btnMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCanzone != null : "fx:id=\"cmbCanzone\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMemoria != null : "fx:id=\"txtMemoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<Genre> generi = new ArrayList<>(model.getGeneri());
    	Collections.sort(generi, new Comparator<Genre>() {

			@Override
			public int compare(Genre o1, Genre o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
    	cmbGenere.getItems().addAll(generi);
    }

}
