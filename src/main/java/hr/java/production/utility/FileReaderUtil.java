package hr.java.production.utility;

import hr.java.production.constants.Constants;
import hr.java.production.enums.City;
import hr.java.production.genericsi.FoodStore;
import hr.java.production.genericsi.TechnicalStore;
import hr.java.production.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;



/**
 * Pomoćna klasa koja služi za učitavanje podataka iz datoteka.
 */
public class FileReaderUtil {


    private static final Logger logger = LoggerFactory.getLogger(FileReaderUtil.class);


    /**
     * Čita kategorije iz datoteke i vraća ih kao listu objekata klase Category.
     * @return Lista kategorija učitanih iz datoteke.
     * @throws IOException Ako dođe do pogreške prilikom čitanja datoteke.
     */
    public static List<Category> getCategoriesFromFile(){
        File categoriesFile = new File(Constants.CATEGORIES_FILE_NAME);
        List <Category> categoriesList = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(categoriesFile))){

            String line;
            while((Optional.ofNullable(line = reader.readLine()).isPresent())){

                Long categoryId = Long.parseLong(line);
                String categoryName = reader.readLine();
                String categoryDescription = reader.readLine();

                categoriesList.add(new Category(categoryId, categoryName, categoryDescription));

            }

        }
        catch (IOException ex){
            String message = "Dogodila se pogreška kod čitanja datoteke - + " + Constants.CATEGORIES_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);
        }

        return categoriesList;

    }


    /**
     * Čita artikle iz datoteke i vraća ih kao listu objekata klase Item.
     * @return Lista artikala učitanih iz datoteke.
     * @throws IOException Ako dođe do pogreške prilikom čitanja datoteke.
     */
     public static List<Item> getItemsFromFile(List<Category> categories){
         File itemsFile = new File(Constants.ITEMS_FILE_NAME);
         List<Item> itemsList = new ArrayList<>();

         try(BufferedReader reader = new BufferedReader(new FileReader(itemsFile))){
             String line;
             while(Optional.ofNullable(line = reader.readLine()).isPresent()){
                 Long itemId = Long.parseLong(line);
                 String itemName = reader.readLine();

                 Long categoryId = Long.parseLong(reader.readLine());
                 Optional <Category> categoryOfItem = categories.stream()
                         .filter(category -> category.getId().equals(categoryId))
                         .findFirst();

                 BigDecimal itemWidth = new BigDecimal(reader.readLine());
                 BigDecimal itemHeight = new BigDecimal(reader.readLine());
                 BigDecimal itemLength = new BigDecimal(reader.readLine());
                 BigDecimal itemProductionCost = new BigDecimal(reader.readLine());
                 BigDecimal itemSellingPrice = new BigDecimal(reader.readLine());
                 Integer itemDiscountInteger = Integer.parseInt(reader.readLine());

                 String itemType = reader.readLine();
                 BigDecimal itemWeigth;
                 Integer itemWarranty;

                 if(categoryOfItem.isPresent()){

                     Item basicItem = new Item(itemId, itemName, categoryOfItem.get(), itemWidth, itemHeight, itemLength,
                             itemProductionCost, itemSellingPrice, new Discount(itemDiscountInteger));

                     itemsList.add(switch (itemType){
                         case "Laptop" -> {
                             itemWarranty = Integer.parseInt(reader.readLine());
                             yield new Laptop(basicItem, itemWarranty);
                         }

                         case "Apple" -> {
                             itemWeigth = new BigDecimal(reader.readLine());
                             yield new Apple(basicItem, itemWeigth);
                         }
                         case "Peanut" -> {
                             itemWeigth = new BigDecimal(reader.readLine());
                             yield new PeanutButter(basicItem, itemWeigth);

                         }

                         default -> basicItem;
                     });
                 }



             }

         }
         catch (IOException ex){
             String message = "Dogodila se pogreška kod čitanja datoteke - + " + Constants.ITEMS_FILE_NAME;
             logger.error(message, ex);
             System.out.println(message);
         }



         return itemsList;
     }



    /**
     * Čita adrese iz datoteke i vraća ih kao listu objekata klase Address.
     * @return Lista adresa učitanih iz datoteke.
     * @throws IOException Ako dođe do pogreške prilikom čitanja datoteke.
     */
    public static List<Address> getAdressesFromFile(){

        File categoriesFile = new File(Constants.ADDRESSES_FILE_NAME);
        List <Address> addresses = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(categoriesFile))){

            String line;
            while((Optional.ofNullable(line = reader.readLine()).isPresent())){

                Long addressId = Long.parseLong(line);
                String streetName = reader.readLine();
                String houseNumber = reader.readLine();
                String cityName = reader.readLine();

                City city = City.getCityFromStringName(cityName);

                Address.AdressBuilder adressBuilder = new Address.AdressBuilder(city).setId(addressId)
                        .setStreet(streetName).setHouseNumber(houseNumber);


                addresses.add(adressBuilder.build());


            }

        }
        catch (IOException ex){
            String message = "Dogodila se pogreška kod čitanja datoteke - + " + Constants.ADDRESSES_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);
        }

        return addresses;
    }


    /**
     * Čita trgovine iz datoteke i vraća ih kao listu objekata klase Store.
     * @return Lista trgovina učitanih iz datoteke.
     * @throws IOException Ako dođe do pogreške prilikom čitanja datoteke.
     */
    public static List<Store> getStoresFromFile(List<Item> items) {
        File categoriesFile = new File(Constants.STORES_FILE_NAME);
        List <Store> storesList = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(categoriesFile))){

            String line;
            while((Optional.ofNullable(line = reader.readLine()).isPresent())){

                Long storeId = Long.parseLong(line);
                String storeName = reader.readLine();
                String storeWebAddress = reader.readLine();

                List<String> itemsIdForStoreStrings = Arrays.stream(reader.readLine().split(","))
                        .map(id -> id.trim())
                        .toList();

                List<Long> itemsIdForStore = itemsIdForStoreStrings.stream()
                        .map(id -> Long.parseLong(id))
                        .collect(Collectors.toList());

                Set<Item> itemsInStore = itemsIdForStore.stream()
                        .map(itemId -> findItemById(itemId, items))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet());

                String storeType = reader.readLine();

                storesList.add(switch(storeType){
                    case "Technical Store" -> {
                        TechnicalStore <Laptop> laptopStore =  new TechnicalStore<Laptop>(storeId, storeName, storeWebAddress, itemsInStore);
                        addLaptopsToTechnicalStore(laptopStore, itemsInStore.stream().toList());
                        yield laptopStore;
                    }
                    case "Apple Store" ->{
                        FoodStore <Apple> appleStore = new FoodStore<>(storeId, storeName, storeWebAddress, itemsInStore);
                        addApplesToAppleFoodStore(appleStore, itemsInStore.stream().toList());
                        yield appleStore;

                    }
                    case "Peanut Store" ->{
                        FoodStore <PeanutButter> peanutStore = new FoodStore<>(storeId, storeName, storeWebAddress, itemsInStore);
                         addPeanutsToPeanutStore(peanutStore, itemsInStore.stream().toList());
                         yield peanutStore;
                    }

                    default ->{
                        yield new Store(storeId, storeName, storeWebAddress, itemsInStore);
                    }
                });


            }

        }
        catch (IOException ex){
            String message = "Dogodila se pogreška kod čitanja datoteke - + " + Constants.STORES_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);
        }

        return storesList;


    }

    /**
     * Dodaje kikiriki maslace (<code>PeanutButter</code>) u trgovinu prehrambenih proizvoda s jestivim predmetima tipa Apple.
     * @param peanutFoodStore Trgovina u kojoj se dodaju maslaci od kikirikija.
     * @param items Lista artikala iz koje se filtriraju maslaci od kikirikija i dodaju u trgovinu.
     */
    private static void addPeanutsToPeanutStore(FoodStore<PeanutButter> peanutFoodStore, List<Item> items) {
        List<PeanutButter> peanuts = items.stream()
                .filter(item -> item instanceof PeanutButter)
                .map(item -> (PeanutButter) item)
                .collect(Collectors.toList());

        peanuts.forEach(peanutFoodStore::addEdibleItem);
    }

    /**
     * Dodaje laptope (<code>Laptop</code>) u trgovinu tehničkih proizvoda s tehničkim predmetima tipa Laptop.
     * @param laptopTechnicalStore Trgovina u kojoj se dodaju laptopi.
     * @param items Lista artikala iz koje se filtriraju laptopi i dodaju u trgovinu.
     */
    private static void addLaptopsToTechnicalStore(TechnicalStore<Laptop> laptopTechnicalStore, List<Item> items) {
        List<Laptop> laptops = items.stream()
                .filter(item -> item instanceof Laptop)
                .map(item -> (Laptop) item)
                .collect(Collectors.toList());

        laptops.forEach(laptopTechnicalStore::addTechnicalItem);
    }


    /**
     * Dodaje jabuke (<code>Apple</code>) u trgovinu prehrambenih proizvoda s jestivim predmetima tipa Apple.
     * @param appleFoodStore Trgovina u kojoj se dodaju jabuke.
     * @param items Lista artikala iz koje se filtriraju jabuke i dodaju u trgovinu.
     */
    public static void addApplesToAppleFoodStore(FoodStore<Apple> appleFoodStore, List<Item> items) {
        List<Apple> apples = items.stream()
                .filter(item -> item instanceof Apple)
                .map(item -> (Apple) item)
                .collect(Collectors.toList());

        apples.forEach(appleFoodStore::addEdibleItem);
    }


    /**
     * Čita tvornice iz datoteke i vraća ih kao listu objekata klase Factory.
     * @return Lista tvornica učitanih iz datoteke.
     * @throws IOException Ako dođe do pogreške prilikom čitanja datoteke.
     */
    public static List<Factory> getFactoriesFromFile(List<Item> items, List<Address> addresses) {
        List <Factory> factories = new ArrayList<>();
        File factoriesFile = new File(Constants.FACTORIES_FILE_NAME);
        String line;
        try(BufferedReader reader = new BufferedReader(new FileReader(factoriesFile))){

            while (Optional.ofNullable(line = reader.readLine()).isPresent()){
                Long factoryId = Long.parseLong(line);
                String factoryName = reader.readLine();
                Long factoryAddressId = Long.parseLong(reader.readLine());
                Optional <Address> factoryAddress = addresses.stream()
                        .filter(address -> address.getAddressId().compareTo(factoryAddressId) == 0)
                        .findFirst();

                List<String>  itemsIdForFactoryStrings = Arrays.asList(reader.readLine().split(","));

                List<Long> itemsIdForFactory = itemsIdForFactoryStrings.stream()
                        .map(idString -> idString.trim())
                        .map(idString -> Long.parseLong(idString))
                        .collect(Collectors.toList());

                Set<Item> itemsForFactory = itemsIdForFactory.stream()
                        .map(itemId -> findItemById(itemId, items))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet());

                factoryAddress.ifPresent(address -> factories.add(new Factory(factoryId, factoryName, address, itemsForFactory)));

            }

        }
        catch (IOException ex){
            String message = "Dogodila se pogreška kod čitanja datoteke - + " + Constants.FACTORIES_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);
        }

        return factories;
    }



    /**
     * Pronalazi artikl s određenim identifikacijskim brojem u zadanoj listi.
     * @param id Identifikacijski broj artikla kojeg treba pronaći.
     * @param items Lista artikala u kojoj treba tražiti.
     * @return {@code Optional} koji sadrži pronađeni artikl ili prazan {@code Optional} ako artikl nije pronađen.
     */
    private static Optional <Item> findItemById(Long id, List<Item> items){
        return items.stream().filter(item -> item.getId().equals(id))
                .findFirst();
    }
}
