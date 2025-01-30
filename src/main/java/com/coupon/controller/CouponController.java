package com.coupon.controller;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.CouponEntity;
import com.coupon.entity.ConfirmStatus;
import com.coupon.entity.PackageEntity;
import com.coupon.model.*;
import com.coupon.responObject.ResourceNotFoundException;
import com.coupon.service.CouponService;
import com.coupon.service.QRService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private QRService qrService;

    @PostMapping("/accept/{purchaseId}")
    public ResponseEntity<Map<String, String>> confirmPurchase(@PathVariable Integer purchaseId) {
        try {
            couponService.confirmPurchaseAndGenerateQR(purchaseId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Purchase confirmed and QR codes generated successfully.");
            return ResponseEntity.ok(response); // Return as a JSON object
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error accepting purchase!");
            return ResponseEntity.badRequest().body(response); // Return as a JSON object
        }
    }

    @PostMapping("/decline/{purchaseId}")
    public ResponseEntity<Map<String, String>> declinePurchase(@PathVariable Integer purchaseId) {
        try {
            couponService.declinePurchaseAndRestoreQuantity(purchaseId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Purchase declined and coupon quantities restored successfully.");
            return ResponseEntity.ok(response); // Return as a JSON object
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error declining purchase!");
            return ResponseEntity.badRequest().body(response); // Return as a JSON object
        }
    }


    @GetMapping("/public/list/{userId}")
    public ResponseEntity<List<CouponDTO>> shwoAllCoupon(@PathVariable Integer userId) {
        System.out.println("userId" + userId);
        List<CouponDTO> coupons = couponService.showCouponbyUserId(userId);

        if (coupons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/code/{id}")
    public ResponseEntity<QRDTO> getQRCode(@PathVariable("id") Integer id) {
        QRDTO qrDto = qrService.getQRCodeByCouponId(id);
        return ResponseEntity.ok(qrDto);
    }

    @GetMapping("/listbypurchaseId/{purchase_id}")
    public ResponseEntity<List<CouponDTO>> getCouponsByPurchaseId(@PathVariable Integer purchase_id) {
        try {
            List<CouponDTO> coupons = couponService.showCouponbyPurchaseId(purchase_id);

            if (coupons.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(coupons);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/admin/find")
    public ResponseEntity<List<CouponDTO>> getAllCoupons() {
        try {
            List<CouponDTO> coupons = couponService.findall();
            return ResponseEntity.ok(coupons);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/public/findbyUserId/{userId}")
    public ResponseEntity<List<CouponDTO>> findAllbyUserId(@PathVariable Integer userId) {
        List<CouponDTO> couponList = couponService.findallByUserId(userId);
        if (couponList.isEmpty()) {
            throw new RuntimeException("coupon not found");
        }
        return ResponseEntity.ok(couponList);
    }


    @GetMapping("/countALlCoupons")
    public long countALlCoupons() {
        return couponService.countConfirmedCoupons();
    }


    @GetMapping("/countCouponsByBusinessId/{businessId}")
    public ResponseEntity<Long> countConfirmedCoupons(@PathVariable Long businessId) {
        Long count = couponService.countConfirmedCouponsByBusinessId(businessId);
        return ResponseEntity.ok(count);
    }


    @PostMapping("/searchCoupon")
    public ResponseEntity<CouponDTO> useCoupon(@RequestBody HashMap<String,String> coupon) throws IOException {
        Integer bussinessID= Integer.valueOf(coupon.get("businessId"));
        String couponcode=coupon.get("couponCode");

        return ResponseEntity.ok(couponService.searchCoupon(bussinessID,couponcode));
    }

    @PutMapping("comfrimedTouse")
    public ResponseEntity<Map<String,String>> useCoupo1(@RequestBody Map<String,Integer> coupon)throws IOException {
        Integer couponId=coupon.get("couponId");
        Integer userId=coupon.get("userId");
        if(couponService.useCoupon(couponId,userId)){
        return ResponseEntity.ok(Collections.singletonMap("message","your coupon is sussfully used"));
    }
        return ResponseEntity.badRequest().body(Collections.singletonMap("message","failed to use"));
    }

    @GetMapping("findBusiness/{couponId}")
    public BusinessDTO getBusinessByCouponId(@PathVariable Integer couponId) {
        return couponService.getBusinessByCouponId(couponId);
    }

    @GetMapping("/confirm-count")
    public List<Map<String, Object>> getConfirmedCouponsByPurchaseDate(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        Calendar calendar = Calendar.getInstance();

        // If no endDate is provided, set it to the current date
        if (endDate == null) {
            endDate = calendar.getTime();
        }

        // Strip the time from the endDate (set it to the last moment of the day, 23:59:59.999)
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        endDate = calendar.getTime();

        // If no startDate is provided, set it to 7 days ago from endDate
        if (startDate == null) {
            calendar.setTime(endDate);
            calendar.add(Calendar.DAY_OF_MONTH, -7); // Set start date to 7 days ago
            startDate = calendar.getTime();
        }

        // Call the service to get the filtered data
        return couponService.getConfirmedCouponsByPurchaseDate(startDate, endDate);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    // Try both formats
                    if (text != null && !text.isEmpty()) {
                        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            setValue(sdf1.parse(text));
                        } catch (Exception e) {
                            setValue(sdf2.parse(text));
                        }
                    }
                } catch (Exception e) {
                    setValue(null);
                }
            }
        });
    }

    @GetMapping("/coupon-count")
    public Map<String, Integer> getCouponCountByBusinessAndPurchaseDate(
            @RequestParam("businessId") Integer businessId,
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate) {
        return couponService.getCouponCountByBusinessAndPurchaseDate(businessId, startDate, endDate);
    }

    @GetMapping("/filter2")
    public ResponseEntity<List<CouponDTO>> getCouponsByBusinessAndDateRange(
            @RequestParam Integer businessId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        List<CouponDTO> coupons = couponService.getCouponsByBusinessAndDateRange(businessId, startDate, endDate);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/public/findbyBusinessId/{businessId}")
    public ResponseEntity<List<CouponDTO>> findAllbybusinessId(@PathVariable Integer businessId){
        List<CouponDTO> couponList=  couponService.findByBusinessId(businessId);
        if(couponList.isEmpty()){
            throw new RuntimeException("coupon not found");
        }
        return ResponseEntity.ok(couponList);
    }
    @GetMapping("/public/findScanedCouponbyBusinessId/{businessId}")
    public ResponseEntity<List<CouponDTO>> findscanedCouponbybusinessId(@PathVariable Integer businessId){
        List<CouponDTO> couponList=  couponService.findscanedByBusinessId(businessId);
        if(couponList.isEmpty()){
            throw new RuntimeException("coupon not found");
        }
        return ResponseEntity.ok(couponList);
    }

    @GetMapping("/report")
    public void downloadCouponReport(@RequestParam String fileType, HttpServletResponse response) {

        if ("pdf".equalsIgnoreCase(fileType)) {
            couponService.exportCouponReport(response);
        } else if ("excel".equalsIgnoreCase(fileType)) {
            couponService.exportCouponReportToExcel(response);
        } else {
            throw new IllegalArgumentException("Invalid file type: " + fileType);
        }
    }

    @GetMapping("/exportCouponReport")
    public void downloadReport(@RequestParam("businessId") Integer businessId,
                               @RequestParam("fileType") String fileType,
                               HttpServletResponse response) {
        System.out.println("Received businessId: " + businessId);
        try {
            if ("pdf".equalsIgnoreCase(fileType)) {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=coupon_report.pdf");
                couponService.exportCouponReportbybusinessId( response,businessId);
            } else if ("excel".equalsIgnoreCase(fileType)) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=coupon_report.xlsx");
                couponService.exportCouponReportToExcelBybusinessId( response,businessId);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file type");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/exportScanedCouponReport")
    public void downloadScanedCouponReport(@RequestParam("businessId") Integer businessId,
                               @RequestParam("fileType") String fileType,
                               HttpServletResponse response) {
        System.out.println("Received businessId: " + businessId);
        try {
            if ("pdf".equalsIgnoreCase(fileType)) {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=coupon_report.pdf");
                couponService.exportScanedCouponReportbybusinessId( response,businessId);
            } else if ("excel".equalsIgnoreCase(fileType)) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=coupon_report.xlsx");
                couponService.exportScanedCouponReportToExcelBybusinessId( response,businessId);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file type");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    public List<CouponDTO> filterCoupons(
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) String selectedCategory,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {


        return couponService.filterCoupons(searchText, selectedCategory, startDate, endDate);
    }
    @GetMapping("/filterByBusinessId")
    public ResponseEntity<List<CouponDTO>> filterCouponsByBusinessId(
            @RequestParam Integer businessId,
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) String selectedCategory,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        List<CouponDTO> coupons = couponService.filterCouponsWithBusinessId(businessId, searchText, selectedCategory, startDate, endDate);
        return ResponseEntity.ok(coupons);
    }


    @GetMapping("/exportFilteredReport")
    public void downloadFilteredReport(
            @RequestParam String fileType,
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) String selectedCategory,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            HttpServletResponse response) {

        if ("pdf".equalsIgnoreCase(fileType)) {
            couponService.exportReportbyfilterbyPdf(response, searchText, selectedCategory, startDate, endDate);
        } else if ("excel".equalsIgnoreCase(fileType)) {
            couponService.exportReportByFilterByExcel(response, searchText, selectedCategory, startDate, endDate);
        } else {
            throw new IllegalArgumentException("Invalid file type: " + fileType);
        }
    }
    @GetMapping("/exportFilteredReportbybusinessId")
    public void downloadFilteredReportBybusinessId(
            @RequestParam String fileType,
            @RequestParam(required = false) Integer businessId,
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) String selectedCategory,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            HttpServletResponse response) {

        if ("pdf".equalsIgnoreCase(fileType)) {
            couponService.exportReportbyfilterbyBusinessId(response,businessId, searchText, selectedCategory, startDate, endDate);
        } else if ("excel".equalsIgnoreCase(fileType)) {
            couponService.exportReportByFilterByExcelbyBusinessId(response, businessId,searchText, selectedCategory, startDate, endDate);
        } else {
            throw new IllegalArgumentException("Invalid file type: " + fileType);
        }
    }

    @GetMapping("/count-pending")
    public long getPendingCouponCount() {
        return couponService.countPendingCoupons();
    }

    @GetMapping("/ScanedfilterByBusinessId")
    public ResponseEntity<List<CouponDTO>> filterScanedCouponsByBusinessId(
            @RequestParam Integer businessId,
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) String selectedCategory,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        List<CouponDTO> coupons = couponService.filterScanedCouponsWithBusinessId(businessId, searchText, selectedCategory, startDate, endDate);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/exportScanedFilteredReportbybusinessId")
    public void downloadScanedFilteredReportBybusinessId(
            @RequestParam String fileType,
            @RequestParam(required = false) Integer businessId,
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) String selectedCategory,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            HttpServletResponse response) {

        if ("pdf".equalsIgnoreCase(fileType)) {
            couponService.exportScanedfilterbyBusinessId(response,businessId, searchText, selectedCategory, startDate, endDate);
        } else if ("excel".equalsIgnoreCase(fileType)) {
            couponService.exportScanedByFilterByExcelbyBusinessId(response, businessId,searchText, selectedCategory, startDate, endDate);
        } else {
            throw new IllegalArgumentException("Invalid file type: " + fileType);
        }
    }
    @GetMapping("/used/{userId}")
    public ResponseEntity<List<CouponDTO>> getUsedCouponsByUserId(@PathVariable Integer userId) {
        System.out.println("userId" + userId);
        List<CouponDTO> coupons = couponService.getUsedCouponsByUserId(userId);

        if (coupons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(coupons);
    }



}


