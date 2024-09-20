package com.office.seoul.facility;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IFacilityDao {

	public List<FacilityDto> getAllFacility(String type);
	
	public List<FacilityDto> getFacilities(Map<String, Object> params);
	
	public int getTotalFacilitiesCount(Map<String, Object> params);

	public FacilityDto getFacilityById(String id);

	public List<String> getAreas();

	public List<String> getCategoriesByArea(String area);

	public List<String> getResults(Map<String, Object> params);

	public String getFacilityIdBySvcnm(String svcnm);

	public List<FacilityDto> findFacilitiesByIds(@Param("facilityIds") List<String> facilityIds);



}