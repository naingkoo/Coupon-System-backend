package com.coupon.service;

import com.coupon.entity.*;
import com.coupon.model.BusinessDTO;
import com.coupon.model.CouponDTO;

import com.coupon.model.NotificationDTO;
import com.coupon.reposistory.*;
import com.coupon.responObject.ResourceNotFoundException;
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
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponService {
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    IsUsedRepository isUsedRepository;

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
        purchase.setConfirm(ConfirmStatus.CONFIRM);
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
        purchase.setConfirm(ConfirmStatus.DECLINED);
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

        UserEntity user = purchase.getUser();

        // Update confirm status for all coupons to "DECLINE"
        coupons.forEach(coupon -> coupon.setConfirm(ConfirmStatus.DECLINED));
        couponRepository.saveAll(coupons);

        messagingTemplate.convertAndSend("/queue/"+user.getId().toString(), "Your payment is declined!");
        System.out.print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa : " + user.getId());
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

    public long countConfirmedCoupons() {
        return couponRepository.countByConfirm(ConfirmStatus.CONFIRM);
    }


    public Long countConfirmedCouponsByBusinessId(Long businessId) {
        return couponRepository.countByConfirmAndBusinessId(ConfirmStatus.CONFIRM, businessId);
    }

    public CouponDTO searchCoupon(Integer bussinessID, String couponcode) throws ResourceNotFoundException, IOException {
        CouponEntity couponEntity=couponRepository.searchCoupon(bussinessID,couponcode).orElseThrow(()->new ResourceNotFoundException("CouponCode not found"));
        couponEntity.getPackageEntity().setBusiness(null);

        if(!couponEntity.getUsed_status() || couponEntity.getExpired_date().before(new Date(System.currentTimeMillis()))){
            throw new ResourceNotFoundException("your coupon been used or expired");

        }else {
            Boolean istransfer =couponEntity.getTransfer_status();
            CouponDTO couponDTO = new CouponDTO();
            couponDTO.setId(couponEntity.getId());
            couponDTO.setExpired_date(couponEntity.getExpired_date());
            couponDTO.setImage(couponEntity.getPackageEntity().getImage());
            couponDTO.setPackageName(couponEntity.getPackageEntity().getName());
            couponDTO.setDescription(couponEntity.getPackageEntity().getDescription());
            couponDTO.setUnit_price(couponEntity.getPackageEntity().getUnit_price());
            couponDTO.setPurchase_date(couponEntity.getPurchase().getPurchase_date());
            couponDTO.setUsed_status(couponEntity.getUsed_status());
            if(istransfer) {
                String ownner = couponEntity.getPurchase().getUser().getId().toString();
                messagingTemplate.convertAndSend("/queue/" + ownner, couponDTO);
                NotificationEntity notification = new NotificationEntity();
                notification.setTitle("Confrim to Use Coupon");
                notification.setNotificationStatus(NotificationStatus.UNREAD);
                notification.setContent(Map.of(
                        "bussinessID", bussinessID,
                        "couponcode", couponcode
                ));
                notification.setUser(couponEntity.getPurchase().getUser());
                notificationRepository.save(notification);
            }else{
                Integer receiver_id=transferRepository.findOwner(couponEntity.getId());
                messagingTemplate.convertAndSend("/queue/" + receiver_id.toString(), couponDTO);
                NotificationEntity notification = new NotificationEntity();
                notification.setTitle("Confrim to Use Coupon");
                notification.setNotificationStatus(NotificationStatus.UNREAD);
                notification.setContent(Map.of(
                        "bussinessID", bussinessID,
                        "couponcode", couponcode
                ));
                notificationRepository.save(notification);
            }
       //     messagingTemplate.convertAndSendToUser(ownner,"/usecoupon","your coupon is ready to use");
            return couponDTO;
        }
    }
    @Transactional
    public boolean useCoupon(Integer id,Integer userId) {
        try {
                CouponEntity usedCoupon=couponRepository.useCoupon(id);
            if (usedCoupon != null) {
                IsUsedEntity isUsedEntity = new IsUsedEntity();
                isUsedEntity.setUsed_date(new Date());
                CouponEntity couponEntity=new CouponEntity();
                couponEntity.setId(id);
                isUsedEntity.setCoupon(couponEntity);
                NotificationEntity notificationEntity=new NotificationEntity();
                UserEntity user=new UserEntity();
                user.setId(userId);
                notificationEntity.setUser(user);
                notificationEntity.setNotificationStatus(NotificationStatus.UNREAD);
                notificationEntity.setTitle("Use Coupon");
                CouponDTO usedcouponDTO=mapper.map(usedCoupon, CouponDTO.class);
                notificationEntity.setContent(Map.of(
                        "action","TEXT",
                        "context","your Coupon is used",
                        "object",usedcouponDTO
                ));
                notificationRepository.save(notificationEntity);
                isUsedRepository.save(isUsedEntity);
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to use coupon", e);
        }
        return false;
    }




    public BusinessDTO getBusinessByCouponId(Integer couponId) {
        System.out.println("couponId: " + couponId);

        // Fetch the business entity based on couponId
        BusinessEntity businessEntity = couponRepository.findBusinessByCouponId(couponId);

        // If the businessEntity is null, you can return null or throw an exception
        if (businessEntity == null) {
            return null; // or throw new EntityNotFoundException("Business not found");
        }

        // Map BusinessEntity to BusinessDTO
        BusinessDTO businessDTO = new BusinessDTO();
        businessDTO.setId(businessEntity.getId());
        businessDTO.setName(businessEntity.getName());
        businessDTO.setPhone(businessEntity.getPhone());
        businessDTO.setEmail(businessEntity.getEmail());
        businessDTO.setAddress(businessEntity.getAddress());
        businessDTO.setImage(businessEntity.getImage());

        return businessDTO;
    }

    public List<Map<String, Object>> getConfirmedCouponsByPurchaseDate(Date startDate, Date endDate) {
        // Fetch data from the repository
        List<Object[]> results = couponRepository.countConfirmedCouponsByPurchaseDate(startDate, endDate);

        // Transform the data into the required format
        List<Map<String, Object>> chartData = new ArrayList<>();
        for (Object[] result : results) {
            Date purchaseDate = (Date) result[0];
            Long count = (Long) result[1];

            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("name", new SimpleDateFormat("yyyy-MM-dd").format(purchaseDate)); // Format date
            dataPoint.put("value", count); // Confirmed count
            chartData.add(dataPoint);
        }

        return chartData;
    }

    public Map<String, Integer> getCouponCountByBusinessAndPurchaseDate(Integer businessId, Date startDate, Date endDate) {
        // Adjust endDate to include the entire day by setting the time to 23:59:59
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date adjustedEndDate = calendar.getTime();

        List<Object[]> result = couponRepository.countCouponsByBusinessAndPurchaseDate(businessId, startDate, adjustedEndDate);

        Map<String, Integer> couponCountByDate = new LinkedHashMap<>();

        for (Object[] row : result) {
            Timestamp purchaseDate = (Timestamp) row[0];
            Integer couponCount = ((Number) row[1]).intValue();

            // Format the date as yyyy-MM-dd
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(purchaseDate);

            // Accumulate the counts (if there are multiple entries for the same date, sum them)
            couponCountByDate.put(formattedDate, couponCountByDate.getOrDefault(formattedDate, 0) + couponCount);
        }

        return couponCountByDate;
    }

}






