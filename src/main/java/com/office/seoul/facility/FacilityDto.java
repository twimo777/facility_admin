package com.office.seoul.facility;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TBL_FACILITY")
public class FacilityDto {
	
	private String GUBUN;		//서비스구분

	@Id
	private String SVCID;		//서비스id
	
	private String MAXCLASSNM;	//대분류명
	private String MINCLASSNM;	//소분류명
	private String SVCSTATNM;	//서비스상태
	private String SVCNM;		//서비스명
	private String PAYATNM;		//결제방법
	private String PLACENM;		//장소명
	private String USETGTINFO;	//서비스대상
	private String SVCURL;		//바로가기url
	private String X;			//장소x좌표
	private String Y;			//장소y좌표
	private String SVCOPNBGNDT;	//서비스개시시작일시
	private String SVCOPNENDDT;	//서비스개시종료일시
	private String RCPTBGNDT;	//접수시작일시
	private String RCPTENDDT;	//접수종료일시
	private String AREANM;		//지역명
	private String IMGURL;		//이미지경로
	
	@Lob
	@Column(name = "DTLCONT")
	private String DTLCONT;		//상세정보
	
	private String TELNO;		//전화번호
	private String V_MIN;		//서비스이용 시작시간
	private String V_MAX;		//서비스이용 종료시간
	private String REVSTDDAYNM;	//취소기간 기준정보
	private String REVSTDDAY;	//취소기간 기준일까지

}
