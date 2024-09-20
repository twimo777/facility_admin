$(document).ready(function() {
    let yearlyChart, monthlyChart, facilityChart;

	// 귀찮아서 아작스말고 에이싱크로
    async function fetchMonthlyStatistics() {
        const response = await fetch('/statistics/monthly_statistics');
        return await response.json();
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

    async function renderCharts() {
        destroyCharts(); // 기존 차트 파괴

        const monthlyData = await fetchMonthlyStatistics();
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
                    y: { beginAtZero: true, title: { display: true, text: '예약 수' }},
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

        renderCharts();
    };

    // 초기 탭 열기
    document.querySelector(".tablinks").click();
});
