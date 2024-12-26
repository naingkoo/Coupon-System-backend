package com.coupon.service;

import com.coupon.entity.CartEntity;
import com.coupon.entity.PackageEntity;
import com.coupon.entity.UserEntity;
import com.coupon.model.CartDTO;
import com.coupon.model.PackageDTO;
import com.coupon.reposistory.CartRepository;
import com.coupon.reposistory.PackageRepository;
import com.coupon.reposistory.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;

    public CartDTO addItemToCart(CartDTO cartDTO) {
        // Fetch UserEntity and PackageEntity from the repository
        UserEntity user = userRepository.findById(cartDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("User  not found"));
        PackageEntity packageEntity = packageRepository.findById(cartDTO.getPackage_id())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        // Check if a CartEntity already exists for the same user and package
        CartEntity existingCartEntity = cartRepository.findByUserAndPackageEntity(user, packageEntity);

        if (existingCartEntity != null) {
            // Update existing entry
            existingCartEntity.setUnit_quantity(existingCartEntity.getUnit_quantity() + cartDTO.getUnit_quantity());
            existingCartEntity.setUnit_price(existingCartEntity.getUnit_price() + cartDTO.getUnit_price());

            // Save the updated CartEntity
            CartEntity updatedCartEntity = cartRepository.save(existingCartEntity);

            // Convert updated CartEntity back to CartDTO
            CartDTO updatedCartDTO = new CartDTO();
            updatedCartDTO.setId(updatedCartEntity.getId());
            updatedCartDTO.setUser_id(updatedCartEntity.getUser().getId());
            updatedCartDTO.setPackage_id(updatedCartEntity.getPackageEntity().getId());
            updatedCartDTO.setUnit_quantity(updatedCartEntity.getUnit_quantity());
            updatedCartDTO.setUnit_price(updatedCartEntity.getUnit_price());

            return updatedCartDTO;
        } else {
            // Create a new CartEntity
            CartEntity cartEntity = new CartEntity();
            cartEntity.setUser(user);
            cartEntity.setPackageEntity(packageEntity);
            cartEntity.setUnit_quantity(cartDTO.getUnit_quantity());
            cartEntity.setUnit_price(cartDTO.getUnit_price());

            // Save the new CartEntity
            CartEntity savedCartEntity = cartRepository.save(cartEntity);

            // Convert saved CartEntity back to CartDTO
            CartDTO savedCartDTO = new CartDTO();
            savedCartDTO.setId(savedCartEntity.getId());
            savedCartDTO.setUser_id(savedCartEntity.getUser().getId());
            savedCartDTO.setPackage_id(savedCartEntity.getPackageEntity().getId());
            savedCartDTO.setUnit_quantity(savedCartEntity.getUnit_quantity());
            savedCartDTO.setUnit_price(savedCartEntity.getUnit_price());

            return savedCartDTO;
        }
    }

    public List<CartDTO> getCartByUserId(Integer userId) {
        List<CartEntity> cartEntities = cartRepository.findByUserIdWithPackageData(userId);
        return cartEntities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private CartDTO convertToDTO(CartEntity cartEntity) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cartEntity.getId());
        cartDTO.setUser_id(cartEntity.getUser().getId());
        cartDTO.setPackage_id(cartEntity.getPackageEntity().getId());
        cartDTO.setUnit_quantity(cartEntity.getUnit_quantity());
        cartDTO.setUnit_price(cartEntity.getUnit_price());

        // Populate package details
        PackageDTO packageDTO = new PackageDTO();
        packageDTO.setId(cartEntity.getPackageEntity().getId());
        packageDTO.setName(cartEntity.getPackageEntity().getName());
        packageDTO.setUnit_price(cartEntity.getPackageEntity().getUnit_price());
        packageDTO.setQuantity(cartEntity.getPackageEntity().getQuantity());
        packageDTO.setCreate_date(cartEntity.getPackageEntity().getCreate_date());

        // Set the image with validation
        String imagePath = cartEntity.getPackageEntity().getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            if (!imagePath.startsWith("/images/")) {
                imagePath = "/images/" + imagePath;
            }
        }
        packageDTO.setImage(imagePath);

        packageDTO.setDescription(cartEntity.getPackageEntity().getDescription());
        packageDTO.setDeleted(cartEntity.getPackageEntity().isDelete());
        packageDTO.setBusiness_id(cartEntity.getPackageEntity().getBusiness() != null ? cartEntity.getPackageEntity().getBusiness().getId() : null);

        cartDTO.setPackageDetails(packageDTO); // Set package details in CartDTO

        return cartDTO;
    }

    @Transactional
    public boolean deleteCartItem(Integer id) {
        // Check if the item exists
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id); // Delete the item
            return true; // Item was successfully deleted
        }
        return false; // Item not found
    }

    public CartEntity updateCartItem(Integer cartId, Integer newQuantity) {
        Optional<CartEntity> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            CartEntity cartEntity = optionalCart.get();
            double packageUnitPrice = getPackageUnitPrice(cartEntity.getPackageEntity().getId()   ); // Retrieve package unit price
            double newUnitPrice = packageUnitPrice * newQuantity; // Calculate new unit price based on quantity
            cartEntity.setUnit_quantity(newQuantity); // Update the unit quantity
            cartEntity.setUnit_price(newUnitPrice); // Update the unit price
            return cartRepository.save(cartEntity);
        }
        return null; // or throw an exception
    }

    public CartEntity increaseQuantity(Integer cartId) {
        return updateCartItem(cartId, getCurrentQuantity(cartId) + 1); // Increase quantity by 1
    }

    public CartEntity decreaseQuantity(Integer cartId) {
        int currentQuantity = getCurrentQuantity(cartId);
        if (currentQuantity > 1) {
            return updateCartItem(cartId, currentQuantity - 1); // Decrease quantity by 1
        }
        return null; // or throw an exception if you want to handle it differently
    }

    private int getCurrentQuantity(Integer cartId) {
        return cartRepository.findById(cartId)
                .map(CartEntity::getUnit_quantity)
                .orElse(0);
    }

    private double getPackageUnitPrice(Integer packageId) {
        // Assuming you have a packageRepository to fetch package details
        return packageRepository.findById(packageId)
                .map(PackageEntity::getUnit_price) // Get the unit price from the package entity
                .orElse(0.0); // Return 0.0 if not found
    }
}
