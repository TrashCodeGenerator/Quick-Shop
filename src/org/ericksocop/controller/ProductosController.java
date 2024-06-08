/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ericksocop.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import org.ericksocop.bean.Productos;
import org.ericksocop.bean.Proveedores;
import org.ericksocop.bean.TipoProducto;
import org.ericksocop.dao.Conexion;
import org.ericksocop.report.GenerarReportes;
import org.ericksocop.system.Main;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class ProductosController implements Initializable {

    @FXML
    private TableView tvProductos;
    @FXML
    private JFXButton btnRegresarP;
    @FXML
    private JFXButton btnAgregarPro;
    @FXML
    private JFXButton btnEditarPro;
    @FXML
    private JFXButton btnTipoP;
    @FXML
    private JFXButton btnEliminarPro;
    @FXML
    private JFXButton btnReportesPro;
    @FXML
    private JFXTextField txtCodigoProd;
    @FXML
    private JFXTextField txtDescPro;
    @FXML
    private JFXTextField txtPrecioU;
    @FXML
    private JFXTextField txtPrecioD;
    @FXML
    private JFXTextField txtPrecioM;
    @FXML
    private JFXTextField txtImagenPro;
    @FXML
    private JFXTextField txtExistencia;
    @FXML
    private ComboBox cmbCodigoTipoP;
    @FXML
    private ComboBox cmbCodigoP;
    @FXML
    private TableColumn colCodProd;
    @FXML
    private TableColumn colDescProd;
    @FXML
    private TableColumn colPrecioU;
    @FXML
    private TableColumn colPrecioD;
    @FXML
    private TableColumn colPrecioM;
    @FXML
    private TableColumn colImagenP;
    @FXML
    private TableColumn colExistencia;
    @FXML
    private TableColumn colCodTipoProd;
    @FXML
    private TableColumn colCodProv;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXButton btnCerrar;
    @FXML
    private FontAwesomeIcon iconoCerrar;
    @FXML
    private JFXButton btnMinimizar;
    @FXML
    private FontAwesomeIcon iconMinimizar;
    @FXML
    private JFXButton btnMaximizar;
    @FXML
    private FontAwesomeIcon iconMaximizar;
    @FXML
    private FontAwesomeIcon agregarIcono;
    @FXML
    private FontAwesomeIcon actualizarIcono;
    @FXML
    private FontAwesomeIcon eliminarIcono;
    @FXML
    private FontAwesomeIcon reporteIcono;
    @FXML
    private JFXTextField txtbuscarProducto;
    private Main escenarioPrincipal;
    private ObservableList<Productos> listaProductos;
    private ObservableList<Proveedores> listaProveedores;
    private ObservableList<TipoProducto> listaTipoP;

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operador tipoDeOperador = operador.NINGUNO;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
        cmbCodigoTipoP.setItems(getTipoP());
        cmbCodigoP.setItems(getProveedores());
        desactivarControles();
        colCodProd.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.0476));
        colCodProv.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colCodTipoProd.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colDescProd.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colExistencia.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colImagenP.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colPrecioD.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colPrecioM.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
        colPrecioU.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
    }

    public void cargarDatos() {
        tvProductos.setItems(getProducto());
        colCodProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("codigoProducto"));
        colDescProd.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioUnitario"));
        colPrecioD.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioDocena"));
        colPrecioM.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioMayor"));
        colImagenP.setCellValueFactory(new PropertyValueFactory<Productos, String>("imagenProducto"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("existencia"));
        colCodTipoProd.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("CodigoTipoProducto"));
        colCodProv.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProveedor"));
    }

    public void seleccionarElemento() {
        try {
            txtCodigoProd.setText(String.valueOf(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            txtDescPro.setText((((Productos) tvProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto()));
            txtPrecioU.setText(String.valueOf(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
            txtPrecioD.setText(String.valueOf(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
            txtPrecioM.setText(String.valueOf(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
            txtImagenPro.setText((((Productos) tvProductos.getSelectionModel().getSelectedItem()).getImagenProducto()));
            txtExistencia.setText(String.valueOf(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getExistencia()));
            cmbCodigoTipoP.getSelectionModel().select(buscarTipoProducto(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
            cmbCodigoP.getSelectionModel().select(buscarProveedor(((Productos) tvProductos.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public TipoProducto buscarTipoProducto(int codigoTipoProducto) {
        TipoProducto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarTipoProducto(?)}");
            procedimiento.setInt(1, codigoTipoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new TipoProducto(registro.getInt("CodigoTipoProducto"),
                        registro.getString("descripcionProducto")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public Proveedores buscarProveedor(int codigoProveedor) {
        Proveedores resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarproveedor(?)}");
            procedimiento.setInt(1, codigoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Proveedores(registro.getInt("codigoProveedor"),
                        registro.getString("NITProveedor"),
                        registro.getString("nombresProveedor"),
                        registro.getString("apellidosProveedor"),
                        registro.getString("direccionProveedor"),
                        registro.getString("razonSocial"),
                        registro.getString("contactoPrincipal"),
                        registro.getString("paginaWeb"),
                        registro.getString("telefonoProveedor"),
                        registro.getString("emailProveedor")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<Productos> getProducto() {
        ArrayList<Productos> listaP = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaP.add(new Productos(resultado.getInt("codigoProducto"),
                        resultado.getString("descripcionProducto"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getDouble("precioDocena"),
                        resultado.getDouble("precioMayor"),
                        resultado.getString("imagenProducto"),
                        resultado.getInt("existencia"),
                        resultado.getInt("CodigoTipoProducto"),
                        resultado.getInt("codigoProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableList(listaP);
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> listaPro = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarproveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaPro.add(new Proveedores(resultado.getInt("codigoProveedor"),
                        resultado.getString("NITProveedor"),
                        resultado.getString("nombresProveedor"),
                        resultado.getString("apellidosProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWeb"),
                        resultado.getString("telefonoProveedor"),
                        resultado.getString("emailProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProveedores = FXCollections.observableList(listaPro);
    }

    public ObservableList<TipoProducto> getTipoP() {
        ArrayList<TipoProducto> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoProducto(resultado.getInt("CodigoTipoProducto"),
                        resultado.getString("descripcionProducto")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoP = FXCollections.observableList(lista);
    }

    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                txtPrecioD.setDisable(true);
                txtPrecioM.setDisable(true);
                txtPrecioU.setDisable(true);
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.SAVE);
                btnAgregarPro.setText("Guardar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.CLOSE);
                btnEliminarPro.setText("Cancelar");
                btnEditarPro.setDisable(true);
                btnReportesPro.setDisable(true);
                //imgAgregar.setImage(new Image("URL"));
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();
                cargarDatos();
                desactivarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.PLUS);
                btnAgregarPro.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.TRASH);
                btnEliminarPro.setText("Eliminar");
                btnEditarPro.setDisable(false);
                btnReportesPro.setDisable(false);
                /*regresar de nuevo a sus imagenes originales
                imgAgregar.setImage(new Image("URL"));*/
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void guardar() {
        Productos registro = new Productos();
        registro.setCodigoProducto(Integer.parseInt(txtCodigoProd.getText()));
        registro.setCodigoProveedor(((Proveedores) cmbCodigoP.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setCodigoTipoProducto(((TipoProducto) cmbCodigoTipoP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
        registro.setDescripcionProducto(txtDescPro.getText());
        registro.setPrecioDocena(Double.parseDouble("0.00"));
        registro.setPrecioMayor(Double.parseDouble("0.00"));
        registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
        registro.setImagenProducto(txtImagenPro.getText());
        registro.setPrecioUnitario(Double.parseDouble("0.00"));

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_agregarProducto(?,?,?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setString(6, registro.getImagenProducto());
            procedimiento.setInt(7, registro.getExistencia());
            procedimiento.setInt(8, registro.getCodigoTipoProducto());
            procedimiento.setInt(9, registro.getCodigoProveedor());
            procedimiento.execute();

            listaProductos.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tvProductos.getSelectionModel().getSelectedItem() != null) {
                    actualizarIcono.setFill(Color.WHITE);
                    actualizarIcono.setIcon(FontAwesomeIcons.SAVE);
                    btnEditarPro.setText("Actualizar");
                    reporteIcono.setFill(Color.WHITE);
                    reporteIcono.setIcon(FontAwesomeIcons.CLOSE);
                    btnReportesPro.setText("Cancelar");
                    btnEliminarPro.setDisable(true);
                    btnAgregarPro.setDisable(true);
                    txtPrecioD.setDisable(true);
                    txtPrecioM.setDisable(true);
                    txtPrecioU.setDisable(true);
                    activarControles();
                    txtCodigoProd.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para editar");
                }
                break;
            case ACTUALIZAR:
                try {
                actualizar();
            } catch (Exception e) {
                e.printStackTrace();
            }
            btnEditarPro.setText("Editar");
            btnReportesPro.setText("Reportes");
            btnAgregarPro.setDisable(false);
            btnEliminarPro.setDisable(false);
            desactivarControles();
            limpiarControles();
            tipoDeOperador = operador.NINGUNO;
            cargarDatos();
            break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarProducto(?,?,?,?,?,?,?,?,?)}");
            Productos registro = (Productos) tvProductos.getSelectionModel().getSelectedItem();
            registro.setCodigoProveedor(((Proveedores) cmbCodigoP.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            registro.setCodigoTipoProducto((((TipoProducto) cmbCodigoTipoP.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
            registro.setDescripcionProducto(txtDescPro.getText());
            registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
            registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
            registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
            registro.setImagenProducto(txtImagenPro.getText());
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setString(6, registro.getImagenProducto());
            procedimiento.setInt(7, registro.getExistencia());
            procedimiento.setInt(8, registro.getCodigoTipoProducto());
            procedimiento.setInt(9, registro.getCodigoProveedor());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() throws SQLException {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                agregarIcono.setFill(Color.WHITE);
                agregarIcono.setIcon(FontAwesomeIcons.PLUS);
                btnAgregarPro.setText("Agregar");
                eliminarIcono.setFill(Color.WHITE);
                eliminarIcono.setIcon(FontAwesomeIcons.TRASH);
                btnEliminarPro.setText("Eliminar");
                btnEditarPro.setDisable(false);
                btnReportesPro.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tvProductos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirma esta Acción", "Verificación",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            Productos productoSeleccionado = (Productos) tvProductos.getSelectionModel().getSelectedItem();
                            int codigoProducto = productoSeleccionado.getCodigoProducto();

                            // Eliminar las referencias en DetalleCompra
                            PreparedStatement eliminarDetalleCompraStmt = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_eliminarDetalleCompraPorProducto(?)}");
                            eliminarDetalleCompraStmt.setInt(1, codigoProducto);
                            eliminarDetalleCompraStmt.execute();

                            // Eliminar el producto
                            PreparedStatement eliminarProductoStmt = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_eliminarProducto(?)}");
                            eliminarProductoStmt.setInt(1, codigoProducto);
                            eliminarProductoStmt.execute();

                            listaProductos.remove(productoSeleccionado);
                            limpiarControles();
                            cargarDatos();
                            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente");
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para eliminar");
                }
                break;
        }
    }

    public void reportes() {
        switch (tipoDeOperador) {
            case NINGUNO:
                imprimirReporte();
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                actualizarIcono.setFill(Color.WHITE);
                actualizarIcono.setIcon(FontAwesomeIcons.EDIT);
                btnEditarPro.setText("Actualizar");
                reporteIcono.setFill(Color.WHITE);
                reporteIcono.setIcon(FontAwesomeIcons.FOLDER);
                btnReportesPro.setText("Reportes");
                btnAgregarPro.setDisable(false);
                btnEliminarPro.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
        }
    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        parametros.put("codigoProducto", null);
        GenerarReportes.mostrarReportes("reportProducto.jasper", "Reporte de Cliente", parametros);
    }

    public void buscarProducto() {
        limpiarControles();
        FilteredList<Productos> filtro = new FilteredList<>(listaProductos, e -> true);
        txtbuscarProducto.textProperty().addListener((Observable, oldValue, newValue) -> {
            filtro.setPredicate(predicateProducto -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String param = newValue.toLowerCase();
                String productoID = String.valueOf(predicateProducto.getCodigoProducto());
                String precioU = String.valueOf(predicateProducto.getPrecioUnitario());
                String precioD = String.valueOf(predicateProducto.getPrecioDocena());
                String precioM = String.valueOf(predicateProducto.getPrecioMayor());
                String existencia = String.valueOf(predicateProducto.getExistencia());
                String tipoProdID = String.valueOf(predicateProducto.getCodigoTipoProducto());
                String proveedorID = String.valueOf(predicateProducto.getCodigoProveedor());

                if (productoID.contains(param)) {
                    return true;
                } else if (predicateProducto.getDescripcionProducto().toLowerCase().contains(param)) {
                    return true;
                } else if (precioU.contains(param)) {
                    return true;
                } else if (precioD.contains(param)) {
                    return true;
                } else if (precioM.contains(param)) {
                    return true;
                } else if (existencia.contains(param)) {
                    return true;
                } else if (tipoProdID.contains(param)) {
                    return true;
                } else if (proveedorID.contains(param)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Productos> sortList = new SortedList<>(filtro);
        sortList.comparatorProperty().bind(tvProductos.comparatorProperty());
        tvProductos.setItems(sortList);
    }

    public void desactivarControles() {
        txtCodigoProd.setEditable(false);
        txtDescPro.setEditable(false);
        txtExistencia.setEditable(false);
        txtImagenPro.setEditable(false);
        txtPrecioD.setEditable(false);
        txtPrecioM.setEditable(false);
        txtPrecioU.setEditable(false);
        cmbCodigoTipoP.setDisable(true);
        cmbCodigoP.setDisable(true);
    }

    public void activarControles() {
        txtCodigoProd.setEditable(true);
        txtDescPro.setEditable(true);
        txtExistencia.setEditable(true);
        txtImagenPro.setEditable(true);
        txtPrecioD.setEditable(true);
        txtPrecioM.setEditable(true);
        txtPrecioU.setEditable(true);
        cmbCodigoTipoP.setDisable(false);
        cmbCodigoP.setDisable(false);
    }

    public void limpiarControles() {
        txtCodigoProd.clear();
        txtDescPro.clear();
        txtExistencia.clear();
        txtImagenPro.clear();
        txtPrecioD.clear();
        txtPrecioM.clear();
        txtPrecioU.clear();
        cmbCodigoTipoP.setValue(null);
        cmbCodigoP.setValue(null);
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void handleButtonAction(ActionEvent event) {

        if (event.getSource() == btnRegresarP) {
            escenarioPrincipal.menuPrincipalView();
        }
        if (event.getSource() == btnTipoP) {
            try {
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                escenarioPrincipal.tipoProductoView(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (event.getSource() == iconoCerrar || event.getSource() == btnCerrar) {
            System.exit(0);
        }
        if (event.getSource() == iconMinimizar || event.getSource() == btnMinimizar) {
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.setIconified(true);
        }
    }

    public void ventana(ActionEvent event) {

        if (event.getSource() == iconMaximizar || event.getSource() == btnMaximizar) {
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), iconMaximizar);
            rotateTransition.setByAngle(180);
            rotateTransition.setCycleCount(1);
            rotateTransition.setAutoReverse(false);

            if (stage.isMaximized()) {
                colCodProd.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.0476));
                colCodProv.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
                colCodTipoProd.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
                colDescProd.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
                colExistencia.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
                colImagenP.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
                colPrecioD.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
                colPrecioM.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
                colPrecioU.prefWidthProperty().bind(tvProductos.widthProperty().multiply(0.11905));
                stage.setMaximized(false);
            } else {
                stage.setMaximized(true);
            }
            rotateTransition.play();
        }
    }
        
        public void verificacionRotacion(){
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            if(stage.isMaximized()){
                RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), iconMaximizar);
            rotateTransition.setByAngle(180);
            rotateTransition.setCycleCount(1);
            rotateTransition.setAutoReverse(false);
            stage.setMaximized(false);
            }
        }

}
