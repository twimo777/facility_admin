package com.office.seoul.facility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class FacilityService {
	
	private final IFacilityDao iFacilityDao;
	
	public FacilityService(IFacilityDao iFacilityDao) {
		this.iFacilityDao = iFacilityDao;
	}

	public List<FacilityDto> home(String type) {
		
		return iFacilityDao.getAllFacility(type);
	}
	
	 public Map<String, Object> getFacilities(int page, int size, String type) {
	        int offset = (page - 1) * size;
	        Map<String, Object> params = new HashMap<>();
	        params.put("offset", offset);
	        params.put("size", size);
	        params.put("type", type);
	        
	        log.info("type : {}", type);

	        List<FacilityDto> facilities = iFacilityDao.getFacilities(params);
	        int totalItems = iFacilityDao.getTotalFacilitiesCount(params);

	        Map<String, Object> response = new HashMap<>();
	        response.put("items", facilities);
	        response.put("totalItems", totalItems);

	        return response;
	    }

	public FacilityDto getFacilityById(String id) {
		
		return iFacilityDao.getFacilityById(id);
	}

	public List<String> getAreas() {
		
        return iFacilityDao.getAreas();
    }

	public List<String> getCategoriesByArea(String area) {
		
        return iFacilityDao.getCategoriesByArea(area);
    }

    public List<String> getResults(String area, String category) {
        Map<String, Object> params = new HashMap<>();
        params.put("area", area);
        params.put("category", category);
        return iFacilityDao.getResults(params);
    }

    public Map<String, Object> getOptions(String area, String category) {
        Map<String, Object> options = new HashMap<>();
        
        List<String> areas = getAreas();
        List<String> categories = area != null ? getCategoriesByArea(area) : new ArrayList<>();
        List<String> results = (area != null || category != null) ? getResults(area, category) : new ArrayList<>();
        
        options.put("areas", areas);
        options.put("categories", categories);
        options.put("results", results);
        
        return options;
    }

	public String getFacilityIdBySvcnm(String svcnm) {
		log.info("svcnm : {}", svcnm);
		
		return iFacilityDao.getFacilityIdBySvcnm(svcnm);
	}

	public List<FacilityDto> getFacilitiesByIds(List<String> facilityIds) {
		
		return iFacilityDao.findFacilitiesByIds(facilityIds);
	}

}