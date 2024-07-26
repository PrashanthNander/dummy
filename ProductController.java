package com.gerimedica.assignment.controller;

import com.gerimedica.assignment.model.Product;
import com.gerimedica.assignment.service.ProductService;
import com.gerimedica.assignment.util.CsvUtility;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Prashanth Nander
 */
@Tag(
        name = "REST APIs for Gerimedica application",
        description = "REST APIs UPLOAD, FETCH and DELETE products inside Gerimedica App    "
)
@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (CsvUtility.hasCsvFormat(file)) {
            try {
                productService.save(file);
                message = "The csv file uploaded successfully: " + file.getOriginalFilename();
                return new ResponseEntity<>(message , HttpStatus.OK);
            } catch (Exception e) {
                message = "The csv file is not upload successfully: " + file.getOriginalFilename() + "!";
                return new ResponseEntity<>(message , HttpStatus.EXPECTATION_FAILED);
            }
        }
        message = "Only CSV file upload is supported.!";
        return new ResponseEntity<>(message , HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/products")
    public ResponseEntity<List<Product>> fetchAllProducts() {
        List<Product> products = productService.fetchAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/product/{code}")
    public ResponseEntity<Product> fetchProductByCode(@PathVariable String code) {
        Product product = productService.fetchProductByCode(code);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/products")
    public ResponseEntity<String> deleteProducts() {
        productService.deleteAllProducts();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
