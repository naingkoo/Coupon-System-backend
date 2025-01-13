package com.coupon.service;

import com.coupon.entity.*;
import com.coupon.model.CouponDTO;

import com.coupon.reposistory.CouponRepository;
import com.coupon.reposistory.PackageRepository;
import com.coupon.reposistory.PurchaseRepository;
import com.coupon.reposistory.QRRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CouponService {

    @Autowired
    ModelMapper mapper;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private QRRepository qrRepository;

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    public CouponService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public void confirmPurchaseAndGenerateQR(Integer purchaseId) {

        // Update confirm status for the purchase entity
        PurchaseEntity purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("No purchase found for the given ID."));
        purchase.setConfirm(false);
        purchaseRepository.save(purchase);

        // Retrieve all coupons by purchase ID
        List<CouponEntity> coupons = couponRepository.findByPurchaseId(purchaseId);

        if (coupons.isEmpty()) {
            throw new IllegalArgumentException("No coupons found for the given purchase ID.");
        }

        // Update confirm status for all coupons
        coupons.forEach(coupon -> coupon.setConfirm(ConfirmStatus.CONFIRM));
        couponRepository.saveAll(coupons);

        // Generate QR codes for each coupon and save to QR table
        List<QREntity> qrEntities = coupons.stream().map(coupon -> {
            QREntity qr = new QREntity();
            qr.setCode(generateRandomCode(50)); // Generate random 50-character code
            qr.setCoupon(coupon);
            return qr;
        }).collect(Collectors.toList());

        qrRepository.saveAll(qrEntities);

        // Retrieve the user associated with the purchase
        UserEntity user = purchase.getUser(); // Assuming the PurchaseEntity has a `getUser()` method to get the user

        if (user == null) {
            throw new IllegalArgumentException("No user found for the given purchase.");
        }

        // Send a notification to the specific user who made the purchase
        messagingTemplate.convertAndSend("/queue/"+user.getId().toString(), "Your coupon is ready to use.");
        System.out.print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa : " + user.getId());
    }

    private String generateRandomCode(int length) {
        final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:'\",.<>?/~";
        StringBuilder code = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            code.append(CHAR_POOL.charAt(index));
        }
        return code.toString();
    }

    public List<CouponDTO> showCouponbyUserId(Integer userId) {

        List<CouponEntity> coupon = couponRepository.findCouponsByUserId(userId);

        List<CouponDTO> dtoList = new ArrayList<>();
        for(CouponEntity entity: coupon) {

            if (entity.getTransfer_status() == true) {
                CouponDTO dto = new CouponDTO();
                dto.setId(entity.getId());
                dto.setExpired_date(entity.getExpired_date());
                dto.setConfirm(entity.getConfirm().name());
                if (entity.getPackageEntity() != null) {
                    dto.setPackageName(entity.getPackageEntity().getName());
                    dto.setImage(entity.getPackageEntity().getImage());
                }


                System.out.println("packageName: " + dto.getPackageName());
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    public void declinePurchaseAndRestoreQuantity(Integer purchaseId) {

        // Update confirm status for the purchase entity
        PurchaseEntity purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("No purchase found for the given ID."));
        purchase.setConfirm(false);
        purchaseRepository.save(purchase);

        // Retrieve all coupons associated with the purchase
        List<CouponEntity> coupons = couponRepository.findByPurchaseId(purchaseId);

        if (coupons.isEmpty()) {
            throw new IllegalArgumentException("No coupons found for the given purchase ID.");
        }

        // Restore coupon quantities in their respective packages
        coupons.forEach(coupon -> {
            PackageEntity packageEntity = coupon.getPackageEntity();
            if (packageEntity != null) {
                packageEntity.setQuantity(packageEntity.getQuantity() + 1);
                packageRepository.save(packageEntity); // Update package in the database
            }
        });

        // Update confirm status for all coupons to "DECLINE"
        coupons.forEach(coupon -> coupon.setConfirm(ConfirmStatus.DECLINED));
        couponRepository.saveAll(coupons);
    }

    public List<CouponDTO> showCouponbyPurchaseId(Integer purchase_id) {

        List<CouponEntity> coupon = couponRepository.findByPurchaseId(purchase_id);

        List<CouponDTO> dtoList = new ArrayList<>();
        for(CouponEntity entity: coupon) {

            CouponDTO dto = new CouponDTO();
            dto.setId(entity.getId());
            dto.setExpired_date(entity.getExpired_date());
            dto.setConfirm(entity.getConfirm().name());
            if (entity.getPackageEntity() != null) {
                dto.setPackageName(entity.getPackageEntity().getName());
                dto.setUnit_price(entity.getPackageEntity().getUnit_price());
                dto.setImage(entity.getPackageEntity().getImage());
            }


            System.out.println("packageName: " + dto.getPackageName());
            dtoList.add(dto);
        }

        return dtoList;
    }


    public List<CouponDTO> findall() {
        List<CouponEntity> couponEntityList = couponRepository.findAllCouponsWithPurchaseAndUser();

        return couponEntityList.stream().map(coupon -> {
            CouponDTO dto = new CouponDTO();
            dto.setId(coupon.getId());
            dto.setExpired_date(coupon.getExpired_date());
            dto.setConfirm(coupon.getConfirm().name());
            dto.setUsed_status(coupon.getUsed_status());
            dto.setTransfer_status(coupon.getTransfer_status());
            dto.setUnit_price(coupon.getPackageEntity().getUnit_price());
            dto.setPurchase_date(coupon.getPurchase().getPurchase_date());
            dto.setImage(coupon.getPackageEntity().getImage());
            dto.setPackageName(coupon.getPackageEntity().getName());


            // Map relationships
            dto.setPurchase_id(coupon.getPurchase() != null ? coupon.getPurchase().getId() : null);
            dto.setPackage_id(coupon.getPackageEntity() != null ? coupon.getPackageEntity().getId() : null);

            return dto;
        }).toList();
    }


    public List<CouponDTO> findallByUserId(Integer userId) {
        List<CouponEntity> couponEntity = couponRepository.findbyUserId(userId);
        List<CouponDTO> dtoList = couponEntity.stream().map(coupon -> {
            CouponDTO dto = mapper.map(coupon, CouponDTO.class);
            return dto;
        }).toList();
        return dtoList;
    }

    public void exportCouponReport(HttpServletResponse response) {
        try {

            List<CouponDTO> couponList = findall();

            InputStream reportStream = getClass().getResourceAsStream("/reports/reportCoupon.jrxml");

            if (reportStream == null) {
                throw new RuntimeException("Jasper report template not found in resources/reports/coupon_report.jrxml");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(couponList);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CreatedBy", "Coupon Admin");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=coupons_report.pdf");

            ServletOutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            outputStream.flush();
            outputStream.close();

        } catch (JRException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while generating the report: " + e.getMessage());
        }
    }

    public void exportCouponReportToExcel(HttpServletResponse response) {
        try {
            // Fetch data
            List<CouponDTO> couponList = findall();

            // Load the report template
            InputStream reportStream = getClass().getResourceAsStream("/reports/reportCoupon.jrxml");
            if (reportStream == null) {
                throw new RuntimeException("Jasper report template not found in resources/reports/coupon_report.jrxml");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(couponList);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CreatedBy", "Coupon Admin");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=coupons_report.xlsx");

            JRXlsxExporter exporter = new JRXlsxExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(false);  // Set true to create a sheet for each page
            configuration.setDetectCellType(true);   // Detect cell types (e.g., numbers, text)
            exporter.setConfiguration(configuration);

            // Export the report
            exporter.exportReport();

        } catch (JRException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while generating the Excel report: " + e.getMessage());
        }
    }

}






