/**
 * Created by sunshine on 6/24/16.
 */
$(document).ready(function () {
    $.post(order_url, function (result) {
        var pay_status = result.pay;
        var deliver_4_order_item = result.deliver4OrderItem;
        var deliver_4_cus = result.deliver4Cus;
        $('#order_container').highcharts({
            chart: {
                type: 'bar'
            },
            title: {
                text: '平台订单状态汇总'
            },
            xAxis: {
                categories: ['代理商订单付款状态', '订单项发货状态', '网络订单发货状态'],
                title: {
                    text: null
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '订单数(笔)',
                    align: 'high'
                },
                labels: {
                    overflow: 'justify'
                }
            },
            tooltip: {
                valueSuffix: '笔'
            },
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: true
                    }
                }
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'top',
                x: -40,
                y: 100,
                floating: true,
                borderWidth: 1,
                backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
                shadow: true
            },
            credits: {
                enabled: false
            },
            series: [{
                name: '尚未',
                data: [pay_status[0], deliver_4_order_item[0], deliver_4_cus[0]]
            }, {
                name: '完成',
                data: [pay_status[1], deliver_4_order_item[1], deliver_4_cus[1]]
            }]
        });
    })
});
$(function () {

    $('#agent_container').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: 'Browser market shares at a specific website, 2014'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
        },
        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Firefox', 45.0],
                ['IE', 26.8],
                {
                    name: 'Chrome',
                    y: 12.8,
                    sliced: true,
                    selected: true
                },
                ['Safari', 8.5],
                ['Opera', 6.2],
                ['Others', 0.7]
            ]
        }]
    });
});