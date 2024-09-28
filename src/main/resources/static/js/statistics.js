$(document).ready(function() {
    let yearlyChart, monthlyChart, facilityChart;

    // 귀찮아서 아작스말고 에이싱크로
    async function fetchMonthlyStatistics(year) {
        console.log(`Fetching monthly statistics for year: ${year}`);
        const response = await fetch(`/statistics/monthly_statistics/${year}`);
        if (!response.ok) {
            console.error("Failed to fetch monthly statistics:", response.status);
            return [];
        }
        const data = await response.json();
        console.log("Monthly data:", data); // 데이터 확인
        return data;
    }

    async function fetchYearlyStatistics() {
        const response = await fetch('/statistics/yearly_statistics');
        return await response.json();
    }

    async function fetchFacilityStatistics() {
        const response = await fetch('/statistics/facility_statistics');
        return await response.json();
    }

    // 차트 뭉개기 함수
    function destroyCharts() {
        if (yearlyChart) {
            yearlyChart.destroy();
        }
        if (monthlyChart) {
            monthlyChart.destroy();
        }
        if (facilityChart) {
            facilityChart.destroy();
        }
    }

    async function renderCharts(year) { // year 인자를 추가
        destroyCharts(); // 기존 차트 파괴

        const monthlyData = await fetchMonthlyStatistics(year); // year 전달
        const yearlyData = await fetchYearlyStatistics();
        const facilityData = await fetchFacilityStatistics();

        // 월별 데이터 정렬
        monthlyData.sort((a, b) => new Date(a.month) - new Date(b.month));

        const months = monthlyData.map(item => item.month);
        const monthlyCounts = monthlyData.map(item => item.count);
        const years = yearlyData.map(item => item.year);
        const yearlyCounts = yearlyData.map(item => item.count);
        const facilityNames = facilityData.map(item => item.MINCLASSNM);
        const facilityCounts = facilityData.map(item => item.count);
		
		

        // 시설별 색상 배열 총 13가지
        const facilityColors = [
            'rgba(255, 99, 132, 0.5)', 
            'rgba(54, 162, 235, 0.5)', 
            'rgba(255, 206, 86, 0.5)', 
            'rgba(75, 192, 192, 0.5)', 
            'rgba(153, 102, 255, 0.5)', 
            'rgba(255, 159, 64, 0.5)', 
            'rgba(255, 20, 147, 0.5)', 
            'rgba(0, 255, 0, 0.5)', 
            'rgba(0, 0, 255, 0.5)', 
            'rgba(128, 0, 128, 0.5)', 
            'rgba(255, 215, 0, 0.5)', 
            'rgba(210, 105, 30, 0.5)', 
            'rgba(0, 128, 128, 0.5)'  
        ];

        // 월별 차트 랜더링
        monthlyChart = new Chart(document.getElementById('monthlyChart').getContext('2d'), {
            type: 'bar',
            data: {
                labels: months,
                datasets: [{
                    label: '월별 예약 수',
                    data: monthlyCounts,
                    backgroundColor: 'rgba(75, 192, 192, 0.5)',
                }]
            },
            options: {
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: '예약 수' },
					ticks: {stepSize: 5}},
                    x: { title: { display: true, text: '월' }}
                }
            }
        });

        // 년별 차트 랜더링
        yearlyChart = new Chart(document.getElementById('yearlyChart').getContext('2d'), {
            type: 'line',
            data: {
                labels: years,
                datasets: [{
                    label: '년별 예약 수',
                    data: yearlyCounts,
                    borderColor: 'rgba(54, 162, 235, 1)',
                    backgroundColor: 'rgba(54, 162, 235, 0.5)',
                }]
            },
            options: {
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: '예약 수' }},
                    x: { title: { display: true, text: '년' }}
                }
            }
        });

        // 시설별 차트 랜더링
        facilityChart = new Chart(document.getElementById('facilityChart').getContext('2d'), {
            type: 'pie',
            data: {
                labels: facilityNames,
                datasets: [{
                    label: '시설별 예약 수',
                    data: facilityCounts,
                    backgroundColor: facilityColors, // 각 시설별 색상 배열 사용
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { display: true, position: 'top' },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                let label = context.label || '';
                                if (label) label += ': ';
                                label += context.raw; 
                                return label;
                            }
                        }
                    }
                }
            }
        });
    }
	
	// 년도 선택 드롭다운 초기화
	function initYearSelector() {
	    const currentYear = new Date().getFullYear();
	    const yearSelector = document.getElementById('yearSelector');
	    
	    // 드롭다운 초기화
	    yearSelector.innerHTML = '';

	    // 현재 년도에서 10년 전까지의 년도 추가
	    for (let year = currentYear; year >= currentYear - 10; year--) {
	        const option = document.createElement('option');
	        option.value = year;
	        option.textContent = year;
	        yearSelector.appendChild(option);
	    }

	    // 드롭다운 기본 선택 설정
	    yearSelector.value = currentYear; // 현재 연도로 초기 설정

	    // 초기 차트 랜더링
	    renderCharts(currentYear); // 현재 년도 선택하여 차트 렌더링
	}

	// 년도 선택 시 차트 업데이트
	document.getElementById('yearSelector').addEventListener('change', function() {
	    const selectedYear = this.value; // 선택된 연도 가져오기
	    if (selectedYear) {
	        renderCharts(selectedYear); // 연도가 유효한 경우에만 호출
	    }
	});

    // 탭 전환 함수
    window.openTab = function(evt, tabName) {
        const tabcontents = document.getElementsByClassName("tab-content");
        for (let i = 0; i < tabcontents.length; i++) {
            tabcontents[i].style.display = "none"; // 모든 탭 숨기기
        }

        const tablinks = document.getElementsByClassName("tablinks");
        for (let i = 0; i < tablinks.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" active", ""); 
        }

        document.getElementById(tabName).style.display = "block"; // 선택한 탭 표시
        evt.currentTarget.className += " active"; 

		// 연도 드롭다운 초기화와 차트 렌더링
        if (tabName === 'Monthly') {
            initYearSelector();
        } else {
            renderCharts(new Date().getFullYear()); // 기본 차트 렌더링
        }
    };

	
	// PDF 다운로드 함수
	window.downloadPdf = function() {
		window.jsPDF = window.jspdf.jsPDF
		const fileName = document.querySelector('.tablinks.active').innerText;
		const element = document.querySelector('body'); // Get the HTML element to be converted to PDF
		html2canvas(element).then(canvas => {
			const imgData = canvas.toDataURL('image/png'); // Convert canvas to image data
			const pdf = new jsPDF(); // Initialize jsPDF
			const imgProps = pdf.getImageProperties(imgData);
			const pdfWidth = pdf.internal.pageSize.getWidth();
			const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;
			pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight); // Add image to PDF
			pdf.save(`seoul-facility-report-${fileName}.pdf`); // Save PDF
		});
	};
	
	
    // 초기 탭 열기
    document.querySelector(".tablinks").click();
});
