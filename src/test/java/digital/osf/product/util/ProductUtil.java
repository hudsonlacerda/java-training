package digital.osf.product.util;

import java.util.List;

import digital.osf.product.model.Brand;
import digital.osf.product.model.Category;
import digital.osf.product.model.Product;
import digital.osf.product.request.ProductRequestBody;

public class ProductUtil {

        public static ProductRequestBody productRequestBody() {
                return ProductRequestBody.builder().productName("Shotgun").brand(1).category(1).listPrice(1.0).modelYear(2000).build();
        }

        public static Product productToPersist() {
                Brand brand = brandToPersist();
                Category gunCategory = categoryToPersist();
                return Product.builder().productName("Shotgun")
                                .modelYear(2000)
                                .listPrice(1d)
                                .category(gunCategory)
                                .brand(brand)
                                .build();
        }

        public static Product savedProduct() {
                Brand brand = brandToPersist();
                Category gunCategory = categoryToPersist();
                return Product.builder().id(1).productName("Shotgun")
                                .listPrice(1.0)
                                .modelYear(2000)
                                .brand(brand)
                                .category(gunCategory)
                                .build();
        }

        public static List<Product> listProductToPersist() {
                Brand brand = brandToPersist();
                Category gunCategory = categoryToPersist();
                Category foodCategory = Category.builder().categoryName("FOOD").build();
                return List.of(
                                Product.builder().modelYear(2000).listPrice(1d).productName("Shotgun").brand(brand)
                                                .category(gunCategory).build(),
                                Product.builder().modelYear(2000).listPrice(1d).productName("Machinegun").brand(brand)
                                                .category(gunCategory).build(),
                                Product.builder().modelYear(2000).listPrice(1d).productName("Revolver").brand(brand)
                                                .category(gunCategory).build(),
                                Product.builder().modelYear(2000).listPrice(1d).productName("Pizza").brand(brand)
                                                .category(foodCategory).build());
        }

        public static List<Product> savedProducts() {
                Brand brand = brandToPersist();
                Category gunCategory = categoryToPersist();
                Category foodCategory = Category.builder().categoryName("FOOD").build();
                return List.of(
                                Product.builder().id(1).modelYear(2000).listPrice(1d).productName("Shotgun")
                                                .brand(brand)
                                                .category(gunCategory).build(),
                                Product.builder().id(2).modelYear(2000).listPrice(1d).productName("Machinegun")
                                                .brand(brand)
                                                .category(gunCategory).build(),
                                Product.builder().id(3).modelYear(2000).listPrice(1d).productName("Revolver")
                                                .brand(brand)
                                                .category(gunCategory).build(),
                                Product.builder().id(4).modelYear(2000).listPrice(1d).productName("Pizza").brand(brand)
                                                .category(foodCategory).build());
        }

        public static Brand brandToPersist() {
                return Brand.builder().brandName("GUN'S PIZZA").build();
        }

        public static Category categoryToPersist() {
                Category gunCategory = Category.builder().categoryName("GUN").build();
                return gunCategory;
        }
}
