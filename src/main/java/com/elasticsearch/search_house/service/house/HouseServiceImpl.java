package com.elasticsearch.search_house.service.house;
import com.elasticsearch.search_house.base.DatatableSearch;
import com.elasticsearch.search_house.base.HouseStatus;
import com.elasticsearch.search_house.base.LoginUserUtil;
import com.elasticsearch.search_house.dto.HouseDTO;
import com.elasticsearch.search_house.dto.HouseDetailDTO;
import com.elasticsearch.search_house.form.HouseForm;
import com.elasticsearch.search_house.form.RentSearch;
import com.elasticsearch.search_house.mapper.*;
import com.elasticsearch.search_house.model.*;
import com.elasticsearch.search_house.service.ServiceMultiResult;
import com.elasticsearch.search_house.service.search.ISearchService;
import com.elasticsearch.search_house.service.userImpl.ServiceResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.net.httpserver.Authenticator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListResourceBundle;


@Service
public class HouseServiceImpl implements IHouseService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SubwayMapper subwayMapper;

    @Autowired
    private SubwayStationMapper subwayStationMapper;

    @Autowired
    private HouseDetailMapper houseDetailMapper;

    @Autowired
    private HouseTagMapper houseTagMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private ISearchService searchService;

    @Override
    public ServiceResult<HouseDTO> save(HouseForm houseForm)  {
        HouseDetail detail = new HouseDetail();
        ServiceResult<HouseDTO> subwayValidationResult = null;

        try {
            subwayValidationResult = wrapperDetailInfo(detail, houseForm);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("HouseDetail save fail");
        }

        if (subwayValidationResult != null) {
            return subwayValidationResult;
        }

        House house = new House();
        modelMapper.map(houseForm, house);

        Date now = new Date();
        house.setCreateTime(now);
        house.setLastUpdateTime(now);
        house.setAdminId(LoginUserUtil.getLoginUserId());

        Long houseId = houseMapper.houseSave(house);


        detail.setHouseId(houseId);

        houseDetailMapper.houseDetailSave(detail);



        HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
        HouseDetailDTO houseDetailDTO = modelMapper.map(detail, HouseDetailDTO.class);

        houseDTO.setHouseDetail(houseDetailDTO);

        List<String> tags = houseForm.getTags();
        if (tags != null || !tags.isEmpty()){
            List<HouseTag> houseTags = new ArrayList<>();
            for (String tag: tags){
                houseTags.add(new HouseTag(house.getId(), tag));
            }

            for (HouseTag houseTag : houseTags ){

                houseTagMapper.houseTagSave(houseTag);
            }

            houseDTO.setTags(tags);
        }

        return new ServiceResult<HouseDTO>(true, null, houseDTO);
    }

    @Override
    @Transactional
    public ServiceResult update(HouseForm houseForm)  {
        House house = this.houseMapper.findOne(houseForm.getId());
        if (house == null) {
            return ServiceResult.notFound();
        }

        HouseDetail detail = this.houseDetailMapper.findByHouseId(house.getId());
        if (detail == null) {
            return ServiceResult.notFound();
        }

        ServiceResult wrapperResult = null;

        try {
            wrapperResult = wrapperDetailInfo(detail, houseForm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (wrapperResult != null) {
            return wrapperResult;
        }

        houseDetailMapper.houseDetailSave(detail);
        modelMapper.map(houseForm, house);
        house.setLastUpdateTime(new Date());
        houseMapper.houseSave(house);


        if (house.getStatus() == HouseStatus.PASSES.getValue()) {
            searchService.index(house.getId());
        }

        return ServiceResult.success();
    }

    @Override
    public ServiceMultiResult<HouseDTO> adminQuery(DatatableSearch searchBody) {
        List<HouseDTO> houseDTOS = new ArrayList<>();


        PageHelper.startPage( (searchBody.getStart()/searchBody.getLength())+1, searchBody.getLength());


        // 有缺陷，只能单一条件匹配，多条件组合不生效
        List<House> list;

        if (searchBody.getCity() != null) {
            list = houseMapper.findAllHouseByCity(searchBody.getCity());
        }

        else if (searchBody.getCreateTimeMax() != null) {
            list = houseMapper.findAllHouseByCreateTimeMax(searchBody.getCreateTimeMax());
        }

        else if (searchBody.getCreateTimeMin() != null) {
            list = houseMapper.findAllHouseByCreateTimeMin(searchBody.getCreateTimeMin());
        }

        else if (searchBody.getStatus() != null) {
            list = houseMapper.findAllHouseByStatus(searchBody.getStatus());
        }

        else if (searchBody .getTitle() != null){
            list = houseMapper.findAllHouseByTitle(searchBody.getTitle());
        }

        else  {
            list = houseMapper.findAllHouse();
        }


        Page<House> houses = (Page<House>)list;


        houses.forEach(house -> {

            HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
            houseDTOS.add(houseDTO);
        });

        return new ServiceMultiResult<>(houses.getTotal(), houseDTOS);
    }

    @Override
    public ServiceResult<HouseDTO> findCompleteOne(Long id) {
        House house = houseMapper.findOne(id);
        if (house == null) {
            return ServiceResult.notFound();
        }

        HouseDetail detail = houseDetailMapper.findByHouseId(id);

        HouseDetailDTO detailDTO = modelMapper.map(detail, HouseDetailDTO.class);

        List<HouseTag> tags = houseDetailMapper.findAllByHouseId(id);
        List<String> tagList = new ArrayList<>();
        for (HouseTag tag : tags) {
            tagList.add(tag.getName());
        }

        HouseDTO result = modelMapper.map(house, HouseDTO.class);
        result.setHouseDetail(detailDTO);
        result.setTags(tagList);

        return  ServiceResult.of(result);

    }

    @Override
    public ServiceResult addTag(Long houseId, String tag) {
        House house = houseMapper.findOne(houseId);
        if (house == null) {
            return ServiceResult.notFound();
        }

        HouseTag houseTag = houseTagMapper.findByNameAndHouseId(tag, houseId);
        if (houseTag != null) {
            return new ServiceResult(false, "标签已存在");
        }

        houseTagMapper.houseTagSave(new HouseTag(houseId, tag));
        return ServiceResult.success();
    }

    @Override
    public ServiceResult removeTag(Long houseId, String tag) {
        House house = houseMapper.findOne(houseId);
        if (house == null) {
            return ServiceResult.notFound();
        }

        HouseTag houseTag = houseTagMapper.findByNameAndHouseId(tag, houseId);
        if (houseTag == null) {
            return new ServiceResult(false, "标签不存在");
        }

        houseTagMapper.deleteHouseTag(houseTag.getId());
        return ServiceResult.success();
    }

    @Override
    public ServiceResult updateStatus(Long id, int status) {
        House house = houseMapper.findOne(id);
        if (house == null) {
            return ServiceResult.notFound();
        }

        if (house.getStatus() == status) {
            return new ServiceResult(false, "状态没有发生改变");
        }

        if (house.getStatus() == HouseStatus.RENTED.getValue()) {
            return new ServiceResult(false, "已出租房屋不允许修改");
        }

        if (house.getStatus() == HouseStatus.DELETED.getValue()) {
            return new ServiceResult(false, "已删除资源不允许操作");
        }

        houseMapper.updateStatus(id, status);

        // 上架更新索引 其他情况都要删除索引
        if (status == HouseStatus.PASSES.getValue()) {
            searchService.index(id);
        } else {
            searchService.remove(id);
            System.out.println();
        }

        return ServiceResult.success();
    }

    @Override
    public ServiceMultiResult<HouseDTO> query(RentSearch rentSearch) {

        String orderBy = "last_update_time desc";
        int page = (rentSearch.getStart() /rentSearch.getSize()) +1;
       PageHelper.startPage(page, rentSearch.getSize(), orderBy);

        List<House> list;
        if (rentSearch.getCityEnName() != null) {
            list = houseMapper.findAllHouseByStatusAndCityEnName(HouseStatus.PASSES.getValue(), rentSearch.getCityEnName());
            Page<House> houses = (Page<House>)list;

            List<HouseDTO> houseDTOS = new ArrayList<>();

            houses.forEach(house -> {
                HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
                houseDTOS.add(houseDTO);
            });

            return new ServiceMultiResult<>(houses.getTotal(), houseDTOS);
        }






        return null;
    }

    private ServiceResult<HouseDTO> wrapperDetailInfo(HouseDetail houseDetail, HouseForm houseForm) throws Exception {
        Subway subway = subwayMapper.findOneById(houseForm.getSubwayLineId());
        if (subway == null) {
            return new ServiceResult<>(false, "Not valid subway line!");
        }

        SubwayStation subwayStation = subwayStationMapper.findOneById(houseForm.getSubwayStationId());
        if (subwayStation == null || subway.getId() != subwayStation.getSubwayId()) {
            return new ServiceResult<>(false, "Not valid subway");
        }

        houseDetail.setSubwayLineId(subway.getId());
        houseDetail.setSubwayLineName(subway.getName());

        houseDetail.setSubwayStationId(subwayStation.getId());
        houseDetail.setSubwayStationName(subwayStation.getName());

        houseDetail.setDescription(houseForm.getDescription());
        houseDetail.setDetailAddress(houseForm.getDetailAddress());
        houseDetail.setLayoutDesc(houseForm.getLayoutDesc());
        houseDetail.setRentWay(houseForm.getRentWay());
        houseDetail.setRoundService(houseForm.getRoundService());
        houseDetail.setTraffic(houseForm.getTraffic());
        houseDetail.setHouseId(houseForm.getId());

       return null;
    }

    /**
     * 处理图片的方法 ---暂时不实现
     */
    private List<HousePicture> generatePictures(HouseForm form, Long houseId){
        return  null;
    }
}
