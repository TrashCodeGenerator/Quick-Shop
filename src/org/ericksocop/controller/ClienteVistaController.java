/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ericksocop.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.ericksocop.bean.Clientes;
import org.ericksocop.dao.Conexion;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class ClienteVistaController implements Initializable {

    private ObservableList<Clientes> listaClientes;
    private Main escenarioPrincipal;

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operador tipoDeOperador = operador.NINGUNO;
    @FXML
    private TableView tvCliente;

    @FXML
    private TableColumn colClienteID;

    @FXML
    private TableColumn colNombreCliente;

    @FXML
    private TableColumn colApellidosClientes;

    @FXML
    private TableColumn colDireccionClientes;

    @FXML
    private TableColumn colNit;

    @FXML
    private TableColumn colTelefonoClientes;

    @FXML
    private TableColumn colCorreoClientes;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReportes;

    @FXML
    private Button btnAgregar;

    @FXML
    private TextField txtClienteID;

    @FXML
    private TextField txtNombreCliente;

    @FXML
    private TextField txtApellidoCliente;

    @FXML
    private TextField txtDireccionCliente;

    @FXML
    private TextField txtNIT;

    @FXML
    private TextField txtTelefonoCli;

    @FXML
    private TextField txtCorreoCliente;

    @FXML
    private Button btnRegresar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
        desactivarControles();
    }

    public void cargarDatos() {
        tvCliente.setItems(getClientes());
        colClienteID.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("clienteID"));
        colNombreCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombreClientes"));
        colApellidosClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidoClientes"));
        colDireccionClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccionClientes"));
        colNit.setCellValueFactory(new PropertyValueFactory<Clientes, String>("NITClientes"));
        colTelefonoClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefonoClientes"));
        colCorreoClientes.setCellValueFactory(new PropertyValueFactory<Clientes, String>("correoClientes"));

    }

    public void seleccionarElemento() {
        txtClienteID.setText(String.valueOf(((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getClienteID()));
        txtNombreCliente.setText((((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getNombreClientes()));
        txtApellidoCliente.setText((((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getApellidoClientes()));
        txtDireccionCliente.setText((((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getDireccionClientes()));
        txtNIT.setText((((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getNITClientes()));
        txtTelefonoCli.setText((((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getTelefonoClientes()));
        txtCorreoCliente.setText((((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getCorreoClientes()));
    }

    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_MostrarClientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Clientes(resultado.getInt("clienteID"),
                        resultado.getString("nombreClientes"),
                        resultado.getString("apellidosClientes"),
                        resultado.getString("direccionClientes"),
                        resultado.getString("NIT"),
                        resultado.getString("telefonoClientes"),
                        resultado.getString("correoClientes")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaClientes = FXCollections.observableList(lista);
    }

    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReportes.setDisable(true);
                //imgAgregar.setImage(new Image("URL"));
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();

                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                // cargarDatos();
                break;
        }

    }

    public void guardar() {
        Clientes registro = new Clientes();
        registro.setClienteID(Integer.parseInt(txtClienteID.getText()));
        registro.setNombreClientes(txtNombreCliente.getText());
        registro.setApellidoClientes(txtApellidoCliente.getText());
        registro.setDireccionClientes(txtDireccionCliente.getText());
        registro.setNITClientes(txtNIT.getText());
        registro.setTelefonoClientes(txtTelefonoCli.getText());
        registro.setCorreoClientes(txtCorreoCliente.getText());
        try {
            /*
                se utiliza la misma variable "Procedimiento" porque es un servidor local
                de lo contrario debe ser diferente el nombre de la variable
             */
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCliente(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getClienteID());
            procedimiento.setString(2, registro.getNombreClientes());
            procedimiento.setString(3, registro.getApellidoClientes());
            procedimiento.setString(4, registro.getDireccionClientes());
            procedimiento.setString(5, registro.getNITClientes());
            procedimiento.setString(6, registro.getTelefonoClientes());
            procedimiento.setString(7, registro.getCorreoClientes());
            procedimiento.execute();
            listaClientes.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvCliente.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminacion del registro?", "Eliminar Clientes", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarClientes(?)}");
                            procedimiento.setInt(1, ((Clientes) tvCliente.getSelectionModel().getSelectedItem()).getClienteID());
                            procedimiento.execute();
                            listaClientes.remove(tvCliente.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un cliente para eliminar");
                }

                break;
        }
    }

    //Editar lleva el mismo concepto que agregar y eliminar
    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tvCliente.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnEliminar.setDisable(true);
                    btnAgregar.setDisable(true);
                    activarControles();
                    txtClienteID.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un cliente para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void reportes() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperador = operador.NINGUNO;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarClientes(?,?,?,?,?,?,?)}");
            Clientes registro = (Clientes) tvCliente.getSelectionModel().getSelectedItem();
            registro.setNombreClientes(txtNombreCliente.getText());
            registro.setApellidoClientes(txtApellidoCliente.getText());
            registro.setDireccionClientes(txtDireccionCliente.getText());
            registro.setNITClientes(txtNIT.getText());
            registro.setTelefonoClientes(txtTelefonoCli.getText());
            registro.setCorreoClientes(txtCorreoCliente.getText());
            procedimiento.setInt(1, registro.getClienteID());
            procedimiento.setString(2, registro.getNombreClientes());
            procedimiento.setString(3, registro.getApellidoClientes());
            procedimiento.setString(4, registro.getDireccionClientes());
            procedimiento.setString(5, registro.getNITClientes());
            procedimiento.setString(6, registro.getTelefonoClientes());
            procedimiento.setString(7, registro.getCorreoClientes());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desactivarControles() {
        txtClienteID.setEditable(false);
        txtNombreCliente.setEditable(false);
        txtApellidoCliente.setEditable(false);
        txtDireccionCliente.setEditable(false);
        txtNIT.setEditable(false);
        txtTelefonoCli.setEditable(false);
        txtCorreoCliente.setEditable(false);
    }

    public void activarControles() {
        txtClienteID.setEditable(true);
        txtNombreCliente.setEditable(true);
        txtApellidoCliente.setEditable(true);
        txtDireccionCliente.setEditable(true);
        txtNIT.setEditable(true);
        txtTelefonoCli.setEditable(true);
        txtCorreoCliente.setEditable(true);
    }

    public void limpiarControles() {
        txtClienteID.clear();
        txtNombreCliente.clear();
        txtApellidoCliente.clear();
        txtDireccionCliente.clear();
        txtNIT.clear();
        txtTelefonoCli.clear();
        txtCorreoCliente.clear();
    }

    public void MenuPrincipalView() {
        escenarioPrincipal.menuPrincipalView();
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }

    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

}