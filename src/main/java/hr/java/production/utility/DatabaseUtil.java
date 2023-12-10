package hr.java.production.utility;

import hr.java.production.enums.City;
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

            while(rs.next()){

                Long categoryId = rs.getLong("ID");
                String categoryName = rs.getString("NAME");
                String categoryDescription = rs.getString("DESCRIPTION");

                categories.add(new Category(categoryId, categoryName, categoryDescription));

            }

        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };


        return categories;
    }


    public static List<Item> getItems(){
        List<Category> categories = getCategories();
        List<Item> items = new ArrayList<>();

        try(Connection connection = connectToDatabase()){
            String sqlQuery = "SELECT * FROM ITEM";
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

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

        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };


        return items;
    }


    public static List<Store> getStores(){
        List <Item> items = getItems();
        List<Store> stores = new ArrayList<>();

        try(Connection connection = connectToDatabase()){
            String sqlQuery = "SELECT * FROM STORE";
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while(rs.next()){

                Long storeId = rs.getLong("ID");
                String storeName = rs.getString("NAME");
                String storeWebAddress = rs.getString("WEB_ADDRESS");
                Set<Item> storeItems = getItemsForStore(storeId, items);

                stores.add(new Store(storeId, storeName, storeWebAddress, storeItems));


            }

        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };


        return stores;
    }

    private static Set<Item> getItemsForStore(Long storeId, List<Item> items){
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

    public static List<Factory> getFactories(){
        List<Address> addresses = getAddresses();
        List <Item> items = getItems();
        List<Factory> factories = new ArrayList<>();

        try(Connection connection = connectToDatabase()){
            String sqlQuery = "SELECT * FROM FACTORY";
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

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

        }catch (SQLException | IOException ex){
            String message = "Dogodila se pogreška kod povezivanja na bazu podataka";
            logger.error(message, ex);
        };


        return factories;
    }


    private static Set<Item> getItemsForFactory(Long factoryId, List<Item> items){
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


}
