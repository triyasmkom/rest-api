package com.ths.restapi.controller;

import com.ths.restapi.dto.ResponseData;
import com.ths.restapi.dto.SearchData;
import com.ths.restapi.dto.SupplierData;
import com.ths.restapi.entity.Product;
import com.ths.restapi.entity.Supplier;
import com.ths.restapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ResponseData<Product>> create(@Valid @RequestBody Product product, Errors errors){

        ResponseData<Product> responseData = new ResponseData<>();

        // cek error (ada/tidak)
        if(errors.hasErrors()){
            // lakukan sesuatu
            for (ObjectError e: errors.getAllErrors()) {
                //System.err.println(errors.getAllErrors());
                responseData.getMessage().add(e.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            //throw new RuntimeException("Validation Error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        responseData.setStatus(true);
        responseData.getMessage().add("Sukses");
        responseData.setPayload(productService.save(product));

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @GetMapping
    public Iterable<Product> findAll(@RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted){
        return productService.findAll(isDeleted);
    }

    @GetMapping("/{id}")
    public Product findAll(@PathVariable("id") Long id){
        return productService.findOne(id);
    }

    @PutMapping
    public ResponseEntity<ResponseData<Product>> update(@Valid @RequestBody Product product, Errors errors){
        ResponseData responseData = new ResponseData<>();
        // cek error (ada/tidak)
        if(errors.hasErrors()){
            // lakukan sesuatu
            for (ObjectError e: errors.getAllErrors()) {
                       responseData.getMessage().add(e.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            //throw new RuntimeException("Validation Error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        responseData.setStatus(true);
        responseData.getMessage().add("Sukses");
        responseData.setPayload(productService.save(product));

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        productService.removeOne(id);
    }

    @PostMapping("/{id}")
    public void addSupplier(@RequestBody Supplier supplier, @PathVariable("id") Long productId){
        productService.addSupplier(supplier, productId);
    }

    @PostMapping("/search/name")
    public Product getProductByName(@RequestBody SearchData searchData){
        return productService.findProductByName(searchData.getSearchKey());
    }

    @PostMapping("/search/name_like")
    public List<Product> getProductByNameLike(@RequestBody SearchData searchData){
        return productService.findProductByNameLike(searchData.getSearchKey());
    }

    @GetMapping("/search/category/{categoryId}")
    public List<Product> getProductByCategory(@PathVariable("categoryId") Long categoryId){
        return productService.findProductByCategory(categoryId);
    }

    @GetMapping("/search/supplier/{supplierId}")
    public List<Product> getProductBySupplier(@PathVariable("supplierId") Long supplierId){
        return productService.findProductBySupplier(supplierId);
    }
}