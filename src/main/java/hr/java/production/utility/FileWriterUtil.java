package hr.java.production.utility;

import hr.java.production.constants.Constants;
import hr.java.production.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FileWriterUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileReaderUtil.class);

    public static Long getNextItemId(){
        List<Item> items = FileReaderUtil.getItemsFromFile(FileReaderUtil.getCategoriesFromFile());

       Long itemId = items.stream().map(NamedEntity::getId).max(Long::compareTo).get();
       return itemId + 1;
    }

    public static Long getNextCategoryId(){
        List<Category> categories = FileReaderUtil.getCategoriesFromFile();

        Long itemId = categories.stream().map(NamedEntity::getId).max(Long::compareTo).get();
        return itemId + 1;
    }


    public static void saveItemsToFile(List<Item> items){
        File itemsFile = new File(Constants.ITEMS_FILE_NAME);

        try(PrintWriter pw = new PrintWriter(itemsFile)){
            for (Item item: items){
                pw.println(item.getId());
                pw.println(item.getName());
                pw.println(item.getCategory().getId());
                pw.println(item.getWidth());
                pw.println(item.getHeight());
                pw.println(item.getLength());
                pw.println(item.getProductionCost());
                pw.println(item.getSellingPrice());
                pw.println(item.getDiscount().discountAmount());

                if (item instanceof Edible edibleItem){
                    if(item instanceof Apple appleItem){
                        pw.println("Apple");
                    }else if (item instanceof PeanutButter peanutButterItem){
                        pw.println("Peanut");
                    }

                    pw.println(edibleItem.getWeight());

                }else if (item instanceof Technical technicalItem){
                    pw.println("Laptop");
                    pw.println(technicalItem.getWarrantyDurationInMonths());
                }
                else{
                    pw.println("Item");
                }
            }


        }catch(IOException ex){
            String message = "Dogodila se pogreška kod pisanja datoteke - + " + Constants.ITEMS_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);

        }
    }

   static public void saveCategoriesToFile(List<Category> categories){
        File categoriesFile = new File(Constants.CATEGORIES_FILE_NAME);
        try(PrintWriter pw = new PrintWriter(categoriesFile)){
            for (Category category: categories){
                pw.println(category.getId());
                pw.println(category.getName());
                pw.println(category.getDescription());
            }

        }catch(IOException ex){
            String message = "Dogodila se pogreška kod pisanja datoteke - + " + Constants.CATEGORIES_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);

        }
    }
}
