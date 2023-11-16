package hr.java.production.utility;


import hr.java.production.model.Factory;
import hr.java.production.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pomoćna klasa koja služi za serijalizaciju i deserijalizaciju objekata.
 */
public class SerializationUtil {

    private static final Logger logger = LoggerFactory.getLogger(SerializationUtil.class);
    private static final String FACTORIES_SERIALIZATION_FILE_NAME = "dat/serialized-factories.txt";
    private static final String STORES_SERIALIZATION_FILE_NAME = "dat/serialized-stores.txt";


    /**
     * Serijalizira listu tvornica u datoteku.
     * @param factories Lista s tvornicama koja će biti serijalizirana.
     */
    public static void serializeFactories(List<Factory> factories){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FACTORIES_SERIALIZATION_FILE_NAME))){
            oos.writeObject(factories);

        }
        catch(IOException ex){
            String message = "Serialization error!";
            logger.error(message, ex);
        }

    }


    /**
     * Serijalizira listu prodavaonica u datoteku.
     * @param stores Lista s prodavaonicama koja će biti serijalizirana.
     */
    public static void serializeStores(List<Store> stores){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STORES_SERIALIZATION_FILE_NAME))){
            oos.writeObject(stores);

        }
        catch(IOException ex){
            String message = "Serialization error!";
            logger.error(message, ex);
        }

    }




    /**
     * Deserijalizira listu prodavaonica iz datoteke.
     * @return Lista prodavaonica pročitana iz datoteke.
     */
    public static List<Store> deserializeStores() {
        List<Store> stores = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STORES_SERIALIZATION_FILE_NAME))) {

            Object object = ois.readObject();

            if (object instanceof List<?>) {
                stores = (List<Store>) object;
                logger.info("sve raid kako treba");
            } else {
                System.out.println("Nije moguće deserializirati listu prodavaonica. Neočekivan format objekta.");
            }

        } catch (IOException | ClassNotFoundException ex) {
            String message = "Greška prilikom deserijalizacije!";
            logger.error(message, ex);
        }

        return stores;
    }


    /**
     * Deserijalizira listu trgovina iz datoteke.
     * @return Lista trgovina pročitana iz datoteke.
     */

    public static List<Factory> deserializeFactories(){
        List<Factory> factories = new ArrayList<>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FACTORIES_SERIALIZATION_FILE_NAME))){
            Object object = ois.readObject();
            if (object instanceof  List<?>){
                factories = (List<Factory>) object;
            }
            else{
                System.out.println("Nije moguće deserializirati listu trgovina. Neočekivan format objekta.");
            }

        }
        catch (IOException | ClassNotFoundException ex){
            String message = "Greška prilikom deserijalizacije!";
            logger.error(message, ex);
        }

        return factories;
    }



}
