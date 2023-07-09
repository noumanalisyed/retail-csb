package com.retail.csb.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.retail.csb.common.Utils;
import com.retail.csb.common.api.APIDataObject;
import com.retail.csb.constant.Constants;
import com.retail.csb.model.cache.PublicApiKey;
import com.retail.csb.model.product.ProductReview;
import com.retail.csb.model.vm.ReviewData;
import com.retail.csb.model.vm.product.ProductVm;
import com.retail.csb.repository.ContactRepository;
import com.retail.csb.repository.ProductRepository;
import com.retail.csb.repository.ProductReviewRepository;
import com.retail.csb.security.JWTUtils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService extends ApiAccessService<PublicApiKey> {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ProductReviewRepository reviewRepository;

    private static ModelMapper modelMapper = new ModelMapper();

    public APIDataObject getAllProducts(final String userToken, final Integer page) {
        try {
            final var userDetail = JWTUtils.getUserFromToken(userToken);
            final var products = productRepository.findAllByBusinessId(userDetail.getBusinessId(),
                    Utils.defaultPage(page, Constants.pageSize));
            return APIDataObject.builder().error(false).data(products).build();
        } catch (final Exception e) {
            return APIDataObject.builder().error(true).data("Invalid user details.").build();
        }
    }

    public APIDataObject getAllProductsByBrand(final String userToken, final String brand, final Integer page) {
        try {
            final var userDetail = JWTUtils.getUserFromToken(userToken);
            final var products = productRepository.findAllByBrandName(String.valueOf(userDetail.getBusinessId()), brand,
                    Utils.defaultPage(page, Constants.pageSize));
            return APIDataObject.builder().error(false).data(products).build();
        } catch (final Exception e) {
            return APIDataObject.builder().error(true).data("Invalid user details.").build();
        }
    }

    public APIDataObject searchProducts(String userToken, String query, Integer page) {
        try {
            final var userDetail = JWTUtils.getUserFromToken(userToken);
            final var products = productRepository.findByNameAllIgnoringCaseContainingAndBusinessId(query,
                    userDetail.getBusinessId(), Utils.defaultPage(page, Constants.pageSize));
            return APIDataObject.builder().error(false).data(products).build();
        } catch (final Exception e) {
            return APIDataObject.builder().error(true).data("Invalid user details.").build();
        }
    }

    public APIDataObject getAllPublicProducts(final String apiKey, final Integer page, final Integer pageSize,
            final String sortBy, final String sortDirection, Optional<String> searchTerm) {
        try {
            var accessKey = getAccessDetails(apiKey); // * Get api key from cache

            if (searchTerm.isPresent() && !searchTerm.get().isBlank()) {
                var searchResults = Stream.of(
                        productRepository.findFirst25ByNameAllIgnoringCaseContainingAndBusinessId(searchTerm.get(),
                                accessKey.getBusinessId()))
                        .map(CompletableFuture::join).flatMap(result -> result.stream()).collect(Collectors.toSet())
                        .stream().map(product -> modelMapper.map(product, ProductVm.class))
                        .collect(Collectors.toCollection(ArrayList::new));
                return APIDataObject.builder().error(false).data(searchResults).build();
            }

            final var products = productRepository.findAllByBusinessId(accessKey.getBusinessId(),
                    Utils.defaultPage(page, pageSize == null ? Constants.pageSize : pageSize, sortBy, sortDirection));

            // * Cut down data to be sent for public endpoint
            // * Use the product vm and map the fields that are needed
            // * Create a page instance and send the new page in response
            final var productVmList = products.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductVm.class))
                    .collect(Collectors.toCollection(ArrayList::new));

            final var newPage = new PageImpl<ProductVm>(productVmList);
            return APIDataObject.builder().error(false).data(newPage).build();
        } catch (final Exception e) {
            log.error("{}", e.getMessage());
            return APIDataObject.builder().error(true).data("Invalid api keys.").build();
        }
    }

    public APIDataObject getAProduct(String apiKey, Integer id) {
        try {
            var accessKey = getAccessDetails(apiKey); // * Get api key from cache

            var product = productRepository.findById(id).orElseThrow(() -> new Exception("Product not found"));

            return APIDataObject.builder().error(false).data(product).build();
        } catch (final Exception e) {
            log.error("{}", e.getMessage());
            return APIDataObject.builder().error(true).data("Invalid api keys.").build();
        }
    }

    public void addReview(ReviewData review){
        ProductReview reviewToAdd = new ProductReview();
        reviewToAdd.setProducts(productRepository.findById(Long.parseLong(review.getProduct())));
        reviewToAdd.setContacts(contactRepository.findById(review.getUser()).get());
        reviewToAdd.setReviewText(review.getReview());
        reviewToAdd.setRating(review.getRating());
        reviewToAdd.setDate(new Date(System.currentTimeMillis()));
        reviewRepository.save(reviewToAdd);
    }

    public APIDataObject getReview(Long Id){
        try {
            List<ProductReview> reviewToSend = reviewRepository.findByProducts_Id(Id);
           log.info("{}", reviewToSend);
            return APIDataObject.builder().error(false).data(reviewToSend).build();
        } catch (final Exception e) {
            log.error("{}", e.getMessage());
            return APIDataObject.builder().error(true).data("Invalid api keys.").build();
        }

    }
}
