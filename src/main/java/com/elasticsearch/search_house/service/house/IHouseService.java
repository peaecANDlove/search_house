package com.elasticsearch.search_house.service.house;

import com.elasticsearch.search_house.base.DatatableSearch;
import com.elasticsearch.search_house.dto.HouseDTO;
import com.elasticsearch.search_house.form.HouseForm;
import com.elasticsearch.search_house.form.RentSearch;
import com.elasticsearch.search_house.service.ServiceMultiResult;
import com.elasticsearch.search_house.service.userImpl.ServiceResult;

import javax.servlet.http.HttpServletRequest;

/**
 * 房屋管理接口
 */

public interface IHouseService {

    /**
     * 新增
     * @param houseForm
     * @return
     */
    ServiceResult<HouseDTO> save(HouseForm houseForm);

    ServiceResult update(HouseForm houseForm);

    ServiceMultiResult<HouseDTO> adminQuery(DatatableSearch searchBody);

    /**
     * 查询完整房源信息
     * @param id
     * @return
     */
    ServiceResult<HouseDTO> findCompleteOne(Long id);

    /**
     * 新增标签
     * @param houseId
     * @param tag
     * @return
     */
    ServiceResult addTag(Long houseId, String tag);

    /**
     * 删除标签
     * @param houseId
     * @param tag
     * @return
     */
    ServiceResult removeTag(Long houseId, String tag);

    /**
     * 更新房源具体状态
     * @param id
     * @param status
     * @return
     */
    ServiceResult updateStatus(Long id, int status);


    /**
     * 查询房源信息集
     * @param rentSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> query(RentSearch rentSearch);
}
