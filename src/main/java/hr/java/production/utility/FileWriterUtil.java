package hr.java.production.utility;

import hr.java.production.constants.Constants;
import hr.java.production.genericsi.FoodStore;
import hr.java.production.genericsi.TechnicalStore;
import hr.java.production.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static hr.java.production.utility.FileReaderUtil.*;

public class FileWriterUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileReaderUtil.class);

    public static Long getNextItemId() {
        List<Item> items = FileReaderUtil.getItemsFromFile(getCategoriesFromFile());

        Long itemId = items.stream().map(NamedEntity::getId).max(Long::compareTo).get();
        return itemId + 1;
    }

    public static Long getNextCategoryId() {
        List<Category> categories = FileReaderUtil.getCategoriesFromFile();

        Long itemId = categories.stream().map(NamedEntity::getId).max(Long::compareTo).get();
        return itemId + 1;
    }

    public static Long getNextFactoryId(){
        List<Factory> factories = FileReaderUtil.getFactoriesFromFile(getItemsFromFile(getCategoriesFromFile()), getAdressesFromFile());
        Long factoryId = factories.stream().map(NamedEntity::getId).max(Comparator.naturalOrder()).get();
        return factoryId + 1;

    }

    public static Long getNextStoreId(){
        List<Store> stores = FileReaderUtil.getStoresFromFile(getItemsFromFile(getCategoriesFromFile()));
        Long storeId = stores.stream().map(NamedEntity::getId).max(Comparator.naturalOrder()).get();
        return storeId + 1;

    }


    public static void saveItemsToFile(List<Item> items) {
        File itemsFile = new File(Constants.ITEMS_FILE_NAME);

        try (PrintWriter pw = new PrintWriter(itemsFile)) {
            for (Item item : items) {
                pw.println(item.getId());
                pw.println(item.getName());
                pw.println(item.getCategory().getId());
                pw.println(item.getWidth());
                pw.println(item.getHeight());
                pw.println(item.getLength());
                pw.println(item.getProductionCost());
                pw.println(item.getSellingPrice());
                pw.println(item.getDiscount().discountAmount());

                if (item instanceof Edible edibleItem) {
                    if (item instanceof Apple appleItem) {
                        pw.println("Apple");
                    } else if (item instanceof PeanutButter peanutButterItem) {
                        pw.println("Peanut");
                    }

                    pw.println(edibleItem.getWeight());

                } else if (item instanceof Technical technicalItem) {
                    pw.println("Laptop");
                    pw.println(technicalItem.getWarrantyDurationInMonths());
                } else {
                    pw.println("Item");
                }
            }


        } catch (IOException ex) {
            String message = "Dogodila se pogreška kod pisanja datoteke - + " + Constants.ITEMS_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);

        }
    }

    static public void saveCategoriesToFile(List<Category> categories) {
        File categoriesFile = new File(Constants.CATEGORIES_FILE_NAME);
        try (PrintWriter pw = new PrintWriter(categoriesFile)) {
            for (Category category : categories) {
                pw.println(category.getId());
                pw.println(category.getName());
                pw.println(category.getDescription());
            }

        } catch (IOException ex) {
            String message = "Dogodila se pogreška kod pisanja datoteke - + " + Constants.CATEGORIES_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);

        }


    }

    static public void saveFactoriesToFile(List<Factory> factories) {

        File categoriesFile = new File(Constants.FACTORIES_FILE_NAME);
        try (PrintWriter pw = new PrintWriter(categoriesFile)) {
            for (Factory factory : factories){
                pw.println(factory.getId());
                pw.println(factory.getName());
                pw.println(factory.getAdress().getAddressId());
                List<String> itemsId = factory.getItems()
                        .stream()
                        .map(item -> item.getId().toString())
                        .toList();

                pw.println(String.join(", ", itemsId));
            }


        }catch (IOException ex) {
            String message = "Dogodila se pogreška kod pisanja datoteke - + " + Constants.FACTORIES_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);

        }

    }



    static public void saveStoresToFile(List<Store> stores) {

        File categoriesFile = new File(Constants.STORES_FILE_NAME);
        try (PrintWriter pw = new PrintWriter(categoriesFile)) {
            for (Store store : stores){
                pw.println(store.getId());
                pw.println(store.getName());
                pw.println(store.getWebAddress());
                List<String> itemsId = store.getItems()
                        .stream()
                        .map(item -> item.getId().toString())
                        .toList();

                pw.println(String.join(", ", itemsId));

                if (store instanceof FoodStore<?> foodStore){
                    if (foodStore.getEdibleItems().get(0) instanceof PeanutButter){
                        pw.println("Peanut Store");
                    }
                    else if (foodStore.getEdibleItems().get(0) instanceof Apple){
                        pw.println("Apple Store");
                    }
                }
                else if (store instanceof TechnicalStore<?>){
                    pw.println("Technical store");
                }
                else{
                    pw.println("Store");
                }
            }


        }catch (IOException ex) {
            String message = "Dogodila se pogreška kod pisanja datoteke - + " + Constants.STORES_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);

        }

    }


}
