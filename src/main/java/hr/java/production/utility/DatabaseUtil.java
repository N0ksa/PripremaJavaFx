package hr.java.production.utility;

import hr.java.production.model.Category;
import hr.java.production.model.Discount;
import hr.java.production.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
}
