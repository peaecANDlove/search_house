package com.elasticsearch.search_house.controller.admin;


import com.elasticsearch.search_house.base.*;
import com.elasticsearch.search_house.dto.*;
import com.elasticsearch.search_house.form.HouseForm;
import com.elasticsearch.search_house.model.HouseDetail;
import com.elasticsearch.search_house.model.SupportAddress;
import com.elasticsearch.search_house.service.ServiceMultiResult;
import com.elasticsearch.search_house.service.house.AddressServiceImpl;
import com.elasticsearch.search_house.service.house.IAddressService;
import com.elasticsearch.search_house.service.house.IHouseService;
import com.elasticsearch.search_house.service.userImpl.ServiceResult;
import org.apache.ibatis.annotations.Delete;
import org.modelmapper.internal.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private IAddressService addressService;

    @Autowired
    private IHouseService houseService;


    @GetMapping("/admin/center")
    public String adminCenterPage(){
        return "admin/center";
    }

    @GetMapping("/admin/welcome")
    public String welcomePage(){
        return "admin/welcome";
    }

    @GetMapping("/admin/login")
    public String adminLoginPage(){
        return "admin/login";
    }

    /**
     * 新增房源功能页面
     * @return
     */
    @GetMapping("admin/add/house")
    public String addHousePage(){
        return "admin/house-add";
    }

    /**
     * 房源列表页
     * @return
     */
    @GetMapping("admin/house/list")
    public String listHousePage(){
        return "admin/house-list";
    }

    @PostMapping("admin/houses")
    @ResponseBody
    public ApiDataTableResponse house(@ModelAttribute DatatableSearch searchBody) {
        ServiceMultiResult<HouseDTO> result = houseService.adminQuery(searchBody);

        ApiDataTableResponse response = new ApiDataTableResponse(ApiResponse.Status.SUCCESS);
        response.setData(result.getResult());
        response.setRecordsFiltered(result.getTotal());
        response.setRecordsTotal(result.getTotal());

        response.setDraw(searchBody.getDraw());

        return response;
    }

    /**
     * 上传图片接口定义----暂未实现
     * @return
     */
    @PostMapping(value = "admin/upload/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ApiResponse uploadPhoto(){
        return ApiResponse.OfSuccess(null);
    }


    /**
     * 添加房源
     * @param houseForm
     * @param bindingResult
     * @return
     */
    @PostMapping("admin/add/house")
    @ResponseBody
    public ApiResponse addHouse(@Valid @ModelAttribute("form-house-add") HouseForm houseForm, BindingResult bindingResult) {
        if ((bindingResult.hasErrors())) {
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(), bindingResult.getAllErrors().get(0).getDefaultMessage(), null);
        }

//        if (houseForm.getPhotos() == null || houseForm.getCover() == null){
//            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), "必须上传图片");
//        }

        Map<SupportAddress.Level, SupportAddressDTO> addressDMap = addressService.findCityAndRegion(houseForm.getCityEnName(), houseForm.getRegionEnName());
        if (addressDMap.keySet().size() != 2){
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
        }

        ServiceResult<HouseDTO> result = houseService.save(houseForm);
        if (result.isSuccess()){
            return ApiResponse.OfSuccess(result.getResult());
        }

        return ApiResponse.OfSuccess(ApiResponse.Status.NOT_VALID_PARAM);


    }

    @GetMapping("admin/house/edit")
    public String houseEditPage(@RequestParam(value = "id") Long id, Model model) {

        if (id == null || id < 1) {
            return "404";
        }

        ServiceResult<HouseDTO> serviceResult = houseService.findCompleteOne(id);
        if (!serviceResult.isSuccess()) {
            return "404";
        }

        HouseDTO result = serviceResult.getResult();
        model.addAttribute("house", result);

        Map<SupportAddress.Level, SupportAddressDTO> addressMap = addressService
                .findCityAndRegion(result.getCityEnName(), result.getRegionEnName());
        model.addAttribute("city", addressMap.get(SupportAddress.Level.CITY));
        model.addAttribute("region", addressMap.get(SupportAddress.Level.REGION));

        HouseDetailDTO detailDTO = result.getHouseDetail();
        ServiceResult<SubwayDTO> subwayServiceResult = addressService.findSubway(detailDTO.getSubwayLineId());
        if (subwayServiceResult.isSuccess()) {
            model.addAttribute("subway", subwayServiceResult.getResult());
        }

        ServiceResult<SubwayStationDTO> subwayStationServiceResult = addressService
                .findSubwayStation(detailDTO.getSubwayStationId());
        if (subwayServiceResult.isSuccess()) {
            model.addAttribute("station", subwayStationServiceResult.getResult());
        }

        return "admin/house-edit";
    }

    /**
     * 编辑接口
     * @param houseForm
     * @param bindingResult
     * @return
     */

    @PostMapping("admin/house/edit")
    @ResponseBody
    public ApiResponse saveHouse(@Valid @ModelAttribute("form-house-edit") HouseForm houseForm,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(), bindingResult.getAllErrors().get(0)
                    .getDefaultMessage(), null);
        }

        Map<SupportAddress.Level, SupportAddressDTO> addressMap = addressService.findCityAndRegion(houseForm.getCityEnName(),
                houseForm.getRegionEnName());

        if (addressMap.keySet().size() != 2) {
            return ApiResponse.OfSuccess(ApiResponse.Status.NOT_VALID_PARAM);
        }

        ServiceResult result = houseService.update(houseForm);
        if (result.isSuccess()) {
            return ApiResponse.OfSuccess(null);
        }


        ApiResponse response = ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        response.setMessage(result.getMessage());
        return response;

    }

    /**
     * 添加标签
     * @param houseId
     * @param tag
     * @return
     */
    @PostMapping("admin/house/tag")
    @ResponseBody
    public ApiResponse addHouseTag(@RequestParam(value = "house_id") Long houseId,
                                   @RequestParam(value = "tag") String tag) {
        if (houseId <1 || tag.isEmpty() || tag == null) {
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        ServiceResult result = this.houseService.addTag(houseId, tag);
        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);

        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }

    @DeleteMapping("admin/house/tag")
    @ResponseBody
    public ApiResponse removeHouseTag(@RequestParam(value = "house_id") Long houseId,
                                      @RequestParam(value = "tag") String tag) {
        if (houseId < 1 || tag.isEmpty() || tag == null) {
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        ServiceResult result = this.houseService.removeTag(houseId, tag);
        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);

        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }

    }

    /**
     * 审核接口
     * @param id
     * @param operation
     * @return
     */
    @PutMapping("admin/house/operate/{id}/{operation}")
    @ResponseBody
    public ApiResponse operateHouse(@PathVariable(value = "id") Long id,
                                    @PathVariable(value = "operation") int operation) {

        if (id <= 0) {
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
        }


        ServiceResult result;

        switch (operation) {
            case HouseOperation.PASS:
                result = this.houseService.updateStatus(id, HouseStatus.PASSES.getValue());
                break;
            case HouseOperation.DELETE:
                result = this.houseService.updateStatus(id, HouseStatus.DELETED.getValue());
                break;
            case HouseOperation.PULL_OUT:
                result = this.houseService.updateStatus(id, HouseStatus.NOT_AUDITED.getValue());
                break;
            case HouseOperation.RENT:
                result = this.houseService.updateStatus(id, HouseStatus.RENTED.getValue());
                break;
            default:
                return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        if (result.isSuccess()) {
            return ApiResponse.OfSuccess(null);
        }

        return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
    }
}
