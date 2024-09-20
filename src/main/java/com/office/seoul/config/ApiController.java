package com.office.seoul.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.office.seoul.facility.FacilityDto;
import com.office.seoul.facility.FacilityRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/api")
public class ApiController {
	
	@Value("${seoul-api-key}")
    private String apiKey;
	
	private final FacilityRepository facilityRepository;
	
	public ApiController(FacilityRepository facilityRepository) {
		this.facilityRepository = facilityRepository;
	}
	
	@GetMapping("/data")
	public String facility(Model model) {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");

        try {
            urlBuilder.append("/").append(URLEncoder.encode(apiKey, "UTF-8"));							// api 키
            urlBuilder.append("/").append(URLEncoder.encode("json", "UTF-8"));							// data type 결정
            urlBuilder.append("/").append(URLEncoder.encode("ListPublicReservationSport", "UTF-8"));	// 요청한 url
            urlBuilder.append("/").append(URLEncoder.encode("1", "UTF-8"));								// 페이지 시작 번호
            urlBuilder.append("/").append(URLEncoder.encode("446", "UTF-8"));							// 페이지 끝 번호

            // HTTP 요청 및 응답 처리
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            int responseCode = conn.getResponseCode();
            BufferedReader rd;
            if (responseCode >= 200 && responseCode <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            
            String response = sb.toString();
            String cleanResponse = removeCommentsFromJson(response);

            // JSON 응답 파싱
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(cleanResponse);
            Map<String, Object> map = (Map<String, Object>) jsonObject.get("ListPublicReservationSport");
            JSONArray jsonArray = (JSONArray) map.get("row");
            
            for(int i = 0; i < jsonArray.size(); i++) {
            List<FacilityDto> facilityDtos = ((List<JSONObject>) jsonArray).stream().map(json -> {
				FacilityDto facilityDto = new FacilityDto();
				facilityDto.setGUBUN((String) json.get("GUBUN"));
				facilityDto.setSVCID((String) json.get("SVCID"));
				facilityDto.setMAXCLASSNM((String) json.get("MAXCLASSNM"));
				facilityDto.setMINCLASSNM((String) json.get("MINCLASSNM"));
				facilityDto.setSVCSTATNM((String) json.get("SVCSTATNM"));
				facilityDto.setSVCNM((String) json.get("SVCNM"));
				facilityDto.setPAYATNM((String) json.get("PAYATNM"));
				facilityDto.setPLACENM((String) json.get("PLACENM"));
				facilityDto.setUSETGTINFO((String) json.get("USETGTINFO"));
				facilityDto.setSVCURL((String) json.get("SVCURL"));
				facilityDto.setX((String) json.get("X"));
				facilityDto.setY((String) json.get("Y"));
				facilityDto.setSVCOPNBGNDT((String) json.get("SVCOPNBGNDT"));
				facilityDto.setSVCOPNENDDT((String) json.get("SVCOPNENDDT"));
				facilityDto.setRCPTBGNDT((String) json.get("RCPTBGNDT"));
				facilityDto.setRCPTENDDT((String) json.get("RCPTENDDT"));
				facilityDto.setAREANM((String) json.get("AREANM"));
				facilityDto.setIMGURL((String) json.get("IMGURL"));
				facilityDto.setDTLCONT((String) json.get("DTLCONT"));
				facilityDto.setTELNO((String) json.get("TELNO"));
				facilityDto.setV_MIN((String) json.get("V_MIN"));
				facilityDto.setV_MAX((String) json.get("V_MAX"));
				facilityDto.setREVSTDDAYNM((String) json.get("REVSTDDAYNM"));
				facilityDto.setREVSTDDAY((String) json.get("REVSTDDAY"));
				return facilityDto;
				}).collect(Collectors.toList());
            
             facilityRepository.saveAll(facilityDtos);

            }
            
        } catch (Exception e){
        	e.printStackTrace();
        }
        return "home";
	}
	
	private String removeCommentsFromJson(String jsonResponse) {

        return jsonResponse.replaceAll("<!--.*?-->", "");
    }
	
}