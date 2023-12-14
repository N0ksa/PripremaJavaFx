package hr.java.production.utility;

import hr.java.production.enums.City;
import hr.java.production.filter.CategoryFilter;
import hr.java.production.filter.FactoryFilter;
import hr.java.production.filter.ItemFilter;
import hr.java.production.filter.StoreFilter;
import hr.java.production.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class DatabaseUtil {


    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private static final String DATABASE_FILE = "conf/database.properties";

    private static Connection connectToDatabase() throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(DATABASE_FILE));
        String urlDataBase = properties.getProperty("databaseUrl");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        Connection connection = DriverManager.getConnection(urlDataBase,
                username, password);
        return connection;

    }


    public static List<Category> getCategories(){
        List<Category> categories = new ArrayList<>();

        try(Connection connection = connectToDatabase()){
            String sqlQuery = "SELECT * FROM CATEGORY ";
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            mapResultSetToCategoriesList(rs, categories);

        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };


        return categories;
    }


    public static List<Category> getCategoriesByFilters(CategoryFilter categoryFilter){
        List<Category> categories = new ArrayList<>();
        Map<Integer, Object> queryParams = new HashMap<>();
        Integer paramOrdinalNumber = 1;

        try(Connection connection = connectToDatabase()){
            String baseSqlQuery = "SELECT * FROM CATEGORY WHERE 1=1 ";

            if (!categoryFilter.getName().isEmpty()){
                baseSqlQuery += " AND NAME = ?";
                queryParams.put(paramOrdinalNumber, categoryFilter.getName());
                paramOrdinalNumber++;
            }


            PreparedStatement pstmt = connection.prepareStatement(baseSqlQuery);

            for (Integer paramNumber : queryParams.keySet()){
                if (queryParams.get(paramNumber) instanceof String sqp){
                    pstmt.setString(paramNumber, sqp);
                }
            }

            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();

            mapResultSetToCategoriesList(rs, categories);



        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };

        return categories;

    }

    public static void saveCategories(List<Category> categories) {
        try (Connection connection = connectToDatabase()) {

            for (Category category: categories){
                String insertCategorySql = "INSERT INTO CATEGORY(NAME, DESCRIPTION) VALUES(?, ?);";

                PreparedStatement pstmt = connection.prepareStatement(insertCategorySql);
                pstmt.setString(1, category.getName());
                pstmt.setString(2, category.getDescription());
                pstmt.execute();
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se pogreška kod spremanja kategorija u bazu podataka";
            logger.error(message, ex);
        }

    }




    public static List<Item> getItems(){
        List<Category> categories = getCategories();
        List<Item> items = new ArrayList<>();

        try(Connection connection = connectToDatabase()){
            String sqlQuery = "SELECT * FROM ITEM";
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            mapResultSetToItemsList(rs, categories, items);

        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };


        return items;
    }

    public static List<Item> getItemsByFilters(ItemFilter itemFilter){

        List<Item> items = new ArrayList<>();
        Map<Integer, Object> queryParams = new HashMap<>();
        Integer paramOrdinalNumber = 1;

        try(Connection connection = connectToDatabase()){
            String baseSqlQuery = "SELECT * FROM ITEM WHERE 1=1 ";

            if (!itemFilter.getName().isEmpty()){
                baseSqlQuery += " AND NAME = ?";
                queryParams.put(paramOrdinalNumber, itemFilter.getName());
                paramOrdinalNumber++;
            }

            if (Optional.ofNullable(itemFilter.getCategory()).isPresent()){
                baseSqlQuery += " AND CATEGORY_ID = ?";
                queryParams.put(paramOrdinalNumber, itemFilter.getCategory());
                paramOrdinalNumber++;
            }


            PreparedStatement pstmt = connection.prepareStatement(baseSqlQuery);

            for (Integer paramNumber : queryParams.keySet()){
                if (queryParams.get(paramNumber) instanceof String sqp){
                    pstmt.setString(paramNumber, sqp);
                }
                else if (queryParams.get(paramNumber) instanceof  Category cqp){
                    pstmt.setLong(paramNumber, cqp.getId());
                }
            }

            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();

            mapResultSetToItemsList(rs, getCategories(), items);



        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };


        return items;

    }



    public static void saveItems(List<Item> items) {
        try (Connection connection = connectToDatabase()) {

            for (Item item: items){
                String insertItemSql = "INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST" +
                        ", SELLING_PRICE, DISCOUNT) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

                PreparedStatement pstmt = connection.prepareStatement(insertItemSql);
                pstmt.setInt(1, item.getCategory().getId().intValue());
                pstmt.setString(2, item.getName());
                pstmt.setBigDecimal(3, item.getWidth());
                pstmt.setBigDecimal(4, item.getHeight());
                pstmt.setBigDecimal(5, item.getLength());
                pstmt.setBigDecimal(6, item.getProductionCost());
                pstmt.setBigDecimal(7, item.getSellingPrice());
                pstmt.setInt(8, item.getDiscount().discountAmount());
                pstmt.execute();
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se pogreška kod spremanja artikala u bazu podataka";
            logger.error(message, ex);
        }

    }

    public static List<Store> getStores(){
        List <Item> items = getItems();
        List<Store> stores = new ArrayList<>();

        try(Connection connection = connectToDatabase()){
            String sqlQuery = "SELECT * FROM STORE";
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            mapResultSetToStoresList(rs, items, stores);

        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };


        return stores;
    }

    public static List<Store> getStoresByFilters(StoreFilter storeFilter){
        List<Store> stores = new ArrayList<>();
        Map<Integer, Object> queryParams = new HashMap<>();
        Integer paramOrdinalNumber = 1;

        try(Connection connection = connectToDatabase()){
            String baseSqlQuery = "SELECT * FROM STORE WHERE 1=1 ";

            if (!storeFilter.getName().isEmpty()){
                baseSqlQuery += " AND NAME = ?";
                queryParams.put(paramOrdinalNumber, storeFilter.getName());
                paramOrdinalNumber++;
            }


            PreparedStatement pstmt = connection.prepareStatement(baseSqlQuery);

            for (Integer paramNumber : queryParams.keySet()){
                if (queryParams.get(paramNumber) instanceof String sqp){
                    pstmt.setString(paramNumber, sqp);
                }
            }

            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();

            mapResultSetToStoresList(rs, getItems(), stores);



        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };

        return stores;

    }

    public static Set<Item> getItemsForStore(Long storeId, List<Item> items){
        Set<Item> storeItems = new HashSet<>();
        try(Connection connection = connectToDatabase()){
            String sqlQuery = String.format("SELECT * FROM STORE_ITEM SI, ITEM I WHERE SI.STORE_ID = %d AND SI.ITEM_ID = I.ID", storeId);
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while(rs.next()){
                Long itemId = rs.getLong("ITEM_ID");

                Optional <Item> storeItem = items.stream().filter(item -> item.getId().equals(itemId)).findFirst();
                storeItem.ifPresent(storeItems::add);

            }



        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        }

        return storeItems;
    }

    public static Set<Item> getItemsNotInStore(Long storeId, List<Item> items) {
        Set<Item> nonStoreItems = new HashSet<>();
        try (Connection connection = connectToDatabase()) {

            String sqlQuery = String.format("SELECT I.* FROM ITEM I " +
                    "LEFT JOIN STORE_ITEM SI ON I.ID = SI.ITEM_ID AND SI.STORE_ID = %d " +
                    "WHERE SI.STORE_ID IS NULL", storeId);

            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long itemId = rs.getLong("ID");

                Optional<Item> nonStoreItem = items.stream()
                        .filter(item -> item.getId().equals(itemId)).findFirst();
                nonStoreItem.ifPresent(nonStoreItems::add);
            }

        } catch (SQLException | IOException ex) {
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka prilikom dohvaćanja dostupnih " +
                    "artikala za trgovinu";
            logger.error(message, ex);
        }

        return nonStoreItems;
    }


    public static void saveStores(List<Store> stores) {
        try (Connection connection = connectToDatabase()) {
            for (Store store: stores) {
                String insertStoreSql = "INSERT INTO STORE(NAME, WEB_ADDRESS) VALUES(?, ?);";

                // Use PreparedStatement.RETURN_GENERATED_KEYS flag
                PreparedStatement pstmt = connection.prepareStatement(insertStoreSql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, store.getName());
                pstmt.setString(2, store.getWebAddress());
                pstmt.executeUpdate(); // Use executeUpdate instead of execute

                // Retrieve the generated keys
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int storeId = generatedKeys.getInt(1);

                    for (Item item : store.getItems()) {
                        String insertItemsIntoStoreSql = "INSERT INTO STORE_ITEM(STORE_ID, ITEM_ID) VALUES(?, ?);";
                        pstmt = connection.prepareStatement(insertItemsIntoStoreSql);
                        pstmt.setInt(1, storeId);
                        pstmt.setInt(2, item.getId().intValue());
                        pstmt.executeUpdate(); // Use executeUpdate instead of execute
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            String message = "Dogodila se pogreška kod spremanja trgovina u bazu podataka";
            logger.error(message, ex);
        }
    }

    public static void addItemsToStore(Store store, List<Item> itemsToAdd) {
        try (Connection connection = connectToDatabase()) {
            String insertItemsIntoStoreSql = "INSERT INTO STORE_ITEM(STORE_ID, ITEM_ID) VALUES(?, ?);";

            for (Item item : itemsToAdd) {
                try (PreparedStatement pstmt = connection.prepareStatement(insertItemsIntoStoreSql)) {
                    pstmt.setInt(1, store.getId().intValue());
                    pstmt.setInt(2, item.getId().intValue());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException | IOException ex) {
            String message = "Dogodila se pogreška kod spremanja artikala u bazu podataka";
            logger.error(message, ex);
        }
    }

    public static void deleteItemsFromStore(Store store, List<Item> itemsToRemove) {
        try (Connection connection = connectToDatabase()) {
            String deleteItemsFromStoreSql = "DELETE FROM STORE_ITEM WHERE STORE_ID = ? AND ITEM_ID = ?;";

            for (Item item : itemsToRemove) {
                try (PreparedStatement pstmt = connection.prepareStatement(deleteItemsFromStoreSql)) {
                    pstmt.setInt(1, store.getId().intValue());
                    pstmt.setInt(2, item.getId().intValue());
                    pstmt.executeUpdate();
                }
            }

        } catch (SQLException | IOException ex) {
            String message = "Dogodila se pogreška kod spremanja artikala u bazu podataka";
            logger.error(message, ex);
        }
    }


    public static List<Factory> getFactories(){
        List<Address> addresses = getAddresses();
        List <Item> items = getItems();
        List<Factory> factories = new ArrayList<>();

        try(Connection connection = connectToDatabase()){
            String sqlQuery = "SELECT * FROM FACTORY";
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            mapResultSetToFactoriesList(rs, addresses, items, factories);

        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };


        return factories;
    }


    public static List<Factory> getFactoriesByFilters(FactoryFilter factoryFilter){
        List<Factory> factories = new ArrayList<>();
        Map<Integer, Object> queryParams = new HashMap<>();
        Integer paramOrdinalNumber = 1;

        try(Connection connection = connectToDatabase()){
            String baseSqlQuery = "SELECT * FROM FACTORY WHERE 1=1 ";

            if (!factoryFilter.getName().isEmpty()){
                baseSqlQuery += " AND NAME = ?";
                queryParams.put(paramOrdinalNumber,factoryFilter.getName());
                paramOrdinalNumber++;
            }


            PreparedStatement pstmt = connection.prepareStatement(baseSqlQuery);

            for (Integer paramNumber : queryParams.keySet()){
                if (queryParams.get(paramNumber) instanceof String sqp){
                    pstmt.setString(paramNumber, sqp);
                }
            }

            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();

            mapResultSetToFactoriesList(rs, getAddresses(), getItems(), factories);



        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };

        return factories;

    }


   public static Set<Item> getItemsForFactory(Long factoryId, List<Item> items){
        Set<Item> factoryItems = new HashSet<>();
        try(Connection connection = connectToDatabase()){
            String sqlQuery = String.format("SELECT * FROM FACTORY_ITEM SI, ITEM I WHERE SI.FACTORY_ID = %d AND SI.ITEM_ID = I.ID", factoryId);
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while(rs.next()){
                Long itemId = rs.getLong("ITEM_ID");

                Optional <Item> factoryItem = items.stream().filter(item -> item.getId().equals(itemId)).findFirst();
                factoryItem.ifPresent(factoryItems::add);

            }



        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        }

        return factoryItems;
    }

    public static Set<Item> getItemsNotInFactory(Long factoryId, List<Item> items) {
        Set<Item> nonFactoryItems = new HashSet<>();
        try (Connection connection = connectToDatabase()) {
            
            String sqlQuery = String.format("SELECT I.* FROM ITEM I " +
                    "LEFT JOIN FACTORY_ITEM SI ON I.ID = SI.ITEM_ID AND SI.FACTORY_ID = %d " +
                    "WHERE SI.FACTORY_ID IS NULL", factoryId);

            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Long itemId = rs.getLong("ID");

                Optional<Item> nonFactoryItem = items.stream()
                        .filter(item -> item.getId().equals(itemId)).findFirst();
                nonFactoryItem.ifPresent(nonFactoryItems::add);
            }

        } catch (SQLException | IOException ex) {
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka prilikom dohvaćanja dostupnih " +
                    "artikala za tvornicu";
            logger.error(message, ex);
        }

        return nonFactoryItems;
    }



    public static void saveFactories(List<Factory> factories) {
        try (Connection connection = connectToDatabase()) {
            for (Factory factory: factories) {
                String insertFactorySql = "INSERT INTO FACTORY(NAME, ADDRESS_ID) VALUES(?, ?);";


                PreparedStatement pstmt = connection.prepareStatement(insertFactorySql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, factory.getName());
                pstmt.setInt(2, factory.getAdress().getAddressId().intValue());
                pstmt.executeUpdate();


                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int factoryId = generatedKeys.getInt(1);

                    for (Item item : factory.getItems()) {
                        String insertItemsIntoFactorySql = "INSERT INTO FACTORY_ITEM(FACTORY_ID, ITEM_ID) VALUES(?, ?);";
                        pstmt = connection.prepareStatement(insertItemsIntoFactorySql);
                        pstmt.setInt(1, factoryId);
                        pstmt.setInt(2, item.getId().intValue());
                        pstmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            String message = "Dogodila se pogreška kod spremanja trgovina u bazu podataka";
            logger.error(message, ex);
        }
    }

    public static void addItemsToFactory(Factory factory, List<Item> itemsToAdd){
        try (Connection connection = connectToDatabase()) {

            String insertItemsIntoFactorySql = "INSERT INTO FACTORY_ITEM(FACTORY_ID, ITEM_ID) VALUES(?, ?);";

            for (Item item : itemsToAdd) {
                PreparedStatement pstmt = connection.prepareStatement(insertItemsIntoFactorySql);
                pstmt.setInt(1, factory.getId().intValue());
                pstmt.setInt(2, item.getId().intValue());
                pstmt.executeUpdate();
            }
        }catch (SQLException | IOException ex) {
            String message = "Dogodila se pogreška kod spremanja artikala u bazu podataka";
            logger.error(message, ex);
        }
    }

    public static void deleteItemsFromFactory(Factory factory, List<Item> itemsToRemove) {
        try (Connection connection = connectToDatabase()) {

            String deleteItemsFromFactorySql = "DELETE FROM FACTORY_ITEM WHERE FACTORY_ID = ? AND ITEM_ID = ?;";

            for (Item item : itemsToRemove) {
                try (PreparedStatement pstmt = connection.prepareStatement(deleteItemsFromFactorySql)) {
                    pstmt.setInt(1, factory.getId().intValue());
                    pstmt.setInt(2, item.getId().intValue());
                    pstmt.executeUpdate();
                }
            }

        } catch (SQLException | IOException ex) {
            String message = "Dogodila se pogreška kod brisanja artikala iz baze podataka";
            logger.error(message, ex);
        }
    }




    public static List<Address> getAddresses(){
        List<Address> addresses = new ArrayList<>();

        try(Connection connection = connectToDatabase()){
            String sqlQuery = "SELECT * FROM ADDRESS";
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while(rs.next()){

               Long addressId = rs.getLong("ID");
               String streetName = rs.getString("STREET");
               String houseNumber = rs.getString("HOUSE_NUMBER");
               City city = City.getCityFromStringName(rs.getString("CITY"));
               Address.AdressBuilder address = new Address.AdressBuilder(city)
                       .setHouseNumber(houseNumber)
                       .setId(addressId)
                       .setStreet(streetName);

               addresses.add(address.build());
            }

        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };

        return  addresses;

    }

    public static void saveAddresses(List<Address> addresses) {
        try (Connection connection = connectToDatabase()) {

            for (Address address: addresses){
                String insertCategorySql = "INSERT INTO ADDRESS(STREET, HOUSE_NUMBER, CITY, POSTAL_CODE) VALUES(?, ?, ?, ?);";

                PreparedStatement pstmt = connection.prepareStatement(insertCategorySql);
                pstmt.setString(1, address.getStreet());
                pstmt.setString(2, address.getHouseNumber());
                pstmt.setString(3, address.getCity().getName());
                pstmt.setString(4, address.getCity().getPostalCode());
                pstmt.execute();
            }


        } catch (SQLException | IOException ex) {
            String message = "Dogodila se pogreška kod spremanja adresa u bazu podataka";
            logger.error(message, ex);
        }

    }

    private static void mapResultSetToItemsList(ResultSet rs, List<Category> categories, List<Item> items) throws SQLException {
        while(rs.next()){

            Long itemId = rs.getLong("ID");
            Long categoryId = rs.getLong("CATEGORY_ID");
            String itemName = rs.getString("NAME");
            BigDecimal itemWidth = rs.getBigDecimal("WIDTH");
            BigDecimal itemHeight = rs.getBigDecimal("HEIGHT");
            BigDecimal itemLength = rs.getBigDecimal("LENGTH");
            BigDecimal itemProductionCost = rs.getBigDecimal("PRODUCTION_COST");
            BigDecimal itemSellingPrice = rs.getBigDecimal("SELLING_PRICE");
            Integer itemDiscount = rs.getInt("DISCOUNT");

            Category itemCategory = categories.stream()
                    .filter(category -> category.getId().equals(categoryId)).findFirst().get();

            items.add(new Item(itemId, itemName, itemCategory, itemWidth, itemHeight, itemLength, itemProductionCost,
                    itemSellingPrice, new Discount(itemDiscount)));

        }
    }

    private static void mapResultSetToStoresList(ResultSet rs, List<Item> items, List<Store> stores) throws SQLException {

        while(rs.next()){
            Long storeId = rs.getLong("ID");
            String storeName = rs.getString("NAME");
            String storeWebAddress = rs.getString("WEB_ADDRESS");
            Set<Item> storeItems = getItemsForStore(storeId, items);

            stores.add(new Store(storeId, storeName, storeWebAddress, storeItems));

        }
    }

    private static void mapResultSetToFactoriesList(ResultSet rs, List<Address> addresses, List<Item> items, List<Factory> factories) throws SQLException {
        while(rs.next()){

            Long factoryId = rs.getLong("ID");
            String factoryName = rs.getString("NAME");
            Long factoryAddressId = rs.getLong("ADDRESS_ID");

            Address factoryAddress = addresses.stream()
                    .filter(address -> address.getAddressId().equals(factoryAddressId))
                    .findFirst()
                    .get();

            Set<Item> factoryItems = getItemsForFactory(factoryId, items);

            factories.add(new Factory(factoryId, factoryName, factoryAddress, factoryItems));
        }
    }

    private static void mapResultSetToCategoriesList(ResultSet rs, List<Category> categories) throws SQLException {
        while(rs.next()){

            Long categoryId = rs.getLong("ID");
            String categoryName = rs.getString("NAME");
            String categoryDescription = rs.getString("DESCRIPTION");

            categories.add(new Category(categoryId, categoryName, categoryDescription));

        }
    }

}
