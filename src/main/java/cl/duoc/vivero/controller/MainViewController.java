package cl.duoc.vivero.controller;

import cl.duoc.vivero.model.Planta;
import cl.duoc.vivero.model.PlantaExterior;
import cl.duoc.vivero.model.PlantaInterior;
import cl.duoc.vivero.repository.PlantaRepository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class MainViewController {

    // REPOSITORY
    private PlantaRepository repositorio = new PlantaRepository();

    // LISTA OBSERVABLE
    private ObservableList<Planta> plantas = FXCollections.observableArrayList();

    // ESTRUCTURAS Y CAMPOS
    // ---- DATOS COMUNES
    @FXML private TextField txtNombre;
    @FXML private TextField txtAltura;
    @FXML private TextField txtCantidad;
    // -------- DATOS INTERIOR
    @FXML private TextField txtVolumenMaceta;
    // -------- DATOS EXTERIOR
    @FXML private TextField txtEspecie;
    @FXML private CheckBox chkResisteHeladas;
    // ---- TABLAS
    @FXML private TableView<Planta> tablaPlantas;
    @FXML private TableColumn<Planta, String> colTipo;
    @FXML private TableColumn<Planta, String> colNombre;
    @FXML private TableColumn<Planta, String> colAltura;
    @FXML private TableColumn<Planta, String> colCantidad;
    @FXML private TableColumn<Planta, String> colPrecio;
    // ---- BOTONES
    @FXML private Button btnRegistrar;
    @FXML private Button btnEliminar;
    // ---- OTROS
    @FXML private ComboBox cbTipoPlanta;
    @FXML private Label lblMensaje;


    @FXML
    public void initialize(){
        // cargo plantas de los archivos JSON
        if (repositorio.cargarPlantasInteriores() != null){
            plantas.addAll(repositorio.cargarPlantasInteriores());
        }
        if(repositorio.cargarPlantasExteriores() != null){
            plantas.addAll(repositorio.cargarPlantasExteriores());
        }

        // acá inicializo las columnas
        colTipo.setCellValueFactory(new PropertyValueFactory<Planta, String>("tipo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Planta, String>("nombre"));
        colAltura.setCellValueFactory(new PropertyValueFactory<Planta, String>("alturaCm"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Planta, String>("cantidad"));
        colPrecio.setCellValueFactory(celda -> new ReadOnlyObjectWrapper<>(Planta.pesos(celda.getValue().calcularPrecio())));

        // acá linkeo la lista a la tabla
        tablaPlantas.setItems(plantas);

        // agrego las opciones de Tipo de Planta al ComboBox
        cbTipoPlanta.getItems().add("Interior");
        cbTipoPlanta.getItems().add("Exterior");

        //acá deshabilité los campos para que no se puedan llenar hasta seleccionar un tipo de planta
        txtVolumenMaceta.setDisable(true);
        txtEspecie.setDisable(true);
        chkResisteHeladas.setDisable(true);
    }

    public void limpiarCampos(){
        cbTipoPlanta.setValue(null);
        txtNombre.clear();
        txtAltura.clear();
        txtCantidad.clear();
        txtVolumenMaceta.clear();
        txtEspecie.clear();
        chkResisteHeladas.setSelected(false);
    }

    @FXML
    public void activarCamposSegunTipo(){
        if ("Interior".equals(cbTipoPlanta.getValue())){
            txtVolumenMaceta.setDisable(false);
            txtEspecie.setDisable(true);
            chkResisteHeladas.setDisable(true);

        }else if ("Exterior".equals(cbTipoPlanta.getValue())){
            txtVolumenMaceta.setDisable(true);
            txtEspecie.setDisable(false);
            chkResisteHeladas.setDisable(false);
        }
    }

    @FXML
    public void registrarPlanta(){
        try{
            if (cbTipoPlanta.getValue()==null) {
                lblMensaje.setText("Debe seleccionar un tipo de planta para el registro");
                return;
            }

            Planta newPlanta = null;
            String nombrePlanta = txtNombre.getText();
            int alturaPlanta = Integer.parseInt(txtAltura.getText());
            int cantidadPlanta = Integer.parseInt(txtCantidad.getText());

            if (cbTipoPlanta.getValue().equals("Interior")) {
                double volumenMaceta = Double.parseDouble(txtVolumenMaceta.getText());
                newPlanta = new PlantaInterior(nombrePlanta, alturaPlanta, cantidadPlanta, volumenMaceta);

            } else if (cbTipoPlanta.getValue().equals("Exterior")) {
                String especiePlanta = txtEspecie.getText();
                boolean resisteHeladas = chkResisteHeladas.isSelected();
                newPlanta = new PlantaExterior(nombrePlanta, alturaPlanta, cantidadPlanta, especiePlanta, resisteHeladas);
            }

            if (newPlanta != null) {
                plantas.add(newPlanta);
                limpiarCampos();
                lblMensaje.setText("¡Planta registrada exitosamente!");
            }

            List<PlantaInterior> interiores = new ArrayList<>();
            List<PlantaExterior> exteriores = new ArrayList<>();

            for (Planta p : plantas){
                if (p instanceof PlantaInterior){
                    interiores.add((PlantaInterior) p);
                } else if (p instanceof PlantaExterior) {
                    exteriores.add((PlantaExterior) p);
                }
            }
            repositorio.agregarPlantasInteriores(interiores);
            repositorio.agregarPlantasExteriores(exteriores);

        }catch (IllegalArgumentException e){
            lblMensaje.setText("Error: "+e.getMessage());
        }
    }

    @FXML
    public void eliminarPlanta(){

    }




}
