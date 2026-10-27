package cl.duoc.vivero.repository;

import cl.duoc.vivero.model.PlantaExterior;
import cl.duoc.vivero.model.PlantaInterior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlantaRepository {
    // ATRIBUTOS
    private Gson gson;

    // CONSTRUCTOR
    public PlantaRepository(){
        this.gson = new Gson();
    }

    // MÉTODOS
    public void agregarPlantasInteriores(List<PlantaInterior> plantas){
        Path rutaArchivo = Path.of("interiores.json");
        try{
            String contenidoArchivo = this.gson.toJson(plantas);
            Files.writeString(rutaArchivo, contenidoArchivo);
        }catch(IOException e){
            System.out.println("Error Input/Output: "+e.getMessage());
        }
    }

    public void agregarPlantasExteriores(List<PlantaExterior> plantas){
        Path rutaArchivo = Path.of("exteriores.json");
        try{
            String contenidoArchivo = this.gson.toJson(plantas);
            Files.writeString(rutaArchivo, contenidoArchivo);
        }catch(IOException e){
            System.out.println("Error Input/Output: "+e.getMessage());
        }
    }

    public ObservableList<PlantaInterior> cargarPlantasInteriores(){
        Path rutaArchivo = Path.of("interiores.json");
        if (!Files.exists(rutaArchivo)){
            return FXCollections.observableArrayList();
        }else{
            try {
                String contenidoArchivo = Files.readString(rutaArchivo);         //acá lee el contenido del JSON
                Type tipoLista = new TypeToken<ArrayList<PlantaInterior>>(){}.getType();  //acá especifico qué tipo de datos están dentro del JSON
                List<PlantaInterior> listaInteriores = this.gson.fromJson(contenidoArchivo, tipoLista); // acá crea la lista con el tipo de datos, PlantasInteriores

                if (listaInteriores != null) {
                    return FXCollections.observableArrayList(listaInteriores);
                } else {
                    return FXCollections.observableArrayList();
                }
            } catch (IOException e){
                System.out.println("Error de Input/Output: " + e.getMessage());
                return FXCollections.observableArrayList();
            }
        }
    }

    public ObservableList<PlantaExterior> cargarPlantasExteriores(){
        Path rutaArchivo = Path.of("exteriores.json");
        if (!Files.exists(rutaArchivo)){
            return FXCollections.observableArrayList();
        }else{
            try {
                String contenidoArchivo = Files.readString(rutaArchivo);         //acá lee el contenido del JSON
                Type tipoLista = new TypeToken<ArrayList<PlantaExterior>>(){}.getType();  //acá especifico qué tipo de datos están dentro del JSON
                List<PlantaExterior> listaExteriores = this.gson.fromJson(contenidoArchivo, tipoLista); // acá crea la lista con el tipo de datos, PlantasExteriores

                if (listaExteriores != null) {
                    return FXCollections.observableArrayList(listaExteriores);
                } else {
                    return FXCollections.observableArrayList();
                }
            } catch (IOException e){
                System.out.println("Error de Input/Output: " + e.getMessage());
                return FXCollections.observableArrayList();
            }
        }
    }
}
